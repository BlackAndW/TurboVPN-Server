package com.mobplus.greenspeed.repository;

import com.mobplus.greenspeed.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Title
 *
 * Date: 2020-09-17 11:18:57
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Integer>, QuerydslPredicateExecutor<Member> {

}