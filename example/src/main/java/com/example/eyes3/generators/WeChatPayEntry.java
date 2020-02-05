package com.example.eyes3.generators;


import com.example.annotations.PayEntryGenerator;
import com.example.latte_core.wechat.templates.WXPayEntryTemplate;

@SuppressWarnings("unused")
@PayEntryGenerator(
        packageName = "com.example.eyes3",
        payEntryTemplate = WXPayEntryTemplate.class
)
public interface WeChatPayEntry {
}
