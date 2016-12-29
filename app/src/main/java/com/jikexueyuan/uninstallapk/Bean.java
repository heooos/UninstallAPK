package com.jikexueyuan.uninstallapk;

/**
 * Created by zh on 2016/12/29.
 */

public class Bean {

    private String packageName;
    private String apkName;
    private boolean isChecked;
    private int type;

    public Bean(String packageName, String apkName, boolean isChecked,int type) {
        this.packageName = packageName;
        this.apkName = apkName;
        this.isChecked = isChecked;
        this.type = type;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getApkName() {
        return apkName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getType() {
        return type;
    }
}
