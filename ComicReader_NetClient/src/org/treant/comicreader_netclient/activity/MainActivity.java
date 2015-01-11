package org.treant.comicreader_netclient.activity;

import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.controller.DefaultDialog;
import org.treant.comicreader_netclient.controller.SingleChoiceDialog;
import org.treant.comicreader_netclient.sys.RatingActivity;
import org.treant.comicreader_netclient.utils.Constant;
import org.treant.comicreader_netclient.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
	public static List<String> pathesList;
	public static int pageIndex; // ҳ����ʾ�ĺ��ĸ��ٱ���
	private ImageView imageView;
	private TextView imageName, pagePosition;
	private ImageButton openSDcard, setUp, lastPage, nextPage, page, zoomOut,
			zoomIn, gobackhome, logout;
	private RelativeLayout layout1, layout2, layout3, layout4;
	private Handler handler;// �Ƿ���Ҫ��дhandleMessage()�����Ա�����ʹ��??
	private GestureDetector gestureDetector;
	private final static int FLING_MIN_DISTANCE = 150;
	private final static int FLING_MIN_VELOCITY = 150;
	private Bitmap bitmap;
	private float scaleWidth = 1, scaleHeight = 1;
	private int shift, temp, id; // ͼƬ������ת shift���
	private String lastReadTime;
	private Map<String,String> bookMap;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		allActivity.add(this);
		lastReadTime = Utils.getCurrentTime();
		this.initialize();
		bookMap=(Map<String, String>) getIntent().getSerializableExtra("bookMap");
	//	Log.i("Main�е�Map", bookMap.get("bookName")+bookMap.get("updateTime"));
		String imagePath = this.getIntent().getStringExtra("imagePath");  //��Intent���������źŰ�
		MainActivity.pathesList = Utils.getImagesPathesOfParentFolder(imagePath);
		MainActivity.pageIndex = this.getPageIndex(pathesList, imagePath);
		if (imagePath != null) {
			if (pathesList != null && pathesList.size() > 0) {
				this.showImage(pageIndex);
				this.showImageInfo(pageIndex);
			} else {
				layout2.setVisibility(View.VISIBLE);
				layout3.setVisibility(View.VISIBLE);
				layout4.setVisibility(View.VISIBLE);
				Utils.showToastMsg(this, R.string.noPics);
			}
		} else {
			layout2.setVisibility(View.VISIBLE);
			layout3.setVisibility(View.VISIBLE);
			layout4.setVisibility(View.VISIBLE);
			Utils.showToastMsg(this, R.string.noPath);
		}

//		this.fillHistoriesList();
		this.imageView.setOnClickListener(imageTapListener);
		this.gestureDetector = new GestureDetector(MainActivity.this,
				gestureListener);
		this.openSDcard.setOnClickListener(openSDcardListener);
		this.setUp.setOnClickListener(setUpListener);
		this.lastPage.setOnClickListener(skipListener);
		this.nextPage.setOnClickListener(skipListener);
		this.page.setOnClickListener(pageListener);
		this.zoomOut.setOnClickListener(zoomListener);
		this.zoomIn.setOnClickListener(zoomListener);
		this.gobackhome.setOnClickListener(gobackhomeListener);
		this.logout.setOnClickListener(logoutListener);
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0) {
					handler.removeCallbacks(runnable);
				}
			}
		};
		handler.postDelayed(hideNavigation, 5000);
	}

	// Runnable�ӿڵ�ʵ����runnable.���ñ�ʶ������temp��
	// Ϊ����ʱ��Ĳ���(��Ϊint������switch���жϲ���֮һ(int.char.Enum))
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (pathesList != null && pathesList.size() > 0) {
				if (pageIndex < pathesList.size() - 1) {
					skipToNextPage();
					handler.postDelayed(runnable, temp * 1000);
					refreshFollowPageIndex();
				} else if (pageIndex == pathesList.size() - 1) {
					// �����ĩҳ ����Ϣ �߳���ֹ
					Message message = handler.obtainMessage();
					message.what = 0;
					handler.sendMessage(message);
					Utils.showToastMsg(MainActivity.this, R.string.pageEnd);
					toRatingActivity();
				}
			}
		}

	};
	/*
	 * �����������̣߳�5����Զ�����
	 */
	Runnable hideNavigation = new Runnable() {

		@Override
		public void run() {
			MainActivity.this.layout2.setVisibility(View.GONE);
			MainActivity.this.layout3.setVisibility(View.GONE);
			MainActivity.this.layout4.setVisibility(View.GONE);
		}

	};

	/*
	 * ������ͼƬ���������imageTapListener,����󵼺������֣�5�������ʧ
	 */
	private View.OnClickListener imageTapListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			handler.removeCallbacks(hideNavigation);
			MainActivity.this.layout2.setVisibility(View.VISIBLE);
			MainActivity.this.layout3.setVisibility(View.VISIBLE);
			MainActivity.this.layout4.setVisibility(View.VISIBLE);
			handler.postDelayed(hideNavigation, 5000);
		}

	};
	/*
	 * ���ϽǸ�������setUp��ť���� setUpListener
	 */
	private ImageButton.OnClickListener setUpListener = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			new DefaultDialog(MainActivity.this, R.array.moreSetUp) {

				@Override
				public void itemsAction(int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case 0: // ��ʱ����/�Ի��򵯳�ǰ��ֹԭrunnable�߳� ѡ����������Ҫ�ı����ʱ��
						handler.removeCallbacks(runnable);
						new SingleChoiceDialog(MainActivity.this,
								R.array.fixTimeToBrowse) {

							@Override
							public void positiveAction() {
								// TODO Auto-generated method stub
								if (temp == 0) {
									handler.removeCallbacks(runnable);
									Utils.showToastMsg(MainActivity.this, R.string.handcontrol);
								}
								if (temp == 3) {
									handler.postDelayed(runnable, 3000);
									Utils.showToastMsg(MainActivity.this, R.string.second3);
								}
								if (temp == 5) {
									handler.postDelayed(runnable, 5000);
									Utils.showToastMsg(MainActivity.this, R.string.second5);
								}

							}

							@Override
							public void singleChoiceAction(int which) {
								// TODO Auto-generated method stub
								switch (which) {
								case 0:
									temp = 0;
									break;
								case 1:
									temp = 3;
									break;
								case 2:
									temp = 5;
									break;
								}

							}

						}.setTitle(R.string.fixtimetobrowse).setIcon(
								R.drawable.menu_more).show();
						break;
//					case 1: // �����ǩ      �����ȡ������ǩ����
//						// ���bookMarks.txt������(��δ������).����ڵ���ǩ����Ϊ0(ȫ��ɾ����)����ʾ
//						// ���ܵ���file.length()�ж�,���ռ������л���txt�ļ�Ҳ��58byte
//						// Returns the length of this file in bytes.
//						// Returns 0 if the file does not exist.
//						if (new File(Constants.BOOKMARKS).length() == 0
//								|| BookMarkActivity.bookMarksList.size() == 0) {
//							Utils.showMsg(MainActivity.this,
//									getString(R.string.nobookmark));
//						} else {
//							startActivity(new Intent(MainActivity.this,
//									BookMarkActivity.class));
//							Log.i("��ǩ��¼�ļ�bookMarks.txt�Ĵ�С", String
//									.valueOf(new File(Constants.BOOKMARKS)
//											.length()));
//						}
//						break;
					case 1: // ������ת
						new SingleChoiceDialog(MainActivity.this,
								R.array.comicRotation) {

							@Override
							public void positiveAction() {
								// TODO Auto-generated method stub
								rotate(shift * 45);
//								Utils.showToastMsg(MainActivity.this, shift * 45
//										+ "��");
							}

							@Override
							public void singleChoiceAction(int which) {
								// TODO Auto-generated method stub
								switch (which) {
								case 0:
									shift--;
									break;
								case 1:
									shift++;
									break;
								}

							}

						}.setTitle(R.string.picRotate).setIcon(
								R.drawable.menu_more).show();
						break;
					}
				}

			}.setTitle(R.string.menu_more).setIcon(R.drawable.menu_more).show();
		}

	};

	/*
	 * ��.����ת45��
	 */
	private void rotate(int angle) {
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap zoomedBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth,
				bitmapHeight, matrix, true);
		BitmapDrawable drawable = new BitmapDrawable(getResources(), zoomedBmp);
		MainActivity.this.imageView.setImageDrawable(drawable);

	}

	/*
	 * ��ǩ����ťbookmark���� bookmarkAddListener
	 * ��HashMap��Utils.bookMarksList,���л���Constants.BOOKMARKS
	 */
//	private ImageButton.OnClickListener bookmarkAddListener = new ImageButton.OnClickListener() {
//
//		@Override
//		public void onClick(View arg0) {
//			// ��������Զ���ҳ,�򵯳���ǩ�����֮ǰ����ֹ��ҳ�߳�,���򱣴��ҳ�治����Ҫ��!
//			handler.removeCallbacks(runnable);
//			View bookmarkAddView = LayoutInflater.from(MainActivity.this)
//					.inflate(R.layout.layout_bookmark_add, null);
//			final EditText bookmarkName = (EditText) bookmarkAddView
//					.findViewById(R.id.bookmarkName);
//			new AlertDialog.Builder(MainActivity.this).setTitle(
//					getString(R.string.bookmarkTitle)).setIcon(
//					R.drawable.bookmark).setView(bookmarkAddView)
//					.setPositiveButton(getString(R.string.confirm),
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									if ((new File(Constants.BOOKMARKS).length() != 0)
//											|| (BookMarkActivity.bookMarksList
//													.size() != 0)) {
//										for (int i = 0; i < BookMarkActivity.bookMarksList
//												.size(); i++) {
//											if ((Integer) BookMarkActivity.bookMarksList
//													.get(i).get("imageId") == pageIndex) {
//												Map<String, Object> map = new HashMap<String, Object>();
//												String bookMarkName = bookmarkName
//														.getText().toString();
//												String saveTime = Utils
//														.getCurrentTime();
//												int imageId = pageIndex;
//												map
//														.put(
//																"imagePath",
//																MainActivity.pathesList
//																		.get(pageIndex));
//												map.put("bookMarkName",
//														bookMarkName);
//												map.put("lastTimeReaded",
//														lastTimeReaded);
//												map.put("saveTime", saveTime);
//												map.put("imageId", imageId);
//												Utils
//														.showToastMsg(
//																MainActivity.this,R.string.bookmarkOverride);
//												BookMarkActivity.bookMarksList
//														.set(i, map);
//												return;
//											}
//										}
//									}
//									Map<String, Object> map = new HashMap<String, Object>();
//									String bookMarkName = bookmarkName
//											.getText().toString();
//									String saveTime = Utils.getCurrentTime();
//									int imageId = pageIndex;
//									map.put("imagePath",
//											MainActivity.pathesList
//													.get(pageIndex));
//									map.put("bookMarkName", bookMarkName);
//									map.put("lastTimeReaded", lastTimeReaded);
//									map.put("saveTime", saveTime);
//									map.put("imageId", imageId);
//									Utils.bookMarkSerialize(
//											BookMarkActivity.bookMarksList,
//											map, Constants.BOOKMARKS);
//									Utils.showToastMsg(MainActivity.this,R.string.bookmarkSave);
//								}
//							}).setNegativeButton(getString(R.string.cancel),
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//									dialog.dismiss();
//								}
//							}).show();
//		}
//
//	};

	private ImageButton.OnClickListener gobackhomeListener=new ImageButton.OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(MainActivity.this,HomeActivity.class);
			
			startActivity(intent);
		}
		
	};
	/*
	 * ���½�page��ť���� pageListener ��ת����һҳ����һҳ����һҳ�����һҳ
	 */
	private ImageButton.OnClickListener pageListener = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			new DefaultDialog(MainActivity.this, R.array.pageSkip) {

				@Override
				public void itemsAction(int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case 0:
						if (pageIndex == 0) {
							Utils.showToastMsg(MainActivity.this, R.string.pageFirst);
						}
						pageIndex = 0;
						showImage(pageIndex);
						break;
					case 1:
						if (pageIndex > 0) {
							showImage(--pageIndex);
						} else if (pageIndex == 0) {
							showImage(pageIndex);
							Utils.showToastMsg(MainActivity.this, R.string.pageFirst);
						}
						break;
					case 2:
						if (pageIndex < pathesList.size() - 1) {
							showImage(++pageIndex);
						} else if (pageIndex == pathesList.size() - 1) {
							showImage(pageIndex);
							Utils.showToastMsg(MainActivity.this, R.string.pageEnd);
							toRatingActivity();
						}
						break;
					case 3:
						if (pageIndex == pathesList.size() - 1) {
							Utils.showToastMsg(MainActivity.this, R.string.pageEnd);
						}
						pageIndex = pathesList.size() - 1;
						showImage(pageIndex);
						break;
					}
					MainActivity.this.showImageInfo(pageIndex);
					refreshFollowPageIndex();
				}

			}.setTitle(R.string.pageTitle).setIcon(R.drawable.menu_more).show();

		}

	};
	/*
	 * ����List<String> ȷ����ͼƬ������ͼƬ�е�λ��
	 */
	private int getPageIndex(List<String> list, String path) {
		int pageIndex = 0;
		for (int i = 0; i < list.size(); i++) {
			if (path.equalsIgnoreCase(list.get(i))) {
				pageIndex = i;
			}
		}
		return pageIndex;
	}
	/*
	 * ���Ƽ��� gestureListener
	 */
	private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
				skipToNextPage();
			} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
				skipToLastPage();
			}
			refreshFollowPageIndex();
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub

			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

	};
	/*
	 * ʵ��Activity�е�dispatchTouchEvent����
	 */
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		gestureDetector.onTouchEvent(ev);
		this.imageView.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
	/*
	 * �м���/�ҷ�ҳ��ť���� skipListener
	 */
	private ImageButton.OnClickListener skipListener = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.lastPage:
				skipToLastPage();
				break;
			case R.id.nextPage:
				skipToNextPage();
				break;
			}
			refreshFollowPageIndex();
			Log.i("��ҳ��sacleWidth", scaleWidth + "");
		}

	};
	/*
	 * �Ϸ�ҳ
	 */
	private void skipToLastPage() {
		if (pageIndex > 0) {
			showImage(--pageIndex);
			showImageInfo(pageIndex);
		} else{
			Utils.showToastMsg(MainActivity.this, R.string.pageFirst);}

	}
	/*
	 * �·�ҳ
	 */
	private void skipToNextPage() {
		if (pageIndex < pathesList.size() - 1) {
			showImage(++pageIndex);
			showImageInfo(pageIndex);
		} else{
			Utils.showToastMsg(MainActivity.this, R.string.pageEnd);
			toRatingActivity();
		}
	}
	/*
	 * �Ŵ�.��С��ť���� zoomListener
	 */
	private ImageButton.OnClickListener zoomListener = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.zoomSmall:
				if (scaleWidth < 0.35) {
					Utils.showToastMsg(MainActivity.this, R.string.tooSmall);
				} else {
					zoomChange(0.8f);
				}
				break;
			case R.id.zoomBig:
				if (scaleWidth > 3.0) {
					Utils.showToastMsg(MainActivity.this, R.string.tooBig);
				} else {
					zoomChange(1.25f);
				}
				break;
			}
		}

		private void zoomChange(float scale) {
			int bitmapWidth = bitmap.getWidth();
			int bitmapHeight = bitmap.getHeight();
			Matrix matrix = new Matrix();
			MainActivity.this.scaleWidth *= scale;
			MainActivity.this.scaleHeight *= scale;
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap zoomedBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth,
					bitmapHeight, matrix, true);
			// if (id == 0) {
			// MainActivity.this.layout1.removeView(imageView);
			// Log.i("�Ƴ�ԭͼ", "��ľ��");
			// } else {
			// MainActivity.this.layout1.removeView(findViewById(id));
			// Log.i("��ͼ", "��ô");
			// }
			// id++;
			// ImageView zoomedImage = new ImageView(MainActivity.this);
			// zoomedImage.setId(id);
			// zoomedImage.setImageBitmap(zoomedBmp);
			// MainActivity.this.layout1.addView(zoomedImage);
			// MainActivity.this.setContentView(layout1);
			MainActivity.this.imageView.setImageBitmap(zoomedBmp);
			Log.i("���ź�sacleWidth", scaleWidth + "");
			Log.i("���ź�sacleWidth", scaleHeight + "");
		}

	};
	/*
	 * ���ϽǷ����ղؼм��� collectionListener  ȥ���
	 */
	private ImageButton.OnClickListener openSDcardListener = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			handler.removeCallbacks(runnable);
			startActivity(new Intent(MainActivity.this, BookShelfActivity.class));
//			Utils.listSerialize(BrowsingHistoryActivity.historiesList,
//					Constants.BROWSEHISTORY);
			//�˳�ʱ���ƺ���
		}

	};
	/*
	 * ���½��˳���ť���� logoutListener
	 */
	private ImageButton.OnClickListener logoutListener = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			handler.removeCallbacks(runnable);
			BaseActivity.exitRightNow(MainActivity.this);
//			Utils.listSerialize(BrowsingHistoryActivity.historiesList,
//					Constants.BROWSEHISTORY);  //ԭ�ȵ��˳�ʱ�����������ʷ��¼�õ�
		}

	};
	// override BaseActivity�е�onKeyDown����,�����ؼ����ȵ���ֹrunnable�߳�
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			handler.removeCallbacks(runnable);
			exitRightNow(MainActivity.this);
		}
		return true;
	}
	/*
	 * ��������pageIndex��Activity����ʾͼƬ ���� public static Bitmap decodeFile (String
	 * pathName, BitmapFactory.Options opts)
	 */
	private void showImage(int pageIndex) {
		// �ڴ��Ż���������ʾ����ͼƬʱ�ڴ����
		String pathName = pathesList.get(pageIndex);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(pathName, options);
		// ��ȡͼƬԭʼ�߿�
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;
		// ��ȡ��Ļ�߿�
		WindowManager windowManager = this.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		// ���ų̶ȳ�ʼ��
		options.inSampleSize = 1;
		// ������Ļ��ͼƬ��С��������ű���
		if (imageWidth > imageHeight) {
			if (imageWidth > screenWidth)
				options.inSampleSize = imageWidth / screenWidth;
		} else {
			if (imageHeight > screenHeight)
				options.inSampleSize = imageHeight / screenHeight;
		}
		// ����һ��������,���������˵�bitmap
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(pathName, options);
		MainActivity.this.imageView.setImageBitmap(bitmap);
		MainActivity.this.imageView.setScaleType(ScaleType.CENTER);
	}
	/*
	 * ���ݵ�ǰͼƬ������pageIndex��ʾ��λ��?/?.����??����Ϣ
	 */
	private void showImageInfo(int pageIndex) {
		if (bookMap != null && bookMap.size() > 0) {
			MainActivity.this.imageName.setText(bookMap.get("bookName"));
			MainActivity.this.pagePosition.setText(pageIndex + 1 + "/"
					+ pathesList.size());
		}
	}
	/*
	 * ÿ�λ�ҳ����Ҫ���µķ���(��ת�Ƕ�.�Ŵ����.��ʷ�ж�)
	 */
	private void refreshFollowPageIndex() {
		shift = 0;
		scaleWidth = 1;
		scaleHeight = 1;
	//	fillHistoriesList();
	}
	// onPause�� ������������¼
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
		bookMap.put("lastReadTime", lastReadTime);
		bookMap.put("lastReadPageIndex", String.valueOf(pageIndex+1));
		bookMap.put("pagesAmount", String.valueOf(pathesList.size()));
		for(int i=0;i<BookRecentlyReadActivity.recentlyreadBooks.size();i++){
			Map<String,String>map=BookRecentlyReadActivity.recentlyreadBooks.get(i);
			if(map.containsValue(bookMap.get("bookId"))){
				BookRecentlyReadActivity.recentlyreadBooks.remove(i);
				Log.i("ִ��delete��", map.get("lastReadTime"));
			}
		}
		BookRecentlyReadActivity.recentlyreadBooks.add(bookMap);
		
		Log.i("onPause", "onPause Success");
	}
	// onStop����ʱ����ͼƬ��Ϣ   �������һ��ͼƬ��·�� ����browse��ť��
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferences sp=getSharedPreferences("lastReadImagePath", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sp.edit();
		editor.putString("lastReadImagePath", pathesList.get(pageIndex));
		editor.commit();
		Utils.serializeBookInfoAsXml(BookRecentlyReadActivity.recentlyreadBooks, Constant.CACHE_PATH, Constant.RECENTLYREAD_PATH);
		Log.i("onStop", "onStop Success---"+pageIndex);
	}
	// to RatingActivity
	private void toRatingActivity(){
		Intent intent=new Intent(MainActivity.this,RatingActivity.class);

		Log.i("lastPageҳ��", pageIndex+"");
		intent.putExtra("bookId", bookMap.get("bookId"));
		Log.i("bookId", bookMap.get("bookId"));
		startActivity(intent);
	}
	/*
	 * ��ʼ������ȡ��widget�ؼ�
	 */
	private void initialize() {
		this.imageView = (ImageView) super.findViewById(R.id.imageView);
		this.openSDcard = (ImageButton) super.findViewById(R.id.openSDcard);
		this.imageName = (TextView) super.findViewById(R.id.imageName);
		this.pagePosition = (TextView) super.findViewById(R.id.pagePosition);
		this.setUp = (ImageButton) super.findViewById(R.id.setUp);
		this.lastPage = (ImageButton) super.findViewById(R.id.lastPage);
		this.nextPage = (ImageButton) super.findViewById(R.id.nextPage);
		this.page = (ImageButton) super.findViewById(R.id.page);
		this.zoomOut = (ImageButton) super.findViewById(R.id.zoomSmall);
		this.zoomIn = (ImageButton) super.findViewById(R.id.zoomBig);
		this.gobackhome = (ImageButton) super.findViewById(R.id.gobackhome);
		this.logout = (ImageButton) super.findViewById(R.id.logout);
		this.layout1 = (RelativeLayout) super.findViewById(R.id.layout1);
		this.layout2 = (RelativeLayout) super.findViewById(R.id.layout2);
		this.layout3 = (RelativeLayout) super.findViewById(R.id.layout3);
		this.layout4 = (RelativeLayout) super.findViewById(R.id.layout4);
	}
}
