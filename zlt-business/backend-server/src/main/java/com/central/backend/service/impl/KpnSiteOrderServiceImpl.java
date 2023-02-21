package com.central.backend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.backend.co.KpnSiteOrderCo;
import com.central.backend.co.KpnSiteOrderTotalCo;
import com.central.backend.co.KpnSiteOrderUpdateCo;
import com.central.backend.mapper.KpnSiteOrderMapper;
import com.central.backend.service.IKpnSiteOrderService;
import com.central.common.model.*;
import com.central.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;;import java.math.BigDecimal;
import java.util.List;


@Slf4j
@Service
public class KpnSiteOrderServiceImpl extends SuperServiceImpl<KpnSiteOrderMapper, KpnSiteOrder> implements IKpnSiteOrderService {


    @Override
    public PageResult<KpnSiteOrder> findOrderList(KpnSiteOrderCo params) {
        Page<KpnSiteOrder> page = new Page<>(params.getPage(), params.getLimit());
        LambdaQueryWrapper<KpnSiteOrder> wrapper=new LambdaQueryWrapper<>();
        if (params.getSiteId()!=null){
            wrapper.eq(KpnSiteOrder::getSiteId, params.getSiteId());
        }
        if (StringUtils.isNotBlank(params.getUserName())){
            wrapper.eq(KpnSiteOrder::getUserName, params.getUserName());
        }
        if (StringUtils.isNotBlank(params.getOrderNo())){
            wrapper.eq(KpnSiteOrder::getOrderNo, params.getOrderNo());
        }

        if (StringUtils.isNotBlank(params.getStartTime())) {
            wrapper.ge(KpnSiteOrder::getCreateTime, params.getStartTime());
        }
        if (StringUtils.isNotBlank(params.getEndTime())) {
            wrapper.le(KpnSiteOrder::getCreateTime, params.getEndTime());
        }

        if (params.getStatus()!=null){
            wrapper.eq(KpnSiteOrder::getStatus, params.getStatus());
        }
        wrapper.orderByDesc(KpnSiteOrder::getCreateTime);
        Page<KpnSiteOrder> list = baseMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        return PageResult.<KpnSiteOrder>builder().data(list.getRecords()).count(total).build();
    }

    @Override
    public BigDecimal findOrderTotal(KpnSiteOrderTotalCo params) {
        return  baseMapper.findOrderTotal(params);
    }

    @Override
    public Result updateStatus(KpnSiteOrderUpdateCo params) {
        Long id = params.getId();
        Integer state = params.getStatus();
        KpnSiteOrder siteOrder = baseMapper.selectById(id);
        if (siteOrder == null) {
            return Result.failed("数据不存在");
        }
        siteOrder.setStatus(state);
        if (StringUtils.isNotBlank(params.getRemark())) {
            siteOrder.setRemark(params.getRemark());
        }
        siteOrder.setUpdateBy(params.getUpdateBy());
        int i = baseMapper.updateById(siteOrder);
        return i > 0 ?Result.succeed(siteOrder, "更新成功") : Result.failed("更新失败");
    }

    @Override
    public  List<KpnSiteOrder> findOrderMobileList(List<Long> userIds) {
        return baseMapper.findOrderMobileList(userIds);
    }
}