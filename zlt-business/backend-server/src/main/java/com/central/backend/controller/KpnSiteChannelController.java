package com.central.backend.controller;

import com.central.backend.co.KpnSiteChannelUpdateCo;
import com.central.backend.service.IKpnSiteChannelService;
import com.central.backend.service.IKpnTagService;
import com.central.backend.vo.KpnTagVo;
import com.central.common.model.KpnSiteChannel;
import com.central.common.model.KpnTag;
import com.central.common.model.PageResult;
import com.central.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "频道栏目配置api")
@Validated
@RequestMapping("/siteChannel")
public class KpnSiteChannelController {

    @Autowired
    private IKpnSiteChannelService siteChannelService;

    @Autowired
    private IKpnTagService kpnTagService;


    @Value("${zlt.minio.externalEndpoint}")
    private String externalEndpoint;


    @ApiOperation("查询频道栏目列表")
    @ResponseBody
    @GetMapping("/findSiteChannelList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteCode", value = "站点code", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    public Result<PageResult<KpnSiteChannel>> findSiteChannelList(@RequestParam Map<String, Object> params) {
        PageResult<KpnSiteChannel> list = siteChannelService.findSiteChannelList(params);
        list.getData().stream().forEach(info->{
            if (info.getIcon()!=null){
                info.setIcon(externalEndpoint+"/"+info.getIcon());
            }
        });
        return Result.succeed(list);
    }



    @ApiOperation(value = "新增or更新频道")
    @PostMapping(value = "/saveOrUpdateSiteChannel")
    public Result saveOrUpdateSiteChannel(@RequestBody  KpnSiteChannel siteChannel) {
/*       if (siteChannel.getId() == null) {
           siteChannel.setUpdateBy(sysUser.getUsername());
           siteChannel.setCreateBy(sysUser.getUsername());
        }else {
           siteChannel.setUpdateBy(sysUser.getUsername());
        }*/
        return  siteChannelService.saveOrUpdateSiteChannel(siteChannel);
    }



    @ApiOperation(value = "修改状态")
    @GetMapping("/updateEnabledChannel")
    public Result updateEnabledChannel(@Valid @ModelAttribute KpnSiteChannelUpdateCo params) {
        // params.setUpdateBy(sysUser.getUsername());
        Result result = siteChannelService.updateEnabledChannel(params);
        return result;
    }

    @ApiOperation("删除")
    @DeleteMapping(value = "/deleteId/{id}")
    public Result deleteId(@PathVariable Long id) {
        boolean b = siteChannelService.deleteId(id);
        return b ? Result.succeed("删除成功") : Result.failed("删除失败");
    }

    @ApiOperation("关联标签")
    @ResponseBody
    @GetMapping("/findTagList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteCode", value = "站点code", required = true, dataType = "String")
    })
    public Result<List<KpnTagVo>> findTagList(@RequestParam Map<String, Object> params) {
        List<KpnTagVo> list = kpnTagService.findTagList(params);
        return Result.succeed(list);
    }

}
