package com.jikexueyuan.uninstallapk;

import java.io.PrintWriter;

/**
 * Created by zh on 2016/12/29.
 */

public class APKController {


    /**
     * 判断是否有root权限
     * @return
     */
    public static boolean hasRootPermission() {
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            // 代表成功
            if (value == 0) {
                return true;
            } else if (value == 1) { // 失败
                return false;
            } else { // 未知情况
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 后台卸载程序
     * @param pkgName
     */
    public static boolean uninstallInBackground(String pkgName){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
            PrintWriter.println("pm uninstall "+pkgName);
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            if (value == 0) {
                return true;
            } else if (value == 1) { // 失败
                return false;
            } else { // 未知情况
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }


}
