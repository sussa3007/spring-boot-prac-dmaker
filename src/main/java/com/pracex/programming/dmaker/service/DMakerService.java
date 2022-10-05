package com.pracex.programming.dmaker.service;

import com.pracex.programming.dmaker.entity.Developer;
import com.pracex.programming.dmaker.repository.DeveloperRepository;
import com.pracex.programming.dmaker.type.DeveloperLever;
import com.pracex.programming.dmaker.type.DeveloperSkillType;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;

    @Transactional
    public void createDeveloper(){
        Developer developer = Developer.builder()
                .developerLever(DeveloperLever.JUNIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .name("Olaf")
                .age(31)
                .experienceYears(3)
                .build();
        // DB에 저장해줌. 영속화
        developerRepository.save(developer);
    }
}
