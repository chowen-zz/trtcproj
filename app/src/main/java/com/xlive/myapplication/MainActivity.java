package com.xlive.myapplication;


//import com.yanzhenjie.permission.AndPermission;
//import com.yanzhenjie.permission.runtime.Permission;
import android.content.Intent;
        import android.os.Bundle;
import android.os.Handler;
import android.view.View;

        import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;

        import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends TRTCBaseActivity {

    protected static final int REQ_PERMISSION_CODE = 0x1000;
    private TXCloudVideoView mTxcvvAnchorPreviewView;
    private TRTCCloud mTRTCCloud;
    private TRTCCloudDef.TRTCParams mTRTCParams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(MainActivity.this, AnchorActivity.class);
              startActivity(intent);
            }
        });
        findViewById(R.id.bt_subscibe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AudienceActivity.class);
                startActivity(intent);
            }
        });
      upCPU();
//      new Handler().postDelayed(new Runnable() {
//          @Override
//          public void run() {
//
//          }
//      }, 10000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                }
            }
        }).start();

    }

    @Override
    protected void onPermissionGranted() {

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