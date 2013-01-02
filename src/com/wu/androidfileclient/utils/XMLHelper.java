package com.wu.androidfileclient.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class XMLHelper {

	private Context context;

	public XMLHelper(Context context) {
		this.context = context;
	}
	
	public void delete(String filename) {
		File dir = context.getFilesDir();
		File file = new File(dir, filename);
		boolean deleted = file.delete();
	}
	
	public void writer(String filename, ArrayList<HashMap<String, String>> data) {
		try {
		    FileOutputStream fos     = context.openFileOutput(filename, Context.MODE_PRIVATE);
		    XmlSerializer serializer = Xml.newSerializer();
	
		    serializer.setOutput(fos, "UTF-8");
		    serializer.startDocument(null, Boolean.valueOf(true));
		    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
	
		    serializer.startTag(null, "entries");
		    for(int i = 0 ; i < data.size() ; i++)
		    {
			    serializer.startTag(null, "entry");

		        serializer.startTag(null, "key");
		        serializer.text(data.get(i).get("key"));
		        serializer.endTag(null, "key");

		        serializer.startTag(null, "value");
		        serializer.text(data.get(i).get("value"));
		        serializer.endTag(null, "value");

			    serializer.endTag(null, "entry");
		    }
		    serializer.endTag(null, "entries");
	
		    serializer.endDocument();
		    serializer.flush();
		    fos.close();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "Error trying to write into internal storage", e);
		}
	}
	
	public ArrayList<HashMap<String, String>> reader(String filename) {
		try {
			FileInputStream fis   = context.openFileInput(filename);;
		    InputStreamReader isr = new InputStreamReader(fis);
		    char[] inputBuffer    = new char[fis.available()];

		    isr.read(inputBuffer);
		    String data = new String(inputBuffer);
		    isr.close();
		    fis.close();
		    
	        InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));

		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();;
		    DocumentBuilder db         = dbf.newDocumentBuilder();
		    Document dom               = db.parse(is);
		    NodeList entries = null;

		    dom.getDocumentElement().normalize();

		    entries = dom.getElementsByTagName("entry");

		    ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		    for (int i = 0; i < entries.getLength(); i++){
		        Element entry = (Element) entries.item(i);

		        HashMap<String, String> hashmap = new HashMap<String, String>();
		        hashmap.put("key", entry.getElementsByTagName("key").item(0).getTextContent());
		        hashmap.put("value", entry.getElementsByTagName("value").item(0).getTextContent());
		        
		        result.add(hashmap);
		    }
		    return result;
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "Error trying to read from internal storage", e);
			return null;
		}
	}
}
