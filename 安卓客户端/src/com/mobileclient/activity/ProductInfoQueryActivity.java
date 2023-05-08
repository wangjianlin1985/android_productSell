package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.ProductInfo;
import com.mobileclient.domain.ProductClass;
import com.mobileclient.service.ProductClassService;

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
public class ProductInfoQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明产品条形码输入框
	private EditText ET_barCode;
	// 声明所属类别下拉框
	private Spinner spinner_classId;
	private ArrayAdapter<String> classId_adapter;
	private static  String[] classId_ShowText  = null;
	private List<ProductClass> productClassList = null; 
	/*产品类别管理业务逻辑层*/
	private ProductClassService productClassService = new ProductClassService();
	// 声明产品名称输入框
	private EditText ET_productName;
	// 生产日期控件
	private DatePicker dp_madeDate;
	private CheckBox cb_madeDate;
	/*查询过滤条件保存到这个对象中*/
	private ProductInfo queryConditionProductInfo = new ProductInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.productinfo_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置产品信息查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_barCode = (EditText) findViewById(R.id.ET_barCode);
		spinner_classId = (Spinner) findViewById(R.id.Spinner_classId);
		// 获取所有的产品类别
		try {
			productClassList = productClassService.QueryProductClass(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int productClassCount = productClassList.size();
		classId_ShowText = new String[productClassCount+1];
		classId_ShowText[0] = "不限制";
		for(int i=1;i<=productClassCount;i++) { 
			classId_ShowText[i] = productClassList.get(i-1).getClassName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		classId_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, classId_ShowText);
		// 设置所属类别下拉列表的风格
		classId_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_classId.setAdapter(classId_adapter);
		// 添加事件Spinner事件监听
		spinner_classId.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionProductInfo.setClassId(productClassList.get(arg2-1).getClassId()); 
				else
					queryConditionProductInfo.setClassId(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_classId.setVisibility(View.VISIBLE);
		ET_productName = (EditText) findViewById(R.id.ET_productName);
		dp_madeDate = (DatePicker) findViewById(R.id.dp_madeDate);
		cb_madeDate = (CheckBox) findViewById(R.id.cb_madeDate);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionProductInfo.setBarCode(ET_barCode.getText().toString());
					queryConditionProductInfo.setProductName(ET_productName.getText().toString());
					if(cb_madeDate.isChecked()) {
						/*获取生产日期*/
						Date madeDate = new Date(dp_madeDate.getYear()-1900,dp_madeDate.getMonth(),dp_madeDate.getDayOfMonth());
						queryConditionProductInfo.setMadeDate(new Timestamp(madeDate.getTime()));
					} else {
						queryConditionProductInfo.setMadeDate(null);
					} 
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionProductInfo", queryConditionProductInfo);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
