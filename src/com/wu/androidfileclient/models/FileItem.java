package com.wu.androidfileclient.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.webkit.MimeTypeMap;

public class FileItem extends BaseListItem {

	private static final long serialVersionUID = -3056032740471401976L;
	public long size;
	public Date last_modified;

	public FileItem() {}

	public FileItem(JSONObject jsonObject) {
		super(jsonObject);
        	try {
            	size = jsonObject.getLong("size");
//        		this is very temporary until a better solution is found
        		last_modified = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.ENGLISH).parse(jsonObject.getString("last_update"));
        	} catch (Exception e) {}
	}

	public String ext() {
		return MimeTypeMap.getFileExtensionFromUrl(name).toLowerCase(Locale.ENGLISH);
	}

}
