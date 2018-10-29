package com.stardust.auojs.inrt;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.stardust.auojs.inrt.autojs.AutoJs;
import com.stardust.auojs.inrt.launch.GlobalProjectLauncher;
import com.stardust.autojs.core.console.ConsoleView;
import com.stardust.autojs.core.console.StardustConsole;



public class LogActivity extends AppCompatActivity {


    public static final String EXTRA_LAUNCH_SCRIPT = "launch_script";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
//        new Rect().bottom;
        findViewById(R.id.btn_start).setOnClickListener(view -> {
            if (getIntent().getBooleanExtra(EXTRA_LAUNCH_SCRIPT, false)) {
                GlobalProjectLauncher.getInstance().launch(LogActivity.this);
            }
        });

    }

    private void setupView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("自动运行");
        setSupportActionBar(toolbar);
        ConsoleView consoleView = (ConsoleView) findViewById(R.id.console);
        consoleView.setConsole((StardustConsole) AutoJs.getInstance().getGlobalConsole());
        consoleView.findViewById(R.id.input_container).setVisibility(View.GONE);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        startActivity(new Intent(this, SettingsActivity.class));
//        return true;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
}
