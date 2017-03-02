package com.rolemodelmentors.android.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMNotificationHandler extends FirebaseMessagingService {
    public FCMNotificationHandler() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d("RoleModelMentors", "From: " + remoteMessage.getFrom());
        Log.d("RoleModelMentors", "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
