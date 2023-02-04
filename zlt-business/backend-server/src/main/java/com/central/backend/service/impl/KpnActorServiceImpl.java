package com.central.backend.service.impl;

import com.central.backend.mapper.KpnActorMapper;
import com.central.backend.model.vo.KpnActorVO;
import com.central.backend.service.IKpnActorService;
import com.central.common.model.KpnActor;
import com.central.common.model.SysUser;
import com.central.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 演员列表
 *
 * @author yixiu
 * @date 2023-02-03 16:31:09
 */
@Slf4j
@Service
public class KpnActorServiceImpl extends SuperServiceImpl<KpnActorMapper, KpnActor> implements IKpnActorService {
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnActorVO> findList(Map<String, Object> params, SysUser user){
        if(user.getSiteId()==null || user.getSiteId()==0){//
            params.put("headquarters","1");
        }else {
            params.put("headquarters","0");
            params.put("siteId",user.getSiteId());
        }
        Page<KpnActorVO> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnActorVO> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnActorVO>builder().data(list).count(page.getTotal()).build();
    }
}
