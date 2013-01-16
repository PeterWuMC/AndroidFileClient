package com.wu.androidfileclient.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

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
		String ext = null;
		int i = name.lastIndexOf('.');

		if (i > 0 && i < name.length() - 1) ext = name.substring(i+1).toLowerCase(Locale.ENGLISH);

		if(ext == null) return "";
		return ext;
	}

}
