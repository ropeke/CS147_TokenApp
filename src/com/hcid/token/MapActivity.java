package com.hcid.token;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
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
import com.parse.ParseFacebookUtils;

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
	private Marker f3;
	private Marker p1;
	private Marker p2;
	private Marker m1;
	private Marker m2;

	private Notification n;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		setOnClickListeners();

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		mMap.getUiSettings().setMyLocationButtonEnabled(false);
		mMap.getUiSettings().setZoomControlsEnabled(false);

		// Setting a custom info window adapter for the google map
		mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

			// Use default InfoWindow frame
			@Override
			public View getInfoWindow(Marker arg0) {
				return null;
			}

			// Defines the contents of the InfoWindow
			@Override
			public View getInfoContents(Marker arg0) {

				View v;
				// Getting view from the layout file info_window_layout
				if (!arg0.getTitle().equalsIgnoreCase("Rotimi's Token")) {
					v = getLayoutInflater().inflate(R.layout.windowlayout, null);
				} else {
					v = getLayoutInflater().inflate(R.layout.windowlayout_friend, null);
				}

				// Getting reference to the TextView to set latitude
				TextView title = (TextView) v.findViewById(R.id.title);
				if (arg0.getTitle() == "Rotimi's Token") {
					Log.d("TAG", "Working?");
					title.setTextColor(getResources().getColor(R.color.NewTokenGreen));
				}
				title.setText(arg0.getTitle());

				TextView date = (TextView) v.findViewById(R.id.date);
				date.setText("10/12/14");
				//
				TextView likeCount = (TextView) v.findViewById(R.id.like_count);
				if (arg0.getTitle() != "Rotimi's Token") {
					Log.d("TAG", "Working?");
					likeCount.setText("+100");
				}

				Bitmap myBitmap = BitmapFactory.decodeFile(arg0.getSnippet());
				ImageView imagePreview = (ImageView) v.findViewById(R.id.image_preview);

				try {
					ExifInterface exif = new ExifInterface(arg0.getSnippet());
					int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
					Log.d("EXIF", "Exif: " + orientation);
					Matrix matrix = new Matrix();
					if (orientation == 6) {
						matrix.postRotate(90);
					}
					else if (orientation == 3) {
						matrix.postRotate(180);
					}
					else if (orientation == 8) {
						matrix.postRotate(270);
					}
					myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
				}
				catch (Exception e) {

				}

				imagePreview.setImageBitmap(myBitmap);

				// Returning the view containing InfoWindow contents
				return v;

			}
		});

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

		Toast.makeText(this, "Location Restrictions removed for demo purposes", Toast.LENGTH_LONG).show();
		
		
//		String mCounter = getIntent().getStringExtra("key");
//		Toast.makeText(this, mCounter, Toast.LENGTH_LONG).show();

		f1 = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(37.424989,-122.170510))
		.title("Rotimi's Token")
		.snippet("/storage/emulated/0/Pictures/Token/janet_fry.jpg")
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.green_map_pin_small)));

//		f2 = mMap.addMarker(new MarkerOptions()
//		.position(new LatLng(37.427344,-122.169743))
//		.title("Marco's Token")
//		.icon(BitmapDescriptorFactory
//				.fromResource(R.drawable.green_map_pin_small))); //TODO: Need to take care of his image preview

		p1 = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(37.425341,-122.167586))
		.title("Lindsey's Token")
		.snippet("/storage/emulated/0/Pictures/Token/Meyer_Library.jpg")
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.blue_map_pin_small)));

//		p2 = mMap.addMarker(new MarkerOptions()
//		.position(new LatLng(37.427689, -122.169697))
//		.title("Public 2 Token")
//		.snippet("/storage/emulated/0/DCIM/Camera/claw.png")
//		.icon(BitmapDescriptorFactory
//				.fromResource(R.drawable.blue_map_pin_small)));
//		
//		m1 = mMap.addMarker(new MarkerOptions()
//		.position(new LatLng(37.427680, -122.166912))
//		.title("Me Token")
//		.snippet("/storage/emulated/0/DCIM/Camera/claw.png")
//		.icon(BitmapDescriptorFactory
//				.fromResource(R.drawable.red_map_pin_small)));
//		
//		m2 = mMap.addMarker(new MarkerOptions()
//		.position(new LatLng(37.424984, -122.169417))
//		.title("Me Token")
//		.snippet("/storage/emulated/0/DCIM/Camera/claw.png")
//		.icon(BitmapDescriptorFactory
//				.fromResource(R.drawable.red_map_pin_small)));
//		
//		f3 = mMap.addMarker(new MarkerOptions()
//		.position(new LatLng(37.426756, -122.168698))
//		.title("Me Token")
//		.snippet("/storage/emulated/0/DCIM/Camera/claw.png")
//		.icon(BitmapDescriptorFactory
//				.fromResource(R.drawable.green_map_pin_small))); 

		loadDBTokens();
		ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
//		List<String> permissions = Arrays.asList("public_profile", "user_about_me", "user_relationships", "user_birthday", "user_location", "email");

		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				LatLng tokenLoc = marker.getPosition();
				if (marker.equals(p1) && isTokenOpenable(tokenLoc.latitude, tokenLoc.longitude)) {
					Intent showMedia = new Intent(MapActivity.this, ShowMediaActivity.class);
//					NotificationManager notificationManager = 
//							(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//					notificationManager.notify(0, n); 
					showMedia.putExtra("marker_key", "public");
					startActivity(showMedia);
				}
				if (marker.equals(p2) && isTokenOpenable(tokenLoc.latitude, tokenLoc.longitude)) {
					Intent showMedia = new Intent(MapActivity.this, ShowMediaActivity.class);
					showMedia.putExtra("marker_key", "public");
					startActivity(showMedia);
				}
				if (marker.equals(f1) && isTokenOpenable(tokenLoc.latitude, tokenLoc.longitude)) {
					Intent showFriendMedia = new Intent(MapActivity.this, ShowFriendMediaActivity.class);
					showFriendMedia.putExtra("marker_key", "friend1");
					startActivity(showFriendMedia);
				}
				if (marker.equals(f2) && isTokenOpenable(tokenLoc.latitude, tokenLoc.longitude)) {
					Intent showFriendMedia = new Intent(MapActivity.this, ShowFriendMediaActivity.class);
					showFriendMedia.putExtra("marker_key", "friend1");
					startActivity(showFriendMedia);
				}
			}
		});

		// prepare intent which is triggered if the
		// notification is selected
		
		Toast.makeText(this, "Location Restrictions removed for demo purposes", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, MapActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// build notification
		// the addAction re-use the same intent to keep the example short
		n  = new Notification.Builder(this)
		.setContentTitle("Rotimi's Token Nearby!")
		.setContentText("Check it out!")
		.setSmallIcon(R.drawable.green_map_pin_small)
		.setTicker("Friend Token Nearby!")
		.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
		.setAutoCancel(true).build();
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
			startTokenCamera.putExtra("location", locationclient.getLastLocation());
			startActivity(startTokenCamera);
		}
		if (v.getId() == R.id.profile_button) {
			Intent startProfilePage = new Intent(this, ProfileActivity.class);
			startActivity(startProfilePage);
		}
		if (v.getId() == R.id.search_button) {
			
			
		}
		if (v.getId() == R.id.me_toggle_button) {
			Button meToggleButton = (Button) findViewById(v.getId());
//			TextView meToggleButtonText = (TextView) findViewById(R.id.me_toggle_button_text);
			if (meMarkers) {
				meToggleButton.setBackgroundResource(R.drawable.lock48_grey);
//				meToggleButtonText.setTextColor(getResources().getColor(R.color.TokenGray));
				meMarkers = false;
			} else {
				meToggleButton.setBackgroundResource(R.drawable.lock48_red);
//				meToggleButtonText.setTextColor(getResources().getColor(R.color.TokenRed));
				meMarkers = true;
			}
		}
		if (v.getId() == R.id.friend_toggle_button) {
			Button friendToggleButton = (Button) findViewById(v.getId());
//			TextView friendToggleButtonText = (TextView) findViewById(R.id.friend_toggle_button_text);
			if (friendMarkers) {
				friendToggleButton.setBackgroundResource(R.drawable.friends_grey);
//				friendToggleButtonText.setTextColor(getResources().getColor(R.color.TokenGray));
				f1.setAlpha(TRANSPARENT);
//				f2.setAlpha(TRANSPARENT);
				friendMarkers = false;
			} else {
				friendToggleButton.setBackgroundResource(R.drawable.multiple25_green);
//				friendToggleButtonText.setTextColor(getResources().getColor(R.color.TokenGreen));
				f1.setAlpha(VISIBLE);
//				f2.setAlpha(VISIBLE);
				friendMarkers = true;
			}
		}
		if (v.getId() == R.id.public_toggle_button) {
			Button publicToggleButton = (Button) findViewById(v.getId());
//			TextView publicToggleButtonText = (TextView) findViewById(R.id.public_toggle_button_text);
			if (publicMarkers) {
				publicToggleButton.setBackgroundResource(R.drawable.earth_grey);
//				publicToggleButtonText.setTextColor(getResources().getColor(R.color.TokenGray));
				p1.setAlpha(TRANSPARENT);
//				p2.setAlpha(TRANSPARENT);
				publicMarkers = false;
			} else {
				publicToggleButton.setBackgroundResource(R.drawable.earth_blue);
//				publicToggleButtonText.setTextColor(getResources().getColor(R.color.TokenBlue));
				p1.setAlpha(VISIBLE);
//				p2.setAlpha(VISIBLE);
				publicMarkers = true;
			}
		}
		if (v.getId() == R.id.notification_button) {
			showNotificationsNotImplementedDialog();
//			Intent startHooverActivity = new Intent(this, HooverTower.class);
//			startActivity(startHooverActivity);
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
		if (mCounter.equalsIgnoreCase("value1")) {
			if (locationclient != null && locationclient.isConnected()) {
				Location loc = locationclient.getLastLocation();
				LatLng currLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
				mMap.addMarker(new MarkerOptions()
				.position(currLatLng)
				.title("My Token")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.blue_map_pin_small)));
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 15));
				showTokenPlacedDialog();
				//showFakeTokenDialog();
			} else {
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(STANFORD, 15));
			}
		}
		if (mCounter.equalsIgnoreCase("value2")) {
			if (locationclient != null && locationclient.isConnected()) {
				Location loc = locationclient.getLastLocation();
				LatLng currLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
				mMap.addMarker(new MarkerOptions()
				.position(currLatLng)
				.title("My Token")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.green_map_pin_small)));
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 15));
				showTokenPlacedDialog();
				//showFakeTokenDialog();
			} else {
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(STANFORD, 15));
			}
		}
		if (mCounter.equalsIgnoreCase("value")) {
			if (locationclient != null && locationclient.isConnected()) {
				Location loc = locationclient.getLastLocation();
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
				Location loc = locationclient.getLastLocation();
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

		private void showNotificationsNotImplementedDialog() {
			AlertDialog fakeTokenDialog; 
			//following code will be in your activity.java file 
	
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Oops!");
			builder.setMessage("The notification popout has not been fully implemented yet. Sorry!");
			//set action buttons
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					//  Your code when user clicked on OK
					//  You can write the code  to save the selected item here
	
				}
			});
	
			fakeTokenDialog = builder.create();//AlertDialog dialog; create like this outside onClick
			fakeTokenDialog.show();
			TextView messageText = (TextView)fakeTokenDialog.findViewById(android.R.id.message);
			messageText.setGravity(Gravity.CENTER);
		}

	public boolean isTokenOpenable(double tokenLatitude, double tokenLongitude) {

//		Location loc;
//		loc = locationclient.getLastLocation();
//		//TODO: Don't be stupid! Do Longitude.
//		if (Math.abs(tokenLatitude - loc.getLatitude()) < 0.0004 && 
//				Math.abs(tokenLongitude - loc.getLongitude()) < 0.0004) {
//			return true;
//		}
//		Toast.makeText(this, "You are too far to access this token. Go Explore!", Toast.LENGTH_SHORT).show();
//		return false;
		return true;

	}

	public void setOnClickListeners() {
		ImageView cameraButton = (ImageView) findViewById(R.id.camera_button);
		cameraButton.setOnClickListener(this);
		ImageView profileButton = (ImageView) findViewById(R.id.profile_button);
		profileButton.setOnClickListener(this);
//		Button searchButton = (Button) findViewById(R.id.search_button);
//		searchButton.setOnClickListener(this);
		Button meToggleButton = (Button) findViewById(R.id.me_toggle_button);
		meToggleButton.setOnClickListener(this);
		Button friendToggleButton = (Button) findViewById(R.id.friend_toggle_button);
		friendToggleButton.setOnClickListener(this);
		Button publicToggleButton = (Button) findViewById(R.id.public_toggle_button);
		publicToggleButton.setOnClickListener(this);
		ImageView notificationButton = (ImageView) findViewById(R.id.notification_button);
		notificationButton.setOnClickListener(this);
	}

	public void loadDBTokens() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

}
