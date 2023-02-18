package com.central.backend.service.ipmanage.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.central.backend.mapper.ipmanage.KpnBlackIpMapper;
import com.central.backend.service.ipmanage.IKpnBlackIpService;
import com.central.common.model.ipmanage.KpnBlackIp;
import com.central.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 *
 * @author yixiu
 * @date 2023-02-03 15:50:11
 */
@Slf4j
@Service
public class KpnBlackIpServiceImpl extends SuperServiceImpl<KpnBlackIpMapper, KpnBlackIp> implements IKpnBlackIpService {
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnBlackIp> findList(Map<String, Object> params){
        Page<KpnBlackIp> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnBlackIp> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnBlackIp>builder().data(list).count(page.getTotal()).build();
    }

    @Override
    public Boolean ipcheck(String ip){
        Boolean b = false;
        LambdaQueryWrapper<KpnBlackIp> wrapper = new LambdaQueryWrapper<>();
        List<KpnBlackIp> list  =  baseMapper.selectList(wrapper);
        if(null!=list&&list.size()>0){
            for(KpnBlackIp kpnBlackIp:list){
                if(ip.equals(kpnBlackIp.getIpSection())){
                    return true;
                }
//                String[] ipBytes = kpnBlackIp.getIpSection().split("-");
//                //判断给定ip地址是否在指定范围内:
//                long start = IP2Long( ipBytes[0] );
//                long end = IP2Long( ipBytes[1] );
//                long ipAddress = IP2Long( ip );
//                Boolean inRange = (ipAddress >= start && ipAddress <= end);
//                if (inRange){
//                    //IP 地址在范围内！
//                    return true;
//                }
            }
        }
        return b;
    }
    public long IP2Long(String ip)
    {
        String[] ipBytes;
        double num = 0;
        ipBytes = ip.split(".");
        for (int i = ipBytes.length - 1; i >= 0; i--)
        {
            num += ((Integer.parseInt(ipBytes[i]) % 256) * Math.pow(256, (3 - i)));
        }
        return (long)num;
    }

}
