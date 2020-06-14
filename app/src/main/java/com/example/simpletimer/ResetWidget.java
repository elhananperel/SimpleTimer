package com.example.simpletimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class ResetWidget extends AppWidgetProvider {
    private static final String TAG = "timer_i";
    //private AlarmManager am;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.i(TAG,"ResetAppWidget : updateAppWidget");



        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reset_widget);

        Intent intent = new Intent(context, ClickIntentService.class);
        intent.setAction(ClickIntentService.ACTION_CLICK);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        int clicks = context.getSharedPreferences("sp", MODE_PRIVATE).getInt("clicks", 0);

        //views.setTextViewText(R.id.appwidget_text, String.valueOf(clicks));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

//        Intent intent = new Intent(context, ClickIntentService.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.reset_app_widget);
//
//        remoteViews.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
//
//        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}