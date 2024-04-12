package com.example.dungeonwalkin;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StepResultActivity extends AppCompatActivity {

    private final String TAG = "StepResultActivity";

    private StepListView listView;
    private StepAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step_result);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listView), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        listView = findViewById(R.id.listView);
        Log.i(TAG, "FindListView");
        adapter = new StepAdapter(this);
        Log.i(TAG, "Create Adapter");
        Cursor cursor = ((DWApp)getApplication()).readAllStepsPerDateInDB();
        while(cursor.moveToNext()){
            String starttime = cursor.getString(0);
            int steps = cursor.getInt(1);
            adapter.addItem(starttime,steps);
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Button button = findViewById(R.id.returnButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}