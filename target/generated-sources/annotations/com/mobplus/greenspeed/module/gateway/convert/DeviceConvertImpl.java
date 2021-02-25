package com.mobplus.greenspeed.module.gateway.convert;

import com.mobplus.greenspeed.entity.Device;
import com.mobplus.greenspeed.module.gateway.form.DeviceForm;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-25T10:29:28+0800",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class DeviceConvertImpl implements DeviceConvert {

    @Override
    public Device convert(DeviceForm dev) {
        if ( dev == null ) {
            return null;
        }

        Device device = new Device();

        device.setPkgName( dev.getPkgName() );
        device.setPkgVersion( dev.getPkgVersion() );
        device.setAppName( dev.getAppName() );
        device.setUuid( dev.getUuid() );
        device.setOs( dev.getOs() );
        device.setOsv( dev.getOsv() );
        device.setDevType( dev.getDevType() );
        device.setDevBrand( dev.getDevBrand() );
        device.setDevModel( dev.getDevModel() );
        device.setDevMake( dev.getDevMake() );
        device.setFingerPrint( dev.getFingerPrint() );
        device.setAndroidId( dev.getAndroidId() );
        device.setGaid( dev.getGaid() );
        device.setImei( dev.getImei() );
        device.setImsi( dev.getImsi() );
        device.setIccid( dev.getIccid() );
        device.setScreen( dev.getScreen() );
        device.setOrien( dev.getOrien() );
        device.setBssid( dev.getBssid() );
        device.setMac( dev.getMac() );
        device.setNetwork( dev.getNetwork() );
        device.setNetworkExtra( dev.getNetworkExtra() );
        device.setLac( dev.getLac() );
        device.setGpsLat( dev.getGpsLat() );
        device.setGpsLng( dev.getGpsLng() );
        device.setGpsTimestamp( dev.getGpsTimestamp() );

        return device;
    }
}
