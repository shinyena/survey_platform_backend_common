package com.cloud.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_job")
@EntityListeners(AuditingEntityListener.class)
public class UserJob {
    @Id
    @Comment("PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_job_id", nullable = false)
    private Integer userJobId;

    @Comment("사용자 직업 코드")
    @Column(name = "code", nullable = false)
    private Integer code;

    @Comment("사용자 직업")
    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @Comment("정렬순서")
    @Column(name = "sort_idx", nullable = false)
    private Integer sortIdx;

    @Comment("삭제여부")
    @ColumnDefault("false")
    @Column(name = "del_yn", nullable = false)
    private Boolean delYn;

    @Comment("생성자")
    @Column(name = "reg_id", nullable = false, length = 20)
    private String regId;

    @Comment("생성일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    @Column(name = "reg_dt", nullable = false)
    private LocalDateTime regDt;
}
