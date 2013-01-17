package com.wu.androidfileclient.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.models.Credential;

public final class Utilities {

	public final static void longToast(AllActivities activity, int id) {
		Resources res = activity.getResources();
		longToast(activity, res.getString(id));
	}

	public final static void longToast(final AllActivities activity, final CharSequence message) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(activity.getContext(), message, Toast.LENGTH_LONG).show();
			}
		});
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
	
	public final static String humanReadableDatesDifferemce(Date originalDate, Date targetDate) {
		long seconds = Math.abs(targetDate.getTime() - originalDate.getTime()) / 1000;

		if (TimeUnit.SECONDS.toDays(seconds) > 365)       return "1 year ago";
		else if (TimeUnit.SECONDS.toDays(seconds) > 182)  return "6 months ago";
		else if (TimeUnit.SECONDS.toDays(seconds) > 30)   return "1 month ago";
		else if (TimeUnit.SECONDS.toDays(seconds) > 0)    return (int) TimeUnit.SECONDS.toDays(seconds) + " days ago";
		else if (TimeUnit.SECONDS.toHours(seconds) > 0)   return (int) TimeUnit.SECONDS.toHours(seconds) + " hours ago";
		else if (TimeUnit.SECONDS.toMinutes(seconds) > 0) return (int) TimeUnit.SECONDS.toMinutes(seconds) + " minutes ago";
		
		return "< a minute old";
	}

	public final static File getFileFromUri(Uri uri,
	        ContentResolver contentResolver) {
		File file;
		if (uri.getScheme().toString().compareTo("content")==0) {
		    String filePath;
		    String[] filePathColumn = {MediaColumns.DATA};
	
		    Cursor cursor = contentResolver.query(uri, filePathColumn, null, null, null);
		    cursor.moveToFirst();
	
		    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		    filePath = cursor.getString(columnIndex);
		    cursor.close();
		    file = new File(filePath);
		} else {
			file = FileUtils.getFile(uri);
		}
		return file;
	}

}