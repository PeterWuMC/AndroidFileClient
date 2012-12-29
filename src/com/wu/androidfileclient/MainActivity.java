package com.wu.androidfileclient;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androidfileclient.R;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.services.FileDownloader;
import com.wu.androidfileclient.services.FileLister;
import com.wu.androidfileclient.ui.FileItemsListAdapter;

public class MainActivity extends ListActivity {

	private ArrayList<FileItem> filesList = new ArrayList<FileItem>();
	private FileItemsListAdapter filesAdapter;
	private ProgressDialog progressDialog;
	private FileItem goBack = new FileItem();
	private HashMap<String, String> previousKeys;
	private String currentKey;

	@SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
		goBack.name = "Back";
		goBack.type = "action";
		currentKey  = "initial";
		previousKeys = new HashMap<String, String>();
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
//        	downloadFile(file.key);
        	longToast("Action is not supported yet");
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

	

	public void longToast(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	public void downloadFile(String key) {
		progressDialog = ProgressDialog.show(MainActivity.this, "Please wait...", "Retrieving data...", true, true);
		
		PerformFileDownloadTask task = new PerformFileDownloadTask();
		task.execute(key);
		
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
	}
	
    public void loadFilesList(String key) {
    	if (!previousKeys.containsKey(key)) previousKeys.put(key, currentKey);
    	currentKey = key;

    	goBack.key = previousKeys.get(currentKey);
    	
    	progressDialog = ProgressDialog.show(MainActivity.this, "Please wait...", "Retrieving data...", true, true);

    	PerformFileListSearchTask task = new PerformFileListSearchTask();
		task.execute(key);
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
		
		
    }
    
    private class PerformFileDownloadTask extends AsyncTask<String, Void, FileItem> {
    	private FileDownloader fileDownloader;
    	@Override
    	protected FileItem doInBackground(String... params) {
    		String key = params[0];
    		fileDownloader = new FileDownloader();
            return fileDownloader.retrieveFile(key);
    	}
    	
    	@Override
    	protected void onPostExecute(final FileItem result) {
    		runOnUiThread(new Runnable() {
    			@Override
    			public void run() {
    				if (progressDialog != null) {
    					progressDialog.dismiss();
    					progressDialog = null;
    				}
    				longToast(result.getContent());
    			}
    		});
    	}
    }
    
    private class PerformFileListSearchTask extends AsyncTask<String, Void, ArrayList<FileItem>> {
    	private FileLister fileLister;
    	@Override
    	protected ArrayList<FileItem> doInBackground(String... params) {
    		String key = params[0];
            fileLister = new FileLister();
            return fileLister.retrieveFilesList(key);
    	}
    	
    	@Override
    	protected void onPostExecute(final ArrayList<FileItem> result) {
    		runOnUiThread(new Runnable() {
    			@Override
    			public void run() {
    				if (progressDialog != null) {
    					progressDialog.dismiss();
    					progressDialog = null;
    				}
    				filesList.clear();
    				for (int i = 0; i < result.size(); i++) {
    					filesList.add(result.get(i));
    				}
    				if (!currentKey.equals(goBack.key)) filesList.add(0, goBack);
    		        filesAdapter.notifyDataSetChanged();
    			}
    		});
    	}
    }
    
    private class CancelTaskOnCancelListener implements OnCancelListener {
    	private AsyncTask<?, ?, ?> task;
    	
    	public CancelTaskOnCancelListener(AsyncTask<?, ?, ?> task) {
    		this.task = task;
    	}
    	@Override
		public void onCancel(DialogInterface dialog) {
    		if (task!=null) {
        		task.cancel(true);
        	}
		}
    }
    
}
