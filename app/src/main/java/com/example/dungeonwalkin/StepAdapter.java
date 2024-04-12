package com.example.dungeonwalkin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class StepAdapter extends BaseAdapter {
    private static final String TAG = "StepAdapterAdapter";
    private final Activity activity;
    private ArrayList<PrintSteps> arrayList = new ArrayList<>();

    public StepAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.i(TAG, "getView called");
        if (convertView == null) {
            Log.i(TAG, "getView: convertView==null");
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.steps_item, parent, false);
        }
        LinearLayout linearLayout = (LinearLayout) convertView;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int itemHeight = size.x/5;
        ViewGroup.LayoutParams param = linearLayout.getLayoutParams();
        param.height = itemHeight;
        linearLayout.setLayoutParams(param);
        Log.i(TAG,"LinearLayout Setting complete");
        PrintSteps itemData = arrayList.get(position);
        Log.i(TAG,"get "+position+"st itemdata complete");
        TextView textViewDate = linearLayout.findViewById(R.id.textViewDate);
        TextView textViewSteps = linearLayout.findViewById(R.id.textViewSteps);
        textViewDate.setText(itemData.getDate());
        textViewSteps.setText(""+itemData.getSteps());

        return linearLayout;
    }

    public void addItem(String date, int steps) {
        PrintSteps item = new PrintSteps(date,steps);
        arrayList.add(item);
    }

}
