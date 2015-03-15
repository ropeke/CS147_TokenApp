package com.hcid.token;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.facebook.Session;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.parse.LogInCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.ParseException;

import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("public_profile"));
		TextView tutorialText = (TextView) findViewById(R.id.tutorial_text);
		tutorialText.setOnClickListener(this);
		printKeyHash(this);
		//for debugging use!
		checkGooglePlayServices(this);	
		if (isLoggedIn()) {
			Intent startMapActivity = new Intent(this, MapActivity.class);
			Toast.makeText(this, "Please Wait...", Toast.LENGTH_LONG).show();
			startMapActivity.putExtra("key", "valu8");
			startActivity(startMapActivity);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v == findViewById(R.id.authButton)) {
			ParseFacebookUtils.logIn(this, new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException err) {
					if (user == null) {
						Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
					} else if (user.isNew()) {
						Log.d("MyApp", "User signed up and logged in through Facebook!");
					} else {
						Log.d("TAG", "User logged in through Facebook!");
					}
				}
			});

		}
		if (v == findViewById(R.id.tutorial_text)) {
			Intent startTutorialActivity = new Intent(this, TutorialActivity.class);
			startActivity(startTutorialActivity);
		}

	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	public static boolean checkGooglePlayServices(Activity activity) {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity, 9000).show();
			}
			return false;
		}
		return true;
	}

	public static String printKeyHash(Activity context) {
		PackageInfo packageInfo;
		String key = null;
		try {
			//getting application package name, as defined in manifest
			String packageName = context.getApplicationContext().getPackageName();

			//Retriving package info
			packageInfo = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);

			Log.e("Package Name=", context.getApplicationContext().getPackageName());

			for (Signature signature : packageInfo.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				key = new String(Base64.encode(md.digest(), 0));

				// String key = new String(Base64.encodeBytes(md.digest()));
				Log.e("Key Hash=", key);
			}
		} catch (NameNotFoundException e1) {
			Log.e("Name not found", e1.toString());
		}
		catch (NoSuchAlgorithmException e) {
			Log.e("No such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}

		return key;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
		Intent startMapActivity = new Intent(this, MapActivity.class);
		Toast.makeText(this, "Please Wait...", Toast.LENGTH_LONG).show();
		startMapActivity.putExtra("key", "valu8");
		startActivity(startMapActivity);
	}
	
	public boolean isLoggedIn() {
	    Session session = Session.getActiveSession();
	    return (session != null && session.isOpened());
	}
}
