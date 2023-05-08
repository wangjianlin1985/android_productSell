package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.SellPerson;
import com.mobileclient.service.SellPersonService;
import com.mobileclient.domain.CountyInfo;
import com.mobileclient.service.CountyInfoService;
import com.mobileclient.domain.TownInfo;
import com.mobileclient.service.TownInfoService;
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
public class SellPersonDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明手机号控件
	private TextView TV_telephone;
	// 声明登录密码控件
	private TextView TV_password;
	// 声明姓名控件
	private TextView TV_name;
	// 声明所在县市控件
	private TextView TV_countyId;
	// 声明所在乡镇控件
	private TextView TV_townId;
	/* 要保存的销售人员信息 */
	SellPerson sellPerson = new SellPerson(); 
	/* 销售人员管理业务逻辑层 */
	private SellPersonService sellPersonService = new SellPersonService();
	private CountyInfoService countyInfoService = new CountyInfoService();
	private TownInfoService townInfoService = new TownInfoService();
	private String telephone;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.sellperson_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看销售人员详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_telephone = (TextView) findViewById(R.id.TV_telephone);
		TV_password = (TextView) findViewById(R.id.TV_password);
		TV_name = (TextView) findViewById(R.id.TV_name);
		TV_countyId = (TextView) findViewById(R.id.TV_countyId);
		TV_townId = (TextView) findViewById(R.id.TV_townId);
		Bundle extras = this.getIntent().getExtras();
		telephone = extras.getString("telephone");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SellPersonDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    sellPerson = sellPersonService.GetSellPerson(telephone); 
		this.TV_telephone.setText(sellPerson.getTelephone());
		this.TV_password.setText(sellPerson.getPassword());
		this.TV_name.setText(sellPerson.getName());
		CountyInfo countyId = countyInfoService.GetCountyInfo(sellPerson.getCountyId());
		this.TV_countyId.setText(countyId.getCityName());
		TownInfo townId = townInfoService.GetTownInfo(sellPerson.getTownId());
		this.TV_townId.setText(townId.getTownName());
	} 
}
