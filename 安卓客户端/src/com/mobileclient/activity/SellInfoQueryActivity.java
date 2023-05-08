package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.SellInfo;
import com.mobileclient.domain.ProductInfo;
import com.mobileclient.service.ProductInfoService;
import com.mobileclient.domain.SellPerson;
import com.mobileclient.service.SellPersonService;

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
public class SellInfoQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明产品名称下拉框
	private Spinner spinner_productBarCode;
	private ArrayAdapter<String> productBarCode_adapter;
	private static  String[] productBarCode_ShowText  = null;
	private List<ProductInfo> productInfoList = null; 
	/*产品信息管理业务逻辑层*/
	private ProductInfoService productInfoService = new ProductInfoService();
	// 声明销售人员下拉框
	private Spinner spinner_sellPerson;
	private ArrayAdapter<String> sellPerson_adapter;
	private static  String[] sellPerson_ShowText  = null;
	private List<SellPerson> sellPersonList = null; 
	/*销售人员管理业务逻辑层*/
	private SellPersonService sellPersonService = new SellPersonService();
	// 销售日期控件
	private DatePicker dp_sellDate;
	private CheckBox cb_sellDate;
	/*查询过滤条件保存到这个对象中*/
	private SellInfo queryConditionSellInfo = new SellInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.sellinfo_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置销售信息查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_productBarCode = (Spinner) findViewById(R.id.Spinner_productBarCode);
		// 获取所有的产品信息
		try {
			productInfoList = productInfoService.QueryProductInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int productInfoCount = productInfoList.size();
		productBarCode_ShowText = new String[productInfoCount+1];
		productBarCode_ShowText[0] = "不限制";
		for(int i=1;i<=productInfoCount;i++) { 
			productBarCode_ShowText[i] = productInfoList.get(i-1).getProductName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		productBarCode_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, productBarCode_ShowText);
		// 设置产品名称下拉列表的风格
		productBarCode_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_productBarCode.setAdapter(productBarCode_adapter);
		// 添加事件Spinner事件监听
		spinner_productBarCode.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionSellInfo.setProductBarCode(productInfoList.get(arg2-1).getBarCode()); 
				else
					queryConditionSellInfo.setProductBarCode("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_productBarCode.setVisibility(View.VISIBLE);
		spinner_sellPerson = (Spinner) findViewById(R.id.Spinner_sellPerson);
		// 获取所有的销售人员
		try {
			sellPersonList = sellPersonService.QuerySellPerson(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int sellPersonCount = sellPersonList.size();
		sellPerson_ShowText = new String[sellPersonCount+1];
		sellPerson_ShowText[0] = "不限制";
		for(int i=1;i<=sellPersonCount;i++) { 
			sellPerson_ShowText[i] = sellPersonList.get(i-1).getName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		sellPerson_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sellPerson_ShowText);
		// 设置销售人员下拉列表的风格
		sellPerson_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_sellPerson.setAdapter(sellPerson_adapter);
		// 添加事件Spinner事件监听
		spinner_sellPerson.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionSellInfo.setSellPerson(sellPersonList.get(arg2-1).getTelephone()); 
				else
					queryConditionSellInfo.setSellPerson("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_sellPerson.setVisibility(View.VISIBLE);
		dp_sellDate = (DatePicker) findViewById(R.id.dp_sellDate);
		cb_sellDate = (CheckBox) findViewById(R.id.cb_sellDate);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					if(cb_sellDate.isChecked()) {
						/*获取销售日期*/
						Date sellDate = new Date(dp_sellDate.getYear()-1900,dp_sellDate.getMonth(),dp_sellDate.getDayOfMonth());
						queryConditionSellInfo.setSellDate(new Timestamp(sellDate.getTime()));
					} else {
						queryConditionSellInfo.setSellDate(null);
					} 
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionSellInfo", queryConditionSellInfo);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
