package com.central.backend.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.backend.co.KpnSiteCo;
import com.central.backend.vo.KpnSiteListVo;
import com.central.backend.vo.KpnSiteVo;
import com.central.common.model.KpnSite;
import com.central.db.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
 * @Author: Lulu
 * @Date: 2023/2/3
 */
@Mapper
public interface KpnSiteMapper extends SuperMapper<KpnSite> {

    List<KpnSite> findList(Page<KpnSite> page, @Param("r") KpnSiteCo params);


    KpnSiteVo findSiteTotal();


    List<KpnSiteListVo>  findSiteBoxList();
}
