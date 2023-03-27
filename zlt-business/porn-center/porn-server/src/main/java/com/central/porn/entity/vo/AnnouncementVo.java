package com.central.porn.entity.vo;

import com.central.common.model.KpnSiteAnnouncement;
import com.central.common.language.LanguageEnum;
import com.central.common.language.LanguageThreadLocal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("公告")
public class AnnouncementVo implements Serializable {
    @ApiModelProperty(value = "广告Id")
    private Long id;
    @ApiModelProperty(value = "状态 0下架,1上架")
    private Integer status;
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "状态 0未读,1已读")
    private Integer isRead;
    @ApiModelProperty(value = "公告时间")
    private Date annTime;
    @ApiModelProperty(value = "读取时间")
    private Date readTime;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String content;

    public AnnouncementVo(AnnouncementUserVo kpnSiteAnnouncement) {
        setTitle(kpnSiteAnnouncement);
        setContent(kpnSiteAnnouncement);
    }

    public void setTitle(AnnouncementUserVo kpnSiteAnnouncement) {
        final String language = LanguageThreadLocal.getLanguage();
        if (language.equalsIgnoreCase(LanguageEnum.ZH.name().toLowerCase())) {
            this.title = kpnSiteAnnouncement.getTitleZh();
        }

        else if (language.equalsIgnoreCase(LanguageEnum.EN.name().toLowerCase())) {
            this.title = kpnSiteAnnouncement.getTitleEn();
        }

        else if (language.equalsIgnoreCase(LanguageEnum.KH.name().toLowerCase())) {
            this.title = kpnSiteAnnouncement.getTitleKh();
        }
    }

    public void setContent(AnnouncementUserVo kpnSiteAnnouncement) {
        final String language = LanguageThreadLocal.getLanguage();
        if (language.equalsIgnoreCase(LanguageEnum.ZH.name().toLowerCase())) {
            this.content = kpnSiteAnnouncement.getContentZh();
        }

        else if (language.equalsIgnoreCase(LanguageEnum.EN.name().toLowerCase())) {
            this.content = kpnSiteAnnouncement.getContentEn();
        }

        else if (language.equalsIgnoreCase(LanguageEnum.KH.name().toLowerCase())) {
            this.content = kpnSiteAnnouncement.getContentKh();
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public void setAnnTime(Date annTime) {
        this.annTime = annTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

}
