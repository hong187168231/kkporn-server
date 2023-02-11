package com.central.backend.controller.ipmanage;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.central.backend.service.ipmanage.IKpnBlackIpService;
import com.central.common.model.ipmanage.KpnBlackIp;
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
 * @date 2023-02-03 15:50:11
 */
@Slf4j
@RestController
@RequestMapping("/kpnblackip")
@Api(tags = "")
public class KpnBlackIpController {
    @Autowired
    private IKpnBlackIpService kpnBlackIpService;

    /**
     * 列表
     */
    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping
    public PageResult list(@RequestParam Map<String, Object> params) {
        return kpnBlackIpService.findList(params);
    }

    /**
     * 查询
     */
    @ApiOperation(value = "查询")
    @GetMapping("/{id}")
    public Result findUserById(@PathVariable Long id) {
        KpnBlackIp model = kpnBlackIpService.getById(id);
        return Result.succeed(model, "查询成功");
    }

    /**
     * IP黑名单检查
     * @param ip
     * @return
     */
    @ApiOperation(value = "IP黑名单检查")
    @GetMapping("/ipcheck")
    public Boolean ipcheck(@PathVariable String ip) {
        return kpnBlackIpService.ipcheck(ip);
    }

    /**
     * 新增or更新
     */
    @ApiOperation(value = "保存")
    @PostMapping
    public Result save(@RequestBody KpnBlackIp kpnBlackIp) {
        String ip = kpnBlackIp.getIpSection();
        if(null==ip || "".equals(ip)){
            Result.failed("黑名单IP段不能为空");
        }
        //1.创建匹配模式
        Pattern pattern = Pattern.compile("(25[0-5]|2[0-4]\\\\d|1\\\\d{2}|[1-9]?\\\\d)(\\\\.(25[0-5]|2[0-4]\\\\d|1\\\\d{2}|[1-9]?\\\\d)){3}-(25[0-5]|2[0-4]\\\\d|1\\\\d{2}|[1-9]?\\\\d)(\\\\.(25[0-5]|2[0-4]\\\\d|1\\\\d{2}|[1-9]?\\\\d)){3}");//匹配一个或多个数字字符
        //2.选择匹配对象
        Matcher matcher = pattern.matcher(ip);
        if(!matcher.matches()){
            Result.failed("黑名单IP段格式错误");
        }
        kpnBlackIpService.saveOrUpdate(kpnBlackIp);
        return Result.succeed("保存成功");
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        kpnBlackIpService.removeById(id);
        return Result.succeed("删除成功");
    }
}
