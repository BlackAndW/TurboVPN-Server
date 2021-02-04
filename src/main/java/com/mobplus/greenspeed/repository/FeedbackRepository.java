package com.mobplus.greenspeed.repository;

import com.mobplus.greenspeed.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Title
 *
 * Date: 2020-09-18 14:47:34
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Integer>, QuerydslPredicateExecutor<Feedback> {
}
