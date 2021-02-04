package com.mobplus.greenspeed.module.gateway.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class FeedbackForm {
    @JSONField(name = "a01")
    private String subject;
    @JSONField(name = "a02")
    private String content;
    @JSONField(name = "a03")
    private String contact;
}
