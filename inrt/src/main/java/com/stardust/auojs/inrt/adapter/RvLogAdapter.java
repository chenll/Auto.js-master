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

public class RvLogAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public RvLogAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_log, item);


    }
}
