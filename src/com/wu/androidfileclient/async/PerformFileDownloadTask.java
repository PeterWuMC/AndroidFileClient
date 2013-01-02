package com.wu.androidfileclient.async;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.http.HttpStatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.services.FileDownloader;
import com.wu.androidfileclient.utils.HttpRetriever;
import com.wu.androidfileclient.utils.Utilities;

public class PerformFileDownloadTask extends AsyncTask<String, String, String> {
    private Context context;
	ProgressDialog progressDialog;
	private HashMap<String, String> credential;
	
	public PerformFileDownloadTask(Context context, HashMap<String, String> credential) {
		super();
		this.context = context;
		this.credential = credential;
		progressDialog = new ProgressDialog(context);
    	progressDialog.setMessage("Downloading file. Please wait...");
    	progressDialog.setIndeterminate(false);
    	progressDialog.setMax(100);
    	progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	progressDialog.setCancelable(true);
    	
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    	progressDialog.show();
    }

	@Override
	protected String doInBackground(String... params) {
		int count;
		Long lenghtOfFile;
		OutputStream outputStream;
		
		String key          = params[0];
		String fileName     = params[1]; 
		String url          = new FileDownloader(credential.get("user_name"), credential.get("device_code")).constructUrl(key);
		String fileLocation = Environment.getExternalStorageDirectory().getPath() + "/wu_files/";

		HttpRetriever httpRetreiever = new HttpRetriever(url);
		int statusCode = httpRetreiever.startGETConnection();
		InputStream tempStrem        = httpRetreiever.retrieveStream();
		InputStream inputStream      = new BufferedInputStream(tempStrem, 8192);

		if (statusCode != HttpStatus.SC_OK) cancel(true);

		if (tempStrem != null) {
    		try {
    			File folder = new File(fileLocation);
    			byte data[] = new byte[1024];
                long total  = 0;

    			if (!folder.exists()) folder.mkdir();

    			lenghtOfFile = (Long) httpRetreiever.retrieveContentSize();
    			outputStream = new BufferedOutputStream(new FileOutputStream(fileLocation + fileName));

                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    outputStream.write(data, 0, count);
                }

                outputStream.flush();
                outputStream.close();
    		
    	    } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
    	    } finally {
    	    	try{
                    inputStream.close();
    	    		inputStream.close();
        	    	httpRetreiever.closeConnect();
        	    }
    	    	catch (Exception e) { Log.e("Error: ", e.getMessage()); }
    	    }
    		return fileLocation + fileName;
		} else {
			return null;
		}
	}
	
	protected void onProgressUpdate(String... params) {
        progressDialog.setProgress(Integer.parseInt(params[0]));
    }

	@Override
	protected void onCancelled() {
		if (progressDialog.isShowing()) progressDialog.dismiss();
		Utilities.longToast(context, "Something wrong with your connection...");
	}

	@Override
	protected void onPostExecute(String file_location) {
		MimeTypeMap myMime = MimeTypeMap.getSingleton();

		progressDialog.dismiss();

		Intent fileViewIntent = new Intent();
		fileViewIntent.setAction(android.content.Intent.ACTION_VIEW);
		if (file_location != null) {
    		File file = new File(file_location);

    		String mimeType = myMime.getMimeTypeFromExtension(Utilities.fileExt(file.toString()).substring(1));
    		
    	       
    		fileViewIntent.setDataAndType(Uri.fromFile(file), mimeType);
    		try {
    			context.startActivity(fileViewIntent);
    		} catch (android.content.ActivityNotFoundException e) {
    			Utilities.longToast(context, "No Application found to open this file");
    			Log.e(getClass().getSimpleName(), "Activity Not Found");
    		}
		} else {
			cancel(true);
		}
	}
}

