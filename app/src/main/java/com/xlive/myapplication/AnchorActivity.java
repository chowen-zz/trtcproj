package com.xlive.myapplication;


import static com.tencent.trtc.TRTCCloudDef.TRTCRoleAnchor;
import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_LIVE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.app.ActivityCompat;

import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class AnchorActivity extends TRTCBaseActivity {

    protected static final int REQ_PERMISSION_CODE = 0x1000;
    private TXCloudVideoView mTxcvvAnchorPreviewView;
    private TRTCCloud mTRTCCloud;
    private TRTCCloudDef.TRTCParams mTRTCParams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        mTxcvvAnchorPreviewView = findViewById(R.id.live_cloud_view_t);
        mTRTCCloud = TRTCCloud.sharedInstance(this);

        mTRTCParams = new TRTCCloudDef.TRTCParams();
//        runOnPermissionGranted(new Runnable() {
//            @Override
//            public void run() {
//                enterRoom();
//            }
//
//        });
        if (checkPermission()) {
            enterRoom();
        }
        mTRTCCloud.addListener(new TRTCCloudListener() {
            @Override
            public void onError(int errCode, String errMsg, Bundle extraInfo) {
                super.onError(errCode, errMsg, extraInfo);
                Logger.getLogger("chowen").info("onError errCode=" + errCode);
            }

            @Override
            public void onEnterRoom(long result) {
                super.onEnterRoom(result);
                Logger.getLogger("chowen").info("onEnterRoom result=" + result);
            }

            @Override
            public void onExitRoom(int reason) {
                super.onExitRoom(reason);
                Logger.getLogger("chowen").info("onExitRoom reason=" + reason);
            }

            @Override
            public void onSwitchRole(int errCode, String errMsg) {
                super.onSwitchRole(errCode, errMsg);
                Logger.getLogger("chowen").info("onSwitchRole errCode=" + errCode);
            }

            @Override
            public void onSwitchRoom(int errCode, String errMsg) {
                super.onSwitchRoom(errCode, errMsg);
                Logger.getLogger("chowen").info("onError errCode=" + errCode);
            }

            @Override
            public void onConnectOtherRoom(String userId, int errCode, String errMsg) {
                super.onConnectOtherRoom(userId, errCode, errMsg);
            }

            @Override
            public void onDisConnectOtherRoom(int errCode, String errMsg) {
                super.onDisConnectOtherRoom(errCode, errMsg);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                upCPU();
            }
        }, 1000);

    }

    @Override
    protected void onPermissionGranted() {
        enterRoom();
    }

    protected boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(AnchorActivity.this, permissions.toArray(new String[0]),
                        REQ_PERMISSION_CODE);
                return false;
            }
        }
        return true;
    }

    public void enterRoom() {
        mTRTCParams = new TRTCCloudDef.TRTCParams();
        mTRTCParams.sdkAppId = 1600000675;
        mTRTCParams.userId = "z21231";
        mTRTCParams.roomId = 12315;
        mTRTCParams.userSig = GenerateTestUserSig.genTestUserSig(mTRTCParams.userId);
        Logger.getLogger("chowen").info("userSig="+ mTRTCParams.userSig);
        mTRTCParams.role = TRTCCloudDef.TRTCRoleAnchor;

//        mTRTCCloud.switchRole(TRTCRoleAnchor);
        mTRTCCloud.startLocalPreview(true, mTxcvvAnchorPreviewView);
        mTRTCCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT);

        mTRTCCloud.enterRoom(mTRTCParams, TRTC_APP_SCENE_LIVE);
    }

    private long calculate(int n){
        if (n<=1){
            return n;
        } else {
            return calculate(n-1)+calculate(n-2);
        }
    }
    private void upCPU() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i=0; i<4; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true){
                       calculate(400);
                    }
                }
            });
        }
    }
}