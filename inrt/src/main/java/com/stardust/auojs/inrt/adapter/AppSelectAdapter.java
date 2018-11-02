package com.stardust.auojs.inrt.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stardust.auojs.inrt.R;
import com.stardust.auojs.inrt.bean.AppBean;

import java.util.List;

public class AppSelectAdapter extends BaseQuickAdapter<AppBean, BaseViewHolder> {

    public AppSelectAdapter(int layoutResId, @Nullable List<AppBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppBean item) {
        helper.setText(R.id.tv_app_name, item.getAppName()).setText(R.id.tv_app_packagename, item.getAppPackageName());
    }
}
