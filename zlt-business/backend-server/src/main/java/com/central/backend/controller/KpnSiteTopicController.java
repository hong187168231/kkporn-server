package com.central.backend.controller;

import com.central.backend.co.KpnSiteTopicUpdateCo;
import com.central.backend.service.IKpnSiteTopicService;
import com.central.backend.vo.KpnSiteTopicVo;
import com.central.common.model.PageResult;
import com.central.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "推荐页专题api")
@Validated
@RequestMapping("/siteTopic")
public class KpnSiteTopicController {

    @Autowired
    private IKpnSiteTopicService siteTopicService;


    @ApiOperation("查询推荐页专题列表")
    @ResponseBody
    @GetMapping("/findSiteTopicList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteCode", value = "站点编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    public Result<PageResult<KpnSiteTopicVo>> findSiteTopicList(@RequestParam Map<String, Object> params) {
        PageResult<KpnSiteTopicVo> list = siteTopicService.findSiteTopicList(params);
        return Result.succeed(list);
    }




    @ApiOperation(value = "修改状态")
    @GetMapping("/updateEnabledTopic")
    public Result updateEnabledTopic(@Valid @ModelAttribute KpnSiteTopicUpdateCo params) {
        // params.setUpdateBy(sysUser.getUsername());
        Result result = siteTopicService.updateEnabledTopic(params);
        return result;
    }

    @ApiOperation("删除")
    @DeleteMapping(value = "/deleteId/{id}")
    public Result deleteId(@PathVariable Long id) {
        boolean b = siteTopicService.deleteId(id);
        return b ? Result.succeed("删除成功") : Result.failed("删除失败");
    }



}
