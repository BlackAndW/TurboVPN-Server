package com.mobplus.greenspeed.repository;

import com.mobplus.greenspeed.entity.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * 广告
 *
 * Date: 2019-11-10 15:07:57
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version v1.0.0
 */
@Repository
public interface SocialUserRepository extends JpaRepository<SocialUser, Integer>, QuerydslPredicateExecutor<SocialUser> {

    SocialUser findTopByAppIdAndPluginNameAndUniqueId(Integer appId, String pluginName, String uniqueId);
}