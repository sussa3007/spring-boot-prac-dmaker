package com.pracex.programming.dmaker.entity;

import com.pracex.programming.dmaker.code.StatusCode;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder // 생성자 어노테이션이랑 같이 써야됨
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class RetiredDeveloper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;


    private String memberId;
    private String name;

    @CreatedDate // 생성시점
    private LocalDateTime createAt;

    @LastModifiedDate // 수정시점
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;
}
