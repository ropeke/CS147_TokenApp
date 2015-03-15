package com.hcid.token;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class HooverTower extends Activity implements OnClickListener {

	String filename = "/storage/emulated/0/Pictures/Token/IMG_20150308_160249.jpg";
	private boolean meSelector = true;
	private boolean friendSelector = false;
	private boolean publicSelector = false;
	private Bitmap myBitmap = null;
	
	private Uri fileUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hoover_tower);
		
		ImageView placeholderImage = (ImageView) findViewById(R.id.fullscreen_hoover_tower_image);
		Bitmap myBitmap = BitmapFactory.decodeFile(filename);
		placeholderImage.setImageBitmap(myBitmap);
		
    	ImageView rejectIcon = (ImageView) findViewById(R.id.exit_button);
    	ImageView acceptIcon = (ImageView) findViewById(R.id.post_button);
    	acceptIcon.setOnClickListener(this);
    	
    	Button shareWithFriendsIcon = (Button) findViewById(R.id.friend_toggle_button);
    	shareWithFriendsIcon.setOnClickListener(this);
    	
    	Button shareWithMeIcon = (Button) findViewById(R.id.me_toggle_button);
    	shareWithMeIcon.setOnClickListener(this);
    	
    	Button shareWithPublicIcon = (Button) findViewById(R.id.public_toggle_button);
    	shareWithPublicIcon.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hoover_tower, menu);
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
	
	private void showDialog() {
		AlertDialog dialog; 
		//following code will be in your activity.java file 
		final CharSequence[] items = {" --- All --- "," Janet "," Stephany "," Rotimi "};
		                
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
		                    	 Button friendsButton = (Button) findViewById(R.id.friend_toggle_button);
		                    	 friendsButton.setBackgroundResource(R.drawable.multiple25_green);
		                    	 friendSelector = true;
		                     } else {
		                    	 friendSelector = false;
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

	@Override
	public void onClick(View v) {
		if (v == findViewById(R.id.friend_toggle_button)) {
//			closeKeyboard();
        	showDialog();
		}
		if (v.getId() == R.id.me_toggle_button) {
			Button meSelectorButton = (Button) findViewById(v.getId());
//			closeKeyboard();
			if (!meSelector) {
				meSelectorButton.setBackgroundResource(R.drawable.lock48_red);
			} else {
				meSelectorButton.setBackgroundResource(R.drawable.lock48_grey);
			}
			meSelector = !meSelector;
		}
		if (v.getId() == R.id.public_toggle_button) {
			Button publicSelectorButton = (Button) findViewById(v.getId());
//			closeKeyboard();
			if (!publicSelector) {
				publicSelectorButton.setBackgroundResource(R.drawable.earth_blue);
			} else {
				publicSelectorButton.setBackgroundResource(R.drawable.earth_grey);
			}
			publicSelector = !publicSelector;
		}
		
		if (v.getId() == R.id.post_button) {
			Intent startMapActivity = new Intent(this, MapActivity.class);
			String determinedValue = determineValue();
			startMapActivity.putExtra("key", determinedValue);
			startActivity(startMapActivity);
		}
	}
	
	private String determineValue() {
		if (publicSelector) return "value1";
		if (friendSelector) return "value2";
		return "value";
	}
}
