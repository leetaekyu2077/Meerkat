package com.example.meerkat;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyFirebaseInstanceIDService extends android.app.Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
