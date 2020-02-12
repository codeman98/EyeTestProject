package com.example.eyes3.generators;

import com.example.annotations.AppRegisterGenerator;
import com.example.latte_core.wechat.templates.AppRegisterTemplate;

@SuppressWarnings("unused")
@AppRegisterGenerator(
        packageName = "com.example.eyes3",
        registerTemplate = AppRegisterTemplate.class
)
public interface AppRegister {
}
