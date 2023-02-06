package com.central.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.backend.co.KpnSiteCo;
import com.central.backend.co.KpnSiteUpdateCo;
import com.central.backend.mapper.KpnSiteMapper;
import com.central.backend.service.IKpnSiteService;
import com.central.backend.util.PictureUtil;
import com.central.backend.vo.KpnSiteVo;
import com.central.common.model.*;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.oss.model.ObjectInfo;
import com.central.oss.template.MinioTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Random;


@Slf4j
@Service
@CacheConfig(cacheNames = {"KpnSite"})
public class KpnSiteServiceImpl extends SuperServiceImpl<KpnSiteMapper, KpnSite> implements IKpnSiteService {
    @Autowired
    private MinioTemplate minioTemplate;


    @Override
    public PageResult<KpnSite> findSiteList(KpnSiteCo params) {
        Page<KpnSite> page = new Page<>(params.getPage(), params.getLimit());
        List<KpnSite> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnSite>builder().data(list).count(page.getTotal()).build();
    }

    @Override
    public KpnSiteVo findSiteTotal() {
        return baseMapper.findSiteTotal();
    }

    @Override
    public Result saveOrUpdateSite(KpnSite kpnSite, MultipartFile file) {
        if (file!=null){
            //校验图片格式支持MP4,JPG,GIF,PNG,JPEG,不超过10M
            Boolean aBoolean = PictureUtil.verifyFormat(file);
            if (!aBoolean){
                return  Result.failed("文件仅支持MP4,JPG,GIF,PNG,JPEG,且大小不超过10M");
            }
            //随机生成文件名字
            file = PictureUtil.generateRandomName(file);
            ObjectInfo objectInfo = minioTemplate.upload(file);
            kpnSite.setLogoUrl(objectInfo.getObjectPath());
        }
        boolean b = super.saveOrUpdate(kpnSite);
        return b ? Result.failed("操作成功") : Result.failed("操作失败") ;
    }

    @Override
    public Result updateEnabledSite(KpnSiteUpdateCo params) {
        Long id = params.getId();
        KpnSite siteInfo = baseMapper.selectById(id);
        if (siteInfo == null) {
            return Result.failed("此站点不存在");
        }
        if (params.getStatus()!=null){
            siteInfo.setStatus(params.getStatus());
        }
        if (params.getRepairStatus()!=null){
            siteInfo.setRepairStatus(params.getRepairStatus());
        }
        siteInfo.setUpdateBy(params.getUpdateBy());
        int i = baseMapper.updateById(siteInfo);
        return i>0 ? Result.succeed(siteInfo, "更新成功"): Result.failed("更新失败");
    }



    @Override
    public  String getStringRandom(int length) {
        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

}