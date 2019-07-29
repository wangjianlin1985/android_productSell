package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.TownInfo;
import com.mobileclient.domain.CountyInfo;
import com.mobileclient.service.CountyInfoService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.ImageView;
import android.widget.TextView;
public class TownInfoQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明所在县市下拉框
	private Spinner spinner_countyId;
	private ArrayAdapter<String> countyId_adapter;
	private static  String[] countyId_ShowText  = null;
	private List<CountyInfo> countyInfoList = null; 
	/*县市信息管理业务逻辑层*/
	private CountyInfoService countyInfoService = new CountyInfoService();
	// 声明乡镇名称输入框
	private EditText ET_townName;
	/*查询过滤条件保存到这个对象中*/
	private TownInfo queryConditionTownInfo = new TownInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.towninfo_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置乡镇信息查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_countyId = (Spinner) findViewById(R.id.Spinner_countyId);
		// 获取所有的县市信息
		try {
			countyInfoList = countyInfoService.QueryCountyInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int countyInfoCount = countyInfoList.size();
		countyId_ShowText = new String[countyInfoCount+1];
		countyId_ShowText[0] = "不限制";
		for(int i=1;i<=countyInfoCount;i++) { 
			countyId_ShowText[i] = countyInfoList.get(i-1).getCityName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		countyId_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countyId_ShowText);
		// 设置所在县市下拉列表的风格
		countyId_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_countyId.setAdapter(countyId_adapter);
		// 添加事件Spinner事件监听
		spinner_countyId.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionTownInfo.setCountyId(countyInfoList.get(arg2-1).getCityId()); 
				else
					queryConditionTownInfo.setCountyId(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_countyId.setVisibility(View.VISIBLE);
		ET_townName = (EditText) findViewById(R.id.ET_townName);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionTownInfo.setTownName(ET_townName.getText().toString());
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionTownInfo", queryConditionTownInfo);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
