package com.central.porn.entity.vo;

import com.central.common.model.KpnSiteAnnouncement;
import com.central.porn.core.language.LanguageEnum;
import com.central.porn.core.language.LanguageThreadLocal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("公告")
public class AnnouncementVo implements Serializable {

    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String content;

    public AnnouncementVo(KpnSiteAnnouncement kpnSiteAnnouncement) {
        setTitle(kpnSiteAnnouncement);
        setContent(kpnSiteAnnouncement);
    }

    public void setTitle(KpnSiteAnnouncement kpnSiteAnnouncement) {
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

    public void setContent(KpnSiteAnnouncement kpnSiteAnnouncement) {
        final String language = LanguageThreadLocal.getLanguage();
        if (language.equalsIgnoreCase(LanguageEnum.ZH.name().toLowerCase())) {
            this.content = kpnSiteAnnouncement.getTitleZh();
        }

        else if (language.equalsIgnoreCase(LanguageEnum.EN.name().toLowerCase())) {
            this.content = kpnSiteAnnouncement.getTitleEn();
        }

        else if (language.equalsIgnoreCase(LanguageEnum.KH.name().toLowerCase())) {
            this.content = kpnSiteAnnouncement.getTitleKh();
        }
    }

    public void setContent(String content) {
        this.content = content;
    }
}
