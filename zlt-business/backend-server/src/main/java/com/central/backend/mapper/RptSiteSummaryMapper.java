package com.central.backend.mapper;

import com.central.backend.co.RptSiteSummaryCo;
import com.central.backend.vo.RptSiteSummaryVo;
import com.central.common.model.RptSiteSummary;
import com.central.db.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/*
 * @Author: Lulu
 * @Date: 2023/2/10
 */
@Mapper
public interface RptSiteSummaryMapper extends SuperMapper<RptSiteSummary> {



    RptSiteSummaryVo findSummaryTotal( @Param("r") RptSiteSummaryCo params);

}
