package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.SellInfo;
import com.mobileclient.service.SellInfoService;
import com.mobileclient.domain.ProductInfo;
import com.mobileclient.service.ProductInfoService;
import com.mobileclient.domain.SellPerson;
import com.mobileclient.service.SellPersonService;
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
public class SellInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明销售编号控件
	private TextView TV_sellId;
	// 声明产品名称控件
	private TextView TV_productBarCode;
	// 声明销售人员控件
	private TextView TV_sellPerson;
	// 声明销售数量控件
	private TextView TV_sellCount;
	// 声明销售日期控件
	private TextView TV_sellDate;
	// 声明备注1控件
	private TextView TV_firstBeizhu;
	// 声明备注2控件
	private TextView TV_secondBeizhu;
	// 声明备注3控件
	private TextView TV_thirdBeizhu;
	/* 要保存的销售信息信息 */
	SellInfo sellInfo = new SellInfo(); 
	/* 销售信息管理业务逻辑层 */
	private SellInfoService sellInfoService = new SellInfoService();
	private ProductInfoService productInfoService = new ProductInfoService();
	private SellPersonService sellPersonService = new SellPersonService();
	private int sellId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.sellinfo_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看销售信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_sellId = (TextView) findViewById(R.id.TV_sellId);
		TV_productBarCode = (TextView) findViewById(R.id.TV_productBarCode);
		TV_sellPerson = (TextView) findViewById(R.id.TV_sellPerson);
		TV_sellCount = (TextView) findViewById(R.id.TV_sellCount);
		TV_sellDate = (TextView) findViewById(R.id.TV_sellDate);
		TV_firstBeizhu = (TextView) findViewById(R.id.TV_firstBeizhu);
		TV_secondBeizhu = (TextView) findViewById(R.id.TV_secondBeizhu);
		TV_thirdBeizhu = (TextView) findViewById(R.id.TV_thirdBeizhu);
		Bundle extras = this.getIntent().getExtras();
		sellId = extras.getInt("sellId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SellInfoDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    sellInfo = sellInfoService.GetSellInfo(sellId); 
		this.TV_sellId.setText(sellInfo.getSellId() + "");
		ProductInfo productBarCode = productInfoService.GetProductInfo(sellInfo.getProductBarCode());
		this.TV_productBarCode.setText(productBarCode.getProductName());
		SellPerson sellPerson = sellPersonService.GetSellPerson(sellInfo.getSellPerson());
		this.TV_sellPerson.setText(sellPerson.getName());
		this.TV_sellCount.setText(sellInfo.getSellCount() + "");
		Date sellDate = new Date(sellInfo.getSellDate().getTime());
		String sellDateStr = (sellDate.getYear() + 1900) + "-" + (sellDate.getMonth()+1) + "-" + sellDate.getDate();
		this.TV_sellDate.setText(sellDateStr);
		this.TV_firstBeizhu.setText(sellInfo.getFirstBeizhu());
		this.TV_secondBeizhu.setText(sellInfo.getSecondBeizhu());
		this.TV_thirdBeizhu.setText(sellInfo.getThirdBeizhu());
	} 
}
