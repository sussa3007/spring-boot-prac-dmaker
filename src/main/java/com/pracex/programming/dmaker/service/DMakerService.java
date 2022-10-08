package com.pracex.programming.dmaker.service;

import com.pracex.programming.dmaker.dto.CreateDeveloper;
import com.pracex.programming.dmaker.dto.DeveloperDetailDto;
import com.pracex.programming.dmaker.dto.DeveloperDto;
import com.pracex.programming.dmaker.dto.EditDeveloper;
import com.pracex.programming.dmaker.entity.Developer;
import com.pracex.programming.dmaker.entity.RetiredDeveloper;
import com.pracex.programming.dmaker.exception.DMakerException;
import com.pracex.programming.dmaker.repository.DeveloperRepository;
import com.pracex.programming.dmaker.repository.RetiredDeveloperRepository;
import com.pracex.programming.dmaker.type.DeveloperLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.pracex.programming.dmaker.code.StatusCode.*;
import static com.pracex.programming.dmaker.exception.DMakerErrorCode.*;
import static com.pracex.programming.dmaker.type.DeveloperLevel.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional
    // ACID 특성을 반영해줌
    // 작업중 실패하게 되면 롤백 해준다.
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        // developer 생성
        // 비지니스 로직 벨리데이트 검증
        validateCreatDeveloperRequest(request);
        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .name(request.getName())
                .age(request.getAge())
                .experienceYears(request.getExperienceYears())
                .statusCode(EMPLOYED)
                .memberId(request.getMemberId())
                .build();
        // DB에 저장해줌. 영속화
        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreatDeveloperRequest(CreateDeveloper.Request request) {
        // 비지니스 로직 벨리데이트 검증
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        // Optional<developer>
        // 생성하려는 아이디가 있는지 여부 검증
        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
    }

    public List<DeveloperDto> getAllEmployedDevelopers() {
        // 모든 developer 정보 가져오기
        return developerRepository.findDevelopersByStatusCodeEquals(EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        // 하나의 developer 정보 가져오기
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
    }

    @Transactional
    // 변경 사항들을 DB에 커밋
    public DeveloperDetailDto editDevelopper(String memberId, EditDeveloper.Request request) {
        // 정보 수정
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
        // 정보를 수정할때는 멤버 아이디가 있는지 여부를 확인해야한다.
        Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException(NO_DEVELOPER)
        );

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);
    }


    private static void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        // developer Level별 벨리데이터 검증
        if (developerLevel == SENIOR
                && experienceYears < 10) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        if (developerLevel == JUNGNIOR
                && (experienceYears < 4 || experienceYears > 10)) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        if (developerLevel == JUNIOR && experienceYears > 4) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        // EMPLOYED -> RETIRED
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(
                        () -> new DMakerException(NO_DEVELOPER)
                );
        developer.setStatusCode(RETIRED);
        // save into RetiredDeveloper
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();
        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDto.fromEntity(developer);

    }
}
