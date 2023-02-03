package com.central.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * 影片标签分类
 *
 * @author yixiu
 * @date 2023-02-03 16:32:41
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("kpn_tag_category")
public class KpnTagCategory extends SuperEntity {
    private static final long serialVersionUID=1L;

        private String name;
        private String remark;
        private Date createTime;
        private Date updateTime;
        private String createBy;
        private String updateBy;
    }
