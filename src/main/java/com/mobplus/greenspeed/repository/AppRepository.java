package com.mobplus.greenspeed.repository;

import com.mobplus.greenspeed.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Title
 *
 * Date: 2020-09-17 11:19:12
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Repository
public interface AppRepository extends JpaRepository<App, Integer>, QuerydslPredicateExecutor<App> {

    App findTopByPkgName(String pkgName);

    @Override
    @Modifying
    @Query("update App obj set obj.deleted = true where obj.id=:id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update App obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);
}