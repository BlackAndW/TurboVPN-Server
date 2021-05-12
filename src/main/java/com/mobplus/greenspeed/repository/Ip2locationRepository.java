package com.mobplus.greenspeed.repository;

import com.mobplus.greenspeed.entity.Ip2location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author: Leonard
 * @create: 2021/4/30
 */
@Repository
public interface Ip2locationRepository extends JpaRepository<Ip2location, Integer>, QuerydslPredicateExecutor<Ip2location> {

    @Query(value = "select * from ip2location where ip_from <= ?1 order by ip_from desc limit 1", nativeQuery = true)
    Ip2location findIpInfo(long ipLong);

}
