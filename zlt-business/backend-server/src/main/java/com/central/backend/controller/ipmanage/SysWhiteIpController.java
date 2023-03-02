package com.central.backend.controller.ipmanage;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.central.backend.service.ipmanage.ISysWhiteIpService;
import com.central.common.model.ipmanage.SysWhiteIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import com.central.common.model.PageResult;
import com.central.common.model.Result;

/**
 * 
 *
 * @author yixiu
 * @date 2023-02-03 15:07:56
 */
@Slf4j
@RestController
@RequestMapping("/syswhiteip")
@Api(tags = "")
public class SysWhiteIpController {
    @Autowired
    private ISysWhiteIpService sysWhiteIpService;

    /**
     * 列表
     */
    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping
    public Result<PageResult> list(@RequestParam Map<String, Object> params) {
        return Result.succeed(sysWhiteIpService.findList(params));
    }

    /**
     * IP白名单检查
     * @param ip
     * @return
     */
    @ApiOperation(value = "IP白名单检查")
    @GetMapping("/ipcheck")
    public Boolean ipcheck(@PathVariable String ip) {
        return sysWhiteIpService.ipcheck(ip);
    }

    /**
     * 查询
     */
    @ApiOperation(value = "查询")
    @GetMapping("/{id}")
    public Result findUserById(@PathVariable Long id) {
        SysWhiteIp model = sysWhiteIpService.getById(id);
        return Result.succeed(model, "查询成功");
    }

    /**
     * 新增or更新
     */
    @ApiOperation(value = "保存")
    @PostMapping
    public Result save(@RequestBody SysWhiteIp sysWhiteIp) {
        String ip = sysWhiteIp.getIp();
        if(null==ip || "".equals(ip)){
            Result.failed("白名单IP不能为空");
        }
        //1.创建匹配模式
        Pattern pattern = Pattern.compile("(([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])");//匹配一个或多个数字字符
        //2.选择匹配对象
        Matcher matcher = pattern.matcher(ip);
        if(!matcher.matches()){
            Result.failed("白名单IP格式错误");
        }
        sysWhiteIpService.saveOrUpdate(sysWhiteIp);
        return Result.succeed("保存成功");
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        sysWhiteIpService.removeById(id);
        return Result.succeed("删除成功");
    }
}
