package com.cuankeeper.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cuankeeper.app.auth.ActivityLogin;
import com.cuankeeper.app.auth.ActivityRegister;
import com.cuankeeper.app.databinding.ActivityLandingBinding;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;
import com.huawei.hms.analytics.type.HAEventType;
import com.huawei.hms.analytics.type.HAParamType;
import com.huawei.hms.analytics.type.ReportPolicy;

import java.util.HashSet;
import java.util.Set;

public class ActivityLanding extends AppCompatActivity {
    private ActivityLandingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        binding = ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { intendedAction("login"); }
        });
//        binding.btnRegister.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) { intendedAction("register"); }
//        });

        analytics();
    }

    protected void analytics(){
        // Enable SDK log recording.
        HiAnalyticsTools.enableLog();
        HiAnalyticsInstance instance = HiAnalytics.getInstance(this);
        instance.setUserProfile("userKey","value");

        ReportPolicy launchAppPolicy = ReportPolicy.ON_APP_LAUNCH_POLICY;
        ReportPolicy moveBackgroundPolicy = ReportPolicy.ON_MOVE_BACKGROUND_POLICY;
        ReportPolicy scheduledTimePolicy = ReportPolicy.ON_SCHEDULED_TIME_POLICY;
        scheduledTimePolicy.setThreshold(600);
        ReportPolicy cacheThresholdPolicy = ReportPolicy.ON_CACHE_THRESHOLD_POLICY;
        cacheThresholdPolicy.setThreshold(100);
        Set<ReportPolicy> reportPolicies = new HashSet<>();

        // Add Escalation Policy
        reportPolicies.add(launchAppPolicy);
        reportPolicies.add(moveBackgroundPolicy);
        reportPolicies.add(scheduledTimePolicy);
        reportPolicies.add(cacheThresholdPolicy);

        // Reporting policy settings take effect.
        instance.setReportPolicies(reportPolicies);

        Bundle bundle_pre = new Bundle();
        bundle_pre.putString(HAParamType.PRODUCTID, "item_ID");
        bundle_pre.putString(HAParamType.PRODUCTNAME, "name");
        bundle_pre.putString(HAParamType.CATEGORY, "category");
        bundle_pre.putLong(HAParamType.QUANTITY, 100L);
        bundle_pre.putDouble(HAParamType.PRICE, 10.01);
        bundle_pre.putDouble(HAParamType.REVENUE, 10);
        bundle_pre.putString(HAParamType.CURRNAME, "currency");
        bundle_pre.putString(HAParamType.PLACEID, "location_ID");
        instance.onEvent(HAEventType.ADDPRODUCT2WISHLIST, bundle_pre);
    }

    protected void intendedAction(String page){
        Intent intent = null;
        Bundle bundle = new Bundle();
        switch (page){
            case "login":
                bundle.putString("account", "Move to Login Page");
                intent = new Intent(this, ActivityLogin.class);
                startActivity(intent);
                break;
            case "register":
                intent = new Intent(this, ActivityRegister.class);
                startActivity(intent);
                break;
            default:
                intent = new Intent(this, ActivityMain.class);
                startActivity(intent);
                return;
        }
    }
}