package com.hcid.token;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import android.app.Application;
import android.content.res.Configuration;

public class MyApplication extends Application {

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		ParseObject.registerSubclass(TokenPost.class);
		Parse.initialize(this, "86hS3laxGVl5ZVv0ekFJIgioAIxmSULC0TmaVnS4", "4vORQhjYNUWjPDjqfduxnhc9NqQoT02CeEk5vxOt");
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
 
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
}
