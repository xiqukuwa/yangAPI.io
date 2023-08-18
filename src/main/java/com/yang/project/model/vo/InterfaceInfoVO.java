package com.yang.project.model.vo;

import com.yang.common.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口消息封装视图
 *
 * @author yang
 * @TableName product
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends InterfaceInfo {

    /**
     * 是否已点赞
     */
    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}