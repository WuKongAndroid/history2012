package org.treant.comicreader_netclient.utils;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncDownloadImage {
	private Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	private static AsyncDownloadImage mAsyncDownload=null;
	private AsyncDownloadImage(){
		
	}
	public static AsyncDownloadImage getInstance(){
		if(mAsyncDownload==null){
			mAsyncDownload=new AsyncDownloadImage();
		}
		return mAsyncDownload;
	}
	
	public Drawable loadDrawable(final String url,
			final AsyncMissionCallBack callBack) {
		if (imageCache.containsKey(url)) {
			SoftReference<Drawable> softReference = imageCache.get(url);
			if (softReference.get() != null) {
				Log.i("Cache", " π”√ª∫¥Ê");
				return softReference.get();
			}
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				callBack.handleDrawable((Drawable) msg.obj);
			}
		};

		executorService.submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Drawable drawable = downloadDrawableRes(url);
				imageCache.put(url, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				message.obj = drawable;
				handler.sendMessage(message);
			}

		});
		return null;
	}

	private Drawable downloadDrawableRes(String url) {
		try {
			Log.i("download", "œ¬‘ÿimage");
			return Drawable.createFromStream(new URL(url).openStream(), "image");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public interface AsyncMissionCallBack {
		public void handleDrawable(Drawable drawable);
	}

}
