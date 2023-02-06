package com.central.porn.controller;

import com.central.common.model.Result;
import com.central.porn.entity.vo.KpnMovieBaseVo;
import com.central.porn.entity.vo.KpnMovieVo;
import com.central.porn.service.IKpnMovieService;
import com.central.porn.service.IKpnSiteMovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private IKpnSiteMovieService siteMovieService;

    /**
     * 获取影片
     *
     * @param sid      站点id
     * @param movieIds 影片id集合
     * @return
     */
    @PostMapping("/ids")
    @ApiOperation(value = "按id获取影片信息")
    public Result<List<KpnMovieBaseVo>> getMovieByIds(@RequestHeader("sid") Long sid, @RequestBody List<Long> movieIds) {
        try {
            List<KpnMovieBaseVo> siteMovieVos = movieService.getMovieByIds(movieIds);

            return Result.succeed(siteMovieVos, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 统计影片点播量
     *
     * @return
     */
    @PostMapping("/count/vv")
    @ApiOperation(value = "统计影片点播量(注意暂停后,再点播不掉该接口)")
    public Result<Long> countVv(@RequestHeader("sid") Long sid, Long movieId) {
        try {
            Long siteMovieVv = siteMovieService.addSiteMovieVv(sid, movieId);

            return Result.succeed(siteMovieVv,"succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 统计影片收藏量
     *
     * @return
     */
    @PostMapping("/count/favorites")
    @ApiOperation(value = "统计影片收藏量")
    public Result<Long> countFavorites(@RequestHeader("sid") Long sid, Long movieId) {
        try {
            Long siteMovieFavorites = siteMovieService.addSiteMovieFavorites(sid, movieId);

            return Result.succeed(siteMovieFavorites,"succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }


}
