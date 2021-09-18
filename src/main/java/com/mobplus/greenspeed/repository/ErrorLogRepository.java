package com.mobplus.greenspeed.repository;

import com.mobplus.greenspeed.entity.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author: Leonard
 * @create: 2021/9/17
 */
@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Integer>, QuerydslPredicateExecutor<ErrorLog> {
}
