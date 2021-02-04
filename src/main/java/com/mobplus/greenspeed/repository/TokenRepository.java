package com.mobplus.greenspeed.repository;

import com.mobplus.greenspeed.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Title
 *
 * Date: 2020-09-17 11:18:31
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>, QuerydslPredicateExecutor<Token> {

    Token findTopByAppIdAndToken(Integer appId, String token);

    void deleteByAppIdAndToken(Integer appId, String token);
}