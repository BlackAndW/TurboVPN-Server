package com.mobplus.greenspeed.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Title
 *
 * Date: 2019-11-06 17:25:03
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Configuration
public class CacheConfiguration {
    @Bean
    public CacheManager createCacheManager(){
        return new EhCacheCacheManager();
    }

}
