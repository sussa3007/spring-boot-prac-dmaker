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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.pracex.programming.dmaker.code.StatusCode.*;
import static com.pracex.programming.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional
    // 작업중 실패하게 되면 롤백 해준다.
    public CreateDeveloper.Response createDeveloper(
            @NonNull CreateDeveloper.Request request) {
        // 비지니스 로직 유효성 검증(벨리데이터)
        validateCreatDeveloperRequest(request);
        // DB에 저장해줌. 영속화
        return CreateDeveloper.Response.fromEntity(
                        developerRepository.save(
                                createDeveloperFromRequest(request)
                        ));
    }
    private Developer createDeveloperFromRequest(
            CreateDeveloper.Request request
    ){
        return Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .name(request.getName())
                .age(request.getAge())
                .experienceYears(request.getExperienceYears())
                .statusCode(EMPLOYED)
                .memberId(request.getMemberId())
                .build();
    }

    private void validateCreatDeveloperRequest(
            @NonNull CreateDeveloper.Request request
    ) {
        // developer Level 유효성 검증
        request.getDeveloperLevel()
                .validateExperienceYears(request.getExperienceYears());

        // Optional<developer>
        // 생성하려는 아이디가 있는지 여부 검증
        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
    }

    @Transactional
    public List<DeveloperDto> getAllEmployedDevelopers() {
        // 모든 developer 정보 가져오기
        return developerRepository.findDevelopersByStatusCodeEquals(EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        // 하나의 developer 정보 가져오기
        return DeveloperDetailDto
                .fromEntity(getDeveloperByMemberId(memberId));
    }

    @Transactional
    // 변경 사항들을 DB에 커밋
    public DeveloperDetailDto editDevelopper(
            String memberId, EditDeveloper.Request request
    ) {
        // developer Level 유효성 검증
        request.getDeveloperLevel()
                .validateExperienceYears(request.getExperienceYears());

        // 정보를 수정할때는 멤버 아이디가 있는지 여부를 확인해야한다.
        return DeveloperDetailDto.fromEntity(
                // 정보 수정
                getUpdatedDeveloperFromRequest(
                                // 아이디 유효성 검증한 엔티티 삽입
                        request, getDeveloperByMemberId(memberId)
                ));
    }

    //유효성 검증된 Edit request 엔티티, 기존 developer 엔티티를 받아서 정보 수정
    private Developer getUpdatedDeveloperFromRequest(
            EditDeveloper.Request request, Developer developer
    ) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());
        return developer;
    }


    // 아이디가 유효성 검증
    private Developer getDeveloperByMemberId(String memberId){
        return developerRepository.findByMemberId(memberId)
                .orElseThrow(
                () -> new DMakerException(NO_DEVELOPER)
        );
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
