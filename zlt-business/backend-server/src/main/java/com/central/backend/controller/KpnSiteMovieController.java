package com.central.backend.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.central.backend.model.dto.KpnSiteMoviePayTpyeDto;
import com.central.backend.model.dto.KpnSiteMovieStatusDto;
import com.central.backend.model.vo.KpnSiteMovieVO;
import com.central.backend.service.IAsyncService;
import com.central.backend.service.IKpnSiteMovieService;
import com.central.common.annotation.LoginUser;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.PageResult;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 站点影片
 *
 * @author yixiu
 * @date 2023-02-20 17:00:56
 */
@Slf4j
@RestController
@RequestMapping("/kpnsitemovie")
@Api(tags = "站点影片库")
public class KpnSiteMovieController {
    @Autowired
    private IKpnSiteMovieService kpnSiteMovieService;

    @Autowired
    private IAsyncService asyncService;

    /**
     * 列表
     */
    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nameZh", value = "影片名称-中文", required = false, dataType = "String"),
            @ApiImplicitParam(name = "nameEn", value = "影片名称-英文", required = false, dataType = "String"),
            @ApiImplicitParam(name = "nameKh", value = "影片名称-柬文", required = false, dataType = "String"),
            @ApiImplicitParam(name = "actorNameZh", value = "演员中文名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "actorNameEn", value = "演员英文名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "actorNameKh", value = "演员柬文名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态 0待发布,1上架,2下架", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "country", value = "国家", required = false, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "影片类型 0/false无码 1/true有码", required = false, dataType = "Boolean"),
            @ApiImplicitParam(name = "shootingType", value = "拍摄性质 special:专业拍摄,sneak:偷拍,selfie:自拍,other:其他", required = false, dataType = "String"),
            @ApiImplicitParam(name = "subtitleType", value = "字幕类型 no:无字幕,zh:中文,en:英文,zhen:中英,other:其他", required = false, dataType = "String"),
            @ApiImplicitParam(name = "payType", value = "付费类型 0/false:免费,1/true:付费", required = false, dataType = "Boolean"),
            @ApiImplicitParam(name = "startTime", value = "更新时间起始", required = false),
            @ApiImplicitParam(name = "endTime", value = "更新时间结束", required = false),
            @ApiImplicitParam(name = "movieId", value = "影片id", required = false, dataType = "Long"),
            @ApiImplicitParam(name = "orderByParms", value = "排序字段：1影片数量、2播放数量，3收藏数，默认演员创建时间", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "sortBy", value = "排序方式：1正序、2倒叙(默认)", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping
    public Result<PageResult<KpnSiteMovieVO>> list(@RequestParam Map<String, Object> params,@ApiIgnore @LoginUser SysUser user) {
        if (ObjectUtil.isEmpty(params)) {
            return Result.failed("请求参数不能为空");
        }
        if (ObjectUtil.isEmpty(params.get("page"))) {
            return Result.failed("分页起始位置不能为空");
        }
        if (ObjectUtil.isEmpty(params.get("limit"))) {
            return Result.failed("分页结束位置不能为空");
        }
        return Result.succeed(kpnSiteMovieService.findList(params,user));
    }

    /**
     * 查询
     */
    @ApiOperation(value = "查询")
    @GetMapping("/{id}")
    public Result findUserById(@PathVariable Long id) {
        KpnSiteMovie model = kpnSiteMovieService.getById(id);
        return Result.succeed(model, "查询成功");
    }
    /**
      * 批量发布上架下架
      */
    @ApiOperation(value = "批量发布上架下架",notes = "@ApiImplicitParams({\n" +
            "            @ApiImplicitParam(name = \"id\", value = \"站点影片ID\", required = true, dataType = \"Integer\"),\n" +
            "            @ApiImplicitParam(name = \"status\", value = \"状态 0待发布,1上架,2下架\", required = true, dataType = \"Integer\")\n" +
            "    })")
    @ApiImplicitParams({
    })
    @PostMapping("/settingStatus")
    public Result updateBatchStatusById(@RequestBody List<KpnSiteMovieStatusDto> kpnSiteMovieStatusDtoList,@ApiIgnore @LoginUser SysUser user) {
        if (ObjectUtil.isEmpty(kpnSiteMovieStatusDtoList) || kpnSiteMovieStatusDtoList.size() == 0) {
            return Result.failed("请求参数不能为空");
        }
        kpnSiteMovieService.updateBatchStatusById(kpnSiteMovieStatusDtoList, user);

        //add by year 删除站点影片缓存
        List<Long> siteMovieIds = kpnSiteMovieStatusDtoList.stream().map(KpnSiteMovieStatusDto::getId).collect(Collectors.toList());
        asyncService.deleteSiteMovieVoCacheById(siteMovieIds);
        asyncService.deleteSiteActorMovieNumCache(siteMovieIds);
        if (CollUtil.isNotEmpty(siteMovieIds)) {
            KpnSiteMovie siteMovie = kpnSiteMovieService.getById(siteMovieIds.get(0));
            if (ObjectUtil.isNotEmpty(siteMovie)) {
                asyncService.openSiteMoviesChangeSwitch(siteMovie.getSiteId());
            }
        }
        return Result.succeed("保存成功");
    }
    /**
     * 批量发布上架下架
     */
    @ApiOperation(value = "批量设置付费类型",notes="@ApiImplicitParams({\n" +
            "            @ApiImplicitParam(name = \"id\", value = \"站点影片ID\", required = true, dataType = \"Integer\"),\n" +
            "            @ApiImplicitParam(name = \"payType\", value = \"付费类型 false:免费,true:付费\", required = true, dataType = \"Integer\")\n" +
            "    })")
    @ApiImplicitParams({
    })
    @PostMapping("/settingPayType")
    public Result updateBatchPayTypeById(@RequestBody List<KpnSiteMoviePayTpyeDto> kpnSiteMoviePayTpyeDtoList, @ApiIgnore @LoginUser SysUser user) {
        if (ObjectUtil.isEmpty(kpnSiteMoviePayTpyeDtoList) || kpnSiteMoviePayTpyeDtoList.size() == 0) {
            return Result.failed("请求参数不能为空");
        }
        kpnSiteMovieService.updateBatchPayTypeById(kpnSiteMoviePayTpyeDtoList, user);

        //add by year 删除站点影片缓存
        List<Long> siteMovieIds = kpnSiteMoviePayTpyeDtoList.stream().map(KpnSiteMoviePayTpyeDto::getId).collect(Collectors.toList());
        asyncService.deleteSiteMovieVoCacheById(siteMovieIds);
        return Result.succeed("保存成功");
    }

//    /**
//     * 新增or更新
//     */
//    @ApiOperation(value = "保存")
//    @PostMapping
//    public Result save(@RequestBody KpnSiteMovie kpnSiteMovie) {
//        kpnSiteMovieService.saveOrUpdate(kpnSiteMovie);
//        return Result.succeed("保存成功");
//    }
//
//    /**
//     * 删除
//     */
//    @ApiOperation(value = "删除")
//    @DeleteMapping("/{id}")
//    public Result delete(@PathVariable Long id) {
//        kpnSiteMovieService.removeById(id);
//        return Result.succeed("删除成功");
//    }
}
