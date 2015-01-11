package org.treant.comicreader_netclient.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.adapter.MyGalleryAdapter;
import org.treant.comicreader_netclient.controller.BookManager;
import org.treant.comicreader_netclient.utils.Constant;
import org.treant.comicreader_netclient.utils.Utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class AdviseActivity extends BaseActivity {
	private EditText content, user_name, user_birth, user_email;
	private Button cancel, submit, confirm;
	private Spinner citySpinner, areaSpinner;
	private TextView location;
	private ImageView user_icon, handle;
	private RadioGroup gender_rg;
	private CheckBox cb1, cb2, cb3, cb4, cb5, cb6;
	private SlidingDrawer drawer;
	private int[] locations = new int[] { R.array.xian, R.array.beijing,
			R.array.shanghai, R.array.guangzhou };
	private String city, area ;
	private int imageSwitcherId;
	private StringBuilder sb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_advise);
		allActivity.add(this);
		initWidgets();
		this.cancel.setOnClickListener(this);
		this.submit.setOnClickListener(this);
		this.drawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
				handle.setImageResource(R.drawable.ico_down);
			}

		});
		this.drawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				handle.setImageResource(R.drawable.ico_up);
			}

		});
		super.getMenu();
	}

	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.submit:
			new AlertDialog.Builder(AdviseActivity.this)
					.setTitle(R.string.warmlyAlert)
					.setIcon(R.drawable.dota_cm)
					.setMessage(R.string.promise)
					.setPositiveButton(R.string.willing,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									drawer.animateOpen();
								}

							})
					.setNegativeButton(R.string.maybe_later,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									Utils.showToastMsg(AdviseActivity.this,R.string.thanks);
									Map<String,String>map=new HashMap<String,String>();
									map.put("userInfo.uname", "路人某");
									map.put("userInfo.uadvise", content.getText().toString().trim());
									Utils.prepareCommitPost(Constant.REQUEST_ADVISE_URL, map, "gbk");
									finish();
								}
							}).show();
			break;
		case R.id.confirm:
			String usex = (gender_rg.getCheckedRadioButtonId() == R.id.male) ? this
					.getResources().getString(R.string.male) : this.getResources()
					.getString(R.string.female);
			String uemail=user_email.getText().toString().trim();
			
			if(!uemail.matches("^\\w+@\\w+\\.(com|cn|com.cn|net|org)")){
				uemail="ErrorFormat";
				Utils.showToastMsg(this, R.string.wrongEmail);
			}			
			Map<String,String>map=new HashMap<String,String>();
			map.put("userInfo.uname", user_name.getText().toString().trim());
			map.put("userInfo.usex", usex);
			map.put("userInfo.ubirthday",user_birth.getText().toString().trim() );
			map.put("userInfo.ulocation", location.getText().toString());
			map.put("userInfo.uemail", uemail);
			map.put("userInfo.uhobby", sb.toString());
			map.put("userInfo.uadvise", content.getText().toString().trim());
			if(Utils.prepareCommitPost(Constant.REQUEST_ADVISE_URL, map, "gbk")){
				Utils.showToastMsg(this, R.string.advise_Success);
				drawer.animateClose();
				finish();
			}
			break;
		case R.id.cancel:
			content.setText("");
			finish();
			break;
		}
	}

	private Spinner.OnItemSelectedListener cityListener = new Spinner.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			city = (String) parent.getItemAtPosition(position);
			areaSpinner.setAdapter(new ArrayAdapter<CharSequence>(
					AdviseActivity.this, android.R.layout.simple_spinner_item,
					AdviseActivity.this.getResources().getStringArray(
							locations[position])));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	};

	private Spinner.OnItemSelectedListener areaListener = new Spinner.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			area = (String) parent.getItemAtPosition(position);
			location.setText(city + "-->" + area);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	};

	private OnClickListener birthListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new DatePickerDialog(AdviseActivity.this,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							user_birth.setText(year + "年" + (monthOfYear + 1)
									+ "月" + dayOfMonth + "日");
						}
					}, 1980, 0, 1).show();
		}

	};

	private CompoundButton.OnCheckedChangeListener hobbyListener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				sb.append(buttonView.getText().toString() + "、");
			} else {
				if (sb.toString().contains(buttonView.getText().toString())) {
					sb = new StringBuilder(sb.toString().replace(
							buttonView.getText().toString() + "、", ""));
				}
			}
		}
	};

	private View.OnClickListener chooseIconListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			View galleryView = LayoutInflater.from(AdviseActivity.this)
					.inflate(R.layout.image_gallery, null);
			Gallery gallery = (Gallery) galleryView.findViewById(R.id.gallery);
			final ImageSwitcher imageSwitcher = (ImageSwitcher) galleryView
					.findViewById(R.id.imageSwitcher);

			gallery.setAdapter(new MyGalleryAdapter(AdviseActivity.this, Utils
					.loadDrawableByReflect("dota")));
			imageSwitcher.setFactory(new ViewFactory() {

				@Override
				public View makeView() {
					// TODO Auto-generated method stub
					ImageView imageView = new ImageView(AdviseActivity.this);
					imageView.setBackgroundColor(0xa27e4c8d);
					imageView.setScaleType(ImageView.ScaleType.CENTER);
					imageView
							.setLayoutParams(new ImageSwitcher.LayoutParams(
									LayoutParams.FILL_PARENT,
									LayoutParams.WRAP_CONTENT));
					return imageView;
				}

			});
			imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
					AdviseActivity.this, android.R.anim.fade_in));
			imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
					AdviseActivity.this, android.R.anim.fade_out));
			gallery.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					imageSwitcherId = (Integer) parent
							.getItemAtPosition(position);
					imageSwitcher.setImageResource(imageSwitcherId);
				}

			});
			new AlertDialog.Builder(AdviseActivity.this)
					.setTitle(R.string.chooseIcon)
					.setIcon(R.drawable.flames)
					.setView(galleryView)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									user_icon.setImageResource(imageSwitcherId);

								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();

		}

	};

	private void initWidgets() {
		this.content = (EditText) super.findViewById(R.id.content);
		this.user_name = (EditText) super.findViewById(R.id.user_name);
		this.user_birth = (EditText) super.findViewById(R.id.user_birth);
		this.user_email = (EditText) super.findViewById(R.id.user_email);
		this.cancel = (Button) super.findViewById(R.id.cancel);
		this.submit = (Button) super.findViewById(R.id.submit);
		this.confirm = (Button) super.findViewById(R.id.confirm);
		this.citySpinner = (Spinner) super.findViewById(R.id.citySpinner);
		this.areaSpinner = (Spinner) super.findViewById(R.id.areaSpinner);
		this.location = (TextView) super.findViewById(R.id.location);
		this.user_icon = (ImageView) super.findViewById(R.id.user_icon);
		this.handle = (ImageView) super.findViewById(R.id.handle);
		this.gender_rg = (RadioGroup) super.findViewById(R.id.gender_rg);
		
		this.drawer = (SlidingDrawer) super.findViewById(R.id.drawer);
		this.cancel.setOnClickListener(this);
		this.submit.setOnClickListener(this);
		this.confirm.setOnClickListener(this);
		this.citySpinner.setOnItemSelectedListener(cityListener);
		this.areaSpinner.setOnItemSelectedListener(areaListener);
		this.user_birth.setInputType(InputType.TYPE_NULL);
		this.user_birth.setFocusable(false);
		this.user_birth.setOnClickListener(birthListener);
		this.user_icon.setOnClickListener(chooseIconListener);
		cb1=(CheckBox) super.findViewById(R.id.cb1);
		cb1.setOnCheckedChangeListener(hobbyListener);
		cb2=(CheckBox) super.findViewById(R.id.cb2);
		cb2.setOnCheckedChangeListener(hobbyListener);
		cb3=(CheckBox) super.findViewById(R.id.cb3);
		cb3.setOnCheckedChangeListener(hobbyListener);
		cb4=(CheckBox) super.findViewById(R.id.cb4);
		cb4.setOnCheckedChangeListener(hobbyListener);
		cb5=(CheckBox) super.findViewById(R.id.cb5);
		cb5.setOnCheckedChangeListener(hobbyListener);
		cb6=(CheckBox) super.findViewById(R.id.cb6);
		cb6.setOnCheckedChangeListener(hobbyListener);
		List<Map<String,String>> categoryList=BookManager.getBookCategoryList(Constant.REQUEST_ALLBOOKCATEGORY_URL);
		CheckBox[] cbs=new CheckBox[]{cb1,cb2,cb3,cb4,cb5,cb6};
		for(int i=0;i<categoryList.size();i++){
			String category=categoryList.get(i).get("cname");
			int count=Utils.getBooInfoCountsByAttribute(Constant.BOOKINFO_CACHE_PATH, "bookCategory", category);
			if(category!=null&&count>0){
				cbs[i].setVisibility(View.VISIBLE);
				cbs[i].setText(category);
			}
		}	
		this.sb = new StringBuilder();
	}

}
