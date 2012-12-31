package com.wu.androidfileclient;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.wu.androidfileclient.async.PerformFileDownloadTask;
import com.wu.androidfileclient.async.PerformFileListSearchTask;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.ui.FileItemsListAdapter;

public class MainActivity extends ListActivity {
	
	private ArrayList<FileItem> filesList = new ArrayList<FileItem>();
	private FileItem goBack               = new FileItem();

	private FileItemsListAdapter filesAdapter;
	private HashMap<String, String> previousKeys;
	private String currentKey;

	private ProgressDialog progressDialog;

	@SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		goBack.name = "Back";
		goBack.type = "action";
		currentKey  = "initial";
		previousKeys = new HashMap<String, String>();

        filesList = (ArrayList<FileItem>) getIntent().getSerializableExtra("files");
        if (filesList == null) filesList = new ArrayList<FileItem>();
        if (filesList.isEmpty()) loadFilesList("initial");

    	filesAdapter = new FileItemsListAdapter(this, R.layout.file_list_row, filesList);
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
    	progressDialog = new ProgressDialog(this);
    	progressDialog.setMessage("Downloading file. Please wait...");
    	progressDialog.setIndeterminate(false);
    	progressDialog.setMax(100);
    	progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	progressDialog.setCancelable(true);

		PerformFileDownloadTask task = new PerformFileDownloadTask(this);
		task.execute(key, name);

		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
	}

    public void loadFilesList(String key) {
    	if (!previousKeys.containsKey(key)) previousKeys.put(key, currentKey);
    	currentKey = key;

    	goBack.key = previousKeys.get(currentKey);

    	PerformFileListSearchTask task = new PerformFileListSearchTask(this, filesList, filesAdapter, goBack, currentKey);
		task.execute(key);
    }
}
