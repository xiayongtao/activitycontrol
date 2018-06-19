package com.happok.live.activitycontrol.common;

import com.alibaba.fastjson.JSONObject;


public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(200, "成功"),
    CREATED(201, "已创建"),
    ACCEPTED(202, "已接受"),



    /* 参数错误：411-419 */
    IS_NOTEXIST(404, "不存在"),
    IS_EXIST(409, "已存在"),
    PARAM_IS_INVALID(411, "参数无效"),
    PARAM_IS_BLANK(412, "参数为空"),
    PARAM_TYPE_BIND_ERROR(413, "参数类型错误"),
    PARAM_NOT_COMPLETE(414, "参数缺失"),

    /* 用户错误：421-429*/
    USER_NOT_LOGGED_IN(421, "用户未登录"),
    USER_LOGIN_ERROR(422, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(423, "账号已被禁用"),
    USER_NOT_EXIST(424, "用户不存在"),
    USER_HAS_EXISTED(425, "用户已存在"),

    /* 内部错误：501-509 */
    RESULE_DATA_NONE(501, "数据未找到"),
    DATA_IS_WRONG(502, "数据有误"),

    /* 调用接口错误：511-519 */
    INTERFACE_INNER_INVOKE_ERROR(511, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(512, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(513, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(514, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(515, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(516, "接口负载过高"),

    /* 权限错误：521-529 */
    PERMISSION_NO_ACCESS(521, "无访问权限");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public String toJSONString() {

        JSONObject res = new JSONObject(true);
        res.put("code",this.code);
        res.put("message",this.message);
        return JSONObject.toJSONString(this);
    }

}
