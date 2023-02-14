package com.central.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.backend.co.KpnSiteTopicUpdateCo;
import com.central.backend.mapper.KpnSiteTopicMapper;
import com.central.backend.service.IKpnSiteTopicService;
import com.central.backend.vo.KpnSiteTopicVo;
import com.central.common.model.*;
import com.central.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class KpnSiteTopicServiceImpl extends SuperServiceImpl<KpnSiteTopicMapper, KpnSiteTopic> implements IKpnSiteTopicService {

    @Override
    public PageResult<KpnSiteTopicVo> findSiteTopicList(Map<String, Object> params) {
        Page<KpnSiteTopicVo> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        String siteCode = MapUtils.getString(params, "siteCode");
        List<KpnSiteTopicVo> list = baseMapper.selectTopicPage(page, siteCode);
        return PageResult.<KpnSiteTopicVo>builder().data(list).count(page.getTotal()).build();
    }

    @Override
    public Result updateEnabledTopic(KpnSiteTopicUpdateCo params) {
        Long id = params.getId();
        Integer state = params.getStatus();
        KpnSiteTopic siteTopicInfo = baseMapper.selectById(id);
        if (siteTopicInfo == null) {
            return Result.failed("数据不存在");
        }
        siteTopicInfo.setStatus(state);
        siteTopicInfo.setUpdateBy(params.getUpdateBy());
        int i = baseMapper.updateById(siteTopicInfo);
        return i > 0 ?Result.succeed(siteTopicInfo, "更新成功") : Result.failed("更新失败");
    }

    @Override
    public Boolean deleteId(Long id) {
        return baseMapper.deleteById(id)> 0;
    }
}