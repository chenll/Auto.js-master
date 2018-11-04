package com.stardust.auojs.inrt.adapter;

import android.content.pm.PackageInfo;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stardust.auojs.inrt.App;
import com.stardust.auojs.inrt.R;
import com.stardust.auojs.inrt.bean.AppBean;
import com.stardust.utils.AppUtils;

import java.util.List;

public class AppSelectAdapter extends BaseQuickAdapter<AppBean, BaseViewHolder> {

    public AppSelectAdapter(int layoutResId, @Nullable List<AppBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppBean item) {
        PackageInfo packageInfo = AppUtils.getPackageInfo(App.getApplication(), item.getAppPackageName());
        helper.setText(R.id.tv_app_name, item.getAppName()).setText(R.id.tv_app_packagename, item.getAppPackageName());
        helper.setText(R.id.tv_support_version, "支持版本:" + item.getAppVersionName());
        helper.setVisible(R.id.tv_erro_hint, false);
        if (packageInfo != null) {
            helper.setText(R.id.tv_current_version, "安装版本:" + packageInfo.versionName + "");
            if (!packageInfo.versionName.equals(item.getAppVersionName())) {
                helper.setText(R.id.tv_erro_hint, "版本不支持");
                helper.setVisible(R.id.tv_erro_hint, true);
            }
        } else {
            helper.setText(R.id.tv_current_version, "未安装");
            helper.setText(R.id.tv_erro_hint, "应用未安装");
            helper.setVisible(R.id.tv_erro_hint, true);
        }


    }
}
