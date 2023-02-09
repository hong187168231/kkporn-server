package com.central.porn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.KpnMovieTag;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnTag;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.core.language.LanguageUtil;
import com.central.porn.entity.vo.KpnTagVo;
import com.central.porn.mapper.KpnMovieTagMapper;
import com.central.porn.service.IKpnMovieTagService;
import com.central.porn.service.IKpnTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KpnMovieTagServiceImpl extends SuperServiceImpl<KpnMovieTagMapper, KpnMovieTag> implements IKpnMovieTagService {

    @Autowired
    private IKpnTagService tagService;

    @Override
    public List<KpnTagVo> getTagByMovieId(Long movieId) {
        List<KpnTagVo> result = new ArrayList<>();

        //获取影片标签
        String movieTagRedisKey = StrUtil.format(PornConstants.RedisKey.KPN_MOVIEID_TAGS_KEY, movieId);
        List<String> tagIds = (ArrayList) RedisRepository.getList(movieTagRedisKey, 0, -1);
        //查库重新缓存
        if (CollectionUtil.isEmpty(tagIds)) {
            tagIds = this.lambdaQuery().eq(KpnMovieTag::getMovieId, movieId).list()
                    .stream()
                    .map(movieTag -> String.valueOf(movieTag.getTagId()))
                    .collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(tagIds)) {
                RedisRepository.leftPushAll(movieTagRedisKey, tagIds);
                RedisRepository.setExpire(movieTagRedisKey, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS);
            }
        }

        //获取标签信息
        for (String tagId : tagIds) {
            KpnTagVo kpnTagVo = tagService.getByTagId(Long.valueOf(tagId));
            kpnTagVo.setName(LanguageUtil.getLanguageName(kpnTagVo));
            result.add(kpnTagVo);
        }
        return result;
    }
}





















