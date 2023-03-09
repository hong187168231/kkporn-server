package com.central.backend.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.central.backend.model.co.I18nInfoPageMapperCo;
import com.central.backend.model.co.QueryI18nInfoPageCo;
import com.central.backend.model.co.SaveI18nInfoCo;
import com.central.backend.model.co.UpdateI18nInfoCo;
import com.central.backend.service.II18nInfosService;
import com.central.backend.mapper.I18nInfoMapper;
import com.central.common.redis.template.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.common.constant.I18nKeys;
import com.central.common.dto.I18nSourceDTO;
import com.central.common.model.I18nInfo;
import com.central.common.model.PageResult;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.common.utils.I18nUtil;
import com.central.common.utils.StringUtils;
import com.central.common.vo.I18nInfoPageVO;
import com.central.common.vo.LanguageLabelVO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class I18nInfosServiceImpl extends SuperServiceImpl<I18nInfoMapper, I18nInfo> implements II18nInfosService, CommandLineRunner {

    @Autowired
    private I18nInfoMapper mapper;

    @Override
    public Boolean deleteById(Long id, I18nInfo i18nInfo) {
        int i = mapper.deleteById(id);
        boolean b = i > 0;
        if (b) {
            // 删除redis
            this.deleteI18nRedis(i18nInfo);
        }
        return b;
    }

    @Override
    public I18nInfo selectById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public List<I18nInfo> findListByZhCn(Integer fromOf, String zhCn) {
        return mapper.findListByZhCn(fromOf, zhCn);
    }

    @Override
    public I18nSourceDTO getBackendFullI18nSource() {
        return I18nUtil.getBackendFullSource();
    }

    @Override
    public I18nSourceDTO getFrontFullI18nSource(Integer fromOf) {
        if (fromOf == I18nKeys.FRONT_APP) {
            return I18nUtil. getFrontAppFullSource();
        } else if (fromOf == I18nKeys.FRONT_MESSAGE) {
            return I18nUtil.getFrontMessageFullSource();
        } else {
            return I18nUtil.getFrontFullSource();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        initI18nSourceRedis();
    }

    @Override
    public void initI18nSourceRedis() {
        List<I18nInfo> infos = list();

        // 批量写入redis
        RedisRepository.getRedisTemplate()
                .executePipelined((RedisCallback<?>)c -> {
            for (I18nInfo f : infos) {
                if (I18nKeys.BACKEND.equals(f.getFromOf())) {
                    // 中文国际化
                    c.hSet(I18nKeys.Redis.Backend.ZH_CN_KEY.getBytes(StandardCharsets.UTF_8),
                        f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getZhCn().getBytes(StandardCharsets.UTF_8));

                    // 英文国际化
                    if (StrUtil.isNotBlank(f.getEnUs())) {
                        c.hSet(I18nKeys.Redis.Backend.EN_US_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getEnUs().getBytes(StandardCharsets.UTF_8));
                    }

                    // 高棉语国际化
                    if (StrUtil.isNotBlank(f.getKhm())) {
                        c.hSet(I18nKeys.Redis.Backend.KHM_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getKhm().getBytes(StandardCharsets.UTF_8));
                    }

                    // 泰文国际化
                    if (StrUtil.isNotBlank(f.getTh())) {
                        c.hSet(I18nKeys.Redis.Backend.TH_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getTh().getBytes(StandardCharsets.UTF_8));
                    }
                    // 越南国际化
                    if (StrUtil.isNotBlank(f.getVi())) {
                        c.hSet(I18nKeys.Redis.Backend.VI_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getVi().getBytes(StandardCharsets.UTF_8));
                    }
                    // 马来国际化
                    if (StrUtil.isNotBlank(f.getMy())) {
                        c.hSet(I18nKeys.Redis.Backend.MY_KEY.getBytes(StandardCharsets.UTF_8),
                                f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getMy().getBytes(StandardCharsets.UTF_8));
                    }
                } else if (I18nKeys.FRONT_PC.equals(f.getFromOf())) {

                    // 中文国际化
                    c.hSet(I18nKeys.Redis.FrontPc.ZH_CN_KEY.getBytes(StandardCharsets.UTF_8),
                        f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getZhCn().getBytes(StandardCharsets.UTF_8));

                    // 英文国际化
                    if (StrUtil.isNotBlank(f.getEnUs())) {
                        c.hSet(I18nKeys.Redis.FrontPc.EN_US_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getEnUs().getBytes(StandardCharsets.UTF_8));
                    }

                    // 高棉语国际化
                    if (StrUtil.isNotBlank(f.getKhm())) {
                        c.hSet(I18nKeys.Redis.FrontPc.KHM_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getKhm().getBytes(StandardCharsets.UTF_8));
                    }

                    // 泰文国际化
                    if (StrUtil.isNotBlank(f.getTh())) {
                        c.hSet(I18nKeys.Redis.FrontPc.TH_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getTh().getBytes(StandardCharsets.UTF_8));
                    }
                    // 越南语国际化
                    if (StrUtil.isNotBlank(f.getVi())) {
                        c.hSet(I18nKeys.Redis.FrontPc.VI_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getVi().getBytes(StandardCharsets.UTF_8));
                    }
                    // 马来语国际化
                    if (StrUtil.isNotBlank(f.getMy())) {
                        c.hSet(I18nKeys.Redis.FrontPc.MY_KEY.getBytes(StandardCharsets.UTF_8),
                                f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getMy().getBytes(StandardCharsets.UTF_8));
                    }
                } else if (I18nKeys.FRONT_APP.equals(f.getFromOf())) {

                    // 中文国际化
                    c.hSet(I18nKeys.Redis.FrontApp.ZH_CN_KEY.getBytes(StandardCharsets.UTF_8),
                        f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getZhCn().getBytes(StandardCharsets.UTF_8));

                    // 英文国际化
                    if (StrUtil.isNotBlank(f.getEnUs())) {
                        c.hSet(I18nKeys.Redis.FrontApp.EN_US_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getEnUs().getBytes(StandardCharsets.UTF_8));
                    }

                    // 高棉语国际化
                    if (StrUtil.isNotBlank(f.getKhm())) {
                        c.hSet(I18nKeys.Redis.FrontApp.KHM_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getKhm().getBytes(StandardCharsets.UTF_8));
                    }

                    // 泰文国际化
                    if (StrUtil.isNotBlank(f.getTh())) {
                        c.hSet(I18nKeys.Redis.FrontApp.TH_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getTh().getBytes(StandardCharsets.UTF_8));
                    }

                    // 越南语国际化
                    if (StrUtil.isNotBlank(f.getVi())) {
                        c.hSet(I18nKeys.Redis.FrontApp.VI_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getVi().getBytes(StandardCharsets.UTF_8));
                    }
                    // 马来语国际化
                    if (StrUtil.isNotBlank(f.getMy())) {
                        c.hSet(I18nKeys.Redis.FrontApp.MY_KEY.getBytes(StandardCharsets.UTF_8),
                                f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getMy().getBytes(StandardCharsets.UTF_8));
                    }
                } else if (I18nKeys.FRONT_MESSAGE.equals(f.getFromOf())) {

                    // 中文国际化
                    c.hSet(I18nKeys.Redis.FrontMessage.ZH_CN_KEY.getBytes(StandardCharsets.UTF_8),
                        f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getZhCn().getBytes(StandardCharsets.UTF_8));

                    // 英文国际化
                    if (StrUtil.isNotBlank(f.getEnUs())) {
                        c.hSet(I18nKeys.Redis.FrontMessage.EN_US_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getEnUs().getBytes(StandardCharsets.UTF_8));
                    }

                    // 高棉语国际化
                    if (StrUtil.isNotBlank(f.getKhm())) {
                        c.hSet(I18nKeys.Redis.FrontMessage.KHM_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getKhm().getBytes(StandardCharsets.UTF_8));
                    }

                    // 泰文国际化
                    if (StrUtil.isNotBlank(f.getTh())) {
                        c.hSet(I18nKeys.Redis.FrontMessage.TH_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getTh().getBytes(StandardCharsets.UTF_8));
                    }

                    // 越南语国际化
                    if (StrUtil.isNotBlank(f.getVi())) {
                        c.hSet(I18nKeys.Redis.FrontMessage.VI_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getVi().getBytes(StandardCharsets.UTF_8));
                    }
                    // 马来语国际化
                    if (StrUtil.isNotBlank(f.getMy())) {
                        c.hSet(I18nKeys.Redis.FrontMessage.MY_KEY.getBytes(StandardCharsets.UTF_8),
                                f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getMy().getBytes(StandardCharsets.UTF_8));
                    }
                }else if (I18nKeys.BACKEND_MESSAGE.equals(f.getFromOf())) {

                    // 中文国际化
                    c.hSet(I18nKeys.Redis.BackendMessage.ZH_CN_KEY.getBytes(StandardCharsets.UTF_8),
                        f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getZhCn().getBytes(StandardCharsets.UTF_8));

                    // 英文国际化
                    if (StrUtil.isNotBlank(f.getEnUs())) {
                        c.hSet(I18nKeys.Redis.BackendMessage.EN_US_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getEnUs().getBytes(StandardCharsets.UTF_8));
                    }

                    // 高棉语国际化
                    if (StrUtil.isNotBlank(f.getKhm())) {
                        c.hSet(I18nKeys.Redis.BackendMessage.KHM_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getKhm().getBytes(StandardCharsets.UTF_8));
                    }

                    // 泰文国际化
                    if (StrUtil.isNotBlank(f.getTh())) {
                        c.hSet(I18nKeys.Redis.BackendMessage.TH_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getTh().getBytes(StandardCharsets.UTF_8));
                    }

                    // 越南语国际化
                    if (StrUtil.isNotBlank(f.getVi())) {
                        c.hSet(I18nKeys.Redis.BackendMessage.VI_KEY.getBytes(StandardCharsets.UTF_8),
                            f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getVi().getBytes(StandardCharsets.UTF_8));
                    }
                    // 马来语国际化
                    if (StrUtil.isNotBlank(f.getMy())) {
                        c.hSet(I18nKeys.Redis.BackendMessage.MY_KEY.getBytes(StandardCharsets.UTF_8),
                                f.getZhCn().getBytes(StandardCharsets.UTF_8), f.getMy().getBytes(StandardCharsets.UTF_8));
                    }
                }
            }
            return null;
        });

    }

    @Override
    public boolean updateI18nInfo(Integer from, UpdateI18nInfoCo param) {
        boolean zhcnChange = Objects.nonNull(param.getZhCn());
        boolean enusChange = Objects.nonNull(param.getEnUs());
        boolean khmChange = Objects.nonNull(param.getKhm());
        boolean thChange = Objects.nonNull(param.getTh());
        boolean viChange = Objects.nonNull(param.getVi());
        boolean myChange = Objects.nonNull(param.getMy());
        I18nInfo info = getById(param.getId());
        if (null == info) {
            return false;
        }
        List<I18nInfo> listByZhCn = findListByZhCn(from, param.getZhCn());
        if (CollUtil.isNotEmpty(listByZhCn)) {
            for (I18nInfo i18nInfo : listByZhCn) {
                if (i18nInfo.getId().longValue() != param.getId().longValue()) {
                    return false;
                }
            }
        }
        LambdaUpdateWrapper<I18nInfo> update =
            Wrappers.lambdaUpdate(I18nInfo.class).eq(I18nInfo::getId, param.getId()).eq(I18nInfo::getFromOf, from)
                .set(zhcnChange, I18nInfo::getZhCn, param.getZhCn()).set(enusChange, I18nInfo::getEnUs, param.getEnUs())
                .set(khmChange, I18nInfo::getKhm, param.getKhm()).set(thChange, I18nInfo::getTh, param.getTh()).set(viChange, I18nInfo::getVi, param.getVi())
                    .set(myChange, I18nInfo::getMy, param.getMy())
                .set(StrUtil.isNotBlank(param.getOperator()), I18nInfo::getOperator, param.getOperator())
                .set(I18nInfo::getUpdateTime, new Date());
        int count = mapper.update(null, update);
        boolean succeed = count > 0;

        if (succeed) {
            String oldKey = null;
            if (!info.getZhCn().equals(param.getZhCn())) {
                oldKey = info.getZhCn();
            }
            // 更新redis
            updateI18nRedis(from, param, info, oldKey);
        }
        return succeed;
    }

    @Override
    public boolean saveI18nInfo(Integer from, SaveI18nInfoCo param) {
        I18nInfo info = new I18nInfo();
        BeanUtil.copyProperties(param, info);
        info.setFromOf(from);
        List<I18nInfo> listByZhCn = findListByZhCn(from, param.getZhCn());
        if (CollUtil.isNotEmpty(listByZhCn) && listByZhCn.size() >= 1) {
            return false;
        }
        if (Objects.isNull(info.getEnUs())) {
            info.setEnUs("");
        }
        if (Objects.isNull(info.getKhm())) {
            info.setKhm("");
        }
        if (Objects.isNull(info.getTh())) {
            info.setTh("");
        }
        if (Objects.isNull(info.getVi())) {
            info.setVi("");
        }
        if (Objects.isNull(info.getMy())) {
            info.setMy("");
        }
        info.setUpdateTime(new Date());
        boolean succeed = save(info);
        if (succeed) {
            updateI18nRedis(from, param, info, null);
        }
        return succeed;
    }

    private void updateI18nRedis(Integer from, SaveI18nInfoCo param, I18nInfo info, String oldKey) {
        boolean zhcnChange = StrUtil.isNotBlank(param.getZhCn());
        boolean enusChange = StrUtil.isNotBlank(param.getEnUs());
        boolean khmChange = StrUtil.isNotBlank(param.getKhm());
        boolean thChange = StrUtil.isNotBlank(param.getTh());
        boolean viChange = StrUtil.isNotBlank(param.getVi());
        boolean myChange = StrUtil.isNotBlank(param.getMy());

        // 更新redis
        String i18nKey = info.getZhCn();
        String redisKey = "";
        // 更新中文key
        i18nKey = param.getZhCn();
        if (I18nKeys.FRONT_PC.equals(from)) {
            redisKey = I18nKeys.Redis.FrontPc.ZH_CN_KEY;
        } else if (I18nKeys.FRONT_APP.equals(from)) {
            redisKey = I18nKeys.Redis.FrontApp.ZH_CN_KEY;
        } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.FrontMessage.ZH_CN_KEY;
        } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.BackendMessage.ZH_CN_KEY;
        } else {
            redisKey = I18nKeys.Redis.Backend.ZH_CN_KEY;
        }
        if (zhcnChange) {
            // 更新中文国际化
            I18nUtil.resetSource(redisKey, i18nKey, param.getZhCn());
        } else {
            I18nUtil.deleteByKey(redisKey, i18nKey);
        }
        if (StringUtils.isNotEmpty(oldKey)) {
            I18nUtil.deleteByKey(redisKey, oldKey);
        }

        // 更新英文国际化
        if (I18nKeys.FRONT_PC.equals(from)) {
            redisKey = I18nKeys.Redis.FrontPc.EN_US_KEY;
        } else if (I18nKeys.FRONT_APP.equals(from)) {
            redisKey = I18nKeys.Redis.FrontApp.EN_US_KEY;
        } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.FrontMessage.EN_US_KEY;
        } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.BackendMessage.EN_US_KEY;
        } else {
            redisKey = I18nKeys.Redis.Backend.EN_US_KEY;
        }
        if (enusChange) {
            I18nUtil.resetSource(redisKey, i18nKey, param.getEnUs());
        } else {
            I18nUtil.deleteByKey(redisKey, i18nKey);
        }
        if (StringUtils.isNotEmpty(oldKey)) {
            I18nUtil.deleteByKey(redisKey, oldKey);
        }

        // 更新高棉语国际化
        if (I18nKeys.FRONT_PC.equals(from)) {
            redisKey = I18nKeys.Redis.FrontPc.KHM_KEY;
        } else if (I18nKeys.FRONT_APP.equals(from)) {
            redisKey = I18nKeys.Redis.FrontApp.KHM_KEY;
        } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.FrontMessage.KHM_KEY;
        } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.BackendMessage.KHM_KEY;
        } else {
            redisKey = I18nKeys.Redis.Backend.KHM_KEY;
        }
        if (khmChange) {
            I18nUtil.resetSource(redisKey, i18nKey, param.getKhm());
        } else {
            I18nUtil.deleteByKey(redisKey, i18nKey);
        }
        if (StringUtils.isNotEmpty(oldKey)) {
            I18nUtil.deleteByKey(redisKey, oldKey);
        }

        // 更新泰语国际化
        if (I18nKeys.FRONT_PC.equals(from)) {
            redisKey = I18nKeys.Redis.FrontPc.TH_KEY;
        } else if (I18nKeys.FRONT_APP.equals(from)) {
            redisKey = I18nKeys.Redis.FrontApp.TH_KEY;
        } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.FrontMessage.TH_KEY;
        } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.BackendMessage.TH_KEY;
        } else {
            redisKey = I18nKeys.Redis.Backend.TH_KEY;
        }
        if (thChange) {
            I18nUtil.resetSource(redisKey, i18nKey, param.getTh());
        } else {
            I18nUtil.deleteByKey(redisKey, i18nKey);
        }
        if (StringUtils.isNotEmpty(oldKey)) {
            I18nUtil.deleteByKey(redisKey, oldKey);
        }

        // 更新越南语国际化
        if (I18nKeys.FRONT_PC.equals(from)) {
            redisKey = I18nKeys.Redis.FrontPc.VI_KEY;
        } else if (I18nKeys.FRONT_APP.equals(from)) {
            redisKey = I18nKeys.Redis.FrontApp.VI_KEY;
        } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.FrontMessage.VI_KEY;
        } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.BackendMessage.VI_KEY;
        } else {
            redisKey = I18nKeys.Redis.Backend.VI_KEY;
        }
        if (viChange) {
            I18nUtil.resetSource(redisKey, i18nKey, param.getVi());
        } else {
            I18nUtil.deleteByKey(redisKey, i18nKey);
        }
        if (StringUtils.isNotEmpty(oldKey)) {
            I18nUtil.deleteByKey(redisKey, oldKey);
        }
        // 更新马来语国际化
        if (I18nKeys.FRONT_PC.equals(from)) {
            redisKey = I18nKeys.Redis.FrontPc.MY_KEY;
        } else if (I18nKeys.FRONT_APP.equals(from)) {
            redisKey = I18nKeys.Redis.FrontApp.MY_KEY;
        } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.FrontMessage.MY_KEY;
        } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
            redisKey = I18nKeys.Redis.BackendMessage.MY_KEY;
        } else {
            redisKey = I18nKeys.Redis.Backend.MY_KEY;
        }
        if (myChange) {
            I18nUtil.resetSource(redisKey, i18nKey, param.getMy());
        } else {
            I18nUtil.deleteByKey(redisKey, i18nKey);
        }
        if (StringUtils.isNotEmpty(oldKey)) {
            I18nUtil.deleteByKey(redisKey, oldKey);
        }
    }

    private void deleteI18nRedis(I18nInfo info) {
        boolean zhcnChange = StrUtil.isNotBlank(info.getZhCn());
        boolean enusChange = StrUtil.isNotBlank(info.getEnUs());
        boolean khmChange = StrUtil.isNotBlank(info.getKhm());
        boolean thChange = StrUtil.isNotBlank(info.getTh());
        boolean viChange = StrUtil.isNotBlank(info.getVi());
        boolean myChange = StrUtil.isNotBlank(info.getMy());
        Integer from = info.getFromOf();
        String oldKey = info.getZhCn();
        // 更新redis
        String i18nKey = info.getZhCn();
        String redisKey = "";
        if (zhcnChange) {
            // 更新中文key
            i18nKey = info.getZhCn();
            if (I18nKeys.FRONT_PC.equals(from)) {
                redisKey = I18nKeys.Redis.FrontPc.ZH_CN_KEY;
            } else if (I18nKeys.FRONT_APP.equals(from)) {
                redisKey = I18nKeys.Redis.FrontApp.ZH_CN_KEY;
            } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.FrontMessage.ZH_CN_KEY;
            } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.BackendMessage.ZH_CN_KEY;
            } else {
                redisKey = I18nKeys.Redis.Backend.ZH_CN_KEY;
            }
            I18nUtil.deleteByKey(redisKey, oldKey);
        }
        if (enusChange) {
            // 更新英文国际化
            if (I18nKeys.FRONT_PC.equals(from)) {
                redisKey = I18nKeys.Redis.FrontPc.EN_US_KEY;
            } else if (I18nKeys.FRONT_APP.equals(from)) {
                redisKey = I18nKeys.Redis.FrontApp.EN_US_KEY;
            } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.FrontMessage.EN_US_KEY;
            } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.BackendMessage.EN_US_KEY;
            } else {
                redisKey = I18nKeys.Redis.Backend.EN_US_KEY;
            }
            I18nUtil.deleteByKey(redisKey, oldKey);
        }
        if (khmChange) {
            // 更新高棉语国际化
            if (I18nKeys.FRONT_PC.equals(from)) {
                redisKey = I18nKeys.Redis.FrontPc.KHM_KEY;
            } else if (I18nKeys.FRONT_APP.equals(from)) {
                redisKey = I18nKeys.Redis.FrontApp.KHM_KEY;
            } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.FrontMessage.KHM_KEY;
            } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.BackendMessage.KHM_KEY;
            } else {
                redisKey = I18nKeys.Redis.Backend.KHM_KEY;
            }
            I18nUtil.deleteByKey(redisKey, oldKey);
        }
        if (thChange) {
            // 更新泰语国际化
            if (I18nKeys.FRONT_PC.equals(from)) {
                redisKey = I18nKeys.Redis.FrontPc.TH_KEY;
            } else if (I18nKeys.FRONT_APP.equals(from)) {
                redisKey = I18nKeys.Redis.FrontApp.TH_KEY;
            } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.FrontMessage.TH_KEY;
            } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.BackendMessage.TH_KEY;
            } else {
                redisKey = I18nKeys.Redis.Backend.TH_KEY;
            }
            I18nUtil.deleteByKey(redisKey, oldKey);
        }
        if (viChange) {
            // 更新越南语国际化
            if (I18nKeys.FRONT_PC.equals(from)) {
                redisKey = I18nKeys.Redis.FrontPc.VI_KEY;
            } else if (I18nKeys.FRONT_APP.equals(from)) {
                redisKey = I18nKeys.Redis.FrontApp.VI_KEY;
            } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.FrontMessage.VI_KEY;
            } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.BackendMessage.VI_KEY;
            } else {
                redisKey = I18nKeys.Redis.Backend.VI_KEY;
            }
            I18nUtil.deleteByKey(redisKey, oldKey);
        }
        if (myChange) {
            // 更新越南语国际化
            if (I18nKeys.FRONT_PC.equals(from)) {
                redisKey = I18nKeys.Redis.FrontPc.MY_KEY;
            } else if (I18nKeys.FRONT_APP.equals(from)) {
                redisKey = I18nKeys.Redis.FrontApp.MY_KEY;
            } else if (I18nKeys.FRONT_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.FrontMessage.MY_KEY;
            } else if (I18nKeys.BACKEND_MESSAGE.equals(from)) {
                redisKey = I18nKeys.Redis.BackendMessage.MY_KEY;
            } else {
                redisKey = I18nKeys.Redis.Backend.MY_KEY;
            }
            I18nUtil.deleteByKey(redisKey, oldKey);
        }
    }

    @Override
    public PageResult<I18nInfoPageVO> findInfos(QueryI18nInfoPageCo param) {
        Page<I18nInfoPageVO> page = new Page<>(param.getPage(), param.getLimit());
        I18nInfoPageMapperCo params = new I18nInfoPageMapperCo();

        if (StrUtil.isNotBlank(param.getWord()) && param.getLanguage() != null) {
            switch (param.getLanguage()) {
                case I18nKeys.LocaleCode.ZH_CN:
                    params.setZhCn(param.getWord());
                    break;
                case I18nKeys.LocaleCode.EN_US:
                    params.setEnUs(param.getWord());
                    break;
                case I18nKeys.LocaleCode.KHM:
                    params.setKhm(param.getWord());
                    break;
                case I18nKeys.LocaleCode.TH:
                    params.setTh(param.getWord());
                    break;
                case I18nKeys.LocaleCode.VI:
                    params.setVi(param.getWord());
                    break;
                case I18nKeys.LocaleCode.MY:
                    params.setMy(param.getWord());
                    break;
            }
        }
        params.setFrom(param.getFrom());
        List<I18nInfoPageVO> list = mapper.findPage(page, params);
        long total = page.getTotal();

        return PageResult.<I18nInfoPageVO>builder().data(list).count(total).build();
    }

    @Override
    public List<LanguageLabelVO> getLanguageLabel() {
        return Arrays.asList(new LanguageLabelVO(I18nKeys.LocaleCode.ZH_CN, I18nKeys.LocaleCodeEn.ZH,I18nUtil.t("中文")),
            new LanguageLabelVO(I18nKeys.LocaleCode.EN_US, I18nKeys.LocaleCodeEn.EN, I18nUtil.t("英文")),
            new LanguageLabelVO(I18nKeys.LocaleCode.KHM, I18nKeys.LocaleCodeEn.KM, I18nUtil.t("柬埔寨语")),
            new LanguageLabelVO(I18nKeys.LocaleCode.TH, I18nKeys.LocaleCodeEn.TH, I18nUtil.t("泰语")),
            new LanguageLabelVO(I18nKeys.LocaleCode.VI, I18nKeys.LocaleCodeEn.VI, I18nUtil.t("越南语")),
                new LanguageLabelVO(I18nKeys.LocaleCode.MY, I18nKeys.LocaleCodeEn.MY, I18nUtil.t("马来语")));
    }


}
