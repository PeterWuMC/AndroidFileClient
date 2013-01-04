package com.wu.androidfileclient.models;

import java.io.Serializable;

public abstract class BaseListItem implements Serializable {

	private static final long serialVersionUID = 3824342077396986352L;
	
	public String path;
	public String localPath;
	public String key;
	public String name;

}
