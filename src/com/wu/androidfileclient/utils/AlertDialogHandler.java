package com.wu.androidfileclient.utils;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.services.FolderCreator;

public class AlertDialogHandler extends AlertDialog.Builder {

	private EditText input;
	private AllActivities activity;
	private Credential credential;

	private FolderItem currentFolder;
	private ArrayList<FolderItem> folderItemList;

	public static final int FOLDER_NAME = 1;
	public static final int SELECT_PROJECT = 2;
	
	public AlertDialogHandler(AllActivities activity, Credential credential) {
		super(activity.getContext());
		this.activity = activity;
		this.credential = credential;
	}

	public AlertDialogHandler(AllActivities activity, Credential credential, FolderItem currentFolder) {
		super(activity.getContext());
		this.activity = activity;
		this.credential = credential;
		this.currentFolder = currentFolder;
	}

	public AlertDialogHandler(AllActivities activity, Credential credential, ArrayList<FolderItem> folderItemList) {
		super(activity.getContext());
		this.activity = activity;
		this.credential = credential;
		this.folderItemList = folderItemList;
	}

	public void createAlertDialog(int dialogType) {
		switch (dialogType) {
		case FOLDER_NAME:
			final FolderCreator folderCreater = new FolderCreator(credential);
			input = new EditText(activity.getContext());
			setTitle("Create Folder");
			setMessage("Folder name");
			setView(input);
			setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	FolderItem folderItem = new FolderItem();
			    	folderItem.name = input.getText().toString();
			    	folderItem.projectKey = currentFolder.projectKey;
			    	folderItem.key = currentFolder.key;
                	if (!folderItem.name.isEmpty()) {
	            		folderCreater.create_folder(activity, 1, folderItem);
	            	} else {
	            		Utilities.longToast(activity, "Cannot have a folder without name");
	            	}
			    }
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			        // Do nothing.
			    }
			});
			break;
		case SELECT_PROJECT:
			ArrayAdapter<FolderItem> adapter = new ArrayAdapter<FolderItem>(activity.getContext(), android.R.layout.select_dialog_multichoice);

			for (int i = 0; i < folderItemList.size(); i++) {
				adapter.add(folderItemList.get(i));
			}
			
			setTitle("Please choose a project");
			setAdapter(adapter, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
//			    	activity.resetCurrentAndPrevious(folderItemList.get(item));
//			    	activity.refreshList();
			    }
			});
			break;
		}
	}
}