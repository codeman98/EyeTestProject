package com.example.eyes3.generators;

import com.example.annotations.EntryGenerator;
import com.example.latte_core.wechat.templates.WXEntryTemplate;

@SuppressWarnings("unused")
@EntryGenerator(
        packageName = "com.example.eyes3",
        entryTemplate = WXEntryTemplate.class
)
public interface WeChatEntry {
}
