package com.central.backend.service;

import com.central.backend.co.KpnSiteAdvertiseCo;
import com.central.backend.co.KpnSiteAdvertiseUpdateCo;
import com.central.common.model.KpnSiteAdvertise;
import com.central.common.model.PageResult;
import com.central.common.model.Result;
import com.central.common.service.ISuperService;
import org.springframework.web.multipart.MultipartFile;


/*
 * @Author: Lulu
 * @Date: 2023/2/3
 */
public interface IKpnSiteAdvertiseService extends ISuperService<KpnSiteAdvertise> {

    PageResult<KpnSiteAdvertise> findAdvertiseList(KpnSiteAdvertiseCo params);

    Result saveOrUpdateAdvertise(KpnSiteAdvertise advertise, MultipartFile file);

    Result updateEnabledAdvertise(KpnSiteAdvertiseUpdateCo params);

}