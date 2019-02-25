package com.example.sif.truecallersdkdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueException;
import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TrueSDK;
import com.truecaller.android.sdk.TrueSdkScope;
import com.truecaller.android.sdk.clients.OtpCallback;

public class MainActivity extends AppCompatActivity {

    private TextView tName;
    private TextView tPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tName = findViewById(R.id.tv_name);
        tPhoneNo = findViewById(R.id.tv_phone_no);

        TrueSdkScope trueScope = new TrueSdkScope.Builder(this, sdkCallback)
                .consentMode(TrueSdkScope.CONSENT_MODE_POPUP)
                .consentTitleOption(TrueSdkScope.SDK_CONSENT_TITLE_VERIFY)
                .footerType(TrueSdkScope.FOOTER_TYPE_SKIP)
                .build();

        TrueSDK.init(trueScope);

        if (checkTrueCallerInstalledOrNot()) {
            TrueSDK.getInstance().getUserProfile(this);
            Log.d("TrueCaller", "TrueCaller Installed");
        } else {
            Log.d("TrueCaller", "TrueCaller UnInstalled");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TrueSDK.getInstance().onActivityResultObtained(this, resultCode, data);
    }

    private boolean checkTrueCallerInstalledOrNot() {
        if (TrueSDK.getInstance().isUsable()) {
            return true;
        }
        return false;
    }

    private final ITrueCallback sdkCallback = new ITrueCallback() {
        @Override
        public void onSuccessProfileShared(@NonNull TrueProfile trueProfile) {
            // This method is invoked when the truecaller app is installed on the device and the user gives his
            // consent to share his truecaller profile

            Log.d("TrueCaller", "Verified Successfully : " + trueProfile.firstName);

            tName.setText(trueProfile.firstName + " " + trueProfile.lastName);
            tPhoneNo.setText(trueProfile.phoneNumber);
        }

        @Override
        public void onFailureProfileShared(@NonNull TrueError trueError) {
            // This method is invoked when some error occurs or if an invalid request for verification is made
            Log.d("TrueCaller", "onFailureProfileShared: " + trueError.getErrorType());
            tName.setText("Error : " + trueError.getErrorType());
        }

        @Override
        public void onOtpRequired() {

        }
    };
}
