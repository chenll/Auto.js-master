package com.stardust.Event;

import com.stardust.autojs.execution.ScriptExecution;

public class ScriptEvent {
    private ScriptExecution mScriptExecution;

    public ScriptEvent(ScriptExecution scriptExecution) {
        mScriptExecution = scriptExecution;
    }

    public ScriptExecution getScriptExecution() {
        return mScriptExecution;
    }

    public void setScriptExecution(ScriptExecution scriptExecution) {
        mScriptExecution = scriptExecution;
    }
}
