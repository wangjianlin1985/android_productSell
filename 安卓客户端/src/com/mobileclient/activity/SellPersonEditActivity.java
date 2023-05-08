package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.SellPerson;
import com.mobileclient.service.SellPersonService;
import com.mobileclient.domain.CountyInfo;
import com.mobileclient.service.CountyInfoService;
import com.mobileclient.domain.TownInfo;
import com.mobileclient.service.TownInfoService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class SellPersonEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明手机号TextView
	private TextView TV_telephone;
	// 声明登录密码输入框
	private EditText ET_password;
	// 声明姓名输入框
	private EditText ET_name;
	// 声明所在县市下拉框
	private Spinner spinner_countyId;
	private ArrayAdapter<String> countyId_adapter;
	private static  String[] countyId_ShowText  = null;
	private List<CountyInfo> countyInfoList = null;
	/*所在县市管理业务逻辑层*/
	private CountyInfoService countyInfoService = new CountyInfoService();
	// 声明所在乡镇下拉框
	private Spinner spinner_townId;
	private ArrayAdapter<String> townId_adapter;
	private static  String[] townId_ShowText  = null;
	private List<TownInfo> townInfoList = null;
	/*所在乡镇管理业务逻辑层*/
	private TownInfoService townInfoService = new TownInfoService();
	protected String carmera_path;
	/*要保存的销售人员信息*/
	SellPerson sellPerson = new SellPerson();
	/*销售人员管理业务逻辑层*/
	private SellPersonService sellPersonService = new SellPersonService();

	private String telephone;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.sellperson_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑销售人员信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_telephone = (TextView) findViewById(R.id.TV_telephone);
		ET_password = (EditText) findViewById(R.id.ET_password);
		ET_name = (EditText) findViewById(R.id.ET_name);
		spinner_countyId = (Spinner) findViewById(R.id.Spinner_countyId);
		// 获取所有的所在县市
		try {
			countyInfoList = countyInfoService.QueryCountyInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int countyInfoCount = countyInfoList.size();
		countyId_ShowText = new String[countyInfoCount];
		for(int i=0;i<countyInfoCount;i++) { 
			countyId_ShowText[i] = countyInfoList.get(i).getCityName();
		}
		// 将可选内容与ArrayAdapter连接起来
		countyId_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countyId_ShowText);
		// 设置图书类别下拉列表的风格
		countyId_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_countyId.setAdapter(countyId_adapter);
		// 添加事件Spinner事件监听
		spinner_countyId.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				sellPerson.setCountyId(countyInfoList.get(arg2).getCityId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_countyId.setVisibility(View.VISIBLE);
		spinner_townId = (Spinner) findViewById(R.id.Spinner_townId);
		// 获取所有的所在乡镇
		try {
			townInfoList = townInfoService.QueryTownInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int townInfoCount = townInfoList.size();
		townId_ShowText = new String[townInfoCount];
		for(int i=0;i<townInfoCount;i++) { 
			townId_ShowText[i] = townInfoList.get(i).getTownName();
		}
		// 将可选内容与ArrayAdapter连接起来
		townId_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, townId_ShowText);
		// 设置图书类别下拉列表的风格
		townId_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_townId.setAdapter(townId_adapter);
		// 添加事件Spinner事件监听
		spinner_townId.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				sellPerson.setTownId(townInfoList.get(arg2).getTownId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_townId.setVisibility(View.VISIBLE);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		telephone = extras.getString("telephone");
		/*单击修改销售人员按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取登录密码*/ 
					if(ET_password.getText().toString().equals("")) {
						Toast.makeText(SellPersonEditActivity.this, "登录密码输入不能为空!", Toast.LENGTH_LONG).show();
						ET_password.setFocusable(true);
						ET_password.requestFocus();
						return;	
					}
					sellPerson.setPassword(ET_password.getText().toString());
					/*验证获取姓名*/ 
					if(ET_name.getText().toString().equals("")) {
						Toast.makeText(SellPersonEditActivity.this, "姓名输入不能为空!", Toast.LENGTH_LONG).show();
						ET_name.setFocusable(true);
						ET_name.requestFocus();
						return;	
					}
					sellPerson.setName(ET_name.getText().toString());
					/*调用业务逻辑层上传销售人员信息*/
					SellPersonEditActivity.this.setTitle("正在更新销售人员信息，稍等...");
					String result = sellPersonService.UpdateSellPerson(sellPerson);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		initViewData();
	}

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    sellPerson = sellPersonService.GetSellPerson(telephone);
		this.TV_telephone.setText(telephone);
		this.ET_password.setText(sellPerson.getPassword());
		this.ET_name.setText(sellPerson.getName());
		for (int i = 0; i < countyInfoList.size(); i++) {
			if (sellPerson.getCountyId() == countyInfoList.get(i).getCityId()) {
				this.spinner_countyId.setSelection(i);
				break;
			}
		}
		for (int i = 0; i < townInfoList.size(); i++) {
			if (sellPerson.getTownId() == townInfoList.get(i).getTownId()) {
				this.spinner_townId.setSelection(i);
				break;
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
