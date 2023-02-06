package com.central.porn.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnTag;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.entity.vo.KpnTagVo;
import com.central.porn.mapper.KpnTagMapper;
import com.central.porn.service.IKpnTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KpnTagServiceImpl extends SuperServiceImpl<KpnTagMapper, KpnTag> implements IKpnTagService {

    @Override
    public KpnTagVo getByTagId(Long tagId) {
        String tagRedisKey = StrUtil.format(PornConstants.RedisKey.KPN_TAGID_KEY, tagId);
        KpnTag kpnTag = (KpnTag) RedisRepository.get(tagRedisKey);

        if (ObjectUtil.isEmpty(kpnTag)) {
            kpnTag = this.getById(tagId);
            if (ObjectUtil.isNotEmpty(kpnTag)) {
                RedisRepository.set(tagRedisKey, kpnTag);
            }
        }

        KpnTagVo kpnTagVo = new KpnTagVo();
        BeanUtil.copyProperties(kpnTag, kpnTagVo);

        return kpnTagVo;
    }
}
