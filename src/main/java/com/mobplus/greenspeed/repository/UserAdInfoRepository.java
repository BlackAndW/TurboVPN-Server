package com.mobplus.greenspeed.repository;

import com.mobplus.greenspeed.entity.UserAdInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author: Leonard
 * @create: 2021/3/23
 */

@Repository
public interface UserAdInfoRepository extends JpaRepository<UserAdInfo, Integer>, QuerydslPredicateExecutor<UserAdInfo> {

    UserAdInfo findFirstByUserIp(String userIp);
}
