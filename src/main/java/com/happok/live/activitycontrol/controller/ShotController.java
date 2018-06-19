package com.happok.live.activitycontrol.controller;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.activitycontrol.common.Result;
import com.happok.live.activitycontrol.common.ResultCode;
import com.happok.live.activitycontrol.service.ShotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "截图controller", tags = {"截图控制接口"})
@RestController
@RequestMapping("/v1/api/")
public class ShotController {

    @Autowired
    private ShotService shotService;

    @ApiOperation(value = "截图", notes = "获取直播图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirName", value = "活动ID（目录名）", required = true, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "srcUrl", value = "源流地址", required = true, paramType = "body", dataType = "String")
    })
    @PostMapping("/screenshot")
    public Object getImage(@RequestBody String body) {
        JSONObject jsonResult = JSONObject.parseObject(body);

        String dirName = jsonResult.getString("dirName");
        String srcUrl = jsonResult.getString("srcUrl");
        if (null == jsonResult.getString("dirName") || null == jsonResult.getString("srcUrl")) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        Object image = shotService.StartShot(dirName, srcUrl);
        return Result.success(image);
    }

    @ApiOperation(value = "删除", notes = "删除活动截图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirName", value = "活动ID（目录名）", required = true, paramType = "query", dataType = "String")

    })

    @DeleteMapping("/screenshot/{dirname}")
    public JSONObject deleteAllScreenShot(@PathVariable("dirname") String dirname) {

        JSONObject result = null;
        if (shotService.deleteAllImage(dirname)) {
            result = Result.success();
        } else {
            result = Result.failure(ResultCode.IS_NOTEXIST);
        }

        return result;
    }

    @ApiOperation(value = "删除指定图片", notes = "删除指定截图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirName", value = "活动ID（目录名）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "filename", value = "图片文件名", required = true, paramType = "query", dataType = "String")

    })

    @DeleteMapping("/screenshot/{dirname}/file/{filename}")
    public JSONObject deleteScreenShot(@PathVariable("dirname") String dirname, @PathVariable("filename") String filename) {

        JSONObject result = null;
        if (shotService.deleteImage(dirname, filename)) {
            result = Result.success();
        } else {
            result = Result.failure(ResultCode.IS_NOTEXIST);
        }

        return result;
    }
}
