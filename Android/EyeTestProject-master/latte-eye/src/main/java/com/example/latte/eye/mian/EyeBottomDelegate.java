package com.example.latte.eye.mian;

import android.graphics.Color;

import com.example.latte.eye.mian.index.IndexDelegate;
import com.example.latte.eye.mian.personal.PersonalDelegate;
import com.example.latte_core.delegates.bottom.BaseBottomDelegate;
import com.example.latte_core.delegates.bottom.BottomItemDelegate;
import com.example.latte_core.delegates.bottom.BottomTabBean;
import com.example.latte_core.delegates.bottom.ItemBuilder;

import java.util.LinkedHashMap;

public class EyeBottomDelegate extends BaseBottomDelegate {
    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();
        items.put(new BottomTabBean("{fa-home}", "主页"), new IndexDelegate());//图标http://fontawesome.dashgame.com/
        items.put(new BottomTabBean("{fa-compass}", "咨询"), new IndexDelegate());
        items.put(new BottomTabBean("{fa-user}", "我的"), new PersonalDelegate());
        return builder.addItems(items).build();
    }

    @Override
    public int setIndexDelegate() {
        return 0;
    }

    //设置点击颜色
    @Override
    public int setClickedColor() {
        return Color.parseColor("#1296db");
    }
}
