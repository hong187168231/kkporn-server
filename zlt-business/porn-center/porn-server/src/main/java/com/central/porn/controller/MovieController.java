package com.central.porn.controller;

import cn.hutool.core.util.ObjectUtil;
import com.central.common.annotation.LoginUser;
import com.central.common.model.KpnSiteUserMovieFavorites;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
import com.central.porn.entity.vo.KpnMovieVo;
import com.central.porn.service.IKpnSiteMovieService;
import com.central.porn.service.IKpnSiteUserMovieFavoritesService;
import com.central.porn.service.IKpnSiteUserMovieHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 影片相关
 */
@Slf4j
@RestController
@RequestMapping("/v1/movie")
@Api(tags = "影片相关api接口")
public class MovieController {

    @Autowired
    private IKpnSiteUserMovieHistoryService userMovieHistoryService;

    @Autowired
    private IKpnSiteUserMovieFavoritesService userMovieFavoritesService;

    @Autowired
    private IKpnSiteMovieService siteMovieService;

//    @Autowired
//    private TaskExecutor taskExecutor;

//    /**
//     * 获取影片列表
//     *
//     * @param sid      站点id
//     * @param movieIds 影片id集合
//     * @return
//     */
//    @PostMapping("/ids")
//    @ApiOperation(value = "按id获取影片列表基本信息")
//    public Result<List<KpnSiteMovieBaseVo>> getMovieByIds(@RequestHeader("sid") Long sid, @RequestBody List<Long> movieIds) {
//        try {
//            List<KpnSiteMovieBaseVo> siteMovieVos = siteMovieService.getSiteMovieByIds(sid, movieIds);
//
//            return Result.succeed(siteMovieVos, "succeed");
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            return Result.failed("failed");
//        }
//    }

    /**
     * 进入播放页,获取影片详细信息
     *
     * @return
     */
    @PostMapping("/detail/info")
    @ApiOperation(value = "播放页-获取影片详情")
    public Result<KpnMovieVo> detail(@RequestHeader("sid") Long sid, @ApiIgnore @LoginUser SysUser sysUser, Long movieId) {
        try {
            //站点播放次数
            Long siteMovieVv = siteMovieService.addSiteMovieVv(sid, movieId);

            //获取影片详情
            KpnMovieVo kpnMovieVo = siteMovieService.getSiteMovieDetail(sid, movieId);
            kpnMovieVo.setVv(siteMovieVv);

            //已经登录会员
            if (ObjectUtil.isNotEmpty(sysUser)) {
                Long userId = sysUser.getId();
                //更新浏览历史
                userMovieHistoryService.addUserMovieHistory(userId, movieId);
                //收藏状态
                KpnSiteUserMovieFavorites userMovieFavorites = userMovieFavoritesService.getUserMovieFavorites(userId, movieId);
                if (ObjectUtil.isNotEmpty(userMovieFavorites)) {
                    kpnMovieVo.setHasFavor(true);
                }
            }

            return Result.succeed(kpnMovieVo, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

//    /**
//     * 统计影片点播量
//     *
//     * @return
//     */
//    @PostMapping("/add/vv")
//    @ApiOperation(value = "影片开始播放时调用(注意暂停后,再点播不掉该接口)")
//    public Result<Long> addVv(@RequestHeader("sid") Long sid, @ApiIgnore @LoginUser SysUser sysUser, Long movieId) {
//        try {
//            Long siteMovieVv = siteMovieService.addSiteMovieVv(sid, movieId);
//
//            //放入浏览历史
//            if (ObjectUtil.isNotEmpty(sysUser)) {
//                userMovieHistoryService.addUserMovieHistory(sysUser.getId(), movieId);
//            }
//
//            return Result.succeed(siteMovieVv, "succeed");
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            return Result.failed("failed");
//        }
//    }
}
