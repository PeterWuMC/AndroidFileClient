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
import com.wu.androidfileclient.models.FileItem;

public class FileItemsListAdapter extends ArrayAdapter<FileItem> {
	private ArrayList<FileItem> fileItems;
	private Activity context;

	public FileItemsListAdapter(Activity context, int textViewResourceId, ArrayList<FileItem> fileItems) {
		super(context, textViewResourceId, fileItems);
		this.context = context;
		this.fileItems = fileItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.file_list_row, null);
		}
		FileItem fileItem = fileItems.get(position);

		if (fileItem != null) {
			int icon = 0;
			if (fileItem.type.equals("action") && fileItem.name.equalsIgnoreCase("back")) icon = android.R.drawable.ic_menu_revert;
			else if (fileItem.type.equals("folder")) icon = android.R.drawable.ic_menu_more;
			else if (fileItem.type.equals("file")) icon = android.R.drawable.ic_menu_set_as;
			ImageView typeImage = (ImageView) view.findViewById(R.id.icon);
			typeImage.setImageResource(icon);

			TextView nameTextView = (TextView) view.findViewById(R.id.name);
			nameTextView.setText(fileItem.name);
		}

		return view;
	}
}
