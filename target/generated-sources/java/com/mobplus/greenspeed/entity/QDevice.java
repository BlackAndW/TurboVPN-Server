package com.mobplus.greenspeed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDevice is a Querydsl query type for Device
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDevice extends EntityPathBase<Device> {

    private static final long serialVersionUID = 755637048L;

    public static final QDevice device = new QDevice("device");

    public final StringPath androidId = createString("androidId");

    public final StringPath appName = createString("appName");

    public final StringPath bssid = createString("bssid");

    public final StringPath countryCode = createString("countryCode");

    public final NumberPath<Long> createdAt = createNumber("createdAt", Long.class);

    public final StringPath devBrand = createString("devBrand");

    public final StringPath devMake = createString("devMake");

    public final StringPath devModel = createString("devModel");

    public final NumberPath<Integer> devType = createNumber("devType", Integer.class);

    public final StringPath fingerPrint = createString("fingerPrint");

    public final StringPath gaid = createString("gaid");

    public final NumberPath<Double> gpsLat = createNumber("gpsLat", Double.class);

    public final NumberPath<Double> gpsLng = createNumber("gpsLng", Double.class);

    public final NumberPath<Long> gpsTimestamp = createNumber("gpsTimestamp", Long.class);

    public final StringPath iccid = createString("iccid");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath imei = createString("imei");

    public final StringPath imsi = createString("imsi");

    public final StringPath lac = createString("lac");

    public final StringPath mac = createString("mac");

    public final NumberPath<Long> modifiedAt = createNumber("modifiedAt", Long.class);

    public final NumberPath<Integer> network = createNumber("network", Integer.class);

    public final StringPath networkExtra = createString("networkExtra");

    public final NumberPath<Integer> orien = createNumber("orien", Integer.class);

    public final StringPath os = createString("os");

    public final StringPath osv = createString("osv");

    public final StringPath pkgName = createString("pkgName");

    public final StringPath pkgVersion = createString("pkgVersion");

    public final StringPath region = createString("region");

    public final StringPath remoteIp = createString("remoteIp");

    public final StringPath screen = createString("screen");

    public final StringPath uuid = createString("uuid");

    public QDevice(String variable) {
        super(Device.class, forVariable(variable));
    }

    public QDevice(Path<? extends Device> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDevice(PathMetadata metadata) {
        super(Device.class, metadata);
    }

}

