package com.central.backend.service.ipmanage.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.central.backend.mapper.ipmanage.SysWhiteIpMapper;
import com.central.backend.service.ipmanage.ISysWhiteIpService;
import com.central.common.model.ipmanage.SysWhiteIp;
import com.central.common.model.ipmanage.SysWhiteIp;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.common.service.impl.SuperServiceImpl;

import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 *
 * @author yixiu
 * @date 2023-02-03 15:07:56
 */
@Slf4j
@Service
public class SysWhiteIpServiceImpl extends SuperServiceImpl<SysWhiteIpMapper, SysWhiteIp> implements ISysWhiteIpService {
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<SysWhiteIp> findList(Map<String, Object> params){
        Page<SysWhiteIp> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<SysWhiteIp> list  =  baseMapper.findList(page, params);
        return PageResult.<SysWhiteIp>builder().data(list).count(page.getTotal()).build();
    }

    @Override
    public Boolean ipcheck(String ip){
        Boolean b = false;
        LambdaQueryWrapper<SysWhiteIp> wrapper = new LambdaQueryWrapper<>();
        List<SysWhiteIp> list  =  baseMapper.selectList(wrapper);
        if(null!=list&&list.size()>0){
            for(SysWhiteIp sysWhiteIp:list){
                String[] ipBytes = sysWhiteIp.getIp().split("-");
                //判断给定ip地址是否在指定范围内:
                long start = IP2Long( ipBytes[0] );
                long end = IP2Long( ipBytes[1] );
                long ipAddress = IP2Long( ip );
                Boolean inRange = (ipAddress >= start && ipAddress <= end);
                if (inRange){
                    //IP 地址在范围内！
                    return true;
                }
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
