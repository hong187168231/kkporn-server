package com.central.porn.controller;

import com.central.common.model.Result;
import com.central.porn.entity.vo.KpnSiteMovieVo;
import com.central.porn.service.IKpnMovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 影片相关
 */
@Slf4j
@RestController
@RequestMapping("/v1/movie")
@Api(tags = "影片相关api接口")
public class MovieController {

    @Autowired
    private IKpnMovieService movieService;

    /**
     * 获取影片
     *
     * @param sid      站点id
     * @param movieIds 影片id集合
     * @return
     */
    @PostMapping("/ids")
    @ApiOperation(value = "批量获取影片信息")
    public Result<List<KpnSiteMovieVo>> getUserChannels(@RequestHeader("sid") Long sid, @RequestBody List<Long> movieIds) {
        try {
            List<KpnSiteMovieVo> siteMovieVos = new ArrayList<>();

            return Result.succeed(siteMovieVos, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }


}
