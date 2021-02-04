package com.mobplus.greenspeed.module.gateway.controller;

import com.mobplus.greenspeed.Constants;
import com.mobplus.greenspeed.StatusCode;
import com.mobplus.greenspeed.entity.App;
import com.mobplus.greenspeed.entity.Member;
import com.mobplus.greenspeed.entity.SocialUser;
import com.mobplus.greenspeed.module.gateway.convert.MemberConvert;
import com.mobplus.greenspeed.module.gateway.form.MemberForm;
import com.mobplus.greenspeed.module.gateway.form.SocialLoginForm;
import com.mobplus.greenspeed.service.AppService;
import com.mobplus.greenspeed.service.MemberService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.result.ResultCode;
import com.yeecloud.meeto.common.util.ParamUtils;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/app/api/v1/c02")
public class MemberController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AppService appService;
    @Autowired
    private MemberConvert memberConvert;
    @Autowired
    private MemberService memberService;

    @PostMapping("/c0001")
    public Result<String> socialLogin(@RequestHeader(Constants.H_PACKGE_NAME) String pkgName, @RequestBody SocialLoginForm form) throws ServiceException {
        Integer appId = getAppId(pkgName);
        if (appId == null || StringUtils.isEmpty(form.getUniqueId()) || StringUtils.isEmpty(form.getType())) {
            return Result.FAILURE(ResultCode.PARAM_ERROR);
        }
        Member member = memberService.findMemberBySocialId(appId, form.getType(), form.getUniqueId());
        if (member == null) {
            member = new Member();
            member.setAppId(appId);
            memberService.saveMember(member);

            SocialUser user = new SocialUser();
            user.setAppId(appId);
            user.setPluginName(form.getType());
            user.setUniqueId(form.getUniqueId());
            user.setMember(member);
            memberService.saveSocialUser(user);
        }
        String token = StringUtils.uuid();
        memberService.saveToken(member, token, ParamUtils.getIpAddr(request));
        return Result.SUCCESS(token);
    }

    @PostMapping("/c0002")
    public Result saveProfile(@RequestHeader(Constants.H_PACKGE_NAME) String pkgName, @RequestHeader(Constants.H_TOKEN) String token,
                              @RequestBody MemberForm form) throws ServiceException {
        Integer appId = getAppId(pkgName);
        if (appId == null) {
            return Result.FAILURE(ResultCode.PARAM_ERROR);
        }
        Member member = memberService.findMemberByToken(appId, token);
        if (member == null) {
            return Result.FAILURE(StatusCode.ACCOUNT_NO_AUTH);
        }
        member.setDisplayName(form.getDisplayName());
        member.setAvatarUrl(form.getPhotoUrl());
        member.setFamilyName(form.getFamilyName());
        member.setGivenName(form.getGivenName());
        memberService.saveMember(member);
        return Result.SUCCESS();
    }

    @PostMapping("/c0003")
    public Result updateToken(@RequestHeader(Constants.H_PACKGE_NAME) String pkgName, @RequestHeader(Constants.H_TOKEN) String token) throws ServiceException {
        Integer appId = getAppId(pkgName);
        if (appId == null) {
            return Result.FAILURE(ResultCode.PARAM_ERROR);
        }
        memberService.findMemberByToken(appId, token);
        return Result.SUCCESS();
    }

    @PostMapping("/c0005")
    public Result signOut(@RequestHeader(Constants.H_PACKGE_NAME) String pkgName, @RequestHeader(Constants.H_TOKEN) String token) throws ServiceException {
        Integer appId = getAppId(pkgName);
        if (appId == null) {
            return Result.FAILURE(ResultCode.PARAM_ERROR);
        }
        memberService.removeToken(appId, token);
        return Result.SUCCESS();
    }

    private Integer getAppId(String pkgName) throws ServiceException {
        if (StringUtils.isNotBlank(pkgName)) {
            App app = appService.findAppByPkgName(pkgName);
            if (app != null) {
                return app.getId();
            }
        }
        return null;
    }
}
