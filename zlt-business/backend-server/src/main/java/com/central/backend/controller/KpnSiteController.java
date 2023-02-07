package com.central.backend.controller;

import com.central.backend.co.KpnSiteCo;
import com.central.backend.co.KpnSiteUpdateCo;
import com.central.backend.service.IKpnSiteService;
import com.central.backend.vo.KpnSiteVo;
import com.central.common.annotation.LoginUser;
import com.central.common.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(tags = "站点管理api")
@Validated
@RequestMapping("/site")
public class KpnSiteController {

    @Autowired
    private IKpnSiteService siteService;


    @Value("${zlt.minio.externalEndpoint}")
    private String externalEndpoint;

    @ApiOperation("查询站点列表")
    @ResponseBody
    @GetMapping("/findSiteList")
    public Result<PageResult<KpnSite>> findSiteList(@ModelAttribute KpnSiteCo params) {
        PageResult<KpnSite> siteList = siteService.findSiteList(params);
        siteList.getData().stream().forEach(info->{
            if (info.getLogoUrl()!=null){
                info.setLogoUrl(externalEndpoint+"/"+info.getLogoUrl());
            }
        });
        return Result.succeed(siteList);
    }

    @ApiOperation(value = "站点总计")
    @GetMapping("/findSiteTotal")
    public Result<KpnSiteVo> findSiteTotal() {
        KpnSiteVo siteTotal = siteService.findSiteTotal();
        return Result.succeed(siteTotal);
    }



    @ApiOperation(value = "新增or更新站点")
    @PostMapping(value = "/saveOrUpdateSite",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result saveOrUpdateSite(@Valid KpnSite kpnSite, @Valid @RequestParam("file") MultipartFile file, @LoginUser SysUser sysUser) {
  /*     if (kpnSite.getId() == null) {
           kpnSite.setUpdateBy(sysUser.getUsername());
           kpnSite.setCreateBy(sysUser.getUsername());
        }else {
           kpnSite.setUpdateBy(sysUser.getUsername());
        }*/
        return  siteService.saveOrUpdateSite(kpnSite,file);
    }


    @ApiOperation(value = "修改站点状态")
    @GetMapping("/updateEnabledSite")
    public Result updateEnabledSite(@Valid @ModelAttribute KpnSiteUpdateCo params, @LoginUser SysUser sysUser) {
        // params.setUpdateBy(sysUser.getUsername());
        Result result = siteService.updateEnabledSite(params);
        return result;
    }




    @ApiOperation(value = "随机生成站点编号")
    @GetMapping("/randomNumber")
    public Result randomNumber() {
        String name =siteService.getStringRandom(6);
        return Result.succeed(name,"操作成功");
    }






}
