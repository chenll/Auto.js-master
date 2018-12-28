package com.stardust.auojs.inrt.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stardust.auojs.inrt.R;
import com.stardust.auojs.inrt.bean.AppDownloadBean;

import java.util.List;

public class AppDownloadAdapter extends BaseQuickAdapter<AppDownloadBean, BaseViewHolder> {

    public AppDownloadAdapter(int layoutResId, @Nullable List<AppDownloadBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppDownloadBean item) {
        helper.setText(R.id.tv_app_name, item.getApkName() + "");
    }
}
