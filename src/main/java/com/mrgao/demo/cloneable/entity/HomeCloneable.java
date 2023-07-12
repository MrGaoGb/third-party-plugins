package com.mrgao.demo.cloneable.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mr.Gao
 * @date 2023/7/12 15:37
 * @apiNote:
 */
@Getter
@Setter
public class HomeCloneable implements Cloneable {
    /**
     * 主页名称
     */
    private String homeName;
    /**
     * 个人信息
     */
    private Person person;

    @Override
    public HomeCloneable clone() throws CloneNotSupportedException {
        // --浅拷贝
        return (HomeCloneable) super.clone();
    }
}
