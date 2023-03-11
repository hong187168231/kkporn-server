package com.central.backend.controller.movie;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.central.backend.model.dto.KpnSiteMoviePayTypeDto;
import com.central.backend.model.dto.KpnSiteMovieStatusDto;
import com.central.backend.service.IAsyncService;
import com.central.backend.service.IKpnSiteMovieService;
import com.central.common.annotation.LoginUser;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 站点影片
 */
@Slf4j
@RestController
@RequestMapping("/site/movie")
@Api(tags = "站点影片库")
public class KpnSiteMovieController {
    @Autowired
    private IKpnSiteMovieService siteMovieService;

    @Autowired
    private IAsyncService asyncService;

//    /**
//     * 列表
//     */
//    @ApiOperation(value = "查询列表")
//    @GetMapping("list")
//    public Result<PageResult<KpnSiteMovieVO>> list(@RequestBody QueryMovieCo queryMovieCo, @ApiIgnore @LoginUser SysUser user) {
//        return Result.succeed(siteMovieService.findList(queryMovieCo, user));
//    }

    /**
     * 查询
     */
    @ApiOperation(value = "查询")
    @GetMapping("/{id}")
    public Result findUserById(@PathVariable Long id) {
        KpnSiteMovie model = siteMovieService.getById(id);
        return Result.succeed(model, "查询成功");
    }

    /**
      * 批量发布上架下架
      */
    @ApiOperation(value = "批量发布上架下架")
    @PostMapping("/settingStatus")
    public Result updateBatchStatusById(@RequestBody List<KpnSiteMovieStatusDto> kpnSiteMovieStatusDtoList,@ApiIgnore @LoginUser SysUser user) {
        if (ObjectUtil.isEmpty(kpnSiteMovieStatusDtoList) || kpnSiteMovieStatusDtoList.size() == 0) {
            return Result.failed("请求参数不能为空");
        }
        siteMovieService.updateBatchStatusById(kpnSiteMovieStatusDtoList, user);

        //add by year 删除站点影片缓存
        List<Long> siteMovieIds = kpnSiteMovieStatusDtoList.stream().map(KpnSiteMovieStatusDto::getId).collect(Collectors.toList());
        asyncService.deleteSiteMovieVoCacheById(siteMovieIds);
        asyncService.deleteSiteActorMovieNumCache(siteMovieIds);
        if (CollUtil.isNotEmpty(siteMovieIds)) {
            KpnSiteMovie siteMovie = siteMovieService.getById(siteMovieIds.get(0));
            if (ObjectUtil.isNotEmpty(siteMovie)) {
                asyncService.openSiteMoviesChangeSwitch(siteMovie.getSiteId());
            }
        }
        return Result.succeed("保存成功");
    }

    @ApiOperation(value = "批量设置付费类型")
    @PostMapping("/settingPayType")
    public Result updateBatchPayTypeById(@RequestBody List<KpnSiteMoviePayTypeDto> kpnSiteMoviePayTypeDtoList, @ApiIgnore @LoginUser SysUser user) {
        if (ObjectUtil.isEmpty(kpnSiteMoviePayTypeDtoList) || kpnSiteMoviePayTypeDtoList.size() == 0) {
            return Result.failed("请求参数不能为空");
        }
        siteMovieService.updateBatchPayTypeById(kpnSiteMoviePayTypeDtoList, user);

        //add by year 删除站点影片缓存
        List<Long> siteMovieIds = kpnSiteMoviePayTypeDtoList.stream().map(KpnSiteMoviePayTypeDto::getId).collect(Collectors.toList());
        asyncService.deleteSiteMovieVoCacheById(siteMovieIds);
        return Result.succeed("保存成功");
    }
}
