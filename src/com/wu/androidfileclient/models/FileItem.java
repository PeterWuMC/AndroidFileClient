package com.wu.androidfileclient.models;

import java.io.Serializable;

import android.util.Base64;
import android.util.Log;

public class FileItem implements Serializable {
	
	private static final long serialVersionUID = -3056032740471401976L;
	
	public String type;
	public String path;
	public String key;
	public String name;
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		byte[] decodedContent = Base64.decode(content, Base64.DEFAULT);
		this.content = new String(decodedContent);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (type != null && !type.isEmpty() && !type.equals("action")) {
			builder.append("[");
			builder.append(type);
			builder.append("] ");
		}
		builder.append(name);
		return builder.toString();
	}
}
