package com.jikuibu.app.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jikuibu.app.bean.Directory;
import com.jikuibu.app.bean.DirectoryOutlineList;
import com.jikuibu.app.bean.KuiBuDictList;
import com.jikuibu.app.ui.Adapter.KuiBuDictListAdapter;
import com.jikuibu.app.ui.Adapter.TreeViewAdapter;
import com.jikuibu.app.utils.*;
import com.jikuibu.app.AppContext;
import com.jikuibu.app.AppException;
import com.jikuibu.app.R;


public class MainActivity extends Activity {
	protected static final String TAG = "MainActivity";
	
	private Context context;
	private AppContext appContext;
	LayoutInflater inflater;
	private long exitTime = 0; 
	
	//Header controls.
	private ProgressBar head_progress;
	private TextView head_TextView;
	
	private DirectoryOutlineList dirList = new DirectoryOutlineList();
	private KuiBuDictList kuibuList = new KuiBuDictList();
	TreeViewAdapter treeViewAdapter;
	KuiBuDictListAdapter kuibuAdapter;
	
	private PullToRefreshListView treeview;
	private Handler directoryTreeHandler;
	
	private PullToRefreshListView directoryDetail;
	private Handler  dirDetailHandler;
	
	private PullToRefreshListView kuibuListView;
	
	//Footer controls.
	private RadioButton directCatalog;
	private RadioButton directoryDetailLists;
	private RadioButton userCenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = MainActivity.this;
		appContext = (AppContext)getApplication();
		FileUtils.setGlobalContext(appContext);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
		initHeaderView();
		initDirectoryTreeView();
		initKuibuDictListView();
		initDirectoryDetailListView();
		initFooterBar();
		
		initDirectoryTreeViewData();//This is the first show screen.
		initKuibuDictListViewData();

	}
	
	private void initKuibuDictListView() {
		kuibuListView = (PullToRefreshListView)findViewById(R.id.directorydetail);
		kuibuAdapter = new KuiBuDictListAdapter(this, kuibuList, R.layout.kuibulistitem);
		kuibuListView.setAdapter(kuibuAdapter);
		kuibuListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
			{
				
			}
		});
	}
	
	private void initKuibuDictListViewData()
	{
		dirDetailHandler= new Handler(){
	    @Override
	    public void handleMessage(Message msg) 
	    {
	    
	    }
		};
	    
	}

	private void initHeaderView()
	{
		head_TextView = (TextView)findViewById(R.id.main_head_title);
		head_progress = (ProgressBar)findViewById(R.id.main_head_progress);
		head_progress.setVisibility(View.VISIBLE);
		head_TextView.setText("正在加载目录，请稍后...");
	}
	
	private void initDirectoryTreeViewData()
	{
		directoryTreeHandler = new Handler(){
		    @Override
		    public void handleMessage(Message msg) {
		        super.handleMessage(msg);
		        switch(msg.what)
		        {
		        case 0:
		    		head_progress.setVisibility(View.INVISIBLE);
		    		head_TextView.setText("分类列表");
		    		DirectoryOutlineList tempDirOutlineList = (DirectoryOutlineList)msg.obj;
		    		
		    		dirList.setDirectoryList(tempDirOutlineList.getDirectoryList());
		    		head_progress.setVisibility(View.INVISIBLE);
		    		treeViewAdapter.notifyDataSetChanged();
		    		treeview.onRefreshComplete();
		    		break;
		        case -1:
		        	AppException e = (AppException)msg.obj;
		        	
		        	if(AppException.TYPE_HTTP_CODE == e.getType())
		        		UIHelper.ToastMessage(context, "Network issue with status code " + e.getCode());
		        	treeview.onRefreshComplete();
		        	head_progress.setVisibility(View.INVISIBLE);
		        	UIHelper.ToastMessage(context, "Network error, get the directory list failed");
		        }
		    }
		};
		
		//Other handler.
		
		if(dirList.isEmpty())
		{
			loadDirectoryTreeViewData(0, directoryTreeHandler, UIHelper.LISTVIEW_ACTION_INIT);
		}
		
	}
	
	
	
	
	private void initDirectoryTreeView()
	{
		treeViewAdapter = new TreeViewAdapter(this, dirList, R.layout.treeview_item);
		treeview = (PullToRefreshListView) findViewById(R.id.treeview);
		treeview.setAdapter(treeViewAdapter);
		
		treeview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				loadDirectoryTreeViewData(0, directoryTreeHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
			}

		});
		
		treeview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				//arg1.setBackgroundColor(color.background_dark);
				//http://blog.chengbo.net/2012/03/09/onitemclick-return-wrong-position-when-listview-has-headerview.html
				//Directory dir = (Directory) getItem(position);
				Directory dir = (Directory) arg0.getAdapter().getItem(position);
				if(dir == null)
				{
					Log.e(TAG, "we clicked the header view, do nothing.");
					return;
				}
				Log.e(TAG, "position " + position + " is clicked with direcotry "+ dir.getId() + " " + dir.getContentText());

				if (!dir.isHasChildren()) 
				{
		           
					
					directCatalog.setChecked(false);
					directoryDetailLists.setChecked(true);
					userCenter.setChecked(false);
					
					try {
						KuiBuDictList kuiDictList = appContext.getKuiBuDictList("Type", dir.getActionid(), false);
						kuibuList.setKuiBulist(kuiDictList.getKuiBulist());
						Log.e(TAG, "We get KuiBuDictList from cached file : " + kuibuList);
						kuibuAdapter.notifyDataSetChanged();
					} catch (AppException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					 //Should Start another activity to show content lists.
					treeview.setVisibility(View.GONE);
					directoryDetail.setVisibility(View.VISIBLE);
					
					return;
				}
				
				if (dir.isExpanded()) 
				{
					dir.setExpanded(false);
					ArrayList<Directory> elementsToDel = new ArrayList<Directory>();
					//Fix the click error due to introduce of PullRefreshView-addHeaderView.
					for (int i = position; i < dirList.getDisplayDirectoryList().size(); i++) 
					{  
						if (dir.getLevel() >= dirList.getDisplayDirectoryList().get(i).getLevel())
							break;
						elementsToDel.add(dirList.getDisplayDirectoryList().get(i));
					}
					dirList.getDisplayDirectoryList().removeAll(elementsToDel);

				} else {
					dir.setExpanded(true);
					int i = 1;
					for (Directory e : dirList.getDirectoryList()) {
						if (e.getParendId() == dir.getId()) {
							e.setExpanded(false);
							//Fix the click error due to introduce of PullRefreshView-addHeaderView.
							dirList.getDisplayDirectoryList().add(position -1 + i, e);
							i ++;
						}
					}
					
				}
				treeViewAdapter.notifyDataSetChanged();
			}
		});
	}
	
	private void initDirectoryDetailListView()
	{
		directoryDetail = (PullToRefreshListView) findViewById(R.id.directorydetail);
		directoryDetail.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				loadDirectoryTreeViewData(0, directoryTreeHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
			}

		});
	}
	
	private void initFooterBar()
	{
		directCatalog = (RadioButton) findViewById(R.id.main_footbar_news);
		directoryDetailLists = (RadioButton) findViewById(R.id.main_footbar_question);
		userCenter = (RadioButton) findViewById(R.id.main_footbar_active);
		directCatalog.setChecked(true);
		
		directCatalog.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				directoryDetail.setVisibility(View.GONE);
				treeview.setVisibility(View.VISIBLE);
				
				directCatalog.setChecked(true);
				directoryDetailLists.setChecked(false);
				userCenter.setChecked(false);
			}
		});
		
		directoryDetailLists.setOnClickListener( new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				treeview.setVisibility(View.GONE);
				directoryDetail.setVisibility(View.VISIBLE);
				
				directCatalog.setChecked(false);
				directoryDetailLists.setChecked(true);
				userCenter.setChecked(false);
			}
			
		});
		
		userCenter.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				treeview.setVisibility(View.GONE);
				directoryDetail.setVisibility(View.VISIBLE);
				
				directCatalog.setChecked(false);
				directoryDetailLists.setChecked(false);
				userCenter.setChecked(true);
			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{ 
			if((System.currentTimeMillis()- exitTime) > 2000)
			{ 
				UIHelper.ToastMessage(appContext, "再按一次退出程序");
				exitTime = System.currentTimeMillis(); 
			} else 
			{ 
				finish(); 
				System.exit(0); 
			} 
			return true; 
		} 
		
		return super.onKeyDown(keyCode, event);
	}
	
	private void loadKuiBulistData(final int pageIndex, final Handler handler, final int action)
	{
		head_progress.setVisibility(View.VISIBLE);
		new Thread()
		{
			public void run() 
			{
				Message msg = Message.obtain();
				boolean isRefresh = false;
                
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
                    isRefresh = true;
              
				try {
                	DirectoryOutlineList dirList  = appContext.getDirectoryOutlineList(pageIndex, isRefresh);
                    msg.what = dirList.getPageSize();
                    msg.obj = dirList;
                } catch (AppException e) { //get DirectoryOutlineList failed from internet and cache.
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e; 
                }    
				
                msg.arg1 = action;
                msg.arg2 = UIHelper.LISTVIEW_DATATYPE_NEWS;
                //if (curNewsCatalog == catalog)
                handler.sendMessage(msg);	
			}
			
		}.start();
		
	}


	private void loadDirectoryTreeViewData(final int pageIndex, final Handler handler, final int action)
	{
		head_progress.setVisibility(View.VISIBLE);
		new Thread()
		{
			public void run() 
			{
				Message msg = Message.obtain();
				boolean isRefresh = false;
                
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
                    isRefresh = true;
              
				try {
                	DirectoryOutlineList dirList  = appContext.getDirectoryOutlineList(pageIndex, isRefresh);
                    msg.what = dirList.getPageSize();
                    msg.obj = dirList;
                } catch (AppException e) { //get DirectoryOutlineList failed from internet and cache.
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e; 
                }    
				
                msg.arg1 = action;
                msg.arg2 = UIHelper.LISTVIEW_DATATYPE_NEWS;
                //if (curNewsCatalog == catalog)
                handler.sendMessage(msg);	
			}
			
		}.start();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
