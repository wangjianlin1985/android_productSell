package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.ProductInfo;
import com.mobileclient.service.ProductInfoService;
import com.mobileclient.domain.ProductClass;
import com.mobileclient.service.ProductClassService;
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
public class ProductInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明产品条形码控件
	private TextView TV_barCode;
	// 声明所属类别控件
	private TextView TV_classId;
	// 声明产品名称控件
	private TextView TV_productName;
	// 声明生产日期控件
	private TextView TV_madeDate;
	// 声明备注1控件
	private TextView TV_firstBeizhu;
	// 声明备注2控件
	private TextView TV_secondBeizhu;
	// 声明备注3控件
	private TextView TV_thirdBeizhu;
	/* 要保存的产品信息信息 */
	ProductInfo productInfo = new ProductInfo(); 
	/* 产品信息管理业务逻辑层 */
	private ProductInfoService productInfoService = new ProductInfoService();
	private ProductClassService productClassService = new ProductClassService();
	private String barCode;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.productinfo_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看产品信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_barCode = (TextView) findViewById(R.id.TV_barCode);
		TV_classId = (TextView) findViewById(R.id.TV_classId);
		TV_productName = (TextView) findViewById(R.id.TV_productName);
		TV_madeDate = (TextView) findViewById(R.id.TV_madeDate);
		TV_firstBeizhu = (TextView) findViewById(R.id.TV_firstBeizhu);
		TV_secondBeizhu = (TextView) findViewById(R.id.TV_secondBeizhu);
		TV_thirdBeizhu = (TextView) findViewById(R.id.TV_thirdBeizhu);
		Bundle extras = this.getIntent().getExtras();
		barCode = extras.getString("barCode");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ProductInfoDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    productInfo = productInfoService.GetProductInfo(barCode); 
		this.TV_barCode.setText(productInfo.getBarCode());
		ProductClass classId = productClassService.GetProductClass(productInfo.getClassId());
		this.TV_classId.setText(classId.getClassName());
		this.TV_productName.setText(productInfo.getProductName());
		Date madeDate = new Date(productInfo.getMadeDate().getTime());
		String madeDateStr = (madeDate.getYear() + 1900) + "-" + (madeDate.getMonth()+1) + "-" + madeDate.getDate();
		this.TV_madeDate.setText(madeDateStr);
		this.TV_firstBeizhu.setText(productInfo.getFirstBeizhu());
		this.TV_secondBeizhu.setText(productInfo.getSecondBeizhu());
		this.TV_thirdBeizhu.setText(productInfo.getThirdBeizhu());
	} 
}
