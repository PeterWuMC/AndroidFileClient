package com.wu.androidfileclient.models;

import java.io.Serializable;

import org.json.JSONObject;

public abstract class BaseListItem implements Serializable {

	private static final long serialVersionUID = 3824342077396986352L;
	
	public String path;
	public String localPath;
	public String key;
	public String name;

	
	public BaseListItem() {}
	
	public BaseListItem(JSONObject jsonObject) {
		try {
			name = jsonObject.getString("name");
			path = jsonObject.getString("path");
			key  = jsonObject.getString("key");
		} catch (Exception e) {}
	}

}
