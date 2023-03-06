package com.central.backend.controller;

import java.util.List;
import java.util.Map;

import com.central.backend.model.dto.KpnSiteMovieStatusDto;
import com.central.backend.model.vo.KpnSiteMovieVO;
import com.central.backend.service.IKpnSiteMovieService;
import com.central.common.annotation.LoginUser;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import com.central.common.model.PageResult;
import com.central.common.model.Result;
import springfox.documentation.annotations.ApiIgnore;

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
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping
    public Result<PageResult<KpnSiteMovieVO>> list(@RequestParam Map<String, Object> params,@ApiIgnore @LoginUser SysUser user) {
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
    @ApiOperation(value = "批量发布上架下架")
    @PostMapping("/setingStatus")
    public Result updateBatchStatusById(@RequestBody List<KpnSiteMovieStatusDto> kpnSiteMovie,@ApiIgnore @LoginUser SysUser user) {
        kpnSiteMovieService.updateBatchStatusById(kpnSiteMovie, user);
        return Result.succeed("保存成功");
    }
    /**
     * 批量发布上架下架
     */
    @ApiOperation(value = "批量设置付费类型")
    @PostMapping("/setingPayType")
    public Result updateBatchPayTypeById(@RequestBody List<KpnSiteMovieStatusDto> kpnSiteMovie,@ApiIgnore @LoginUser SysUser user) {
        kpnSiteMovieService.updateBatchPayTypeById(kpnSiteMovie, user);
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
