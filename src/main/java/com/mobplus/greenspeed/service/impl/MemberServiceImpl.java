package com.mobplus.greenspeed.service.impl;

import com.mobplus.greenspeed.entity.Member;
import com.mobplus.greenspeed.entity.SocialUser;
import com.mobplus.greenspeed.entity.Token;
import com.mobplus.greenspeed.repository.MemberRepository;
import com.mobplus.greenspeed.repository.SocialUserRepository;
import com.mobplus.greenspeed.repository.TokenRepository;
import com.mobplus.greenspeed.service.MemberService;
import com.yeecloud.meeto.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Title
 *
 * Date: 2020-07-30 22:25:28
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    private final static long TOKEN_EXPIRE = 1000 * 3600 * 24 * 31;

    @Autowired
    private SocialUserRepository socialUserRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void saveMember(Member member) throws ServiceException {
        member.setModifiedAt(System.currentTimeMillis());
        memberRepository.save(member);
    }

    @Override
    public void saveSocialUser(SocialUser user) throws ServiceException {
        socialUserRepository.save(user);
    }

    @Override
    public Member findMemberBySocialId(Integer appId, String type, String uniqueId) throws ServiceException {
        SocialUser user = socialUserRepository.findTopByAppIdAndPluginNameAndUniqueId(appId, type, uniqueId);
        if (user != null) {
            return user.getMember();
        }
        return null;
    }

    @Override
    public void saveToken(Member member, String token, String ipAddr) throws ServiceException {
        if (StringUtils.isBlank(token)) {
            return;
        }
        Token bean = new Token();
        bean.setAppId(member.getAppId());
        bean.setToken(token);
        bean.setMember(member);
        bean.setIpAddr(ipAddr);
        bean.setCreatedAt(System.currentTimeMillis());
        bean.setExpireAt(bean.getCreatedAt() + TOKEN_EXPIRE);
        bean.setLogoutAt(0L);
        tokenRepository.save(bean);
    }

    @Override
    public Member findMemberByToken(Integer appId, String token) throws ServiceException {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Token token1 = tokenRepository.findTopByAppIdAndToken(appId, token);
        if (token1 != null /*&& token1.getExpireAt() > System.currentTimeMillis() && token1.getLogoutAt() == 0*/) {
            //TOKEN自动延期
            token1.setExpireAt(System.currentTimeMillis() + TOKEN_EXPIRE);
            tokenRepository.save(token1);
            return token1.getMember();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void removeToken(Integer appId, String token) throws ServiceException {
        if (StringUtils.isBlank(token)) {
            return;
        }
        Token token1 = tokenRepository.findTopByAppIdAndToken(appId, token);
        if (token1 != null) {
            token1.setLogoutAt(System.currentTimeMillis());
            tokenRepository.save(token1);
        }
    }
}
