package com.wu.androidfileclient.async;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpStatus;

import android.os.Environment;
import android.util.Log;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;

public class DownloadFileAsyncTask extends CustomAsyncTask<FileItem, Integer, FileItem> {

	public DownloadFileAsyncTask(AllActivities activity, long reference, String url) {
		super(activity, reference, url, ProgressDialogHandler.DOWNLOADING_FILE, AllActivities.DOWNLOAD_FILE);
	}

	@Override
	protected FileItem doInBackground(FileItem... params) {
		int count;
		Long lenghtOfFile;
		OutputStream outputStream;
		
		FileItem fileItem   = params[0];
		fileItem.localPath  = Environment.getExternalStorageDirectory().getPath() + "/wu_files/" + fileItem.path + "/";

		HttpHandler httpHandler = new HttpHandler(url);
		int statusCode = httpHandler.startGETConnection();
		InputStream tempStrem        = httpHandler.retrieveStream();
		InputStream inputStream      = new BufferedInputStream(tempStrem, 8192);

		if (statusCode != HttpStatus.SC_OK) cancel(true);

		if (tempStrem != null) {
    		try {
    			File folder = new File(fileItem.localPath);
    			byte data[] = new byte[1024];
                long total  = 0;

    			if (!folder.exists()) folder.mkdirs();

    			lenghtOfFile = (Long) httpHandler.retrieveContentSize();
    			outputStream = new BufferedOutputStream(new FileOutputStream(fileItem.localPath + fileItem.name));

    			if (progressDialog != null) progressDialog.setMax((int) (long)lenghtOfFile);

                while ((count = inputStream.read(data)) != -1 ) {
                	if (isCancelled()) break;

                    total += count;
                    publishProgress((int)total);

                    outputStream.write(data, 0, count);
                }

                outputStream.flush();
                outputStream.close();
    		
    	    } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
    	    } finally {
    	    	try{
                    inputStream.close();
        	    	httpHandler.closeConnect();
        	    }
    	    	catch (Exception e) { Log.e("Error: ", e.getMessage()); }
    	    }
    		return isCancelled() ? null : fileItem;
		} else {
			return null;
		}
	}
	
	protected void onProgressUpdate(Integer... params) {
		if (progressDialog != null) progressDialog.setProgress((int)params[0]);
    }

}
