package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.TownInfo;
import com.mobileclient.service.TownInfoService;
import com.mobileclient.domain.CountyInfo;
import com.mobileclient.service.CountyInfoService;
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

public class TownInfoEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明乡镇编号TextView
	private TextView TV_townId;
	// 声明所在县市下拉框
	private Spinner spinner_countyId;
	private ArrayAdapter<String> countyId_adapter;
	private static  String[] countyId_ShowText  = null;
	private List<CountyInfo> countyInfoList = null;
	/*所在县市管理业务逻辑层*/
	private CountyInfoService countyInfoService = new CountyInfoService();
	// 声明乡镇名称输入框
	private EditText ET_townName;
	protected String carmera_path;
	/*要保存的乡镇信息信息*/
	TownInfo townInfo = new TownInfo();
	/*乡镇信息管理业务逻辑层*/
	private TownInfoService townInfoService = new TownInfoService();

	private int townId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.towninfo_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑乡镇信息信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_townId = (TextView) findViewById(R.id.TV_townId);
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
				townInfo.setCountyId(countyInfoList.get(arg2).getCityId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_countyId.setVisibility(View.VISIBLE);
		ET_townName = (EditText) findViewById(R.id.ET_townName);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		townId = extras.getInt("townId");
		/*单击修改乡镇信息按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取乡镇名称*/ 
					if(ET_townName.getText().toString().equals("")) {
						Toast.makeText(TownInfoEditActivity.this, "乡镇名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_townName.setFocusable(true);
						ET_townName.requestFocus();
						return;	
					}
					townInfo.setTownName(ET_townName.getText().toString());
					/*调用业务逻辑层上传乡镇信息信息*/
					TownInfoEditActivity.this.setTitle("正在更新乡镇信息信息，稍等...");
					String result = townInfoService.UpdateTownInfo(townInfo);
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
	    townInfo = townInfoService.GetTownInfo(townId);
		this.TV_townId.setText(townId+"");
		for (int i = 0; i < countyInfoList.size(); i++) {
			if (townInfo.getCountyId() == countyInfoList.get(i).getCityId()) {
				this.spinner_countyId.setSelection(i);
				break;
			}
		}
		this.ET_townName.setText(townInfo.getTownName());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
