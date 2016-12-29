package com.jikexueyuan.uninstallapk;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Bean> list;
    private Button uninstall;
    private ListView apkList;
    private CustomAdapter adapter;
    private Button chooseAll;
    private int flag;
    private boolean isPermission;
    private List<Bean> readToUninstall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        readAPKList();
        if (APKController.hasRootPermission()) {
            isPermission = true;
        } else {
            isPermission = false;

        }
        adapter = new CustomAdapter(this, list);
        apkList.setAdapter(adapter);
        uninstall.setOnClickListener(this);
        chooseAll.setOnClickListener(this);
    }

    private void init() {
        uninstall = (Button) findViewById(R.id.uninstall);
        chooseAll = (Button) findViewById(R.id.choose_all);
        apkList = (ListView) findViewById(R.id.apk_list);
        list = new ArrayList<>();
        readToUninstall = new ArrayList<>();
        flag = 0;
    }

    private void readAPKList() {
        list.clear();
        final PackageManager packageManager = this.getPackageManager();//获取packageManager
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        int type;
        if (pInfo != null) {
            for (int i = 0; i < pInfo.size(); i++) {
                String packName = pInfo.get(i).packageName;
                if (packName.equals("com.jikexueyuan.uninstallapk")) {
                    continue;
                }

                ApplicationInfo info = pInfo.get(i).applicationInfo;
                String name = packageManager.getApplicationLabel(info).toString();
                if ((info.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    type = 0;
                } else {
                    type = 1;
                }
                Bean bean = new Bean(packName, name, false, type);
                list.add(bean);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uninstall:
                readToUninstall.clear();
                for (final Bean lists : list) {
                    if (lists.isChecked()) {
                        readToUninstall.add(lists);
                    }
                }
                final int num = readToUninstall.size();
                final int[] completeNum = {0};
                if (isPermission) {
                    new Thread() {
                        @Override
                        public void run() {
                            for (final Bean lists : readToUninstall) {
                                if (APKController.uninstallInBackground(lists.getPackageName())) {
                                    completeNum[0]++;
                                    Message message = new Message();
                                    message.arg1 = num;
                                    message.arg2 = completeNum[0];
                                    handler.sendMessage(message);
                                } else {
                                    Log.d("log", "卸载失败");
                                }
                            }
                            super.run();
                        }
                    }.start();
                } else {
//                    Uri packageURI = Uri.parse("package:" + lists.getPackageName());
//                    Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
//                    uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(uninstallIntent);
                }


                break;
            case R.id.choose_all:
                switch (flag) {
                    case 0:
                        for (Bean lists : list) {
                            if (lists.getType() == 1) {
                                lists.setChecked(true);
                            }
                        }
                        flag = 1;
                        ((Button) v).setText("取消选择全部");
                        break;
                    case 1:
                        for (Bean lists : list) {
                            lists.setChecked(false);
                        }
                        flag = 0;
                        ((Button) v).setText("选择全部非系统程序");
                        break;
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private Object object = new Object();
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.d("要卸载的应用数", msg.arg1 + "个");
            Log.d("已经卸载的应用数", msg.arg2 + "个");
            if (msg.arg2 == msg.arg1) {
                readAPKList();
                adapter.notifyDataSetChanged();
            }

            super.handleMessage(msg);
        }
    };

}
