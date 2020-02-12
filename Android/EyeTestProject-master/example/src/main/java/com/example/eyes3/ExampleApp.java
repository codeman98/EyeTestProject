package com.example.eyes3;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.example.latte.eye.database.DatabaseManager;
import com.example.latte.eye.icon.FontEyeModule;
import com.example.latte_core.app.Latte;
import com.example.latte_core.net.interceptors.DebugInterceptor;
import com.facebook.stetho.Stetho;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class ExampleApp extends MultiDexApplication {
    @Override
    public void onCreate(){
        super.onCreate();
        Latte.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEyeModule())
                .withLoaderDelayed(1000)
                .withApiHost("http://127.0.0.1/")
                .withInterceptor(new DebugInterceptor("test", R.raw.test))
                .configure();
        initStetho();
        DatabaseManager.getInstance().init(this);

    }
        private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
