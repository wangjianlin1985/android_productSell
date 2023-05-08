package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.TownInfo;
import com.mobileclient.service.TownInfoService;
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
public class TownInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明乡镇编号控件
	private TextView TV_townId;
	// 声明所在县市控件
	private TextView TV_countyId;
	// 声明乡镇名称控件
	private TextView TV_townName;
	/* 要保存的乡镇信息信息 */
	TownInfo townInfo = new TownInfo(); 
	/* 乡镇信息管理业务逻辑层 */
	private TownInfoService townInfoService = new TownInfoService();
	private CountyInfoService countyInfoService = new CountyInfoService();
	private int townId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.towninfo_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看乡镇信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_townId = (TextView) findViewById(R.id.TV_townId);
		TV_countyId = (TextView) findViewById(R.id.TV_countyId);
		TV_townName = (TextView) findViewById(R.id.TV_townName);
		Bundle extras = this.getIntent().getExtras();
		townId = extras.getInt("townId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TownInfoDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    townInfo = townInfoService.GetTownInfo(townId); 
		this.TV_townId.setText(townInfo.getTownId() + "");
		CountyInfo countyId = countyInfoService.GetCountyInfo(townInfo.getCountyId());
		this.TV_countyId.setText(countyId.getCityName());
		this.TV_townName.setText(townInfo.getTownName());
	} 
}
