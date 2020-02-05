package com.example.latte_core.ui.loader;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;

import com.example.latte_core.R;
import com.example.latte_core.util.dimen.DimenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
// 对传入的样式/参数封装
public class LatteLoader {
    private static final int LOADER_SIZE_SCALE = 8;//为了适应 不同的设备的屏幕的不同的大小，需要对加载的loader进行缩放
    private static final int LOADER_OFFSET_SCALE = 10;//偏移量

    private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();//存储loader

    private static final String DEFAULT_LOADER = LoaderStyle.BallClipRotatePulseIndicator.name();//默认的loader样式

    public static void showLoading(Context context, Enum<LoaderStyle> type) {
        showLoading(context, type.name());
    }

    public static void showLoading(Context context, String type) {//type是需要传入的使用哪种样式

        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);//在dialog中显示状态标志的显示

        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type, context);
        dialog.setContentView(avLoadingIndicatorView);//以此view作为根布局

        int deviceWidth = DimenUtil.getScreenWidth();//宽
        int deviceHeight = DimenUtil.getScreenHeight();//高

        final Window dialogWindow = dialog.getWindow();

        if (dialogWindow != null) {
            final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = deviceWidth / LOADER_SIZE_SCALE;
            lp.height = deviceHeight / LOADER_SIZE_SCALE;
            lp.height = lp.height + deviceHeight / LOADER_OFFSET_SCALE;
            lp.gravity = Gravity.CENTER;
        }
        LOADERS.add(dialog);
        dialog.show();
    }

    public static void showLoading(Context context) {
        showLoading(context, DEFAULT_LOADER);
    }//显示dialog

    public static void stopLoading() {//停止dialog
        for (AppCompatDialog dialog : LOADERS) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        }
    }
}
