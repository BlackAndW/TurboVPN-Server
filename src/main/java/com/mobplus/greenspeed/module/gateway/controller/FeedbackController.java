package com.mobplus.greenspeed.module.gateway.controller;

import com.mobplus.greenspeed.Constants;
import com.mobplus.greenspeed.entity.App;
import com.mobplus.greenspeed.entity.Feedback;
import com.mobplus.greenspeed.entity.Member;
import com.mobplus.greenspeed.service.AppService;
import com.mobplus.greenspeed.service.MemberService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.result.ResultCode;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Title
 *
 * Date: 2020-09-18 14:49:14
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@RestController
@RequestMapping("/app/api/v1/c05")
public class FeedbackController {

    @Autowired
    private AppService appService;
    @Autowired
    private MemberService memberService;

    @PostMapping("/c0001")
    public Result postFeedback(@RequestHeader(Constants.H_PACKGE_NAME) String pkgName,
                               @RequestHeader(Constants.H_TOKEN) String token, @RequestBody Feedback feedback) throws ServiceException {
        Integer appId = getAppId(pkgName);
        if (appId == null) {
            return Result.FAILURE(ResultCode.PARAM_ERROR);
        }
        feedback.setMemberId(0);
        if (StringUtils.isNotBlank(token)) {
            Member member = memberService.findMemberByToken(appId, token);
            if (member != null) {
                feedback.setMemberId(member.getId());
            }
        }
        feedback.setAppId(appId);
        appService.createFeedback(feedback);
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
