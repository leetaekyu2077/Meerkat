package com.example.meerkat;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import android.Manifest;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;

import androidx.core.app.ActivityCompat;

import android.widget.Toast;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScanner mBluetoothLeScanner;
    BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private static final int PERMISSIONS = 100;
    ListView beaconListView;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-mm-dd HH:mm:ss", Locale.KOREAN);

    static final String TAG = "MainActivity";
    Task<String> token = null;

    // Firebase code
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("token");
    //final TextView textView1 = (TextView) findViewById(R.id.helloWorld);
    //Button btn = (Button) findViewById(R.id.clickButton) ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent != null){
            String notificationData = intent.getStringExtra("test");
            if (notificationData != null){
                Log.d("FCM_TEST", notificationData);
            }
        }
        // Beacon
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS);
        beaconListView = (ListView) findViewById(R.id.beaconListView);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        //beacon = new Vector<>();
        mBluetoothLeScanner.startScan(mScanCallback);

        // Firebase code
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("token");
        // Read from the database
        String TAG = "~warning~";
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
        // now: click button -> write token in database
        // must modify: detect beacon signal -> write token in database
    }

    final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            try {
                ScanRecord scanRecord = result.getScanRecord();
                String id = result.getDevice().getAddress();
                Task<DataSnapshot> prior_token = myRef.get();
                //String name = result.getDevice().getName();
                if (id.equals("B8:27:EB:FB:AE:A8")) {
                    String time = simpleDateFormat.format(new Date());
                    Log.i(TAG, "Detected");
                    Log.i(TAG, time);
                    Log.i(TAG, id);
                    //Task<String> token = FirebaseMessaging.getInstance().getToken();

                    if (token == null) {
                        //token = FirebaseMessaging.getInstance().getToken();
                        token = FirebaseMessaging.getInstance().getToken();
                        Thread.sleep(100);
                        myRef.setValue(token.getResult());
                        //Log.i(TAG, String.valueOf(token));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //인식 주기
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d("onBatchScanResults", results.size() + "");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d("onScanFailed()", errorCode + "");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothLeScanner.stopScan(mScanCallback);
    }

/*
    // 현재 등록 토큰 가져오기 : 가져오려면 FirebaseMessaging.getInstance().getToken()호출
    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
        @Override
        public void onComplete(@NonNull Task<String> task) {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }

            // Get new FCM registration token
            String token = task.getResult();

            // Log and toast
            String msg = getString(R.string.msg_token_fmt, token);
            Log.d(TAG, msg);
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    });
*/
}