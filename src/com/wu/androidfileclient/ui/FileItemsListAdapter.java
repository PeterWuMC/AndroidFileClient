package com.wu.androidfileclient.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wu.androidfileclient.R;
import com.wu.androidfileclient.models.ActionItem;
import com.wu.androidfileclient.models.ListItem;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderItem;

public class FileItemsListAdapter extends ArrayAdapter<ListItem> {
	private ArrayList<ListItem> objectsList;
	private Activity context;

	public FileItemsListAdapter(Activity context, int textViewResourceId, ArrayList<ListItem> objectsList) {
		super(context, textViewResourceId, objectsList);
		this.context     = context;
		this.objectsList = objectsList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.file_list_row, null);
		}
		ListItem listItem = objectsList.get(position);

		if (listItem != null) {
			int icon = 0;
			if (listItem instanceof ActionItem && listItem.name.equalsIgnoreCase("back")) {
				icon = android.R.drawable.ic_menu_revert;
			} else if (listItem instanceof FolderItem) {
				icon = R.drawable.folder;
			}
			else if (listItem instanceof FileItem) {
				FileItem fileItem = (FileItem) listItem;
				icon = context.getResources().getIdentifier(fileItem.ext(), "drawable",  context.getPackageName());
				if (icon == 0) icon = R.drawable.unknown; 
			}
			ImageView typeImage = (ImageView) view.findViewById(R.id.icon);
			typeImage.setImageResource(icon);

			TextView nameTextView = (TextView) view.findViewById(R.id.name);
			nameTextView.setText(listItem.name);
		}

		return view;
	}
}
