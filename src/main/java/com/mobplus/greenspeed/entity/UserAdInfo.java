package com.mobplus.greenspeed.entity;

import com.mobplus.greenspeed.common.entity.AuditorEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/3/23
 */

@Data
@Entity
@Table(name = "t_user_ad_info")
public class UserAdInfo extends AuditorEntity {

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 用户ip地址 */
    @Column(name = "n_user_ip")
    private String userIp;

    /** appId */
    @Column(name = "n_app_id")
    private String appId;

    /** 设备号 */
    @Column(name = "n_uuid")
    private String uuid;

    /** 广告渠道 */
    @Column(name = "n_ad_channel")
    private String adChannel;

    /** 用户广告请求次数 */
    @Column(name = "n_ad_request_count")
    private Integer adRequestCount = 0;

    /** 用户广告展示次数 */
    @Column(name = "n_ad_show_count")
    private Integer adShowCount = 0;

    /** 用户广告点击次数 */
    @Column(name = "n_ad_click_count")
    private Integer adClickCount = 0;

}
