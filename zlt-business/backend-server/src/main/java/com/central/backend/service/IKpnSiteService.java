package com.central.backend.service;

import com.central.backend.co.KpnSiteCo;
import com.central.backend.co.KpnSiteUpdateCo;
import com.central.backend.vo.KpnSiteListVo;
import com.central.backend.vo.KpnSiteVo;
import com.central.common.model.KpnSite;
import com.central.common.model.PageResult;
import com.central.common.model.Result;
import com.central.common.service.ISuperService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/*
 * @Author: Lulu
 * @Date: 2023/2/3
 */
public interface IKpnSiteService extends ISuperService<KpnSite> {

    PageResult<KpnSite> findSiteList( KpnSiteCo params);

    KpnSiteVo findSiteTotal();


    Result saveOrUpdateSite( KpnSite kpnSite,MultipartFile file);


    Result updateEnabledSite(KpnSiteUpdateCo params);

    String getStringRandom(int length) ;


    List<KpnSiteListVo> findSiteBoxList(Integer roleId);


}
