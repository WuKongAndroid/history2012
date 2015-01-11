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
	public static int pageIndex; // 页面显示的核心跟踪变量
	private ImageView imageView;
	private TextView imageName, pagePosition;
	private ImageButton openSDcard, setUp, lastPage, nextPage, page, zoomOut,
			zoomIn, gobackhome, logout;
	private RelativeLayout layout1, layout2, layout3, layout4;
	private Handler handler;// 是否需要重写handleMessage()方法以备后续使用??
	private GestureDetector gestureDetector;
	private final static int FLING_MIN_DISTANCE = 150;
	private final static int FLING_MIN_VELOCITY = 150;
	private Bitmap bitmap;
	private float scaleWidth = 1, scaleHeight = 1;
	private int shift, temp, id; // 图片连续旋转 shift标记
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
	//	Log.i("Main中的Map", bookMap.get("bookName")+bookMap.get("updateTime"));
		String imagePath = this.getIntent().getStringExtra("imagePath");  //由Intent传过来的信号啊
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

	// Runnable接口的实现类runnable.借用标识符参数temp作
	// 为调整时间的参数(因为int类型是switch的判断参数之一(int.char.Enum))
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
					// 浏览到末页 传信息 线程终止
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
	 * 导航栏隐藏线程，5秒后自动隐藏
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
	 * 主界面图片点击监听器imageTapListener,点击后导航栏出现，5秒后又消失
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
	 * 右上角更多设置setUp按钮监听 setUpListener
	 */
	private ImageButton.OnClickListener setUpListener = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			new DefaultDialog(MainActivity.this, R.array.moreSetUp) {

				@Override
				public void itemsAction(int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case 0: // 定时阅览/对话框弹出前终止原runnable线程 选择该项必是需要改变浏览时间
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
//					case 1: // 浏览书签      网络版取消了书签功能
//						// 如果bookMarks.txt不存在(从未创建过).或存在但书签个数为0(全部删除后)则提示
//						// 不能单用file.length()判断,将空集合序列化后txt文件也有58byte
//						// Returns the length of this file in bytes.
//						// Returns 0 if the file does not exist.
//						if (new File(Constants.BOOKMARKS).length() == 0
//								|| BookMarkActivity.bookMarksList.size() == 0) {
//							Utils.showMsg(MainActivity.this,
//									getString(R.string.nobookmark));
//						} else {
//							startActivity(new Intent(MainActivity.this,
//									BookMarkActivity.class));
//							Log.i("书签记录文件bookMarks.txt的大小", String
//									.valueOf(new File(Constants.BOOKMARKS)
//											.length()));
//						}
//						break;
					case 1: // 漫画旋转
						new SingleChoiceDialog(MainActivity.this,
								R.array.comicRotation) {

							@Override
							public void positiveAction() {
								// TODO Auto-generated method stub
								rotate(shift * 45);
//								Utils.showToastMsg(MainActivity.this, shift * 45
//										+ "度");
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
	 * 左.右旋转45度
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
	 * 书签增添按钮bookmark监听 bookmarkAddListener
	 * 传HashMap到Utils.bookMarksList,序列化至Constants.BOOKMARKS
	 */
//	private ImageButton.OnClickListener bookmarkAddListener = new ImageButton.OnClickListener() {
//
//		@Override
//		public void onClick(View arg0) {
//			// 如果正在自动换页,则弹出书签输入框之前先终止换页线程,否则保存的页面不是想要的!
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
	 * 左下角page按钮监听 pageListener 跳转至第一页，上一页，下一页，最后一页
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
	 * 遍历List<String> 确定该图片在所有图片中的位置
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
	 * 手势监听 gestureListener
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
	 * 实现Activity中的dispatchTouchEvent方法
	 */
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		gestureDetector.onTouchEvent(ev);
		this.imageView.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
	/*
	 * 中间左/右翻页按钮监听 skipListener
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
			Log.i("换页后sacleWidth", scaleWidth + "");
		}

	};
	/*
	 * 上翻页
	 */
	private void skipToLastPage() {
		if (pageIndex > 0) {
			showImage(--pageIndex);
			showImageInfo(pageIndex);
		} else{
			Utils.showToastMsg(MainActivity.this, R.string.pageFirst);}

	}
	/*
	 * 下翻页
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
	 * 放大.缩小按钮监听 zoomListener
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
			// Log.i("移除原图", "有木有");
			// } else {
			// MainActivity.this.layout1.removeView(findViewById(id));
			// Log.i("新图", "是么");
			// }
			// id++;
			// ImageView zoomedImage = new ImageView(MainActivity.this);
			// zoomedImage.setId(id);
			// zoomedImage.setImageBitmap(zoomedBmp);
			// MainActivity.this.layout1.addView(zoomedImage);
			// MainActivity.this.setContentView(layout1);
			MainActivity.this.imageView.setImageBitmap(zoomedBmp);
			Log.i("缩放后sacleWidth", scaleWidth + "");
			Log.i("缩放后sacleWidth", scaleHeight + "");
		}

	};
	/*
	 * 左上角返回收藏夹监听 collectionListener  去书架
	 */
	private ImageButton.OnClickListener openSDcardListener = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			handler.removeCallbacks(runnable);
			startActivity(new Intent(MainActivity.this, BookShelfActivity.class));
//			Utils.listSerialize(BrowsingHistoryActivity.historiesList,
//					Constants.BROWSEHISTORY);
			//退出时做善后工作
		}

	};
	/*
	 * 右下角退出按钮监听 logoutListener
	 */
	private ImageButton.OnClickListener logoutListener = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View v) {
			handler.removeCallbacks(runnable);
			BaseActivity.exitRightNow(MainActivity.this);
//			Utils.listSerialize(BrowsingHistoryActivity.historiesList,
//					Constants.BROWSEHISTORY);  //原先的退出时做保存浏览历史记录用的
		}

	};
	// override BaseActivity中的onKeyDown方法,按返回键后先得终止runnable线程
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			handler.removeCallbacks(runnable);
			exitRightNow(MainActivity.this);
		}
		return true;
	}
	/*
	 * 根据索引pageIndex在Activity中显示图片 调用 public static Bitmap decodeFile (String
	 * pathName, BitmapFactory.Options opts)
	 */
	private void showImage(int pageIndex) {
		// 内存优化，避免显示大量图片时内存溢出
		String pathName = pathesList.get(pageIndex);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(pathName, options);
		// 获取图片原始高宽
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;
		// 获取屏幕高宽
		WindowManager windowManager = this.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		// 缩放程度初始化
		options.inSampleSize = 1;
		// 根据屏幕和图片大小计算出缩放比例
		if (imageWidth > imageHeight) {
			if (imageWidth > screenWidth)
				options.inSampleSize = imageWidth / screenWidth;
		} else {
			if (imageHeight > screenHeight)
				options.inSampleSize = imageHeight / screenHeight;
		}
		// 生成一个有像素,经过缩放了的bitmap
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(pathName, options);
		MainActivity.this.imageView.setImageBitmap(bitmap);
		MainActivity.this.imageView.setScaleType(ScaleType.CENTER);
	}
	/*
	 * 根据当前图片的索引pageIndex显示其位置?/?.名称??等信息
	 */
	private void showImageInfo(int pageIndex) {
		if (bookMap != null && bookMap.size() > 0) {
			MainActivity.this.imageName.setText(bookMap.get("bookName"));
			MainActivity.this.pagePosition.setText(pageIndex + 1 + "/"
					+ pathesList.size());
		}
	}
	/*
	 * 每次换页后需要更新的服务(旋转角度.放大基数.历史判断)
	 */
	private void refreshFollowPageIndex() {
		shift = 0;
		scaleWidth = 1;
		scaleHeight = 1;
	//	fillHistoriesList();
	}
	// onPause中 保存最近浏览记录
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
				Log.i("执行delete了", map.get("lastReadTime"));
			}
		}
		BookRecentlyReadActivity.recentlyreadBooks.add(bookMap);
		
		Log.i("onPause", "onPause Success");
	}
	// onStop方法时保存图片信息   包括最后一张图片的路径 用于browse按钮用
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

		Log.i("lastPage页码", pageIndex+"");
		intent.putExtra("bookId", bookMap.get("bookId"));
		Log.i("bookId", bookMap.get("bookId"));
		startActivity(intent);
	}
	/*
	 * 初始化，获取各widget控件
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
