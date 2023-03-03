package com.central.backend.controller.pay;

import java.util.Map;

import com.central.backend.service.pay.IKpnSiteBankCardService;
import com.central.common.annotation.LoginUser;
import com.central.common.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.central.common.model.pay.KpnSiteBankCard;
import com.central.common.model.PageResult;
import com.central.common.model.Result;

/**
 * 收款银行卡配置
 *
 * @author yixiu
 * @date 2023-02-03 19:35:22
 */
@Slf4j
@RestController
@RequestMapping("/kpnsitebankcard")
@Api(tags = "收款银行卡配置")
public class KpnSiteBankCardController {
    @Autowired
    private IKpnSiteBankCardService kpnSiteBankCardService;

    /**
     * 列表
     */
    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping
    public Result<PageResult> list(@RequestParam Map<String, Object> params,@LoginUser SysUser user) {
        return Result.succeed(kpnSiteBankCardService.findList(params,user));
    }

    /**
     * 查询
     */
    @ApiOperation(value = "查询")
    @GetMapping("/{id}")
    public Result findUserById(@PathVariable Long id) {
        KpnSiteBankCard model = kpnSiteBankCardService.getById(id);
        return Result.succeed(model, "查询成功");
    }

    /**
     * 新增or更新
     */
    @ApiOperation(value = "新增or更新")
    @PostMapping
    public Result saveOrUpdateKpnSiteBankCard(@RequestBody KpnSiteBankCard kpnSiteBankCard, @LoginUser SysUser user) {
        return kpnSiteBankCardService.saveOrUpdateKpnSiteBankCard(kpnSiteBankCard,user);
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        kpnSiteBankCardService.removeById(id);
        return Result.succeed("删除成功");
    }
}
