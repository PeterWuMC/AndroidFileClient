package com.wu.androidfileclient.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.widget.Toast;

import com.wu.androidfileclient.models.Credential;

public final class Utilities {
    

	public final static void longToast(Context context, CharSequence message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	public final static Credential getCredential(Context context) {
		Credential credential                         = new Credential();
		XMLHelper xmlHelper                           = new XMLHelper(context);
		ArrayList<HashMap<String, String>> xmlContent = xmlHelper.reader("credential.xml");
		
		if (xmlContent != null) {
			for (int i = 0; i < xmlContent.size(); i++) {
				credential.set(xmlContent.get(i).get("key"), xmlContent.get(i).get("value"));
			}
		}

		return credential;
	}
	
	public final static void saveCredential(Context context, Credential credential) {
		XMLHelper xmlHelper                           = new XMLHelper(context);
		ArrayList<HashMap<String, String>> xmlContent = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> hash = new HashMap<String, String>();
		hash.clear();
		hash.put("key", Credential.USER_NAME_KEY);
		hash.put("value", credential.getUserName());
		xmlContent.add(hash);
		hash = new HashMap<String, String>();
		hash.put("key", Credential.DEVICE_CODE_KEY);
		hash.put("value", credential.getDeviceCode());
		xmlContent.add(hash);

		xmlHelper.writer("credential.xml", xmlContent);
	}
	
	public final static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format(Locale.ENGLISH, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}