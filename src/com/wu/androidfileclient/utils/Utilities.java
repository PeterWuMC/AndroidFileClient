package com.wu.androidfileclient.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.Toast;

public final class Utilities {
    

	public final static void longToast(Context context, CharSequence message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	public final static HashMap<String, String> getCredential(Context context) {
		HashMap<String, String> credential            = new HashMap<String, String>();
		XMLHelper xmlHelper                           = new XMLHelper(context);
		ArrayList<HashMap<String, String>> xmlContent = xmlHelper.reader("credential.xml");
		
		if (xmlContent != null) {
			for (int i = 0; i < xmlContent.size(); i++) {
				credential.put(xmlContent.get(i).get("key"), xmlContent.get(i).get("value"));
			}
		}

		return credential;
	}
	
	public final static void setCredential(Context context, HashMap<String, String> credential) {
		XMLHelper xmlHelper                           = new XMLHelper(context);
		ArrayList<HashMap<String, String>> xmlContent = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> hash;
		for (Map.Entry<String, String> entry : credential.entrySet()) {
			hash = new HashMap<String, String>();
			hash.put("key", entry.getKey());
			hash.put("value", entry.getValue());
			xmlContent.add(hash);
		}
		xmlHelper.writer("credential.xml", xmlContent);
	}
}