package com.wu.androidfileclient;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.wu.androidfileclient.async.PerformFileDownloadTask;
import com.wu.androidfileclient.async.PerformFileListSearchTask;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.ui.FileItemsListAdapter;
import com.wu.androidfileclient.utils.Utilities;

public class MainActivity extends ListActivity {
	
	private ArrayList<FileItem> filesList      = new ArrayList<FileItem>();
	private FileItem goBack                    = new FileItem();
	private HashMap<String, String> credential = new HashMap<String, String>();

	private FileItemsListAdapter filesAdapter;
	private HashMap<String, String> previousKeys;
	private String currentKey;

	@SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		goBack.name  = "Back";
		goBack.type  = "action";
		currentKey   = "initial";
		previousKeys = new HashMap<String, String>();
		
////		TEMP START
//		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
//		HashMap<String, String> hash = new HashMap<String, String>();
//		hash.put("key", "user_name");
//		hash.put("value", "peterwumc");
//		data.add(hash);
//		hash = new HashMap<String, String>();
//		hash.put("key", "device_code");
//		hash.put("value", "882b6577b0073bb7fd0491a1241e5ff1ec9e2bc2");
//		data.add(hash);
//		new XMLHelper(this).writer("credential.xml", data);
////		TEMP END
		
		credential   = Utilities.getCredential(this);

        filesList = (ArrayList<FileItem>) getIntent().getSerializableExtra("files");
        if (filesList == null) filesList = new ArrayList<FileItem>();
        if (filesList.isEmpty()) loadFilesList("initial");

    	filesAdapter = new FileItemsListAdapter(this, R.layout.file_list_row, filesList);
//    	filesAdapter.setNotifyOnChange(true);
    	setListAdapter(filesAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        FileItem file = filesAdapter.getItem(position);
        if (file.type.equals("folder") || file.type.equals("action")) {
        	loadFilesList(file.key);
        } else {
        	downloadFile(file.key, file.name);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	if (!currentKey.equals(goBack.key)) {
        		loadFilesList(goBack.key);
        	} else {
                return super.onKeyDown(keyCode, event);
        	}
        }
        return true;
    }

	public void downloadFile(String key, String name) {
		PerformFileDownloadTask task = new PerformFileDownloadTask(this, credential);
		task.execute(key, name);
	}

    public void loadFilesList(String key) {
    	if (!previousKeys.containsKey(key)) previousKeys.put(key, currentKey);
    	currentKey = key;

    	goBack.key = previousKeys.get(currentKey);

    	PerformFileListSearchTask task = new PerformFileListSearchTask(this, credential);
		task.execute(key);
    }
    
    public void updateFilesList(ArrayList<FileItem> result) {
    	if (result != null) {
			filesList.clear();
			for (int i = 0; i < result.size(); i++) {
				filesList.add(result.get(i));
			}
			if (!currentKey.equals(goBack.key)) filesList.add(0, goBack);
	        filesAdapter.notifyDataSetChanged();
		}
    }
}
