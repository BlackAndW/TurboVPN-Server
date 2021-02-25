package com.mobplus.greenspeed.module.gateway.convert;

import com.mobplus.greenspeed.entity.Member;
import com.mobplus.greenspeed.module.gateway.vo.MemberVO;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-24T16:17:01+0800",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class MemberConvertImpl implements MemberConvert {

    @Override
    public MemberVO convert(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberVO memberVO = new MemberVO();

        memberVO.setDisplayName( member.getDisplayName() );
        memberVO.setFamilyName( member.getFamilyName() );
        memberVO.setGivenName( member.getGivenName() );
        memberVO.setAvatarUrl( member.getAvatarUrl() );

        return memberVO;
    }
}
