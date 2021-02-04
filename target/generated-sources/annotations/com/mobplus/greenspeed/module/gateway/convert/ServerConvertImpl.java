package com.mobplus.greenspeed.module.gateway.convert;

import com.mobplus.greenspeed.entity.Server;
import com.mobplus.greenspeed.entity.ServerAccount;
import com.mobplus.greenspeed.module.gateway.vo.ServerProfileVO;
import com.mobplus.greenspeed.module.gateway.vo.ServerVO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-04T09:32:01+0800",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class ServerConvertImpl implements ServerConvert {

    @Override
    public ServerVO convert(Server server) {
        if ( server == null ) {
            return null;
        }

        ServerVO serverVO = new ServerVO();

        serverVO.setId( server.getId() );
        serverVO.setName( server.getName() );
        serverVO.setSummary( server.getSummary() );
        serverVO.setIconUrl( server.getIconUrl() );
        serverVO.setSpeed( server.getSpeed() );

        return serverVO;
    }

    @Override
    public List<ServerVO> convert(List<Server> list) {
        if ( list == null ) {
            return null;
        }

        List<ServerVO> list1 = new ArrayList<ServerVO>( list.size() );
        for ( Server server : list ) {
            list1.add( convert( server ) );
        }

        return list1;
    }

    @Override
    public ServerProfileVO convert(ServerAccount account) {
        if ( account == null ) {
            return null;
        }

        ServerProfileVO serverProfileVO = new ServerProfileVO();

        serverProfileVO.setPasswd( account.getPasswd() );
        serverProfileVO.setName( accountServerName( account ) );
        serverProfileVO.setCert( accountServerCert( account ) );
        serverProfileVO.setId( accountServerId( account ) );
        serverProfileVO.setUserName( account.getUserName() );
        serverProfileVO.setIpAddr( accountServerIpAddr( account ) );

        return serverProfileVO;
    }

    private String accountServerName(ServerAccount serverAccount) {
        if ( serverAccount == null ) {
            return null;
        }
        Server server = serverAccount.getServer();
        if ( server == null ) {
            return null;
        }
        String name = server.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String accountServerCert(ServerAccount serverAccount) {
        if ( serverAccount == null ) {
            return null;
        }
        Server server = serverAccount.getServer();
        if ( server == null ) {
            return null;
        }
        String cert = server.getCert();
        if ( cert == null ) {
            return null;
        }
        return cert;
    }

    private Integer accountServerId(ServerAccount serverAccount) {
        if ( serverAccount == null ) {
            return null;
        }
        Server server = serverAccount.getServer();
        if ( server == null ) {
            return null;
        }
        Integer id = server.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String accountServerIpAddr(ServerAccount serverAccount) {
        if ( serverAccount == null ) {
            return null;
        }
        Server server = serverAccount.getServer();
        if ( server == null ) {
            return null;
        }
        String ipAddr = server.getIpAddr();
        if ( ipAddr == null ) {
            return null;
        }
        return ipAddr;
    }
}
