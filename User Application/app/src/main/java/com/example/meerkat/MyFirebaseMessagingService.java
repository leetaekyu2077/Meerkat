package com.example.meerkat;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "FCM token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
    }

    // START receive_message
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("body");
        String test = remoteMessage.getData().get("test");

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("test", test);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent
        .FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channel = "채널";
            String channel_nm = "채널명";

            NotificationManager notichannel = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
            channelMessage.setDescription("채널에 대한 설명.");
            channelMessage.enableLights(true);
            channelMessage.enableVibration(true);
            channelMessage.setShowBadge(false);
            channelMessage.setVibrationPattern(new long[]{1000, 1000});
            notichannel.createNotificationChannel(channelMessage);

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channel)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(title)//푸시알림의 제목
                            .setContentText(message)//푸시알림의 내용
                            .setChannelId(channel)
                            .setAutoCancel(true)//선택시 자동으로 삭제되도록 설정.
                            .setContentIntent(pendingIntent)//알림을 눌렀을때 실행할 인텐트 설정.
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(9999, notificationBuilder.build());
        }
        else{
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, "")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults((Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE));

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(9999, notificationBuilder.build());
        }
    }


    // 토큰 생성 모니터링 : 새 토큰이 생성될때마다 onNewToken 콜백이 호출됨
}
