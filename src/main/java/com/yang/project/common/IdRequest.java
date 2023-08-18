package com.yang.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author yang
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}