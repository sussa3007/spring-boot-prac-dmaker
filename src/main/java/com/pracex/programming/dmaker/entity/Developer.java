package com.pracex.programming.dmaker.entity;

import com.pracex.programming.dmaker.code.StatusCode;
import com.pracex.programming.dmaker.dto.CreateDeveloper;
import com.pracex.programming.dmaker.type.DeveloperLevel;
import com.pracex.programming.dmaker.type.DeveloperSkillType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.pracex.programming.dmaker.code.StatusCode.EMPLOYED;

@Getter
@Setter
@Builder // 생성자 어노테이션이랑 같이 써야됨
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Enumerated(EnumType.STRING)
    private DeveloperLevel developerLevel;

    @Enumerated(EnumType.STRING)
    private DeveloperSkillType developerSkillType;

    private Integer experienceYears;
    private String memberId;
    private String name;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;

    @CreatedDate // 생성시점
    private LocalDateTime createAt;

    @LastModifiedDate // 수정시점
    private LocalDateTime updatedAt;


}
