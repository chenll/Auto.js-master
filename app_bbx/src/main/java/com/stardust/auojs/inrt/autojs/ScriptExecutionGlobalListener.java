package com.stardust.auojs.inrt.autojs;

import android.util.Log;

import com.stardust.Event.ScriptEvent;
import com.stardust.app.GlobalAppContext;
import com.stardust.auojs.inrt.R;
import com.stardust.autojs.execution.ScriptExecution;
import com.stardust.autojs.execution.ScriptExecutionListener;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Stardust on 2017/5/3.
 */

public class ScriptExecutionGlobalListener implements ScriptExecutionListener {
    private static final String ENGINE_TAG_START_TIME = "org.autojs.autojs.autojs.Goodbye, World";

    @Override
    public void onStart(ScriptExecution execution) {
        execution.getEngine().setTag(ENGINE_TAG_START_TIME, System.currentTimeMillis());
    }

    @Override
    public void onSuccess(ScriptExecution execution, Object result) {
        onFinish(execution);


    }

    private void onFinish(ScriptExecution execution) {
        Long millis = (Long) execution.getEngine().getTag(ENGINE_TAG_START_TIME);
        if (millis == null)
            return;
        double seconds = (System.currentTimeMillis() - millis) / 1000.0;
        AutoJs.getInstance().getScriptEngineService().getGlobalConsole().verbose(GlobalAppContext.getString(R.string.text_execution_finished), execution.getSource().toString(), seconds);
        EventBus.getDefault().post(new ScriptEvent(execution));

    }

    @Override
    public void onException(ScriptExecution execution, Exception e) {
        onFinish(execution);
        Log.e("aaa","------>onException"+e.getMessage());

    }

}
