package com.stardust.auojs.inrt;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SysRootUtils {


    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @return 应用程序是/否获取Root权限
     */
    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.e("aaa",e.toString());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;

    }


    public static void rootPermission(String... command) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            if (command != null && command.length > 0) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                for (int i = 0; i < command.length; i++) {
                    writer.write(command[i]);
                }
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
