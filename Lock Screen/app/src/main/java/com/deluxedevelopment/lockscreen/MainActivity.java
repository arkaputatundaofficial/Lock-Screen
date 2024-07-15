package com.deluxedevelopment.lockscreen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	Button btnEnable, btnExit;
	static final int RESULT_ENABLE = 1;
	DevicePolicyManager devicePolicyManager;
	ComponentName componentName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		btnEnable = findViewById(R.id.btnEnable);
		btnExit = findViewById(R.id.btnExit);

		devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		componentName = new ComponentName(MainActivity.this, Controller.class);

		boolean active = devicePolicyManager.isAdminActive(componentName);
		if (active) {
			btnEnable.setText("DISABLE");
			devicePolicyManager.lockNow();
			finish();
		} else {
			btnEnable.setText("ENABLE");
		}

		btnEnable.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					boolean active = devicePolicyManager.isAdminActive(componentName);
					if (active) {
						devicePolicyManager.removeActiveAdmin(componentName);
						btnEnable.setText("ENABLE");
					} else {
						Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
						intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
						intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You need to add this app as Device Admin to toggle the screen lock.");
						startActivityForResult(intent, RESULT_ENABLE);
					}
				}
			});

		btnExit.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					finish();
				}
			});
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case RESULT_ENABLE:
				if (resultCode == Activity.RESULT_OK) {
					btnEnable.setText("DISABLE");
				} else {
					Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
				}
				return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
