package com.mobplus.greenspeed.entity;

import com.mobplus.greenspeed.common.entity.AuditorEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/9/17
 */
@Data
@Entity
@Table(name = "t_error_log")
public class ErrorLog extends AuditorEntity {

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    @Column(name = "n_pkg_version")
    private String pkgVersion;

    @Column(name = "n_err_message")
    private String errMsg;
}
