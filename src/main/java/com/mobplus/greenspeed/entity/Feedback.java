package com.mobplus.greenspeed.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Title
 *
 * Date: 2020-09-18 14:46:05
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@Entity
@Table(name = "t_feedback")
@EntityListeners(AuditingEntityListener.class)
public class Feedback implements Serializable {
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "n_app_id")
    private Integer appId;
    @Column(name = "n_member_id")
    private Integer memberId;
    @Column(name = "c_subject")
    private String subject;
    @Column(name = "c_contact")
    private String contact;
    @Column(name = "c_content")
    private String content;
    @CreatedDate
    @Column(name = "n_created_at", updatable = false)
    protected Long createdAt;
}
