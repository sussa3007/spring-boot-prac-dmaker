package com.pracex.programming.dmaker.service;

import com.pracex.programming.dmaker.dto.CreateDeveloper;
import com.pracex.programming.dmaker.dto.DeveloperDetailDto;
import com.pracex.programming.dmaker.entity.Developer;
import com.pracex.programming.dmaker.exception.DMakerException;
import com.pracex.programming.dmaker.repository.DeveloperRepository;
import com.pracex.programming.dmaker.type.DeveloperLevel;
import com.pracex.programming.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.pracex.programming.dmaker.code.StatusCode.*;
import static com.pracex.programming.dmaker.constant.DMakerConstant.*;
import static com.pracex.programming.dmaker.exception.DMakerErrorCode.*;
import static com.pracex.programming.dmaker.type.DeveloperLevel.*;
import static com.pracex.programming.dmaker.type.DeveloperSkillType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
// 모키토 기능을 이용한 테스트
class DMakerServiceTest {
    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DMakerService dMakerService;

    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(SENIOR)
            .developerSkillType(FRONT_END)
            .experienceYears(MIN_SENIOR_EXPERIENCE_YEARS)
            .statusCode(EMPLOYED)
            .name("name")
            .age(32)
            .build();
    private CreateDeveloper.Request getCreateRequest(
            DeveloperLevel developerLevel,
            DeveloperSkillType developerSkillType,
            Integer experienceYears
    ) {
        return CreateDeveloper.Request.builder()
                .developerLevel(developerLevel)
                .developerSkillType(developerSkillType)
                .experienceYears(experienceYears)
                .memberId("memberId")
                .name("name")
                .age(32)
                .build();

    }


    @Test
    public void testSomthing() {
        // mock들의 동작을 정의해준다. mocking
        // given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));
        //when
        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");
        // then
        assertEquals(SENIOR,developerDetail.getDeveloperLevel());
        assertEquals(FRONT_END,developerDetail.getDeveloperSkillType());
        assertEquals(MIN_SENIOR_EXPERIENCE_YEARS,developerDetail.getExperienceYears());
    }

    @Test
    void createDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        given(developerRepository.save(any()))
                .willReturn(defaultDeveloper);
        // mock 객체가 어떠한 동작을 할때 동작한 내용을 켑쳐해줌
        // mock 객체  developerRepository , 동작 save
        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        //when
        dMakerService.createDeveloper(
                getCreateRequest(SENIOR,BACK_END,MIN_SENIOR_EXPERIENCE_YEARS)
        );

        //then
        // 파라미터를 실제 켑쳐를 해서
        verify(developerRepository, times(1))
                .save(captor.capture());
        // 켑쳐 결과를
        Developer saveDeveloper = captor.getValue();
        assertEquals(SENIOR,saveDeveloper.getDeveloperLevel());
        assertEquals(BACK_END,saveDeveloper.getDeveloperSkillType());
        assertEquals(MIN_SENIOR_EXPERIENCE_YEARS,saveDeveloper.getExperienceYears());
    }
    @Test
    void createDeveloperTest_faild_with_suplicated() {
        // Member id exception test
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(SENIOR,FRONT_END,MIN_SENIOR_EXPERIENCE_YEARS))
        );


        assertEquals(DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }
    @Test
    void createDeveloperTest_faild_with_unmatched_level () {
        // Developer Level exception test
        //given

        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(JUNIOR,FRONT_END,MAX_JUNIOR_EXPERIENCE_YEARS+1)
                ));


        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());

        dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(JUNGNIOR,FRONT_END,MIN_SENIOR_EXPERIENCE_YEARS+1)
                ));


        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());

        dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(SENIOR,FRONT_END,MIN_SENIOR_EXPERIENCE_YEARS-1)
                ));


        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());


    }


}