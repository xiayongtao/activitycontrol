package com.happok.live.activitycontrol.controller;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.activitycontrol.common.Result;
import com.happok.live.activitycontrol.common.ResultCode;
import com.happok.live.activitycontrol.service.PushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "直播controller", tags = {"直播控制接口"})
@RestController
@RequestMapping("/v1/api/")
public class PushController {

    @Autowired
    private PushService pushService = null;

    @ApiOperation(value = "开始直播", notes = "传入源流地址和目标流地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dstHost", value = "目标IP", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "dstProt", value = "目标Prot", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "srcUrl", value = "源流地址", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "目标名称", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "apiPort", value = "api端口", required = true, paramType = "form", dataType = "Integer")
    })


    @PostMapping("/stream")
    public Object Start(@RequestBody String body) {

        JSONObject jsonResult = JSONObject.parseObject(body);
        String ip = jsonResult.getString("dstHost");
        String port = jsonResult.getString("dstProt");
        String srcUrl = jsonResult.getString("srcUrl");
        String name = jsonResult.getString("name");
        String apiPort = jsonResult.getString("apiPort");
        if (null == ip || null == srcUrl || null == name) {
            return Result.failure(ResultCode.PARAM_IS_BLANK);
        }

        if (null == port) {
            port = "1935";
        }

        ResultCode code = pushService.Start(srcUrl, ip, port, apiPort, name);
        if (code.code() == ResultCode.SUCCESS.code()) {
            return Result.success();
        }

        return Result.failure(code);
    }


    @ApiOperation(value = "查询推流列表", notes = "主要查询当前又多少流转推")
    @GetMapping("/streams")
    public Object getStreams() {

        JSONObject result = null;
        Object data = pushService.getStreams();

        if (null != data) {
            result = Result.success(data);
        } else {
            result = Result.failure(ResultCode.IS_NOTEXIST);
        }

        return result;
    }

    @ApiOperation(value = "查询指定推流信息", notes = "查询指定推流信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "活动名称", required = true, paramType = "query", dataType = "String")

    })
    @GetMapping("/stream/{name}")
    public Object getStream(@PathVariable("name") String name) {

        JSONObject obj = pushService.getStream(name);
        if (null != obj) {
            return Result.success(obj);
        }

        return Result.failure(ResultCode.IS_NOTEXIST);
    }

    @ApiOperation(value = "删除指定推流", notes = "删除指定推流")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "活动名称", required = true, paramType = "query", dataType = "String")

    })

    @DeleteMapping("/stream/{name}")
    public Object Stop(@PathVariable("name") String name) {

        ResultCode code = pushService.Stop(name);
        if (ResultCode.SUCCESS.code() == code.code()) {
            return Result.success();
        }
        return Result.failure(code);
    }

}
