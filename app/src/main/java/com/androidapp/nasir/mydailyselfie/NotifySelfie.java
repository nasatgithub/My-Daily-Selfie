package com.androidapp.nasir.mydailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by NasirAhmed on 19-Jul-15.
 */
public class NotifySelfie extends BroadcastReceiver {
    public static String TAG="NotifySelfie";
    private String notification_msg="Time for another Selfie!";
    private String tickerText="Selfie Remider";
    private int MY_NOTIFICATION_ID=112244;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"Received Alarm Broadcast ");
        Intent restartHomeActivityIntent=new Intent(context,HomeActivity.class);
        restartHomeActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi=PendingIntent.getActivity(context,0,restartHomeActivityIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        // Uses R.layout.custom_notification for the
        // layout of the notification View. The xml
        // file is in res/layout/custom_notification.xml

        RemoteViews mContentView = new RemoteViews(
                context.getPackageName(),
                R.layout.custom_notification);
        View custom_notification=View.inflate(context,R.layout.custom_notification,null);
        // TODO: Set the notification View's text to
        // reflect whether the download completed
        // successfully


        TextView t=(TextView) custom_notification.findViewById(R.id.text);
        Log.i(TAG,"t = "+t);

        mContentView.setTextViewText(R.id.text, notification_msg);

        // TODO: Use the Notification.Builder class to
        // create the Notification. You will have to set
        // several pieces of information. You can use
        // android.R.drawable.stat_sys_warning
        // for the small icon. You should also
        // setAutoCancel(true).

        Notification.Builder notificationBuilder = new Notification.Builder(
                context)
                .setTicker(tickerText)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setContent(mContentView);



        // TODO: Send the notification
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());
    }
}
