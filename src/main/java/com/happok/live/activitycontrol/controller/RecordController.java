package com.happok.live.activitycontrol.controller;


import com.alibaba.fastjson.JSONObject;
import com.happok.live.activitycontrol.common.Result;
import com.happok.live.activitycontrol.common.ResultCode;
import com.happok.live.activitycontrol.service.RecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "录制controller", tags = {"录制控制接口"})
@RestController
@RequestMapping("/v1/api/")

public class RecordController {

    @Autowired
    private RecordService recordService = null;

    /* @GetMapping("/records")
     public Object getRecords() {

         return Result.success(recordService.getRecords());
     }
 */
    @GetMapping("/records/{dirName}")
    public Object getRecord(@PathVariable("dirName") String dirName) {

        return Result.success(recordService.getRecord(dirName));
    }

    @GetMapping("/records/status/{dirName}")
    public Object getRecordStatus(@PathVariable("dirName") String dirName) {
        return recordService.getRecordStatus(dirName);
    }

    @PostMapping("/records")
    public Object StartRecord(@RequestBody String body) {
        JSONObject jsonResult = JSONObject.parseObject(body);
        String dirName = jsonResult.getString("dirName");
        String srcUrl = jsonResult.getString("srcUrl");

        ResultCode code = recordService.Start(dirName, srcUrl);
        if (code.code() == ResultCode.SUCCESS.code()) {
            return Result.success();
        }

        return Result.failure(code);
    }

    @DeleteMapping("/records/{dirName}")
    public Object StopRecord(@PathVariable("dirName") String dirName) {

        ResultCode code = recordService.Stop(dirName);
        if (code.code() == ResultCode.SUCCESS.code()) {
            return Result.success();
        }

        return Result.failure(code);
    }

    @DeleteMapping("/records/file/{dirName}")
    public Object DeletRecordFile(@PathVariable("dirName") String dirName) {

        ResultCode code = recordService.deleteDirName(dirName);
        if (code.code() == ResultCode.SUCCESS.code()) {
            return Result.success();
        }

        return Result.failure(code);
    }
}
