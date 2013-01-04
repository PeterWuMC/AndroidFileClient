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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.wu.androidfileclient.async.PerformFileDownloadAsyncTask;
import com.wu.androidfileclient.async.PerformUpdateListAsyncTask;
import com.wu.androidfileclient.models.ActionItem;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.models.ListItem;
import com.wu.androidfileclient.ui.FileItemsListAdapter;
import com.wu.androidfileclient.utils.Utilities;

public class MainActivity extends ListActivity {
	
	private ArrayList<ListItem> objectsList    = new ArrayList<ListItem>();
	private ActionItem goBack                  = new ActionItem();
	private Credential credential              = new Credential();

	private FileItemsListAdapter filesAdapter;
	private HashMap<String, String> previousKeys;
	private String currentKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		goBack.name  = "Back";
		currentKey   = "initial";
		previousKeys = new HashMap<String, String>();
		credential   = Utilities.getCredential(this);

        if (objectsList == null) objectsList = new ArrayList<ListItem>();
        if (objectsList.isEmpty()) loadList("initial");

    	filesAdapter = new FileItemsListAdapter(this, R.layout.file_list_row, objectsList);
    	setListAdapter(filesAdapter);

    	registerForContextMenu(getListView());
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
	    MenuInflater inflater = getMenuInflater();
	    if (!(objectsList.get(info.position) instanceof ActionItem))
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
        	loadList(currentKey);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ListItem object = filesAdapter.getItem(position);
        if (object instanceof FolderItem || object instanceof ActionItem) {
        	loadList(object.key);
        } else {
        	FileItem fileItem = (FileItem) object;
        	downloadFile(fileItem);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	if (!currentKey.equals(goBack.key)) {
        		loadList(goBack.key);
        		return true;
        	}
        }
        return super.onKeyDown(keyCode, event);
    }

	public void downloadFile(FileItem file) {
		PerformFileDownloadAsyncTask task = new PerformFileDownloadAsyncTask(this, credential);
		task.execute(file);
	}

    public void loadList(String key) {
    	if (!previousKeys.containsKey(key)) previousKeys.put(key, currentKey);
    	currentKey = key;

    	goBack.key = previousKeys.get(currentKey);

    	PerformUpdateListAsyncTask task = new PerformUpdateListAsyncTask(this, credential);
		task.execute(key);
    }
    
    public void updateList(ArrayList<ListItem> result) {
    	if (result != null) {
			objectsList.clear();
			for (int i = 0; i < result.size(); i++) {
				objectsList.add(result.get(i));
			}
			if (!currentKey.equals(goBack.key)) objectsList.add(0, goBack);
	        filesAdapter.notifyDataSetChanged();
		}
    }
}
