package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.ProductInfo;
import com.mobileclient.service.ProductInfoService;
import com.mobileclient.domain.ProductClass;
import com.mobileclient.service.ProductClassService;
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

public class ProductInfoEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明产品条形码TextView
	private TextView TV_barCode;
	// 声明所属类别下拉框
	private Spinner spinner_classId;
	private ArrayAdapter<String> classId_adapter;
	private static  String[] classId_ShowText  = null;
	private List<ProductClass> productClassList = null;
	/*所属类别管理业务逻辑层*/
	private ProductClassService productClassService = new ProductClassService();
	// 声明产品名称输入框
	private EditText ET_productName;
	// 出版生产日期控件
	private DatePicker dp_madeDate;
	// 声明备注1输入框
	private EditText ET_firstBeizhu;
	// 声明备注2输入框
	private EditText ET_secondBeizhu;
	// 声明备注3输入框
	private EditText ET_thirdBeizhu;
	protected String carmera_path;
	/*要保存的产品信息信息*/
	ProductInfo productInfo = new ProductInfo();
	/*产品信息管理业务逻辑层*/
	private ProductInfoService productInfoService = new ProductInfoService();

	private String barCode;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.productinfo_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑产品信息信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_barCode = (TextView) findViewById(R.id.TV_barCode);
		spinner_classId = (Spinner) findViewById(R.id.Spinner_classId);
		// 获取所有的所属类别
		try {
			productClassList = productClassService.QueryProductClass(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int productClassCount = productClassList.size();
		classId_ShowText = new String[productClassCount];
		for(int i=0;i<productClassCount;i++) { 
			classId_ShowText[i] = productClassList.get(i).getClassName();
		}
		// 将可选内容与ArrayAdapter连接起来
		classId_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, classId_ShowText);
		// 设置图书类别下拉列表的风格
		classId_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_classId.setAdapter(classId_adapter);
		// 添加事件Spinner事件监听
		spinner_classId.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				productInfo.setClassId(productClassList.get(arg2).getClassId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_classId.setVisibility(View.VISIBLE);
		ET_productName = (EditText) findViewById(R.id.ET_productName);
		dp_madeDate = (DatePicker)this.findViewById(R.id.dp_madeDate);
		ET_firstBeizhu = (EditText) findViewById(R.id.ET_firstBeizhu);
		ET_secondBeizhu = (EditText) findViewById(R.id.ET_secondBeizhu);
		ET_thirdBeizhu = (EditText) findViewById(R.id.ET_thirdBeizhu);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		barCode = extras.getString("barCode");
		/*单击修改产品信息按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取产品名称*/ 
					if(ET_productName.getText().toString().equals("")) {
						Toast.makeText(ProductInfoEditActivity.this, "产品名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_productName.setFocusable(true);
						ET_productName.requestFocus();
						return;	
					}
					productInfo.setProductName(ET_productName.getText().toString());
					/*获取出版日期*/
					Date madeDate = new Date(dp_madeDate.getYear()-1900,dp_madeDate.getMonth(),dp_madeDate.getDayOfMonth());
					productInfo.setMadeDate(new Timestamp(madeDate.getTime()));
					/*验证获取备注1*/ 
					if(ET_firstBeizhu.getText().toString().equals("")) {
						Toast.makeText(ProductInfoEditActivity.this, "备注1输入不能为空!", Toast.LENGTH_LONG).show();
						ET_firstBeizhu.setFocusable(true);
						ET_firstBeizhu.requestFocus();
						return;	
					}
					productInfo.setFirstBeizhu(ET_firstBeizhu.getText().toString());
					/*验证获取备注2*/ 
					if(ET_secondBeizhu.getText().toString().equals("")) {
						Toast.makeText(ProductInfoEditActivity.this, "备注2输入不能为空!", Toast.LENGTH_LONG).show();
						ET_secondBeizhu.setFocusable(true);
						ET_secondBeizhu.requestFocus();
						return;	
					}
					productInfo.setSecondBeizhu(ET_secondBeizhu.getText().toString());
					/*验证获取备注3*/ 
					if(ET_thirdBeizhu.getText().toString().equals("")) {
						Toast.makeText(ProductInfoEditActivity.this, "备注3输入不能为空!", Toast.LENGTH_LONG).show();
						ET_thirdBeizhu.setFocusable(true);
						ET_thirdBeizhu.requestFocus();
						return;	
					}
					productInfo.setThirdBeizhu(ET_thirdBeizhu.getText().toString());
					/*调用业务逻辑层上传产品信息信息*/
					ProductInfoEditActivity.this.setTitle("正在更新产品信息信息，稍等...");
					String result = productInfoService.UpdateProductInfo(productInfo);
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
	    productInfo = productInfoService.GetProductInfo(barCode);
		this.TV_barCode.setText(barCode);
		for (int i = 0; i < productClassList.size(); i++) {
			if (productInfo.getClassId() == productClassList.get(i).getClassId()) {
				this.spinner_classId.setSelection(i);
				break;
			}
		}
		this.ET_productName.setText(productInfo.getProductName());
		Date madeDate = new Date(productInfo.getMadeDate().getTime());
		this.dp_madeDate.init(madeDate.getYear() + 1900,madeDate.getMonth(), madeDate.getDate(), null);
		this.ET_firstBeizhu.setText(productInfo.getFirstBeizhu());
		this.ET_secondBeizhu.setText(productInfo.getSecondBeizhu());
		this.ET_thirdBeizhu.setText(productInfo.getThirdBeizhu());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
