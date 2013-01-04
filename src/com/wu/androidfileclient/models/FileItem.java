package com.wu.androidfileclient.models;

import java.util.Date;
import java.util.Locale;

import android.webkit.MimeTypeMap;

public class FileItem extends BaseListItem {

	private static final long serialVersionUID = -3056032740471401976L;
	public long size;
	public Date last_modified;

//	private String content;
//
//	public String getContent() {
//		return content;
//	}
//
//	public void setContent(String content) {
//		byte[] decodedContent = Base64.decode(content, Base64.DEFAULT);
//		this.content = new String(decodedContent);
//	}
	
	public String ext() {
		return MimeTypeMap.getFileExtensionFromUrl(name).toLowerCase(Locale.ENGLISH);
	}
	
//	@Override
//	public String toString() {
//		StringBuilder builder = new StringBuilder();
//		if (type != null && !type.isEmpty() && !type.equals("action")) {
//			builder.append("[");
//			builder.append(type);
//			builder.append("] ");
//		}
//		builder.append(name);
//		return builder.toString();
//	}
}
