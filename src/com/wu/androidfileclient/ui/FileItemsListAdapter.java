package com.wu.androidfileclient.ui;

import java.util.ArrayList;
import java.util.Date;

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
import com.wu.androidfileclient.models.BaseListItem;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.Utilities;

public class FileItemsListAdapter extends ArrayAdapter<BaseListItem> {
	private ArrayList<BaseListItem> objectsList;
	private Activity context;

	public FileItemsListAdapter(Activity context, int textViewResourceId, ArrayList<BaseListItem> objectsList) {
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
		BaseListItem listItem       = objectsList.get(position);
		View additionalInfo         = view.findViewById(R.id.additional_info);
		TextView sizeView           = (TextView) additionalInfo.findViewById(R.id.size);
		TextView lastModifiedView   = (TextView) additionalInfo.findViewById(R.id.last_modified);

		if (listItem != null) {
			int icon = 0;
			additionalInfo.setVisibility(View.INVISIBLE);
			sizeView.setText("");
			lastModifiedView.setText("");
			if (listItem instanceof ActionItem && listItem.name.equalsIgnoreCase("back")) {
				icon = android.R.drawable.ic_menu_revert;
			} else if (listItem instanceof FolderItem) {
				icon = R.drawable.folder;
			}
			else if (listItem instanceof FileItem) {
				additionalInfo.setVisibility(View.VISIBLE);
				FileItem fileItem = (FileItem) listItem;
				icon = context.getResources().getIdentifier(fileItem.ext(), "drawable",  context.getPackageName());

				sizeView.setText("Size: " + Utilities.humanReadableByteCount(fileItem.size, true));
				lastModifiedView.setText("Modified: " + Utilities.humanReadableDatesDifferemce(fileItem.last_modified, new Date()));

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
