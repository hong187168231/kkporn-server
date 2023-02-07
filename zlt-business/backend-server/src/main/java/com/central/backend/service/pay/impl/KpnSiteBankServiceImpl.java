package com.central.backend.service.pay.impl;

import com.central.backend.mapper.pay.KpnSiteBankMapper;
import com.central.backend.service.pay.IKpnSiteBankService;
import com.central.common.model.SysUser;
import com.central.common.model.pay.KpnSiteBank;
import com.central.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 收款银行渠道
 *
 * @author yixiu
 * @date 2023-02-03 19:35:22
 */
@Slf4j
@Service
public class KpnSiteBankServiceImpl extends SuperServiceImpl<KpnSiteBankMapper, KpnSiteBank> implements IKpnSiteBankService {
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnSiteBank> findList(Map<String, Object> params, SysUser user){
        if(user.getSiteId()==null || user.getSiteId()==0){//
            params.put("siteId","");
        }else {
            params.put("siteId",user.getSiteId());
        }
        Page<KpnSiteBank> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnSiteBank> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnSiteBank>builder().data(list).count(page.getTotal()).build();
    }
}
