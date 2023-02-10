package com.central.backend.service;

import com.central.backend.model.vo.KpnFrontpageCountVO;
import com.central.common.model.KpnFrontpageCount;
import com.central.common.model.PageResult;
import com.central.common.model.SysUser;
import com.central.common.service.ISuperService;

import java.util.List;
import java.util.Map;

/**
 * 首页访问量统计
 *
 * @author yixiu
 * @date 2023-02-09 19:41:45
 */
public interface IKpnFrontpageCountService extends ISuperService<KpnFrontpageCount> {
    /**
     * 列表
     * @param params
     * @return
     */
    KpnFrontpageCountVO findSummaryData(Map<String, Object> params, SysUser user);

    /**
     * 数据走势
     * @param params
     * @param user
     * @return
     */
    List<KpnFrontpageCount> dataTrend(Map<String, Object> params, SysUser user);
}

