package com.example.latte_core.delegates.bottom;

import java.util.LinkedHashMap;

/**
 *   【构造器】ItemBuilder 容器，将BottomItemDelegate 和 BottomItemDelegate（fragment的基类） 构造关联起来；
 */
public class ItemBuilder {
    private final LinkedHashMap<BottomTabBean, BottomItemDelegate> ITEMS = new LinkedHashMap<>();//用linkedHashMap则有序

    static ItemBuilder builder() {
        return new ItemBuilder();
    }

    //将item创造出来放入到链式存储，推崇链式存储，单个加入
    public final ItemBuilder addItem(BottomTabBean bean, BottomItemDelegate delegate) {
        ITEMS.put(bean, delegate);
        return this;
    }
////将item创造出来放入到链式存储，推崇链式存储，同时加入多个
    public final ItemBuilder addItems(LinkedHashMap<BottomTabBean, BottomItemDelegate> items) {
        ITEMS.putAll(items);
        return this;
    }
//返回链式存储链表
    public final LinkedHashMap<BottomTabBean, BottomItemDelegate> build() {
        return ITEMS;
    }
}
