package com.mrgao.demo.cloneable.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mr.Gao
 * @date 2023/7/12 15:34
 * @apiNote:
 */
@Getter
@Setter
public class Person {
    /**
     * 姓名
     */
    private String name;
    /**
     * 地址
     */
    private String address;
    /**
     * 主键id
     */
    private Long id;
}
