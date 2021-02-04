package com.mobplus.greenspeed.module.gateway.convert;

import com.mobplus.greenspeed.common.convert.GeneralConvert;
import com.mobplus.greenspeed.entity.Server;
import com.mobplus.greenspeed.entity.ServerAccount;
import com.mobplus.greenspeed.module.gateway.vo.ServerProfileVO;
import com.mobplus.greenspeed.module.gateway.vo.ServerVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Title
 *
 * Date: 2020-07-30 22:01:02
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface ServerConvert {
    ServerVO convert(Server server);

    List<ServerVO> convert(List<Server> list);

    @Mappings({
            @Mapping(target = "id", source = "server.id"),
            @Mapping(target = "name", source = "server.name"),
            @Mapping(target = "ipAddr", source = "server.ipAddr"),
            @Mapping(target = "userName", source = "userName"),
            @Mapping(target = "passwd", source = "passwd"),
            @Mapping(target = "cert", source = "server.cert")
    })
    ServerProfileVO convert(ServerAccount account);
}
