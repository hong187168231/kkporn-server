package com.central.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.backend.co.KpnMoneyLogCo;
import com.central.backend.mapper.KpnMoneyLogMapper;
import com.central.backend.service.IKpnMoneyLogService;
import com.central.common.model.KpnMoneyLog;
import com.central.common.model.PageResult;
import com.central.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class KpnMoneyLogServiceImpl extends SuperServiceImpl<KpnMoneyLogMapper, KpnMoneyLog> implements IKpnMoneyLogService {


    @Override
    public PageResult<KpnMoneyLog> findMoneyLogList(KpnMoneyLogCo params) {
        Page<KpnMoneyLog> page = new Page<>(params.getPage(), params.getLimit());
        LambdaQueryWrapper<KpnMoneyLog> wrapper=new LambdaQueryWrapper<>();
/*        if (StringUtils.isNotBlank(params.getSiteCode())){
            wrapper.eq(KpnMoneyLog::getSiteCode, params.getSiteCode());
        }*/
        if (StringUtils.isNotBlank(params.getUserName())){
            wrapper.eq(KpnMoneyLog::getUserName, params.getUserName());
        }
        if (StringUtils.isNotBlank(params.getOrderNo())){
            wrapper.eq(KpnMoneyLog::getOrderNo, params.getOrderNo());
        }

        if (StringUtils.isNotBlank(params.getStartTime())) {
            wrapper.ge(KpnMoneyLog::getCreateTime, params.getStartTime());
        }
        if (StringUtils.isNotBlank(params.getEndTime())) {
            wrapper.le(KpnMoneyLog::getCreateTime, params.getEndTime());
        }

        wrapper.orderByDesc(KpnMoneyLog::getCreateTime);
        Page<KpnMoneyLog> list = baseMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        return PageResult.<KpnMoneyLog>builder().data(list.getRecords()).count(total).build();
    }
}