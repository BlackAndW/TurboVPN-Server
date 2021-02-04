package com.mobplus.greenspeed.configuration;

import com.yeecloud.meeto.configure.repository.ConfigureRepository;
import com.yeecloud.meeto.configure.service.ConfigureService;
import com.yeecloud.meeto.ipparser.service.IPParserService;
import com.yeecloud.meeto.ipparser.service.impl.DBIPParserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Title
 *
 * Date: 2020-09-19 10:30:36
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Configuration
public class SettingConfiguration {

    @Bean
    public ConfigureRepository createConfigureRepository(DataSource dataSource) {
        ConfigureRepository repository = new ConfigureRepository();
        repository.setDataSource(dataSource);
        return repository;
    }

    @Bean
    public ConfigureService createConfigureService(ConfigureRepository repository) {
        ConfigureService configureService = new ConfigureService();
        configureService.setConfigureRepository(repository);
        return configureService;
    }

    @Bean
    public IPParserService createIPParserService(DataSource dataSource) {
        DBIPParserServiceImpl ipParserService = new DBIPParserServiceImpl();
        ipParserService.setDataSource(dataSource);
        return ipParserService;
    }
}
