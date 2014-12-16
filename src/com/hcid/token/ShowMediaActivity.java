package com.hcid.token;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ShowMediaActivity extends Activity implements OnClickListener {

	private static final String public_string = "/storage/emulated/0/Pictures/Token/IMG_9034.jpg";
	private static final String friend1_string = "/storage/emulated/0/Pictures/Token/IMG_9038.jpg";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_media);
		
    	ImageView placeholderImage = (ImageView) findViewById(R.id.media_image_placeholder);
    	Button thumbsUp = (Button) findViewById(R.id.thumbs_up);
    	thumbsUp.setOnClickListener(this);
    	Button thumbsDown = (Button) findViewById(R.id.thumbs_down);
    	thumbsDown.setOnClickListener(this);
    	
    	String filename;
    	if (getIntent().getStringExtra("marker_key").equalsIgnoreCase("public")) {
    		filename = public_string;
    	} else if (getIntent().getStringExtra("marker_key").equalsIgnoreCase("friend1")) {
    		filename = friend1_string;
    	} else {
    		filename = "lol";
    	}
    	
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_media, menu);
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
		if (v.getId() == R.id.thumbs_down) {
			Button thumbsDown = (Button) findViewById(v.getId());
			thumbsDown.setBackgroundResource(R.drawable.thumb52_dislike);
			Button thumbsUp = (Button) findViewById(R.id.thumbs_up);
			thumbsUp.setBackgroundResource(R.drawable.thumb52_like_grey);
		}
		if (v.getId() == R.id.thumbs_up) {
			Button thumbsDown = (Button) findViewById(R.id.thumbs_down);
			thumbsDown.setBackgroundResource(R.drawable.thumb52_dislike_grey);
			Button thumbsUp = (Button) findViewById(v.getId());
			thumbsUp.setBackgroundResource(R.drawable.thumb52_like);
		}
		
	}
	
}
