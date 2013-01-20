package com.wu.androidfileclient.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BaseArrayList extends ArrayList<BaseListItem> {

	private static final long serialVersionUID = -3912504285837765138L;

	public BaseArrayList sorted() {
		BaseArrayList list = this;
		Collections.sort(list, new Comparator<BaseListItem>() {
    	    public int compare(BaseListItem b1, BaseListItem b2) {
    	    	int a = 0;
    	    	int b = 0;
    	    	a = (b1 instanceof FolderItem) ? 1 : 2;
    	    	b = (b2 instanceof FolderItem) ? 1 : 2;
    	    	if (a - b == 0)
    	    		return b1.name.compareTo(b2.name);
    	    	else
    	    		return a - b;
    	    }
    	});
		return list;
	}

}
