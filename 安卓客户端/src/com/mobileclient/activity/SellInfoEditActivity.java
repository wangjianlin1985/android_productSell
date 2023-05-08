package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.SellInfo;
import com.mobileclient.service.SellInfoService;
import com.mobileclient.domain.ProductInfo;
import com.mobileclient.service.ProductInfoService;
import com.mobileclient.domain.SellPerson;
import com.mobileclient.service.SellPersonService;
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

public class SellInfoEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明销售编号TextView
	private TextView TV_sellId;
	// 声明产品名称下拉框
	private Spinner spinner_productBarCode;
	private ArrayAdapter<String> productBarCode_adapter;
	private static  String[] productBarCode_ShowText  = null;
	private List<ProductInfo> productInfoList = null;
	/*产品名称管理业务逻辑层*/
	private ProductInfoService productInfoService = new ProductInfoService();
	// 声明销售人员下拉框
	private Spinner spinner_sellPerson;
	private ArrayAdapter<String> sellPerson_adapter;
	private static  String[] sellPerson_ShowText  = null;
	private List<SellPerson> sellPersonList = null;
	/*销售人员管理业务逻辑层*/
	private SellPersonService sellPersonService = new SellPersonService();
	// 声明销售数量输入框
	private EditText ET_sellCount;
	// 出版销售日期控件
	private DatePicker dp_sellDate;
	// 声明备注1输入框
	private EditText ET_firstBeizhu;
	// 声明备注2输入框
	private EditText ET_secondBeizhu;
	// 声明备注3输入框
	private EditText ET_thirdBeizhu;
	protected String carmera_path;
	/*要保存的销售信息信息*/
	SellInfo sellInfo = new SellInfo();
	/*销售信息管理业务逻辑层*/
	private SellInfoService sellInfoService = new SellInfoService();

	private int sellId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.sellinfo_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑销售信息信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_sellId = (TextView) findViewById(R.id.TV_sellId);
		spinner_productBarCode = (Spinner) findViewById(R.id.Spinner_productBarCode);
		// 获取所有的产品名称
		try {
			productInfoList = productInfoService.QueryProductInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int productInfoCount = productInfoList.size();
		productBarCode_ShowText = new String[productInfoCount];
		for(int i=0;i<productInfoCount;i++) { 
			productBarCode_ShowText[i] = productInfoList.get(i).getProductName();
		}
		// 将可选内容与ArrayAdapter连接起来
		productBarCode_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, productBarCode_ShowText);
		// 设置图书类别下拉列表的风格
		productBarCode_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_productBarCode.setAdapter(productBarCode_adapter);
		// 添加事件Spinner事件监听
		spinner_productBarCode.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				sellInfo.setProductBarCode(productInfoList.get(arg2).getBarCode()); 
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
		sellPerson_ShowText = new String[sellPersonCount];
		for(int i=0;i<sellPersonCount;i++) { 
			sellPerson_ShowText[i] = sellPersonList.get(i).getName();
		}
		// 将可选内容与ArrayAdapter连接起来
		sellPerson_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sellPerson_ShowText);
		// 设置图书类别下拉列表的风格
		sellPerson_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_sellPerson.setAdapter(sellPerson_adapter);
		// 添加事件Spinner事件监听
		spinner_sellPerson.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				sellInfo.setSellPerson(sellPersonList.get(arg2).getTelephone()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_sellPerson.setVisibility(View.VISIBLE);
		ET_sellCount = (EditText) findViewById(R.id.ET_sellCount);
		dp_sellDate = (DatePicker)this.findViewById(R.id.dp_sellDate);
		ET_firstBeizhu = (EditText) findViewById(R.id.ET_firstBeizhu);
		ET_secondBeizhu = (EditText) findViewById(R.id.ET_secondBeizhu);
		ET_thirdBeizhu = (EditText) findViewById(R.id.ET_thirdBeizhu);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		sellId = extras.getInt("sellId");
		/*单击修改销售信息按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取销售数量*/ 
					if(ET_sellCount.getText().toString().equals("")) {
						Toast.makeText(SellInfoEditActivity.this, "销售数量输入不能为空!", Toast.LENGTH_LONG).show();
						ET_sellCount.setFocusable(true);
						ET_sellCount.requestFocus();
						return;	
					}
					sellInfo.setSellCount(Integer.parseInt(ET_sellCount.getText().toString()));
					/*获取出版日期*/
					Date sellDate = new Date(dp_sellDate.getYear()-1900,dp_sellDate.getMonth(),dp_sellDate.getDayOfMonth());
					sellInfo.setSellDate(new Timestamp(sellDate.getTime()));
					/*验证获取备注1*/ 
					if(ET_firstBeizhu.getText().toString().equals("")) {
						Toast.makeText(SellInfoEditActivity.this, "备注1输入不能为空!", Toast.LENGTH_LONG).show();
						ET_firstBeizhu.setFocusable(true);
						ET_firstBeizhu.requestFocus();
						return;	
					}
					sellInfo.setFirstBeizhu(ET_firstBeizhu.getText().toString());
					/*验证获取备注2*/ 
					if(ET_secondBeizhu.getText().toString().equals("")) {
						Toast.makeText(SellInfoEditActivity.this, "备注2输入不能为空!", Toast.LENGTH_LONG).show();
						ET_secondBeizhu.setFocusable(true);
						ET_secondBeizhu.requestFocus();
						return;	
					}
					sellInfo.setSecondBeizhu(ET_secondBeizhu.getText().toString());
					/*验证获取备注3*/ 
					if(ET_thirdBeizhu.getText().toString().equals("")) {
						Toast.makeText(SellInfoEditActivity.this, "备注3输入不能为空!", Toast.LENGTH_LONG).show();
						ET_thirdBeizhu.setFocusable(true);
						ET_thirdBeizhu.requestFocus();
						return;	
					}
					sellInfo.setThirdBeizhu(ET_thirdBeizhu.getText().toString());
					/*调用业务逻辑层上传销售信息信息*/
					SellInfoEditActivity.this.setTitle("正在更新销售信息信息，稍等...");
					String result = sellInfoService.UpdateSellInfo(sellInfo);
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
	    sellInfo = sellInfoService.GetSellInfo(sellId);
		this.TV_sellId.setText(sellId+"");
		for (int i = 0; i < productInfoList.size(); i++) {
			if (sellInfo.getProductBarCode().equals(productInfoList.get(i).getBarCode())) {
				this.spinner_productBarCode.setSelection(i);
				break;
			}
		}
		for (int i = 0; i < sellPersonList.size(); i++) {
			if (sellInfo.getSellPerson().equals(sellPersonList.get(i).getTelephone())) {
				this.spinner_sellPerson.setSelection(i);
				break;
			}
		}
		this.ET_sellCount.setText(sellInfo.getSellCount() + "");
		Date sellDate = new Date(sellInfo.getSellDate().getTime());
		this.dp_sellDate.init(sellDate.getYear() + 1900,sellDate.getMonth(), sellDate.getDate(), null);
		this.ET_firstBeizhu.setText(sellInfo.getFirstBeizhu());
		this.ET_secondBeizhu.setText(sellInfo.getSecondBeizhu());
		this.ET_thirdBeizhu.setText(sellInfo.getThirdBeizhu());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
