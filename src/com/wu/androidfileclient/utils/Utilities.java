package com.wu.androidfileclient.utils;

import android.content.Context;
import android.widget.Toast;

public final class Utilities {
    

	public final static void longToast(Context context, CharSequence message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	public final static String fileExt(String url) {
        if (url.indexOf("?")>-1) {
            url = url.substring(0,url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") );
            if (ext.indexOf("%")>-1) {
                ext = ext.substring(0,ext.indexOf("%"));
            }
            if (ext.indexOf("/")>-1) {
                ext = ext.substring(0,ext.indexOf("/"));
            }
            return ext.toLowerCase();
        }
    }
}
