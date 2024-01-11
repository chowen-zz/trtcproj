package com.xlive.myapplication;

import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_LIVE;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tencent.liteav.TXLiteAVCode;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import java.util.logging.Logger;

public class AudienceActivity extends TRTCBaseActivity{

    private TXCloudVideoView mAudiencePreviewView;
    private TRTCCloud mTRTCCloud;
    private TRTCCloudDef.TRTCParams mTRTCParams;

    private String  mRemoteUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audience);
        mAudiencePreviewView = findViewById(R.id.live_cloud_view_audience);
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setListener(new TRTCCloudListener() {
            @Override
            public void onError(int errCode, String errMsg, Bundle extraInfo) {
                super.onError(errCode, errMsg, extraInfo);
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    exitRoom();
                }
            }
            @Override
            public void onUserVideoAvailable(String userId, boolean available) {
                Logger.getLogger("chowen").info("onUserVideoAvailable available=" +available);
                if (available) {
                    mRemoteUserId = userId;
                    mTRTCCloud.startRemoteView(mRemoteUserId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                            mAudiencePreviewView);
                } else {
                    mRemoteUserId = "";
                    mTRTCCloud.stopRemoteView(mRemoteUserId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                }
            }
        });
        if (checkPermission()) {
            enterRoom();
            muteAudio();
        }
    }

    protected void exitRoom() {
        if (mTRTCCloud != null) {
            mTRTCCloud.stopLocalAudio();
            mTRTCCloud.stopLocalPreview();
            mTRTCCloud.exitRoom();
            mTRTCCloud.setListener(null);
        }
        mTRTCCloud = null;
        TRTCCloud.destroySharedInstance();
    }
    private void muteAudio() {
        if (!TextUtils.isEmpty(mRemoteUserId)) {
            mTRTCCloud.muteRemoteAudio(mRemoteUserId, false);
        }
    }
    private void enterRoom() {
        mTRTCParams = new TRTCCloudDef.TRTCParams();
        mTRTCParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        mTRTCParams.userId = "zw123";
        mTRTCParams.roomId = 12315;
        mTRTCParams.userSig = GenerateTestUserSig.genTestUserSig(mTRTCParams.userId);
        mTRTCParams.role = TRTCCloudDef.TRTCRoleAudience;

        mTRTCCloud.enterRoom(mTRTCParams, TRTC_APP_SCENE_LIVE);
    }


    @Override
    protected void onPermissionGranted() {

    }
}
