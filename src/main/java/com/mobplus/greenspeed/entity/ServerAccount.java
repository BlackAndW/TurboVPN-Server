package com.mobplus.greenspeed.entity;

import com.mobplus.greenspeed.common.entity.AuditTimeEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * Title
 *
 * Date: 2020-08-04 10:41:27
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@Entity
@Table(name = "t_server_account")
public class ServerAccount extends AuditTimeEntity {

    public static interface State {
        int OFFLINE = 1;
        int ONLINE = 2;
        int PENDING = 3;
    }

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_server_id")
    private Server server;
    @Column(name = "c_user_name")
    private String userName;
    @Column(name = "c_passwd")
    private String passwd;

    /** 状态 0:未知  1:离线 2:在线 3:己分配 */
    @Column(name = "n_status")
    private int status;
    @Column(name = "c_remark")
    private String remark;
}
