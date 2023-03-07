package com.central.backend.mapper.pay;

import com.central.common.model.pay.KpnSiteProduct;
import com.central.db.mapper.SuperMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * vip产品
 * 
 * @author yixiu
 * @date 2023-02-03 19:35:22
 */
@Mapper
public interface KpnSiteProductMapper extends SuperMapper<KpnSiteProduct> {
    /**
     * 分页查询用户列表
     * @param page
     * @param params
     * @return
     */
    List<KpnSiteProduct> findList(Page<KpnSiteProduct> page, @Param("p") Map<String, Object> params);
    List<KpnSiteProduct> findList(@Param("p") Map<String, Object> params);
}
