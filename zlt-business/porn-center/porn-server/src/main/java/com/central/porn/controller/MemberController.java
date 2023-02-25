package com.central.porn.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.annotation.LoginUser;
import com.central.common.constant.PornConstants;
import com.central.common.model.*;
import com.central.common.model.enums.CodeEnum;
import com.central.common.model.pay.KpnSiteBankCard;
import com.central.common.model.pay.KpnSiteProduct;
import com.central.common.redis.lock.RedissLockUtil;
import com.central.oss.model.ObjectInfo;
import com.central.oss.template.MinioTemplate;
import com.central.porn.core.language.LanguageUtil;
import com.central.porn.entity.PornPageResult;
import com.central.porn.entity.co.MemberChannelSortCo;
import com.central.porn.entity.vo.*;
import com.central.porn.service.*;
import com.central.porn.utils.PornUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会员相关
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/member")
@Api(tags = "会员相关api接口")
public class MemberController {

    @Autowired
    private IKpnSiteActorService siteActorService;

    @Autowired
    private IKpnSiteChannelService siteChannelService;

    @Autowired
    private IKpnSiteMovieService siteMovieService;

    @Resource
    private MinioTemplate minioTemplate;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IKpnSiteSignDetailService siteSignDetailService;

    @Value("${zlt.minio.externalEndpoint}")
    private String externalEndpoint;

    @Autowired
    private IKpnSiteSuggestionService siteSuggestionService;

    @Autowired
    private IKpnSiteAnnouncementService siteAnnouncementService;

    @Autowired
    private IKpnSiteUserVipLogService siteUserVipLogService;

    @Autowired
    private IKpnMoneyLogService moneyLogService;

    @Autowired
    private IKpnSiteProductService siteProductService;

    @Autowired
    private IKpnSiteBankCardService siteBankCardService;

    @Autowired
    private IKpnSiteService siteService;

    @Autowired
    private IKpnSitePlatformService sitePlatformService;

    @Autowired
    private IKpnSiteOrderService siteOrderService;

    @Autowired
    private IKpnSiteUserMovieHistoryService userMovieHistoryService;

    @Autowired
    private IKpnSiteUserMovieFavoritesService userMovieFavoritesService;

    @Autowired
    private IKpnSiteUserActorFavoritesService userActorFavoritesService;

    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传头像")
    @PostMapping("/upload/header")
    public Result<String> upload(@ApiIgnore @LoginUser SysUser user, @RequestParam("file") MultipartFile file) {
        ObjectInfo upload = minioTemplate.upload(file);
        String headerImgUrl = upload.getObjectPath();

        userService.lambdaUpdate().eq(SysUser::getId, user.getId()).set(SysUser::getHeadImgUrl, headerImgUrl).update();
        return Result.succeed("操作成功");
    }

    /**
     * 获取用户信息
     */
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/info")
    public Result<SysUserVo> getUserInfo(@ApiIgnore @LoginUser SysUser user) {
        try {
            SysUser userInfo = userService.getById(user.getId());

            SysUserVo sysUserVo = SysUserVo.builder()
                    .username(userInfo.getNickname())
                    .headImgUrl(externalEndpoint + PornConstants.Symbol.FORWARD_SLASH + userInfo.getHeadImgUrl())
                    .inviteCode(userInfo.getInviteCode())
                    .promotionCode(userInfo.getPromotionCode())
                    .vip(userInfo.getVip())
                    .vipExpire(DateUtil.formatDate(userInfo.getVipExpire()))
                    .kBalance(userInfo.getKBalance().setScale(2, RoundingMode.FLOOR))
                    .build();
            return Result.succeed(sysUserVo, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    @ApiOperation("使用K币开通/续费VIP")
    @PostMapping("/buy/kb")
    public Result<String> buyVipUseKb(@ApiIgnore @LoginUser SysUser user, @ApiParam(value = "产品id", required = true) Long productId) {
        try {
            SysUser sysUser = userService.getById(user.getId());
            KpnSiteProduct product = siteProductService.getById(productId);
            BigDecimal userKBalance = sysUser.getKBalance();
            BigDecimal productPrice = product.getPrice();
            //K币
            if (!PornUtil.isDecimalGeThan(userKBalance, productPrice)) {
                return Result.of("余额不足", CodeEnum.KB_NOT_ENOUGH.getCode(), "failed");
            }
            siteProductService.buyUseKb(sysUser, product);
            return Result.succeed("开通/续期成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    @ApiOperation("获取站点支付银行卡")
    @GetMapping("/bank/cards")
    public Result<KpnSitePayResultVo> getSiteBankCards(@ApiIgnore @LoginUser SysUser user,
                                                       @ApiParam("产品id") Long productId) {
        try {
            SysUser sysUser = userService.getById(user.getId());
            KpnSiteProduct product = siteProductService.getById(productId);
            KpnSite siteInfo = siteService.getInfoById(sysUser.getSiteId());
            KpnSitePlatform sitePlatform = sitePlatformService.getBySiteId(sysUser.getSiteId());

            List<KpnSiteBankCard> siteBankCards = siteBankCardService.getBySiteId(sysUser.getSiteId());
            List<KpnSiteBankCardPayVo> bankCardPayVos = new ArrayList<>();
            for (KpnSiteBankCard siteBankCard : siteBankCards) {
                KpnSiteBankCardPayVo bankCardPayVo = new KpnSiteBankCardPayVo();
                BeanUtil.copyProperties(siteBankCard, bankCardPayVo);
                bankCardPayVo.setAmount(product.getPrice().divide(sitePlatform.getExchange(), 2, RoundingMode.CEILING));
                bankCardPayVo.setCurrency(siteInfo.getCurrencyCode());
                bankCardPayVos.add(bankCardPayVo);
            }

            String orderNo = PornConstants.Str.ORDER_NO_PREFIX + RandomUtil.randomNumbers(PornConstants.Numeric.ORDER_NO_LENGTH);
            KpnSitePayResultVo result = KpnSitePayResultVo.builder().orderNo(orderNo).bankCardPayVos(bankCardPayVos).build();
            return Result.succeed(result, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    @ApiOperation("使用现金开通/续费VIP,提交订单")
    @PostMapping("/buy/cash")
    public Result<String> buyVipUseCash(@ApiIgnore @LoginUser SysUser user,
                                        @ApiParam(value = "订单号", required = true) String orderNo,
                                        @ApiParam(value = "汇款人姓名", required = true) String remitterName,
                                        @ApiParam(value = "交易号后6位", required = true) String certificate,
                                        @ApiParam(value = "手机号") String mobile,
                                        @ApiParam(value = "银行卡id", required = true) Long bankCardId,
                                        @ApiParam(value = "产品id", required = true) Long productId) {

        String lockKey = StrUtil.format(PornConstants.Lock.USER_SITEID_SUBMIT_ORDERNO_LOCK, user.getSiteId(), orderNo);
        try {
            boolean lockedSuccess = RedissLockUtil.tryLock(lockKey, PornConstants.Lock.WAIT_TIME, PornConstants.Lock.LEASE_TIME);
            if (!lockedSuccess) {
                throw new RuntimeException("加锁失败");
            }
            if (StrUtil.isBlank(orderNo)) {
                return Result.failed("订单号不能为空");
            }
            if (StrUtil.isBlank(remitterName)) {
                return Result.failed("汇款人姓名不能为空");
            }
            if (StrUtil.isBlank(certificate)) {
                return Result.failed("交易号不能为空");
            }
            if (ObjectUtil.isEmpty(bankCardId)) {
                return Result.failed("无法获取银行卡信息");
            }
            if (ObjectUtil.isEmpty(productId)) {
                return Result.failed("无法获取产品信息");
            }

            boolean isOrderNoExists = siteOrderService.isOrderNoExists(user.getSiteId(), orderNo);
            if (isOrderNoExists) {
                return Result.failed("订单已经存在,不可重复提交");
            }

            SysUser sysUser = userService.getById(user.getId());
            KpnSiteProduct product = siteProductService.getById(productId);
            KpnSite siteInfo = siteService.getInfoById(sysUser.getSiteId());
            KpnSiteBankCard bankCard = siteBankCardService.getById(bankCardId);
            KpnSitePlatform sitePlatform = sitePlatformService.getBySiteId(sysUser.getSiteId());

            KpnSiteOrder siteOrder = new KpnSiteOrder();
            siteOrder.setSiteId(siteInfo.getId());
            siteOrder.setSiteCode(siteInfo.getCode());
            siteOrder.setSiteName(siteInfo.getName());
            siteOrder.setOrderNo(orderNo);
            siteOrder.setUserId(sysUser.getId());
            siteOrder.setUserName(sysUser.getUsername());
            siteOrder.setRemitterName(remitterName);
            siteOrder.setBankId(bankCard.getBankId());
            siteOrder.setBankName(bankCard.getBankName());
            siteOrder.setBankCardId(bankCard.getId());
            siteOrder.setBankCard(bankCard.getCard());
            siteOrder.setBankCardAccount(bankCard.getAccount());
            siteOrder.setProductId(productId);
            siteOrder.setProductName(product.getNameZh());
            if (StrUtil.isNotBlank(mobile)) {
                siteOrder.setMobile(mobile);
            }
            siteOrder.setProductPrice(product.getPrice().divide(sitePlatform.getExchange(), 2, RoundingMode.CEILING));
            siteOrder.setCertificate(certificate);

            //K币
            siteOrderService.save(siteOrder);
            return Result.succeed("订单已经提交,正在审核中");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        } finally {
            RedissLockUtil.unlock(lockKey);
        }
    }

    @ApiOperation(value = "查询我的浏览历史")
    @GetMapping("/history/movie")
    public Result<PornPageResult<KpnSiteMovieBaseVo>> watchHistory(@ApiIgnore @LoginUser SysUser user,
                                                                   @ApiParam("当前页") Integer currPage,
                                                                   @ApiParam("每页条数") Integer pageSize) {
        try {
            PornPageResult<KpnSiteMovieBaseVo> watchHistoryPageResult = userMovieHistoryService.getWatchHistoryByPage(user.getSiteId(), user.getId(), currPage, pageSize);
            return Result.succeed(watchHistoryPageResult, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    @ApiOperation(value = "查询我的收藏影片")
    @GetMapping("/favorites/movie")
    public Result<PornPageResult<KpnSiteMovieBaseVo>> favoritesHistory(@ApiIgnore @LoginUser SysUser user,
                                                                       @ApiParam("当前页") Integer currPage,
                                                                       @ApiParam("每页条数") Integer pageSize) {
        try {
            PornPageResult<KpnSiteMovieBaseVo> watchHistoryPageResult = userMovieFavoritesService.getFavoritesMoviesByPage(user.getSiteId(), user.getId(), currPage, pageSize);
            return Result.succeed(watchHistoryPageResult, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    @ApiOperation(value = "查询我的收藏演员")
    @GetMapping("/favorites/actor")
    public Result<PornPageResult<KpnActorVo>> actorHistory(@ApiIgnore @LoginUser SysUser user,
                                                                       @ApiParam("当前页") Integer currPage,
                                                                       @ApiParam("每页条数") Integer pageSize) {
        try {
            PornPageResult<KpnActorVo> actorPageResult = userActorFavoritesService.getFavoritesActorsByPage(user.getSiteId(), user.getId(), currPage, pageSize);
            return Result.succeed(actorPageResult, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 保存意见反馈
     */
    @ApiOperation(value = "保存意见反馈")
    @PostMapping("/saveSuggestion")
    public Result<String> saveSuggestion(@ApiIgnore @LoginUser SysUser user, String email, String content) {
        try {
            if (StrUtil.isBlank(content)) {
                return Result.failed("意见内容不能为空");
            }

            if (StrUtil.trim(content).length() > 1000) {
                return Result.failed("内容长度不能超过1000");
            }

            if (StrUtil.isNotBlank(email)) {
                email = StrUtil.trim(email);
                if (email.length() > 100) {
                    return Result.failed("邮箱长度不能超过100");
                }
                if (!email.matches(PornConstants.Reg.CHECK_EMAIL)) {
                    return Result.failed("邮箱格式不正确");
                }
            }
            SysUser userInfo = userService.getById(user.getId());

            KpnSiteSuggestion siteSuggestion = new KpnSiteSuggestion();
            siteSuggestion.setSiteId(userInfo.getSiteId());
            siteSuggestion.setSiteCode(userInfo.getSiteCode());
            siteSuggestion.setSiteName(userInfo.getSiteName());
            siteSuggestion.setUserId(userInfo.getId());
            siteSuggestion.setUserName(userInfo.getUsername());
            siteSuggestion.setContent(content);
            if (StrUtil.isNotBlank(email)) {
                siteSuggestion.setEmail(email);
            }
            siteSuggestionService.save(siteSuggestion);
            return Result.succeed("succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 获取邮件消息(公告)
     */
    @ApiOperation(value = "获取邮件消息")
    @GetMapping("/getAnnouncements")
    public Result<List<AnnouncementVo>> getAnnouncements(@ApiIgnore @LoginUser SysUser user) {
        try {
            List<KpnSiteAnnouncement> kpnSiteAnnouncements = siteAnnouncementService.lambdaQuery()
                    .eq(KpnSiteAnnouncement::getSiteId, user.getSiteId())
                    .eq(KpnSiteAnnouncement::getStatus, true)
                    .orderByDesc(KpnSiteAnnouncement::getSort, KpnSiteAnnouncement::getCreateTime)
                    .list();

            List<AnnouncementVo> announcementVos = kpnSiteAnnouncements.stream().map(AnnouncementVo::new).collect(Collectors.toList());

            return Result.succeed(announcementVos, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 获取月份签到信息
     */
    @ApiOperation(value = "获取月份签到信息")
    @GetMapping("/sign/history")
    public Result<List<KpnSiteUserSignHistoryVo>> getSignHistory(@ApiIgnore @LoginUser SysUser user,
                                                                 @ApiParam("当前年月(yyyy-MM),为空时返回当前月份签到数据") String month) {
        try {
            SysUser sysUser = userService.getById(user.getId());
            if (StrUtil.isBlank(month)) {
                month = DateUtil.format(new Date(), PornConstants.Format.YYYY_MM);
            }

            List<KpnSiteUserSignHistoryVo> resultVos = siteSignDetailService.getSignHistory(sysUser, month);
            return Result.succeed(resultVos, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 获取累计签到天数
     */
    @ApiOperation(value = "获取累计签到天数")
    @GetMapping("/sign/days")
    public Result<Integer> getSignDays(@ApiIgnore @LoginUser SysUser user) {
        try {
            Integer days = siteSignDetailService.getUserSignDays(user.getId());
            return Result.succeed(days, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 签到
     */
    @ApiOperation(value = "签到")
    @PostMapping("/sign")
    public Result<KpnSiteUserSignResultVo> sign(@ApiIgnore @LoginUser SysUser user,
                                                @ApiParam("签到日期") String date) {

        String lockKey = StrUtil.format(PornConstants.Lock.USER_SIGN_LOCK, user.getId());
        try {
            boolean lockedSuccess = RedissLockUtil.tryLock(lockKey, PornConstants.Lock.WAIT_TIME, PornConstants.Lock.LEASE_TIME);
            if (!lockedSuccess) {
                throw new RuntimeException("加锁失败");
            }
            //只有当天可以签到
            if (!(DateUtil.formatDate(new Date()).equalsIgnoreCase(date))) {
                log.error("签到日期:{}", date);
                return Result.failed("只有今日可以签到");
            }

            //判断当天是否已经签到
            boolean hasSigned = siteSignDetailService.checkHasSigned(user.getId(), date);
            if (hasSigned) {
                return Result.failed("今日已经签到");
            }

            //签到
            SysUser sysUser = userService.getById(user.getId());
            KpnSiteUserSignResultVo resultVo = siteSignDetailService.sign(sysUser, date);

            return Result.succeed(resultVo, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        } finally {
            log.info("释放锁:" + lockKey);
            RedissLockUtil.unlock(lockKey);
        }
    }

    /**
     * 填写邀请码
     */
    @ApiOperation(value = "会员填写邀请码")
    @PostMapping("/inviteCode/save")
    @Transactional
    public Result<String> saveInviteCode(@ApiIgnore @LoginUser SysUser user,
                                         @ApiParam(value = "邀请码", required = true) String inviteCode) {
        String lockKey = StrUtil.format(PornConstants.Lock.USER_SAVE_INVITE_CODE_LOCK, user.getId());
        try {
            boolean lockedSuccess = RedissLockUtil.tryLock(lockKey, PornConstants.Lock.WAIT_TIME, PornConstants.Lock.LEASE_TIME);
            if (!lockedSuccess) {
                throw new RuntimeException("加锁失败");
            }
            if (StrUtil.isBlank(inviteCode) || inviteCode.length() != PornConstants.Numeric.INVITE_CODE_LENGTH) {
                return Result.failed("邀请码错误");
            }

            SysUser promoteUser = userService.getByInviteCode(inviteCode);
            if (ObjectUtil.isEmpty(promoteUser)) {
                return Result.failed("邀请码错误");
            }

            userService.saveInviteCode(user.getSiteId(), user.getId(), promoteUser, inviteCode);
            return Result.succeed("succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        } finally {
            RedissLockUtil.unlock(lockKey);
        }
    }

    /**
     * 获取我的推广数据
     */
    @ApiOperation(value = "获取我的推广数据")
    @GetMapping("/promotion/info")
    public Result<KpnPromotionInfoVo> getPromotionInfo(@ApiIgnore @LoginUser SysUser user) {
        try {
            SysUser sysUser = userService.getById(user.getId());
            String promotionCode = sysUser.getPromotionCode();
            if (StrUtil.isBlank(promotionCode)) {
                log.error("会员推广码为空,userId: {}", sysUser.getId());
                KpnPromotionInfoVo emptyVo = KpnPromotionInfoVo.builder().promotionCode(promotionCode).members(0).vipDays(0).kbs(BigDecimal.ZERO).build();
                return Result.succeed(emptyVo, "succeed");
            }

            Integer members = userService.getPromotionMemberCount(promotionCode);
            Integer vipDays = siteUserVipLogService.getPromotionRewardVipDays(sysUser.getId());
            BigDecimal kbs = moneyLogService.getPromotionRewardTotalKbs(sysUser.getId()).setScale(2, RoundingMode.FLOOR);

            KpnPromotionInfoVo promotionInfoVo = KpnPromotionInfoVo.builder().promotionCode(promotionCode).members(members).vipDays(vipDays).kbs(kbs).build();
            return Result.succeed(promotionInfoVo, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 获取会员自定义频道
     *
     * @return
     */
    @GetMapping("/channels")
    @ApiOperation(value = "获取会员自定义频道")
    public Result<List<KpnMemberChannelVo>> getUserChannels(@ApiIgnore @LoginUser SysUser user) {
        try {
            List<KpnSiteChannel> memberChannels = siteChannelService.getMemberChannels(user.getId());

            List<KpnMemberChannelVo> memberChannelVos = memberChannels.stream().map(kpnSiteChannel -> {
                KpnMemberChannelVo memberChannelVo = new KpnMemberChannelVo();
                memberChannelVo.setChannelId(kpnSiteChannel.getId());
                BeanUtil.copyProperties(kpnSiteChannel, memberChannelVo);
                memberChannelVo.setName(LanguageUtil.getLanguageName(memberChannelVo));
                return memberChannelVo;
            }).collect(Collectors.toList());

            return Result.succeed(memberChannelVos, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员自定义频道-排序
     *
     * @return
     */
    @PostMapping("/channel/sort")
    @ApiOperation(value = "会员自定义频道排序")
    public Result<String> changeChannelsSort(@ApiIgnore @LoginUser SysUser user, @RequestBody List<MemberChannelSortCo> channelSortCos) {
        try {
            siteChannelService.saveMemberChannelsSort(user.getId(), channelSortCos);
            return Result.succeed("succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员自定义频道-添加
     *
     * @param user      登录会员
     * @param channelId 添加频道id
     * @return
     */
    @PostMapping("/channel/add")
    @ApiOperation(value = "会员自定义频道-添加")
    public Result<String> addChannel(@ApiIgnore @LoginUser SysUser user, Long channelId) {
        try {
            siteChannelService.addChannel(user.getId(), user.getUsername(), user.getSiteId(), user.getSiteCode(), user.getSiteName(), channelId);
            return Result.succeed("succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员自定义频道-移除频道
     *
     * @param user      登录会员
     * @param channelId 添加频道id
     * @return
     */
    @PostMapping("/channel/remove")
    @ApiOperation(value = "会员自定义频道-移除")
    public Result<String> removeChannel(@ApiIgnore @LoginUser SysUser user, Long channelId) {
        try {
            siteChannelService.removeChannel(user.getId(), channelId);
            return Result.succeed("succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员添加影片收藏
     *
     * @return 影片收藏量
     */
    @PostMapping("/favorites/add/movie")
    @ApiOperation(value = "会员添加影片收藏")
    public Result<Long> addMovieFavorites(@ApiIgnore @LoginUser SysUser user, Long movieId) {
        try {
            Long siteMovieFavorites = siteMovieService.addSiteMovieFavorites(user.getSiteId(), user.getId(), movieId);

            return Result.succeed(siteMovieFavorites, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员移除影片收藏
     *
     * @return 影片收藏量
     */
    @PostMapping("/favorites/remove/movie")
    @ApiOperation(value = "取消影片收藏")
    public Result<Long> removeMovieFavorites(@ApiIgnore @LoginUser SysUser user, Long movieId) {
        try {
            Long siteMovieFavorites = siteMovieService.removeSiteMovieFavorites(user.getSiteId(), user.getId(), movieId);

            return Result.succeed(siteMovieFavorites, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }


    /**
     * 会员添加演员收藏
     *
     * @return 站点演员收藏量
     */
    @PostMapping("/favorites/add/actor")
    @ApiOperation(value = "会员添加演员收藏")
    public Result<Long> addActorFavorites(@ApiIgnore @LoginUser SysUser user, Long actorId) {
        try {
            if (ObjectUtil.isEmpty(user)) {
                return Result.failed("请先登录");
            }

            Long siteActorFavorites = siteActorService.addSiteActorFavorites(user.getSiteId(), user.getId(), actorId);

            return Result.succeed(siteActorFavorites, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员移除演员收藏
     *
     * @return 演员收藏量
     */
    @PostMapping("/favorites/remove/actor")
    @ApiOperation(value = "取消演员收藏")
    public Result<Long> removeActorFavorites(@ApiIgnore @LoginUser SysUser user, Long actorId) {
        try {
            if (ObjectUtil.isEmpty(user)) {
                return Result.failed("请先登录");
            }
            Long siteActorFavorites = siteActorService.removeSiteActorFavorites(user.getSiteId(), user.getId(), actorId);

            return Result.succeed(siteActorFavorites, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }


}
