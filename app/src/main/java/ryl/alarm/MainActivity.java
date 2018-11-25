package ryl.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    RunningView running;
    MyCustomerView myCustomerView;
    Ruler ruler_shuzhang;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initView2();
        initView();
    }

    private void initView() {
        running = findViewById(R.id.running);
        /**
         * 所走的步数进度
         *
         * @param startAngleNum开始的步数
         * @param totalStepNum   最大的步数
         * @param currentCounts  当前的步数
         */
        running.setCurrentCount(0, 10000, 1781);

        myCustomerView = findViewById(R.id.myCustomerView);
        myCustomerView.setPercent(66);

        ruler_shuzhang = findViewById(R.id.ruler_shuzhang);
        ruler_shuzhang.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(int value) {
                Log.e("ruler_shuzhang_value", value + "");
            }
        });
    }

    private void initView2() {
        alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);

        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        TextView btn_set = (TextView) findViewById(R.id.btn_set);
        btn_set.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Calendar currentTime = Calendar.getInstance();

                new TimePickerDialog(MainActivity.this, 0,

                        new TimePickerDialog.OnTimeSetListener() {


                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                //设置当前时间

                                Calendar c = Calendar.getInstance();

                                c.setTimeInMillis(System.currentTimeMillis());

                                // 根据用户选择的时间来设置Calendar对象

                                c.set(Calendar.HOUR, hourOfDay);

                                c.set(Calendar.MINUTE, minute);

                                // ②设置AlarmManager在Calendar对应的时间启动Activity
                                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                                Toast.makeText(MainActivity.this, "闹钟已开启！", Toast.LENGTH_SHORT).show();
                            }
                        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime
                        .get(Calendar.MINUTE), false).show();
            }
        });


        TextView textView2 = (TextView) findViewById(R.id.btn_cancel);

        textView2.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                alarmManager.cancel(pendingIntent);

                Toast.makeText(MainActivity.this, "闹钟已取消！", Toast.LENGTH_SHORT).show();

            }

        });

        Toast.makeText(MainActivity.this, "start", Toast.LENGTH_SHORT).show();
        //创建一个闹钟提醒的对话框,点击确定关闭铃声与页面
        new AlertDialog.Builder(this).setTitle("闹钟").setMessage("这是出门的闹铃！")
                .setPositiveButton("关闭闹铃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "finish", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
}