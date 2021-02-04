package com.mobplus.greenspeed.service;

import com.mobplus.greenspeed.entity.Member;
import com.mobplus.greenspeed.entity.SocialUser;
import com.yeecloud.meeto.common.exception.ServiceException;

/**
 * Title
 *
 * Date: 2020-07-30 22:22:01
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
public interface MemberService {

    void saveMember(Member member) throws ServiceException;

    void saveSocialUser(SocialUser user) throws ServiceException;

    Member findMemberBySocialId(Integer appId, String type, String uniqueId) throws ServiceException;

    void saveToken(Member member, String token, String ipAddr) throws ServiceException;

    Member findMemberByToken(Integer appId, String token) throws ServiceException;

    void removeToken(Integer appId, String token) throws ServiceException;
}
