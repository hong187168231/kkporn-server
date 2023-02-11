package com.central.porn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.central.common.model.KpnSiteActor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KpnSiteActorMapper extends BaseMapper<KpnSiteActor> {

    /**
     * 获取演员列表-按收藏量
     *
     * @param sid       站点id
     * @param sortOrder 排序顺序
     * @return
     */
    List<Long> getActorListByFavorites(@Param("sid") Long sid, @Param("sortOrder") String sortOrder);

    /**
     * 获取演员列表-按创建时间
     *
     * @param sid       站点id
     * @param sortOrder 排序顺序
     * @return
     */
    List<Long> getActorListByCreateTime(@Param("sid") Long sid, @Param("sortOrder") String sortOrder);
}
