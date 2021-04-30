package com.mobplus.greenspeed.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @author: Leonard
 * @create: 2021/4/30
 */

@Data
@Entity
@Table(name = "ip2location")
@EntityListeners(AuditingEntityListener.class)
public class Ip2location {

    @Column(name = "ip_from")
    private long ipFrom;

    @Id
    @Column(name = "ip_to")
    private long ipTo;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;
}
