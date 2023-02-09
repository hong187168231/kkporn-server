package com.central.backend.controller;

import com.central.backend.co.KpnLineUpdateCo;
import com.central.backend.service.IKpnLineService;
import com.central.common.annotation.LoginUser;
import com.central.common.model.KpnLine;
import com.central.common.model.PageResult;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
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
@Api(tags = "线路配置api")
@Validated
@RequestMapping("/line")
public class KpnLineController {

    @Autowired
    private IKpnLineService lineService;


    @ApiOperation("查询线路配置列表")
    @ResponseBody
    @GetMapping("/findLineList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "line", value = "线路", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    public Result<PageResult<KpnLine>> findSiteList(@RequestParam Map<String, Object> params) {
        PageResult<KpnLine> lineList = lineService.findSiteList(params);
        return Result.succeed(lineList);
    }

    @ApiOperation(value = "新增or更新线路")
    @PostMapping( "/saveOrUpdateAdvertise")
    public Result saveOrUpdateLine(@RequestBody KpnLine line)  {
   /*    if (line.getId() == null) {
           line.setUpdateBy(sysUser.getUsername());
           line.setCreateBy(sysUser.getUsername());
        }else {
           line.setUpdateBy(sysUser.getUsername());
        }*/
        boolean aBoolean = lineService.saveOrUpdateLine(line);
        return aBoolean ? Result.succeed("操作成功") : Result.failed("操作失败");
    }

    @ApiOperation(value = "修改广告状态")
    @GetMapping("/updateEnabledAdvertise")
    public Result updateEnabledLine(@Valid @ModelAttribute KpnLineUpdateCo params) {
        // params.setUpdateBy(sysUser.getUsername());
        Result result = lineService.updateEnabledLine(params);
        return result;
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/deleteLineId/{id}")
    public Result delete(@PathVariable Long id) {
        lineService.removeById(id);
        return Result.succeed("删除成功");
    }

}
