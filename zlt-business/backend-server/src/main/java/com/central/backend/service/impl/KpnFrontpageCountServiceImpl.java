package com.central.backend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.central.backend.mapper.KpnFrontpageCountMapper;
import com.central.backend.model.vo.KpnFrontpageCountVO;
import com.central.backend.service.IKpnFrontpageCountService;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnFrontpageCount;
import com.central.common.redis.template.RedisRepository;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.common.service.impl.SuperServiceImpl;

import java.math.BigDecimal;
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
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public KpnFrontpageCountVO findSummaryData(Map<String, Object> params){
        //1：今日 2：昨日 3：本月 4：总计
        Integer status = MapUtils.getInteger(params, "status");
        KpnFrontpageCountVO kpnFrontpageCountVO = null;
        Long pv = 0L;//访问量
        Long uv = 0L;//独立访客数
        Long onlineUsers = 0L;//在线人数
        Long rechargeNumber = 0L;//充值单数
        BigDecimal rechargeAmount = BigDecimal.ZERO;//充值金额
        Long addUsers = 0L;//每日新增会员人数
        if(2!=status){//查询缓存
            String redisPVKey = StrUtil.format(PornConstants.RedisKey.KPN_PV_KEY);
            pv = (Long)RedisRepository.get(redisPVKey);//访问量
            String redisUVKey = StrUtil.format(PornConstants.RedisKey.KPN_UV_KEY);
            uv = (Long)RedisRepository.get(redisUVKey);//独立访客数
        }
        if(1==status){
            kpnFrontpageCountVO = new KpnFrontpageCountVO();
            kpnFrontpageCountVO.setPvCount(pv);
            kpnFrontpageCountVO.setUvCount(uv);
            kpnFrontpageCountVO.setOnlineUsers(onlineUsers);
            kpnFrontpageCountVO.setRechargeNumber(rechargeNumber);
            kpnFrontpageCountVO.setRechargeAmount(rechargeAmount);
            kpnFrontpageCountVO.setAddUsers(addUsers);
            return kpnFrontpageCountVO;
        }else{
            kpnFrontpageCountVO  =  baseMapper.findSummaryData(params);
            if(2==status){
                return kpnFrontpageCountVO;
            }else {
                //加上今日缓存数据
                kpnFrontpageCountVO.setPvCount(pv+kpnFrontpageCountVO.getPvCount());
                kpnFrontpageCountVO.setUvCount(uv+kpnFrontpageCountVO.getUvCount());
                kpnFrontpageCountVO.setOnlineUsers(onlineUsers);
                kpnFrontpageCountVO.setRechargeNumber(rechargeNumber+kpnFrontpageCountVO.getRechargeNumber());
                kpnFrontpageCountVO.setRechargeAmount(rechargeAmount.add(kpnFrontpageCountVO.getRechargeAmount()));
                kpnFrontpageCountVO.setAddUsers(addUsers+kpnFrontpageCountVO.getAddUsers());
            }
        }

        return kpnFrontpageCountVO;
    }
}
