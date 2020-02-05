package com.example.latte_core.app;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;


import okhttp3.Interceptor;

/**
 * 配置文件的存储和获取
 */
public class Configurator {
    private static final HashMap<Object, Object> LATTE_CONFIGS = new HashMap<>();
    private static final Handler HANDLER = new Handler();
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();//封装下字体图标iconify
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();
    private Configurator(){
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY.name(),false); //配置开始了，但是没有完成
        LATTE_CONFIGS.put(ConfigType.HANDLER, HANDLER);
    }
    //线程安全的懒汉模式
    public static  Configurator getInstance(){
        return Holder.INSTANCE;
    }

    //直接返回
    final HashMap<Object, Object> getLatteConfigs() {
        return LATTE_CONFIGS;
    }

    //静态内部类单例模式的初始化
    private static class Holder{
        private static final Configurator INSTANCE=new Configurator();
    }
    public final void configure(){
        initIcons();
        Logger.addLogAdapter(new AndroidLogAdapter());
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY, true);//配置开始了，并且已经完成
        Utils.init(Latte.getApplicationContext());
    }

    //开始实例化API—HOST
    public final Configurator withApiHost(String host){
        LATTE_CONFIGS.put(ConfigType.API_HOST,host);
        return this;
    }


    public final Configurator withLoaderDelayed(long delayed) {
        LATTE_CONFIGS.put(ConfigType.LOADER_DELAYED, delayed);
        return this;
    }
    //初始化字体图标
    private void initIcons(){
        if (ICONS.size()>0){
            final Iconify.IconifyInitializer initializer=Iconify.with(ICONS.get(0));

            for (int i = 0; i <ICONS.size() ; i++) {
                initializer.with(ICONS.get(i));

            }
        }
    }

    /*
     * 加入自己的字体图标
     * */
    public final Configurator withIcon(IconFontDescriptor descriptor){
        ICONS.add(descriptor);
        return this;
    }

    public final Configurator withInterceptor(Interceptor interceptor) {
        INTERCEPTORS.add(interceptor);
        LATTE_CONFIGS.put(ConfigType.INTERCEPTOR, INTERCEPTORS);
        return this;
    }
    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors) {
        INTERCEPTORS.addAll(interceptors);
        LATTE_CONFIGS.put(ConfigType.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    public Configurator withJavascriptInterface(@NonNull String name) {
        LATTE_CONFIGS.put(ConfigType.JAVASCRIPT_INTERFACE, name);
        return this;
    }


    /*
     *
     * 检查配置是否正确
     * 设置为不可更改
     *
     *
     * 在应用程序中获取调用
     * */
    private void checkConfiguration() {
        final boolean isReady = (boolean) LATTE_CONFIGS.get(ConfigType.CONFIG_READY);
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }

    /*
     * @SuppressWarnings("unchecked") 没有检测过的
     * */
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key) {
        checkConfiguration();
        final Object value = LATTE_CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + " IS NULL");
        }
        return (T) LATTE_CONFIGS.get(key);
    }

}
