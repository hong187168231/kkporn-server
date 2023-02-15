package com.central.porn.service.impl;

import com.central.common.KpnMovieTag;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.entity.vo.KpnTagVo;
import com.central.porn.mapper.KpnMovieTagMapper;
import com.central.porn.service.IKpnMovieTagService;
import com.central.porn.service.IKpnTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KpnMovieTagServiceImpl extends SuperServiceImpl<KpnMovieTagMapper, KpnMovieTag> implements IKpnMovieTagService {

    @Autowired
    private IKpnTagService tagService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public List<KpnTagVo> getTagByMovieId(Long movieId) {
        List<KpnTagVo> result = new ArrayList<>();

        List<Long> tagIds = this.lambdaQuery().eq(KpnMovieTag::getMovieId, movieId).list()
                .stream()
                .map(KpnMovieTag::getId)
                .collect(Collectors.toList());

        result.addAll(tagService.getByTagIds(tagIds));
        return result;
    }
}





















