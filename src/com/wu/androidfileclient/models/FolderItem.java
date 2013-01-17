package com.wu.androidfileclient.models;

import org.json.JSONObject;


public class FolderItem extends BaseListItem {

	private static final long serialVersionUID = -4161962540130906285L;

	public FolderItem() {}

	public FolderItem(JSONObject jsonObject) {
		super(jsonObject);
	}
	
	public FolderItem(JSONObject jsonObject, FolderItem parent) {
		super(jsonObject, parent);
	}
}
