package com.central.backend.service.ipmanage.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.central.backend.mapper.ipmanage.SysWhiteIpMapper;
import com.central.backend.service.ipmanage.ISysWhiteIpService;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
import com.central.common.model.ipmanage.SysWhiteIp;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.common.service.impl.SuperServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                if(ip.equals(sysWhiteIp.getIp())){
                    return true;
                }
                //IP段验证注释掉
//                String[] ipBytes = sysWhiteIp.getIp().split("-");
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
    public Result saveOrUpdateSysWhiteIp(SysWhiteIp sysWhiteIp, SysUser user){
        String ip = sysWhiteIp.getIp();
        if(null==ip || "".equals(ip)){
            return Result.failed("白名单IP不能为空");
        }
        //1.创建匹配模式
        Pattern pattern = Pattern.compile("(([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])");//匹配一个或多个数字字符
        //2.选择匹配对象
        Matcher matcher = pattern.matcher(ip);
        if(!matcher.matches()){
            return Result.failed("白名单IP格式错误");
        }
        if(null!=user) {
            if(null!=sysWhiteIp.getId()){
                sysWhiteIp.setUpdateBy(user.getUsername());
            }else {
                LambdaQueryWrapper<SysWhiteIp> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SysWhiteIp::getIp,ip);
                List<SysWhiteIp> list  =  baseMapper.selectList(wrapper);
                if(null!=list&&list.size()>0){
                    return Result.failed("黑名单IP已存在");
                }
                sysWhiteIp.setCreateBy(user.getUsername());
            }
        }
        this.saveOrUpdate(sysWhiteIp);
        return Result.succeed("保存成功");
    }
}
