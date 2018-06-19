package com.happok.live.activitycontrol.common;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class Result implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;

    private Integer code;

    private String msg;
    private Object result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return result;
    }

    public void setData(Object data) {
        this.result = data;
    }

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.result = data;
    }

    private static JSONObject toJsonObject(Result obj) {
        JSONObject res = new JSONObject(true);

        res.put("code", obj.code);
        res.put("message", obj.msg);
        if (null != obj.result) {
            res.put("result", obj.result);
        }

        return res;
    }

    public static JSONObject success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        return toJsonObject(result);
    }

    public static JSONObject success(Object data) {

        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);

        return toJsonObject(result);
    }

    public static JSONObject failure(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        return toJsonObject(result);
    }

    public static JSONObject failure(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        return toJsonObject(result);
    }

    public void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }
}
