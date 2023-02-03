package com.central.backend.controller.pay;

import java.util.Map;

import com.central.backend.service.pay.IKpnSiteProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import com.central.common.model.pay.KpnSiteProduct;
import com.central.common.model.PageResult;
import com.central.common.model.Result;

/**
 * vip产品
 *
 * @author yixiu
 * @date 2023-02-03 19:35:22
 */
@Slf4j
@RestController
@RequestMapping("/kpnsiteproduct")
@Api(tags = "vip产品")
public class KpnSiteProductController {
    @Autowired
    private IKpnSiteProductService kpnSiteProductService;

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
        return kpnSiteProductService.findList(params);
    }

    /**
     * 查询
     */
    @ApiOperation(value = "查询")
    @GetMapping("/{id}")
    public Result findUserById(@PathVariable Long id) {
        KpnSiteProduct model = kpnSiteProductService.getById(id);
        return Result.succeed(model, "查询成功");
    }

    /**
     * 新增or更新
     */
    @ApiOperation(value = "保存")
    @PostMapping
    public Result save(@RequestBody KpnSiteProduct kpnSiteProduct) {
        kpnSiteProductService.saveOrUpdate(kpnSiteProduct);
        return Result.succeed("保存成功");
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        kpnSiteProductService.removeById(id);
        return Result.succeed("删除成功");
    }
}
