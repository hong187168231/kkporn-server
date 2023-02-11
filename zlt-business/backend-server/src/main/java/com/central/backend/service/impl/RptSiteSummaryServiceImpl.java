package com.central.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.backend.co.RptSiteSummaryCo;
import com.central.backend.mapper.RptSiteSummaryMapper;
import com.central.backend.service.IRptSiteSummaryService;
import com.central.backend.vo.RptSiteSummaryVo;
import com.central.common.model.*;
import com.central.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RptSiteSummaryServiceImpl extends SuperServiceImpl<RptSiteSummaryMapper, RptSiteSummary> implements IRptSiteSummaryService {

    @Override
    public PageResult<RptSiteSummary> findSummaryList(RptSiteSummaryCo params) {
        Page<RptSiteSummary> page = new Page<>(params.getPage(), params.getLimit());
        LambdaQueryWrapper<RptSiteSummary> wrapper=new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(params.getStartTime())) {
            wrapper.ge(RptSiteSummary::getCreateTime, params.getStartTime());
        }
        if (StringUtils.isNotBlank(params.getEndTime())) {
            wrapper.le(RptSiteSummary::getCreateTime, params.getEndTime());
        }
        wrapper.orderByDesc(RptSiteSummary::getDate);
        Page<RptSiteSummary> list = baseMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        return PageResult.<RptSiteSummary>builder().data(list.getRecords()).count(total).build();
    }

    @Override
    public RptSiteSummaryVo findSummaryTotal(RptSiteSummaryCo params) {
        return baseMapper.findSummaryTotal(params);
    }
}