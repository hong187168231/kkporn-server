package com.central.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.backend.co.KpnSiteChannelUpdateCo;
import com.central.backend.mapper.KpnSiteChannelMapper;
import com.central.backend.service.IKpnSiteChannelService;
import com.central.backend.util.PictureUtil;
import com.central.common.model.*;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.oss.model.ObjectInfo;
import com.central.oss.template.MinioTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Slf4j
@Service
public class KpnSiteChannelServiceImpl extends SuperServiceImpl<KpnSiteChannelMapper, KpnSiteChannel> implements IKpnSiteChannelService {
    @Autowired
    private MinioTemplate minioTemplate;
    @Override
    public PageResult<KpnSiteChannel> findSiteChannelList( Map<String, Object> params) {
        Page<KpnSiteChannel> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        LambdaQueryWrapper<KpnSiteChannel> wrapper=new LambdaQueryWrapper<>();
        String siteCode = MapUtils.getString(params, "siteCode");
        if (StringUtils.isNotBlank(siteCode)){
            wrapper.eq(KpnSiteChannel::getSiteCode,siteCode);
        }
        wrapper.orderByDesc(KpnSiteChannel::getIsStable).orderByAsc(KpnSiteChannel::getId);
        Page<KpnSiteChannel> list = baseMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        return PageResult.<KpnSiteChannel>builder().data(list.getRecords()).count(total).build();
    }

    @Override
    public Result updateEnabledChannel(KpnSiteChannelUpdateCo params) {
        Long id = params.getId();
        KpnSiteChannel siteChannel = baseMapper.selectById(id);
        if (siteChannel == null) {
            return Result.failed("数据不存在");
        }
        siteChannel.setStatus(params.getStatus());
        siteChannel.setUpdateBy(params.getUpdateBy());
        int i = baseMapper.updateById(siteChannel);
        return i>0 ? Result.succeed(siteChannel, "更新成功"): Result.failed("更新失败");
    }

    @Override
    public Boolean deleteId(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public Result saveOrUpdateSiteChannel(KpnSiteChannel siteChannel) {
        boolean insert =false;
        //新增
        if (siteChannel.getId() == null) {
            insert = super.save(siteChannel);
        }else {
            KpnSiteChannel info = baseMapper.selectById(siteChannel.getId());
            if (info == null) {
                return Result.failed("数据不存在");
            }
            //修改
            insert = super.updateById(siteChannel);
        }
        if(insert){
            return  Result.succeed(siteChannel, "操作成功");
        }
        return Result.failed("操作失败");
    }
}