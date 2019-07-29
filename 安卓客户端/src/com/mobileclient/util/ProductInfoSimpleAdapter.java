package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.ProductClassService;
import com.mobileclient.activity.R;
import com.mobileclient.imgCache.ImageLoadListener;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;
import android.content.Context;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.ListView;
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class ProductInfoSimpleAdapter extends SimpleAdapter { 
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    private ListView mListView;
    //图片异步缓存加载类,带内存缓存和文件缓存
    private SyncImageLoader syncImageLoader;

    public ProductInfoSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
        super(context, data, resource, from, to); 
        mTo = to; 
        mFrom = from; 
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
        mListView = listView; 
        syncImageLoader = SyncImageLoader.getInstance();
        ListViewOnScrollListener onScrollListener = new ListViewOnScrollListener(syncImageLoader,listView,getCount());
        mListView.setOnScrollListener(onScrollListener);
    } 

  public View getView(int position, View convertView, ViewGroup parent) { 
	  ViewHolder holder = null;
	  ///*第一次装载这个view时=null,就新建一个调用inflate渲染一个view*/
	  if (convertView == null) convertView = mInflater.inflate(R.layout.productinfo_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_barCode = (TextView)convertView.findViewById(R.id.tv_barCode);
	  holder.tv_classId = (TextView)convertView.findViewById(R.id.tv_classId);
	  holder.tv_productName = (TextView)convertView.findViewById(R.id.tv_productName);
	  holder.tv_madeDate = (TextView)convertView.findViewById(R.id.tv_madeDate);
	  /*设置各个控件的展示内容*/
	  holder.tv_barCode.setText("产品条形码：" + mData.get(position).get("barCode").toString());
	  holder.tv_classId.setText("所属类别：" + (new ProductClassService()).GetProductClass(Integer.parseInt(mData.get(position).get("classId").toString())).getClassName());
	  holder.tv_productName.setText("产品名称：" + mData.get(position).get("productName").toString());
	  try {holder.tv_madeDate.setText("生产日期：" + mData.get(position).get("madeDate").toString().substring(0, 10));} catch(Exception ex){}
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_barCode;
    	TextView tv_classId;
    	TextView tv_productName;
    	TextView tv_madeDate;
    }
} 
