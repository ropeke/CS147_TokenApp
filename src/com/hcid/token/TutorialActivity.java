package com.hcid.token;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class TutorialActivity extends Activity implements OnClickListener {

	private int imageCount = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		ImageView tutorialPhoto = (ImageView) findViewById(R.id.tutorial_photo);
		tutorialPhoto.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tutorial, menu);
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
		if (v.getId() == R.id.tutorial_photo) {
			ImageView currPhoto = (ImageView) findViewById(v.getId());
			if (imageCount == 1) {
				currPhoto.setImageResource(R.drawable.twophoto);
				imageCount++;
			} else if (imageCount == 2) {
				currPhoto.setImageResource(R.drawable.threephoto);
				imageCount++;
			} else {
				Intent goToMainActivity = new Intent(this, MainActivity.class);
				startActivity(goToMainActivity);
			}
		}
	}
}
