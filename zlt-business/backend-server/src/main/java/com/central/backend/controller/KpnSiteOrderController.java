package com.central.backend.controller;

import cn.hutool.core.util.StrUtil;
import com.central.backend.co.*;
import com.central.backend.service.IKpnSiteOrderService;
import com.central.backend.service.IKpnSiteUserVipLogService;
import com.central.backend.service.ISysUserService;
import com.central.backend.vo.UserVipExpireVo;
import com.central.common.model.*;
import com.central.common.model.enums.VipChangeTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@Slf4j
@RestController
@Api(tags = "订单列表api")
@Validated
@RequestMapping("/order")
public class KpnSiteOrderController {

    @Autowired
    private IKpnSiteOrderService orderService;

    @Autowired
    private IKpnSiteUserVipLogService siteUserVipLogService;


    @Autowired
    private ISysUserService userService;


    @ApiOperation("查询订单列表列表")
    @ResponseBody
    @GetMapping("/findOrderList")
    public Result<PageResult<KpnSiteOrder>> findOrderList(@ModelAttribute KpnSiteOrderCo params) {
        PageResult<KpnSiteOrder> siteOrderList = orderService.findOrderList(params);
        return Result.succeed(siteOrderList);
    }


    @ApiOperation(value = "总计")
    @GetMapping("/findOrderTotal")
    public Result<BigDecimal> findOrderTotal(@ModelAttribute KpnSiteOrderTotalCo params) {
        return Result.succeed(orderService.findOrderTotal(params));
    }


    @ApiOperation(value = "审核")
    @GetMapping("/updateStatus")
    @Transactional(rollbackFor = Exception.class)
    public Result updateStatus(@Valid @ModelAttribute KpnSiteOrderUpdateCo params) {
        // params.setUpdateBy(sysUser.getUsername());
        Result result = orderService.updateStatus(params);
        if (result.getResp_code()==0 && params.getStatus()==1){
            //查询订单信息
            KpnSiteUserVipLog KpnSiteUserVipLogInfo = orderService.findKpnSiteOrderInfo(params.getId());
            //修改会员vip到期时间
            Result<UserVipExpireVo> userResult = userService.updateUserVipExpire(KpnSiteUserVipLogInfo.getUserId(), KpnSiteUserVipLogInfo.getDays());
            if (userResult.getResp_code()==0){
                UserVipExpireVo userInfo = userResult.getDatas();
                KpnSiteUserVipLogInfo.setBeforeExpire(userInfo.getBeforeExpire());
                KpnSiteUserVipLogInfo.setAfterExpire(userInfo.getAfterExpire());
            }
            //添加vip日志
            String remark = StrUtil.format(VipChangeTypeEnum.getLogFormatByCode(VipChangeTypeEnum.CASH.getCode()),KpnSiteUserVipLogInfo.getAmount(), KpnSiteUserVipLogInfo.getCurrencyCode(), KpnSiteUserVipLogInfo.getDays());
            KpnSiteUserVipLogInfo.setRemark(remark);
            KpnSiteUserVipLogInfo.setType(VipChangeTypeEnum.CASH.getCode());
           // KpnSiteUserVipLogInfo.setCreateBy(sysUser.getUsername());
             siteUserVipLogService.save(KpnSiteUserVipLogInfo);
        }
        return result;
    }
}
