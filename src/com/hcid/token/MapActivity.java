package com.hcid.token;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class MapActivity extends Activity implements OnClickListener,
GooglePlayServicesClient.ConnectionCallbacks, 
GooglePlayServicesClient.OnConnectionFailedListener, 
LocationListener {

	static final LatLng STANFORD = new LatLng(37.4300, -122.1700);
	static final LatLng EAST = new LatLng(37.424991, -122.170467);
	static final int TRANSPARENT = 0;
	static final int VISIBLE = 1;
	private GoogleMap mMap;

	private LocationClient locationclient;
	private LocationRequest locationrequest;
	private Intent mIntentService;
	private PendingIntent mPendingIntent;

	private boolean meMarkers = true;
	private boolean friendMarkers = true;
	private boolean publicMarkers = true;

	private Marker f1;
	private Marker f2;
	private Marker p1;
	private Marker p2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		Button cameraButton = (Button) findViewById(R.id.camera_button);
		cameraButton.setOnClickListener(this);
		Button profileButton = (Button) findViewById(R.id.profile_button);
		profileButton.setOnClickListener(this);
		Button searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setOnClickListener(this);
		Button meToggleButton = (Button) findViewById(R.id.me_toggle_button);
		meToggleButton.setOnClickListener(this);
		Button friendToggleButton = (Button) findViewById(R.id.friend_toggle_button);
		friendToggleButton.setOnClickListener(this);
		Button publicToggleButton = (Button) findViewById(R.id.public_toggle_button);
		publicToggleButton.setOnClickListener(this);

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);

		mIntentService = new Intent(this,LocationService.class);
		mPendingIntent = PendingIntent.getService(this, 1, mIntentService, 0);

		int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resp == ConnectionResult.SUCCESS) {
			locationclient = new LocationClient(this, this, this);
			locationclient.connect();
			Toast.makeText(this, "Connected!", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
		}

		String mCounter = getIntent().getStringExtra("key");
		Toast.makeText(this, mCounter, Toast.LENGTH_LONG).show();

		f1 = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(37.423549,-122.158327))
		.title("Stephany's Token")
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.green_map_pin_small)));

		f2 = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(37.424287,-122.160526))
		.title("Marco's Token")
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.green_map_pin_small)));

		p1 = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(37.424943,-122.169135))
		.title("Public 1 Token")
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.blue_map_pin_small)));

		p2 = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(37.424885,-122.170932))
		.title("Public 2 Token")
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.blue_map_pin_small)));
		
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				LatLng tokenLoc = marker.getPosition();
				if (marker.equals(p1) && isTokenOpenable(tokenLoc.latitude, tokenLoc.longitude)) {
					Intent showMedia = new Intent(MapActivity.this, ShowMediaActivity.class);
					showMedia.putExtra("marker_key", "public");
					startActivity(showMedia);
				}
				if (marker.equals(p2)) {
					Intent showMedia = new Intent(MapActivity.this, ShowMediaActivity.class);
					showMedia.putExtra("marker_key", "public");
					startActivity(showMedia);
				}
				if (marker.equals(f1)) {
					Intent showMedia = new Intent(MapActivity.this, ShowMediaActivity.class);
					showMedia.putExtra("marker_key", "friend1");
					startActivity(showMedia);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.camera_button) {
			Intent startTokenCamera = new Intent(this, CameraActivity.class);
			startActivity(startTokenCamera);
		}
		if (v.getId() == R.id.profile_button) {
			Intent startProfilePage = new Intent(this, ProfileActivity.class);
			startActivity(startProfilePage);
		}
		if (v.getId() == R.id.search_button) {
			showSearchDialog();
		}
		if (v.getId() == R.id.me_toggle_button) {
			Button meToggleButton = (Button) findViewById(v.getId());
			TextView meToggleButtonText = (TextView) findViewById(R.id.me_toggle_button_text);
			if (meMarkers) {
				meToggleButton.setBackgroundResource(R.drawable.lock48_grey);
				meToggleButtonText.setTextColor(getResources().getColor(R.color.TokenGray));
				meMarkers = false;
			} else {
				meToggleButton.setBackgroundResource(R.drawable.lock48_red);
				meToggleButtonText.setTextColor(getResources().getColor(R.color.TokenRed));
				meMarkers = true;
			}
		}
		if (v.getId() == R.id.friend_toggle_button) {
			Button friendToggleButton = (Button) findViewById(v.getId());
			TextView friendToggleButtonText = (TextView) findViewById(R.id.friend_toggle_button_text);
			if (friendMarkers) {
				friendToggleButton.setBackgroundResource(R.drawable.friends_grey);
				friendToggleButtonText.setTextColor(getResources().getColor(R.color.TokenGray));
				f1.setAlpha(TRANSPARENT);
				f2.setAlpha(TRANSPARENT);
				friendMarkers = false;
			} else {
				friendToggleButton.setBackgroundResource(R.drawable.multiple25_green);
				friendToggleButtonText.setTextColor(getResources().getColor(R.color.TokenGreen));
				f1.setAlpha(VISIBLE);
				f2.setAlpha(VISIBLE);
				friendMarkers = true;
			}
		}
		if (v.getId() == R.id.public_toggle_button) {
			Button publicToggleButton = (Button) findViewById(v.getId());
			TextView publicToggleButtonText = (TextView) findViewById(R.id.public_toggle_button_text);
			if (publicMarkers) {
				publicToggleButton.setBackgroundResource(R.drawable.earth_grey);
				publicToggleButtonText.setTextColor(getResources().getColor(R.color.TokenGray));
				p1.setAlpha(TRANSPARENT);
				p2.setAlpha(TRANSPARENT);
				publicMarkers = false;
			} else {
				publicToggleButton.setBackgroundResource(R.drawable.earth_blue);
				publicToggleButtonText.setTextColor(getResources().getColor(R.color.TokenBlue));
				p1.setAlpha(VISIBLE);
				p2.setAlpha(VISIBLE);
				publicMarkers = true;
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if(location != null){
			Log.i("TAG", "Location Request :" + location.getLatitude() + "," + location.getLongitude());
		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.i("TAG", "onConnectionFailed");

	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.i("TAG", "onConnected");
		String mCounter = getIntent().getStringExtra("key");
		if (mCounter.equalsIgnoreCase("value")) {
			if (locationclient != null && locationclient.isConnected()) {
				Location loc =locationclient.getLastLocation();
				LatLng currLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
				mMap.addMarker(new MarkerOptions()
				.position(currLatLng)
				.title("My Token")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.red_map_pin_small)));
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 15));
				showTokenPlacedDialog();
				//showFakeTokenDialog();
			} else {
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(STANFORD, 15));
			}
		}
		if (mCounter.equalsIgnoreCase("valu8")) {
			if (locationclient != null && locationclient.isConnected()) {
				Location loc =locationclient.getLastLocation();
				LatLng currLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 15));
			}
		}

	}

	@Override
	public void onDisconnected() {
		Log.i("TAG", "onDisconnected");

	}

	private void showSearchDialog() {
		AlertDialog dialog; 
		//following code will be in your activity.java file 

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Work in Progress");
		builder.setMessage("We are sorry to inform you that our servers haven't been "
				+ "fully optimized to store both user media and provide capabilities "
				+ "for a robust search. Please wait for subsequent updates.");
		//set action buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				//  Your code when user clicked on OK
				//  You can write the code  to save the selected item here

			}
		});

		dialog = builder.create();//AlertDialog dialog; create like this outside onClick
		dialog.show();
	}

	private void showTokenPlacedDialog() {
		AlertDialog tokenPlacedDialog; 
		//following code will be in your activity.java file 

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Token Placed!");
		//set action buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				//  Your code when user clicked on OK
				//  You can write the code  to save the selected item here

			}
		});

		tokenPlacedDialog = builder.create();//AlertDialog dialog; create like this outside onClick
		tokenPlacedDialog.show();
	}

	//	private void showFakeTokenDialog() {
	//		AlertDialog fakeTokenDialog; 
	//		//following code will be in your activity.java file 
	//
	//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	//		builder.setTitle("Reminisce!");
	//		builder.setMessage("You left a token here 9 years and 17 days ago!\n\nOpen?");
	//		//set action buttons
	//		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	//			@Override
	//			public void onClick(DialogInterface dialog, int id) {
	//				//  Your code when user clicked on OK
	//				//  You can write the code  to save the selected item here
	//
	//			}
	//		});
	//		builder.setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
	//			@Override
	//			public void onClick(DialogInterface dialog, int id) {
	//				
	//			}
	//		});
	//
	//		fakeTokenDialog = builder.create();//AlertDialog dialog; create like this outside onClick
	//		fakeTokenDialog.show();
	//		TextView messageText = (TextView)fakeTokenDialog.findViewById(android.R.id.message);
	//		messageText.setGravity(Gravity.CENTER);
	//	}
	
	public boolean isTokenOpenable(double tokenLatitude, double tokenLongitude) {
		
		Location loc;
		loc =locationclient.getLastLocation();
		
		if (Math.abs(tokenLatitude - loc.getLatitude()) < 0.000015) {
			return true;
		}
		Toast.makeText(this, "You are too far to access this token. Go Explore!", Toast.LENGTH_SHORT).show();
		return false;
		
	}
}
