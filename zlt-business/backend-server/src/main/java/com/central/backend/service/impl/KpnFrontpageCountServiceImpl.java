package com.central.backend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.central.backend.mapper.KpnFrontpageCountMapper;
import com.central.backend.model.vo.KpnFrontpageCountVO;
import com.central.backend.service.IKpnFrontpageCountService;
import com.central.backend.service.IKpnMoneyLogService;
import com.central.backend.service.ISysUserService;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnFrontpageCount;
import com.central.common.model.SysUser;
import com.central.common.model.enums.UserTypeEnum;
import com.central.common.redis.template.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.backend.model.vo.KpnMoneyLogVO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 首页访问量统计
 *
 * @author yixiu
 * @date 2023-02-09 19:41:45
 */
@Slf4j
@Service
public class KpnFrontpageCountServiceImpl extends SuperServiceImpl<KpnFrontpageCountMapper, KpnFrontpageCount> implements IKpnFrontpageCountService {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IKpnMoneyLogService moneyLogService;
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public KpnFrontpageCountVO findSummaryData(Map<String, Object> params,SysUser user){
        //1：今日 2：昨日 3：本月 4：总计
        Integer status = MapUtils.getInteger(params, "status");
        KpnFrontpageCountVO kpnFrontpageCountVO = null;
        Long pv = 0L;//访问量
        Long uv = 0L;//独立访客数
        //实时在线人数
        Map<String, Object> userparams = new HashMap<>();
        userparams.put("isDel",false);
        userparams.put("isLogin",true);
        userparams.put("type", UserTypeEnum.APP.name());
        if(null!=user&&null!=user.getSiteId()&&0!=user.getSiteId()) {
            userparams.put("siteId",user.getSiteId());
        }
        Integer onlineUsers = userService.findUserNum(userparams);
        Map<String, Object> moneyparams = new HashMap<>();
        moneyparams.put("orderType","1");
        moneyparams.put("status","1");
        KpnMoneyLogVO moneyLogVO = moneyLogService.totalNumber(moneyparams,user);
        Long rechargeNumber = null!=moneyLogVO.getTotalNumber()?moneyLogVO.getTotalNumber():0L;//充值单数
        BigDecimal rechargeAmount = null!=moneyLogVO.getMoney()?moneyLogVO.getMoney():BigDecimal.ZERO;//充值金额
        userparams.remove("isLogin");
        userparams.put("startTime",new Date());
        Integer addUsers = userService.findUserNum(userparams);//每日新增会员人数
        if(2!=status){//查询缓存
            String redisPVKey = StrUtil.format(PornConstants.RedisKey.KPN_PV_KEY);
            String redisUVKey = StrUtil.format(PornConstants.RedisKey.KPN_UV_KEY);
            if(null!=user&&null!=user.getSiteId()&&0!=user.getSiteId()) {
                pv = (Long) RedisRepository.get(redisPVKey + user.getSiteId());//访问量
                uv = (Long) RedisRepository.get(redisUVKey + user.getSiteId());//独立访客数
            }else {
                pv = (Long) RedisRepository.get(redisPVKey + "*");//访问量
                uv = (Long) RedisRepository.get(redisUVKey + "*");//独立访客数
            }
        }
        if(1==status){
            kpnFrontpageCountVO = new KpnFrontpageCountVO();
            kpnFrontpageCountVO.setPvCount(pv);
            kpnFrontpageCountVO.setUvCount(uv);
            kpnFrontpageCountVO.setOnlineUsers(Long.valueOf(onlineUsers));
            kpnFrontpageCountVO.setRechargeNumber(rechargeNumber);
            kpnFrontpageCountVO.setRechargeAmount(rechargeAmount);
            kpnFrontpageCountVO.setAddUsers(Long.valueOf(addUsers));
            return kpnFrontpageCountVO;
        }else{
            kpnFrontpageCountVO  =  baseMapper.findSummaryData(params);
            if(2==status){
                return kpnFrontpageCountVO;
            }else {
                //加上今日缓存数据
                kpnFrontpageCountVO.setPvCount(pv+kpnFrontpageCountVO.getPvCount());
                kpnFrontpageCountVO.setUvCount(uv+kpnFrontpageCountVO.getUvCount());
                kpnFrontpageCountVO.setOnlineUsers(Long.valueOf(onlineUsers));
                kpnFrontpageCountVO.setRechargeNumber(rechargeNumber+kpnFrontpageCountVO.getRechargeNumber());
                kpnFrontpageCountVO.setRechargeAmount(rechargeAmount.add(kpnFrontpageCountVO.getRechargeAmount()));
                kpnFrontpageCountVO.setAddUsers(addUsers+kpnFrontpageCountVO.getAddUsers());
            }
        }

        return kpnFrontpageCountVO;
    }

    @Override
    public List<KpnFrontpageCount> dataTrend(Map<String, Object> params,SysUser user){
        if(null!=user && user.getSiteId()!=null && user.getSiteId()!=0){
            params.put("siteId",user.getSiteId());
        }
        List<KpnFrontpageCount> kpnFrontpageCountList =  baseMapper.dataTrend(params);

        return kpnFrontpageCountList;
    }
}
