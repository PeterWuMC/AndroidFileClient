package com.wu.androidfileclient.ui;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.R;
import com.wu.androidfileclient.fetchers.ThumbnailStreamer;
import com.wu.androidfileclient.models.BaseArrayList;
import com.wu.androidfileclient.models.BaseListItem;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.Utilities;

public class FileItemsListAdapter extends ArrayAdapter<BaseListItem> {

	private Context context;
	private BaseArrayList objectsList;
	private AllActivities activity;
	private Credential credential;
	private LinkedHashMap<String, Bitmap> bitmapCache = new LinkedHashMap<String, Bitmap>();
	private HashMap<String, ImageView> imageMap = new HashMap<String, ImageView>();

	private BaseListItem listItem;
	private View additionalInfo;
	private ListView listView;
	private TextView sizeView;
	private TextView lastModifiedView;
	private ImageView typeImage;
	private TextView nameTextView;

	public FileItemsListAdapter(AllActivities activity, ListView listView, Credential credential, int textViewResourceId, BaseArrayList objectsList) {
		super(activity.getContext(), textViewResourceId, objectsList);
		this.activity    = activity;
		this.listView    = listView;
		this.context     = activity.getContext();
		this.objectsList = objectsList;
		this.credential  = credential;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.file_list_row, null);
		}
		listItem         = objectsList.get(position);
		additionalInfo   = (View) view.findViewById(R.id.additional_info);
		sizeView         = (TextView) additionalInfo.findViewById(R.id.size);
		lastModifiedView = (TextView) additionalInfo.findViewById(R.id.last_modified);
		typeImage        = (ImageView) view.findViewById(R.id.icon);
		nameTextView     = (TextView) view.findViewById(R.id.name);

		if (listItem != null) {
			typeImage.setImageResource(R.drawable.unknown);
			String name = (listItem.name.length() > 23) ? listItem.name.substring(0, 20) + "..." : listItem.name;
			nameTextView.setText(name);
			sizeView.setText("");
			additionalInfo.setVisibility(View.INVISIBLE);
			lastModifiedView.setText("");
			if (listItem.name.equalsIgnoreCase("back")) {
				typeImage.setImageResource(android.R.drawable.ic_menu_revert);
			} else if (listItem instanceof FolderItem) {
				typeImage.setImageResource(R.drawable.folder);
			}
			else if (listItem instanceof FileItem) {
				additionalInfo.setVisibility(View.VISIBLE);
				FileItem fileItem = (FileItem) listItem;

				int resource = context.getResources().getIdentifier(fileItem.ext(), "drawable",  context.getPackageName());
				if (resource != 0) typeImage.setImageResource(resource);

				sizeView.setText("Size: " + Utilities.humanReadableByteCount(fileItem.size, true));
				lastModifiedView.setText("Modified: " + Utilities.humanReadableDatesDifferemce(fileItem.last_modified, new Date()));

				String url = (new ThumbnailStreamer(credential)).constructUrl(fileItem.key, fileItem.projectKey);
				imageMap.put(url, typeImage);

				Bitmap bitmap = fetchBitmapFromCache(url);
				if (bitmap == null) {
					new BitmapDownloaderTask(typeImage, position).execute(url);
				} else {
					typeImage.setImageBitmap(bitmap);
				}
			}
		}

		return view;
	}

	private void addBitmapToCache(String url, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (bitmapCache) {
				bitmapCache.put(url, bitmap);
			}
		}
	}

	private Bitmap fetchBitmapFromCache(String url) {
		synchronized (bitmapCache) {
			final Bitmap bitmap = bitmapCache.get(url);
			if (bitmap != null) {
				bitmapCache.remove(url);
				bitmapCache.put(url, bitmap);
				return bitmap;
			}
		}
		return null;
	}

	private void setThumbnail(ImageView imageView, Bitmap bitmap, int position) {
		if (listView.getFirstVisiblePosition() <= position && listView.getLastVisiblePosition() >= position) imageView.setImageBitmap(bitmap);
	}

	private class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private final WeakReference<ImageView> imageViewReference;
		private int mPosition;
		
		public BitmapDownloaderTask(ImageView imageView, int mPosition) {
			imageViewReference = new WeakReference<ImageView>(imageView);
			this.mPosition = mPosition;
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			HttpHandler httpRetriever = new HttpHandler(url);
			if (httpRetriever.startGETConnection() == HttpStatus.SC_OK) {
				InputStream is = httpRetriever.retrieveStream();
				if (is == null) return null;
				return BitmapFactory.decodeStream(is);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) bitmap = null;
			
			addBitmapToCache(url, bitmap);
			
			if (bitmap != null && imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
//				if (imageView != null) imageView.setImageBitmap(bitmap);
				if (imageView != null) setThumbnail(imageView, bitmap, mPosition);
			}
		}
	}

}
