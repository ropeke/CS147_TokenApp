package com.hcid.token;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

/* Data model for a Token :) Lolsauce
 * 
 */

@ParseClassName("TokenPost")
public class TokenPost extends ParseObject {
	public String getCaption() {
		return getString("caption");
	}
	
	public void setCaption(String value) {
		put("caption", value);
	}
	
	public ParseUser getUser() {
		return getParseUser("user");
	}
	
	public void setUser(ParseUser value) {
		put("user", value);
	}
	
	public ParseGeoPoint getLocation() {
		return getParseGeoPoint("location");
	}
	
	public void setLocation(ParseGeoPoint value) {
		put("location", value);
	}
	
	public void setAccess(boolean meAccess, boolean friendAccess, boolean publicAccess) {
		put ("meAccess", meAccess);
		put ("friendAccess", friendAccess);
		put ("publicAccess", publicAccess);
	}
	
	public boolean getMeAccess() {
		return getBoolean("meAccess");
	}
	
	public boolean getFriendAccess() {
		return getBoolean("friendAccess");
	}
	
	public boolean getPublicAccess() {
		return getBoolean("publicAccess");
	}
	
	public void setLikeCount(int likeCount) {
		put("likes", likeCount);
	}
	
	public int getLikeCount() {
		return getInt("likes");
	}
	
	public void setTokenImage(ParseFile imageFile) {
		put("tokenImage", imageFile);
	}
	
	public ParseFile getTokenImage() {
		return getParseFile("tokenImage");
	}
	
	
}
