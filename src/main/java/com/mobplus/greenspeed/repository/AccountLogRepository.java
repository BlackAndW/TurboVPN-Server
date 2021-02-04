package com.mobplus.greenspeed.repository;

import com.mobplus.greenspeed.entity.AccountLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Title
 *
 * Date: 2020-08-04 11:46:07
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Repository
public interface AccountLogRepository extends JpaRepository<AccountLog, Integer>, QuerydslPredicateExecutor<AccountLog> {

}