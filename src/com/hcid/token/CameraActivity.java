package com.hcid.token;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraActivity extends Activity implements OnClickListener {

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	
	private boolean meSelector = true;
	private boolean publicSelector = false;
	
	private Uri fileUri;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_camera);

	    // create Intent to take a picture and return control to the calling application
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

	    // start the image capture Intent
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "Token");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("Token", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image saved to a certain location
	            //Toast.makeText(this, "Image saved to:\n" +
	                     //	data.getData(), Toast.LENGTH_LONG).show();
	        	Log.d("TAG", "SHIT WORKS NIGGUH");
	        	ImageView placeholderImage = (ImageView) findViewById(R.id.image_placeholder);
	        	String filename = fileUri.toString();
	        	filename = filename.substring(7);
	        	
	        	Bitmap myBitmap = BitmapFactory.decodeFile(filename);
	        	Log.d("TAG", filename);

	            try {
	                ExifInterface exif = new ExifInterface(filename);
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
	        	
	            placeholderImage.setImageBitmap(myBitmap);
	        	ImageView rejectIcon = (ImageView) findViewById(R.id.retake_photo_icon);
	        	rejectIcon.setImageResource(R.drawable.close47);
	        	ImageView acceptIcon = (ImageView) findViewById(R.id.accept_photo_icon);
	        	
	        	acceptIcon.setImageResource(R.drawable.keyboard53);
	        	acceptIcon.setOnClickListener(this);
	        	
	        	Button shareWithFriendsIcon = (Button) findViewById(R.id.friends_selector_button);
	        	shareWithFriendsIcon.setOnClickListener(this);
	        	
	        	Button shareWithMeIcon = (Button) findViewById(R.id.me_selector_button);
	        	shareWithMeIcon.setOnClickListener(this);
	        	
	        	Button shareWithPublicIcon = (Button) findViewById(R.id.public_share_button);
	        	shareWithPublicIcon.setOnClickListener(this);
	        } else if (resultCode == RESULT_CANCELED) {
	            Toast.makeText(this, "User cancelled the image capture", Toast.LENGTH_LONG).show();
	            // User cancelled the image capture
	        } else {
	            Toast.makeText(this, "Your image capture failed bruh", Toast.LENGTH_LONG).show();
	        }
	    }

	    if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Video captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Video saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the video capture
	        } else {
	            // Video capture failed, advise user
	        }
	    }
	}
	
	@Override
	public void onClick(View v) {
		if (v == findViewById(R.id.accept_photo_icon)) {
			Intent startFreshMap = new Intent(this, MapActivity.class);
			startFreshMap.putExtra("key", "value");
			startActivity(startFreshMap);
		}
		if (v == findViewById(R.id.friends_selector_button)) {
        	showDialog();
		}
		if (v.getId() == R.id.me_selector_button) {
			Button meSelectorButton = (Button) findViewById(v.getId());
			if (!meSelector) {
				meSelectorButton.setBackgroundResource(R.drawable.lock48_red);
			} else {
				meSelectorButton.setBackgroundResource(R.drawable.lock48_grey);
			}
			meSelector = !meSelector;
		}
		if (v.getId() == R.id.public_share_button) {
			Button publicSelectorButton = (Button) findViewById(v.getId());
			if (!publicSelector) {
				publicSelectorButton.setBackgroundResource(R.drawable.earth_blue);
			} else {
				publicSelectorButton.setBackgroundResource(R.drawable.earth_grey);
			}
			publicSelector = !publicSelector;
		}

	}
	
	private void showDialog() {
	AlertDialog dialog; 
	//following code will be in your activity.java file 
	final CharSequence[] items = {" --- All --- "," Janet "," Stephany "," Bruno "};
	                // arraylist to keep the selected items
	                final ArrayList<Integer> selectedItems=new ArrayList<Integer>();
	               
	                AlertDialog.Builder builder = new AlertDialog.Builder(this);
	                builder.setTitle("Select Friends!");
	                builder.setMultiChoiceItems(items, null,
	                        new DialogInterface.OnMultiChoiceClickListener() {
	                 // indexSelected contains the index of item (of which checkbox checked)
	                 @Override
	                 public void onClick(DialogInterface dialog, int indexSelected,
	                         boolean isChecked) {
	                     if (isChecked) {
	                         // If the user checked the item, add it to the selected items
	                         // write your code when user checked the checkbox 
	                         selectedItems.add(indexSelected);
	                     } else if (selectedItems.contains(indexSelected)) {
	                         // Else, if the item is already in the array, remove it 
	                         // write your code when user Uchecked the checkbox 
	                         selectedItems.remove(Integer.valueOf(indexSelected));
	                     }
	                 }
	             })
	              // Set the action buttons
	             .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                 @Override
	                 public void onClick(DialogInterface dialog, int id) {
	                     if (!selectedItems.isEmpty()) {
	                    	 Button friendsButton = (Button) findViewById(R.id.friends_selector_button);
	                    	 friendsButton.setBackgroundResource(R.drawable.multiple25_green);
	                     }
	                	 //  Your code when user clicked on OK
	                     //  You can write the code  to save the selected item here
	                    
	                 }
	             })
	             .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                 @Override
	                 public void onClick(DialogInterface dialog, int id) {
	                    //  Your code when user clicked on Cancel
	                   
	                 }
	             });
	       
	                dialog = builder.create();//AlertDialog dialog; create like this outside onClick
	                dialog.show();
	        }
	
}
