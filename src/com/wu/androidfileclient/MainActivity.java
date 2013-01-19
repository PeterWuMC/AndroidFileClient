package com.wu.androidfileclient;

import java.io.File;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.wu.androidfileclient.fetchers.FileDownloader;
import com.wu.androidfileclient.fetchers.FileUploader;
import com.wu.androidfileclient.fetchers.FolderLister;
import com.wu.androidfileclient.fetchers.ItemRemover;
import com.wu.androidfileclient.fetchers.ProjectLister;
import com.wu.androidfileclient.fetchers.PublicPath;
import com.wu.androidfileclient.models.BaseArrayList;
import com.wu.androidfileclient.models.BaseListItem;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderArrayList;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.services.MonitorCameraService;
import com.wu.androidfileclient.ui.FileItemsListAdapter;
import com.wu.androidfileclient.utils.AlertDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class MainActivity extends ListActivity implements AllActivities {
	
	private BaseArrayList objectsList                       = new BaseArrayList();
	private Credential credential                           = new Credential();
	private FolderItem currentFolder                        = new FolderItem();
	FolderItem initialFolderItem = new FolderItem();

	private ItemRemover itemRemover;
	private FolderLister folderLister;
	private ProjectLister projectLister;
	private FileUploader fileUploader;
	private FileDownloader fileDownloader;

	private FileItemsListAdapter filesAdapter;

	public Context getContext() {
		return this;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

		credential     = Utilities.getCredential(this);
		itemRemover    = new ItemRemover(credential); 
		folderLister   = new FolderLister(credential); 
		projectLister  = new ProjectLister(credential);
    	fileUploader   = new FileUploader(credential);
    	fileDownloader = new FileDownloader(credential);

		initialFolderItem.key        = "initial";
		initialFolderItem.projectKey = "cHVibGlj";
		initialFolderItem.parent     = null;
		resetCurrentAndPrevious(initialFolderItem);

        if (objectsList == null) objectsList = new BaseArrayList();
        if (objectsList.isEmpty()) loadList(currentFolder);

    	filesAdapter = new FileItemsListAdapter(this, getListView(), credential, R.layout.file_list_row, objectsList);
    	setListAdapter(filesAdapter);

    	registerForContextMenu(getListView());
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
	    MenuInflater inflater = getMenuInflater();
	    if (!(objectsList.get(info.position).name.equalsIgnoreCase("back")))
	    	inflater.inflate(R.menu.activity_main_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		BaseListItem listItem = objectsList.get(info.position);
		if (listItem instanceof FileItem) {
			FileItem fileItem = (FileItem) listItem;
	
			switch (item.getItemId()) {
			case R.id.open:
				fileDownloader.download(this, 1, fileItem);
				break;
			case R.id.copy_public_link:
				PublicPath publicPath = new PublicPath();
				Utilities.stringToClipBoard(this, publicPath.generatePublicUrl(fileItem.publicPath));
				break;
			case R.id.delete:
//				TODO: currently only allow to delete file
				itemRemover.delete(this, 1, fileItem);
		    	break;
	        default:
	            return super.onOptionsItemSelected(item);
			}
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialogHandler alertDialog;
		switch (item.getItemId()) {
		case R.id.start:
			startService(new Intent(this, MonitorCameraService.class));
			break;
		case R.id.stop:
			stopService(new Intent(this, MonitorCameraService.class));
			break;
		case R.id.change_project:
			projectLister.retrieveList(this, 1);
			break;
        case R.id.refresh:
        	refreshList();
        	break;
        case R.id.create_folder:
        	alertDialog = new AlertDialogHandler(this, credential, currentFolder);
        	alertDialog.createAlertDialog(AlertDialogHandler.FOLDER_NAME);
        	alertDialog.show();
        	break;
        case R.id.upload:
        	Intent target = FileUtils.createGetContentIntent();
            Intent intent = Intent.createChooser(target, "Select a file");
            try {
                startActivityForResult(intent, 1234);
            } catch (ActivityNotFoundException e) {
                // The reason for the existence of aFileChooser
            }
        	break;
        default:
            return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch (requestCode) {
	    case 1234:  
	        if (resultCode == RESULT_OK) {
	        	File f = Utilities.getFileFromUri(data.getData(), this.getContentResolver());
	        	FileItem file = new FileItem();
	        	file.localPath = f.getParentFile().getPath() + "/";
	        	file.name = f.getName();

	        	fileUploader.upload(this, 1, true, currentFolder, file);
	        }
	    }
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        BaseListItem listItem = filesAdapter.getItem(position);
        if (listItem instanceof FolderItem) {
        	loadList((FolderItem) listItem);
        } else {
			FileDownloader fileDownloader = new FileDownloader(credential);
			fileDownloader.download(this, 1, (FileItem) listItem);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	if (currentFolder.parent != null) {
        		loadList((FolderItem) currentFolder.parent);
        		return true;
        	}
        }
        return super.onKeyDown(keyCode, event);
    }

	public void refreshList() {
		loadList(currentFolder);
	}

    public void loadList(FolderItem folderItem) {
    	currentFolder = folderItem;
    	folderLister.retrieveList(this, 1, currentFolder); 
    }

    public void updateList(BaseArrayList result) {
    	if (result != null) {
			objectsList.clear();
			for (int i = 0; i < result.size(); i++) {
				objectsList.add(result.get(i));
			}
			FolderItem parentFolder = currentFolder.parent;
			if (parentFolder != null) parentFolder.name = "Back";
			if (currentFolder.parent != null) objectsList.add(0, parentFolder);
	        filesAdapter.notifyDataSetChanged();
		}
    }

    public void resetCurrentAndPrevious(FolderItem startingFolderItem) {
    	currentFolder = startingFolderItem;
    }

    public void onTaskCancelled(int task, long reference, Object result) {
    	switch (task) {
    	case DOWNLOAD_FILE:
    		
    		break;
    	}
    }

    public void onTaskCompleted(int task, long reference, Object result) {
    	if (result != null) {
	    	switch (task) {
	    	case CREATE_FOLDER:
	    		if (result instanceof Boolean && (Boolean) result) refreshList();
	    		break;
	    	case DELETE_ITEM:
	    		if (result instanceof Boolean && (Boolean) result) refreshList();
	    		break;
	    	case DOWNLOAD_FILE:
	    		if (result instanceof FileItem) {
		    		Intent fileViewIntent = new Intent();
		    		fileViewIntent.setAction(android.content.Intent.ACTION_VIEW);
		    		FileItem fileItem = (FileItem) result;
		    		if (!fileItem.localPath.isEmpty()) {
		        		File file = new File(fileItem.localPath + fileItem.name);
	
		        		fileViewIntent.setDataAndType(Uri.fromFile(file), MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileItem.ext()));
		        		try {
		        			startActivity(fileViewIntent);
		        		} catch (android.content.ActivityNotFoundException e) {
		        			Utilities.longToast(this, R.string.no_compatible_application);
		        			Log.e(getClass().getSimpleName(), "Activity Not Found");
		        		}
		    		}
	    		}
	    		break;
	    	case UPDATE_LIST:
	    		if (result instanceof BaseArrayList) {
	    			updateList((BaseArrayList) result);
	    		}
	    		break;
	    	case UPLOAD_FILE:
	    		refreshList();
	    		break;
	    	case GET_PROJECT:
	    		if (result instanceof FolderArrayList) {
	    			AlertDialogHandler alertDialog;
					alertDialog = new AlertDialogHandler(this, credential, (FolderArrayList) result);
		        	alertDialog.createAlertDialog(AlertDialogHandler.SELECT_PROJECT);
		        	alertDialog.show();
	    		}
	    		break;
	    	case PROJECT_SWITCH:
	    		if (result instanceof FolderItem) {
	    			resetCurrentAndPrevious((FolderItem) result);
	    			refreshList();
	    		}
	    	}
    	}
    }
}
