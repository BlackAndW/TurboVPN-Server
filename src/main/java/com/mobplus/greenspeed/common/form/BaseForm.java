package com.mobplus.greenspeed.common.form;


import javax.validation.groups.Default;
import java.io.Serializable;

/**
 * Title
 *
 * Date: 2019-11-11 16:46:32
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
public class BaseForm implements Serializable {
    /**
     * 保存验证组
     */
    public interface Create extends Default {
    }

    /**
     * 更新验证组
     */
    public interface Update extends Default {
    }
}
