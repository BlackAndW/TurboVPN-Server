package com.mobplus.greenspeed.service.impl;

import com.mobplus.greenspeed.entity.ServerAccount;
import com.mobplus.greenspeed.service.ServerStatusService;
import com.yeecloud.meeto.common.database.helper.DbHelper;
import com.yeecloud.meeto.configure.service.ConfigureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Title
 *
 * Date: 2020-09-18 17:45:01
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@Service
public class ServerStatusServiceImpl implements ServerStatusService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ConfigureService configureService;

    @Override
    @Transactional
    public void updateStatus(final Integer serverId, final String[] accounts) {
        //1, 该服务器账号重置(在线, 等待5分钟以上)
        final String sql_reset = "update t_server_account set n_status=?, n_modified_at=? where n_server_id=? and (n_status=? or (n_status=? and n_modified_at<=?))";
        //2, 设置指定账号为在线账号
        final String sql_seton = "update t_server_account set n_status=?, n_modified_at=? where n_server_id=? and c_user_name=?";

        final long now = System.currentTimeMillis();
        final long time = now - getAccountPendingDuration();//5分钟

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(new ConnectionCallback<Integer>() {
            @Override
            public Integer doInConnection(Connection connect) throws SQLException, DataAccessException {
                DbHelper.executeUpdate(connect, sql_reset, ServerAccount.State.OFFLINE, now, serverId, ServerAccount.State.ONLINE, ServerAccount.State.PENDING, time);
                if (accounts != null) {
                    for (String account : accounts) {
                        if (account.trim().length() > 0) {
                            DbHelper.executeUpdate(connect, sql_seton, ServerAccount.State.ONLINE, now, serverId, account.trim());
                        }
                    }
                }
                return null;
            }
        });
    }

    private int getAccountPendingDuration() {
        int duration = configureService.getValueByKey("server.account.pending.duration", 5);
        return duration * 60 * 1000;
    }
}
