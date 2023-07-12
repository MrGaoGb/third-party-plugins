package com.mrgao.demo.cloneable;

import com.mrgao.demo.cloneable.entity.HomeCloneable;
import com.mrgao.demo.cloneable.entity.Person;

/**
 * @author Mr.Gao
 * @date 2023/7/12 15:34
 * @apiNote: <p>
 * <p>
 * 1、== 对于基本类型，比较的是值是否相等，而对于引用类型，比较的是地址是否相等
 * </p>
 * </p>
 */
public class TestCloneable {
    public static void main(String[] args) throws CloneNotSupportedException {
        // --个人信息
        Person p = new Person();
        p.setId(100L);
        p.setName("Mr.Gao");
        p.setAddress("陕西渭南");
        // --主页信息
        HomeCloneable h = new HomeCloneable();
        h.setHomeName("Mr.Gao 专属定制主页");
        h.setPerson(p);
        System.out.println(h + "HomeHashCode:" + h.hashCode() + ", PersonHashCode:" + p.hashCode());
        // --获取克隆对象
        HomeCloneable hCloneObject = h.clone();
        System.out.println(hCloneObject + "HomeHashCode:" + hCloneObject.hashCode() + ", PersonHashCode:" + hCloneObject.getPerson().hashCode());
        Person person = hCloneObject.getPerson();
        person.setAddress("陕西西安");
        System.out.println("对象p的地址:" + p.getAddress() + ", 对象clonePerson的地址:" + hCloneObject.getPerson().getAddress());
        /*
         * 结论:
         * 1、通过克隆方法clone()获得的对象(hCloneObject)和原对象(h)是不同的两个对象,但是对象中的
         * person属于同一个对象。因此后续的操作相当于操作同一个对象
         * 2、使用clone方法的时候，会重新开辟内存空间
         * 例如：
         * Person p = new Person();
         * Person pClone = p;
         * */
    }
}
