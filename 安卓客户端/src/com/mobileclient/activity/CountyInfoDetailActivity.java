package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.CountyInfo;
import com.mobileclient.service.CountyInfoService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
public class CountyInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明县市编号控件
	private TextView TV_cityId;
	// 声明县市名称控件
	private TextView TV_cityName;
	/* 要保存的县市信息信息 */
	CountyInfo countyInfo = new CountyInfo(); 
	/* 县市信息管理业务逻辑层 */
	private CountyInfoService countyInfoService = new CountyInfoService();
	private int cityId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.countyinfo_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看县市信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_cityId = (TextView) findViewById(R.id.TV_cityId);
		TV_cityName = (TextView) findViewById(R.id.TV_cityName);
		Bundle extras = this.getIntent().getExtras();
		cityId = extras.getInt("cityId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CountyInfoDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    countyInfo = countyInfoService.GetCountyInfo(cityId); 
		this.TV_cityId.setText(countyInfo.getCityId() + "");
		this.TV_cityName.setText(countyInfo.getCityName());
	} 
}
