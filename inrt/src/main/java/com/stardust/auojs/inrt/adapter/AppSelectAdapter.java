package com.stardust.auojs.inrt.adapter;

import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stardust.auojs.inrt.App;
import com.stardust.auojs.inrt.R;
import com.stardust.auojs.inrt.bean.NewTaskBeanById;
import com.stardust.utils.AppUtils;

import java.util.List;

public class AppSelectAdapter extends BaseQuickAdapter<NewTaskBeanById, BaseViewHolder> {

    public AppSelectAdapter(int layoutResId, @Nullable List<NewTaskBeanById> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewTaskBeanById item) {
        CardView cardView = helper.getView(R.id.cardview);
        cardView.setCardBackgroundColor(Color.parseColor(item.isExecuted()?(item.isExecutedSussed()?"#D1EEEE":"#FFFF00"):"#FFFFFF") );
        PackageInfo packageInfo = AppUtils.getPackageInfo(App.getApplication(), item.getF_PackageName());
        helper.setText(R.id.tv_app_name, item.getF_AppName()).setText(R.id.tv_app_packagename, item.getF_PackageName());
        helper.setText(R.id.tv_support_version, "支持版本:" + item.getF_AppVersion());
        helper.setVisible(R.id.tv_erro_hint, false);
        if (packageInfo != null) {
            helper.setText(R.id.tv_current_version, "安装版本:" + packageInfo.versionName + "");
            if (!packageInfo.versionName.equals(item.getF_AppVersion())) {
                helper.setText(R.id.tv_erro_hint, "版本不支持");
                helper.setVisible(R.id.tv_erro_hint, true);
                cardView.setCardBackgroundColor(Color.parseColor("#EE6A50") );
            }
        } else {
            helper.setText(R.id.tv_current_version, "未安装");
            helper.setText(R.id.tv_erro_hint, "应用未安装");
            helper.setVisible(R.id.tv_erro_hint, true);
            cardView.setCardBackgroundColor(Color.parseColor("#EE6A50") );
        }


    }
}
