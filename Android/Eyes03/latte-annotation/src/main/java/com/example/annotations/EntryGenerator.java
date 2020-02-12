package com.example.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//基于butterknife的元注解的原理，annimotionprocessor生成我们所需要的代码；进而绕过微信的限制，最大限度的提高代码的封装方式；
@Target(ElementType.TYPE)//告诉我们用在类上面
@Retention(RetentionPolicy.SOURCE)//编译器源码级别处理
public @interface EntryGenerator {
    String packageName();
    Class<?> entryTemplate();
}