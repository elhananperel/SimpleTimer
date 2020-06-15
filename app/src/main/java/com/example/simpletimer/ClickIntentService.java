package com.example.simpletimer;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ClickIntentService extends IntentService {
    private static final String TAG = "timer_i";
    public static final String ACTION_CLICK = "com.eliperel.pumpsync.widgets.click";

    public ClickIntentService() {
        super("ClickIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_CLICK.equals(action)) {
                handleClick();
            }
        }
    }

    private void handleClick() {
//        int clicks = getSharedPreferences("sp", MODE_PRIVATE).getInt("clicks", 0);
//        clicks++;
//        getSharedPreferences("sp", MODE_PRIVATE)
//                .edit()
//                .putInt("clicks", clicks)
//                .commit();

        Log.i(TAG,"ClickIntentService : handleClick");
        SharedPreferenceTime session = new SharedPreferenceTime(getApplicationContext());
        session.setTime(System.currentTimeMillis());

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), BroadcastReciverNotification.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 100,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.DAY_OF_YEAR, 4);


        assert alarmManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo( cal.getTimeInMillis(), pi), pi);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ResetWidget.class));
        for (int appWidgetId : widgetIds) {
            ResetWidget.updateAppWidget(getApplicationContext(), appWidgetManager, appWidgetId);
        }
    }
}