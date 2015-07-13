package com.ycsoft.daos.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>定义元数据模板,用于配置实体表相关的信息</p>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface POJO {

    //实体类对应的表名
    String tn() default "";

    //主键字段名称
    String pk() default "";

    //序列名称
    String sn() default "" ;
}

