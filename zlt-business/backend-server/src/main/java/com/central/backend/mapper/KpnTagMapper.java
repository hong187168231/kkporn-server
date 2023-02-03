package com.central.backend.mapper;

import com.central.common.model.KpnTag;
import com.central.db.mapper.SuperMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 影片标签
 * 
 * @author yixiu
 * @date 2023-02-03 16:32:41
 */
@Mapper
public interface KpnTagMapper extends SuperMapper<KpnTag> {
    /**
     * 分页查询用户列表
     * @param page
     * @param params
     * @return
     */
    List<KpnTag> findList(Page<KpnTag> page, @Param("p") Map<String, Object> params);
}
