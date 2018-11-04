package com.stardust.auojs.inrt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.stardust.app.GlobalAppContext;
import com.stardust.auojs.inrt.autojs.AutoJs;
import com.stardust.auojs.inrt.bean.AppBean;
import com.stardust.auojs.inrt.launch.AssetsProjectLauncher;
import com.stardust.autojs.core.console.ConsoleView;
import com.stardust.autojs.core.console.StardustConsole;


public class LogActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String EXTRA_LAUNCH_SCRIPT = "launch_script";
    private AssetsProjectLauncher mAssetsProjectLauncher;

    private AppBean mAppBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppBean = getIntent().getExtras().getParcelable("appbean");
        setupView();

        AppAutoMgr.CURRENTPACKAGENAME = mAppBean.getAppPackageName();

//        new AssetsProjectLauncher("project_" + mAppBean.getLable(), GlobalAppContext.get()).launch(LogActivity.this);
        new AssetsProjectLauncher("project", GlobalAppContext.get()).launch(LogActivity.this);
    }

    private void setupView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mAppBean.getAppName());
        setSupportActionBar(toolbar);
        ConsoleView consoleView = (ConsoleView) findViewById(R.id.console);
        consoleView.setConsole((StardustConsole) AutoJs.getInstance().getGlobalConsole());
        consoleView.findViewById(R.id.input_container).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (!getIntent().getBooleanExtra(EXTRA_LAUNCH_SCRIPT, false)) {
            return;
        }
        if (mAssetsProjectLauncher != null && mAssetsProjectLauncher.isRunning()) {
            Toast.makeText(this, "有任务正在运行,请稍后再试", Toast.LENGTH_LONG).show();
            return;
        }
        int id = v.getId();
        if (id == R.id.btn_start_qktx) {
            mAssetsProjectLauncher = new AssetsProjectLauncher("project_qktx", GlobalAppContext.get());
//            GlobalProjectLauncher.getInstance().launch(LogActivity.this);
        }

        if (id == R.id.btn_start_jkd) {
            mAssetsProjectLauncher = new AssetsProjectLauncher("project_jkd", GlobalAppContext.get());
//            GlobalProjectLauncher.getInstance().launch(LogActivity.this);
        }
        if (id == R.id.btn_start_mytt) {
//            mAssetsProjectLauncher = new AssetsProjectLauncher("project_mytt", GlobalAppContext.get());
            mAssetsProjectLauncher = new AssetsProjectLauncher("project_jryk", GlobalAppContext.get());
//            GlobalProjectLauncher.getInstance().launch(LogActivity.this);
        }
        mAssetsProjectLauncher.launch(LogActivity.this);

    }

}
