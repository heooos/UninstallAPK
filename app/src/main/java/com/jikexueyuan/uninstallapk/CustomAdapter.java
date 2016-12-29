package com.jikexueyuan.uninstallapk;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zh on 2016/12/29.
 */

public class CustomAdapter extends BaseAdapter {


    private List<Bean> list;
    private Context context;

    public CustomAdapter(Context context, List<Bean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Bean bean = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_cell, null);
            holder = new ViewHolder();
            holder.apkName = (TextView) convertView.findViewById(R.id.apk_name);
            holder.packageName = (TextView) convertView.findViewById(R.id.package_name);
            holder.isChecked = (CheckBox) convertView.findViewById(R.id.isChecked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.isChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bean.setChecked(isChecked);
            }
        });

        if (bean.getType() == 0) {
            holder.packageName.setTextColor(Color.RED);
            holder.apkName.setTextColor(Color.RED);
            holder.isChecked.setClickable(false);
        }else {
            holder.packageName.setTextColor(Color.BLACK);
            holder.apkName.setTextColor(Color.BLACK);
            holder.isChecked.setClickable(true);
        }
        holder.isChecked.setChecked(bean.isChecked());
        holder.packageName.setText(bean.getPackageName());
        holder.apkName.setText(bean.getApkName());

        return convertView;
    }

    class ViewHolder {
        private TextView packageName;
        private TextView apkName;
        private CheckBox isChecked;

    }

}
