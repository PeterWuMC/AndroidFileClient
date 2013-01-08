package com.wu.androidfileclient.models;

import java.util.Date;
import java.util.Locale;

import android.webkit.MimeTypeMap;

public class FileItem extends BaseListItem {

	private static final long serialVersionUID = -3056032740471401976L;
	public long size;
	public Date last_modified;

	public String ext() {
		return MimeTypeMap.getFileExtensionFromUrl(name).toLowerCase(Locale.ENGLISH);
	}

}
