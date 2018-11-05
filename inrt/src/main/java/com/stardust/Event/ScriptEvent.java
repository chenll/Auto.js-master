package com.stardust.Event;

import com.stardust.autojs.execution.ScriptExecution;

public class ScriptEvent {
    private ScriptExecution mScriptExecution;

    public Exception getException() {
        return mException;
    }

    public void setException(Exception exception) {
        mException = exception;
    }

    public ScriptEvent(ScriptExecution scriptExecution, Exception exception) {
        mScriptExecution = scriptExecution;
        mException = exception;
    }

    private Exception mException;

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
