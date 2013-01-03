package com.wu.androidfileclient;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		goBack.name  = "Back";
		goBack.type  = "action";
		currentKey   = "initial";
		previousKeys = new HashMap<String, String>();
		credential   = Utilities.getCredential(this);

        if (filesList == null) filesList = new ArrayList<FileItem>();
        if (filesList.isEmpty()) loadFilesList("initial");

    	filesAdapter = new FileItemsListAdapter(this, R.layout.file_list_row, filesList);
    	setListAdapter(filesAdapter);

    	registerForContextMenu(getListView());
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main_context, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.refresh:
        	loadFilesList(currentKey);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        FileItem file = filesAdapter.getItem(position);
        if (file.type.equals("folder") || file.type.equals("action")) {
        	loadFilesList(file.key);
        } else {
        	downloadFile(file);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	if (!currentKey.equals(goBack.key)) {
        		loadFilesList(goBack.key);
        		return true;
        	}
        }
        return super.onKeyDown(keyCode, event);
    }

	public void downloadFile(FileItem file) {
		PerformFileDownloadTask task = new PerformFileDownloadTask(this, credential);
		task.execute(file);
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
