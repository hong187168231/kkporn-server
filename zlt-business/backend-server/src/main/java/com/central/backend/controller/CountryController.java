package com.central.backend.controller;

import com.central.common.model.Result;
import com.central.common.model.enums.KpnMovieSearchTypeCountryEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 
 *
 * @author yixiu
 * @date 2023-02-03 15:50:11
 */
@Slf4j
@RestController
@RequestMapping("/country")
@Api(tags = "国家")
public class CountryController {

    /**
     * 列表
     */
    @ApiOperation(value = "查询国家列表")
    @GetMapping
    public Result<Map<Integer, String>> list() {
        return Result.succeed(KpnMovieSearchTypeCountryEnum.getOptions());
    }

}
