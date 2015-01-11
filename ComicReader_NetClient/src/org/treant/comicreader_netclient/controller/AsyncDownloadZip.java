package org.treant.comicreader_netclient.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncDownloadZip extends AsyncTask<String, Integer, Boolean> {
	private String fileSaveDir ;
	private String zipFileName;

	public AsyncDownloadZip(String fileSaveDir,String zipFileName) {
		this.fileSaveDir = fileSaveDir;
		this.zipFileName=zipFileName;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost();
		try {
			request.setURI(new URI(params[0]));
			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			long length = entity.getContentLength();
			InputStream is = entity.getContent();//File zipFile = new File(uncompressDir,zipFileName + ".zip");
			if(!new File(fileSaveDir).exists()){
				new File(fileSaveDir).mkdirs();
				Log.i("createDir", "创建了dir");
			}
			OutputStream os = new FileOutputStream(new File(fileSaveDir,zipFileName));
			byte[] b = new byte[4096];
			int len = 0, count = 0;
			while ((len = is.read(b)) != -1) {
				os.write(b, 0, len);
				count += len;
				publishProgress(count, (int) length);
			}
			os.flush();
			os.close();
			is.close();
			if (response.getStatusLine().getStatusCode() == 200) {
				Log.i("download成功", "Success");
				flag = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

}
