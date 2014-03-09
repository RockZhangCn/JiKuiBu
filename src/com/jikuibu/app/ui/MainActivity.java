package com.jikuibu.app.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ViewFlipper;

import com.jikuibu.app.bean.Directory;
import com.jikuibu.app.bean.CategoryTree;
import com.jikuibu.app.bean.KuiBuDict;
import com.jikuibu.app.bean.KuiBuList;
import com.jikuibu.app.ui.Adapter.KuiBuListAdapter;
import com.jikuibu.app.ui.Adapter.CategoryTreeAdapter;
import com.jikuibu.app.utils.*;
import com.jikuibu.app.AppContext;
import com.jikuibu.app.AppException;
import com.jikuibu.app.R;


public class MainActivity extends Activity {
	protected static final String TAG = "MainActivity";
	
	//Global variables.
	private Context _actContext;
	private AppContext _appContext;
	LayoutInflater _sysInflater;
	private long _exitTime = 0; 
	private ViewFlipper _viewFllipper;
	private View _leftView;
	private View _middleView;
	private View _rightView;
	
	//Header controls.
	private ProgressBar _headProgressBar;
	private TextView _headTextView;
	
	//Left View ----> Category View.
	private CategoryTree _categoryTreeData = new CategoryTree();
	private CategoryTreeAdapter _categoryTreeAdapter;
	private PullToRefreshListView _categoryTreeCtrl;
	private static Handler _categoryTreeHandler;
	
	//Middle View -----> KuiBu List View.
	private KuiBuList _kuibuListData = new KuiBuList();
	private KuiBuListAdapter _kuibuListAdapter;
	private PullToRefreshListView _kuibuListView;
	private static Handler _kuibuListHandler;
	
	//Right View -----> User Center View.
	private ListView _userCenterView; 
	
	//Footer controls.
	private RadioButton _categoryTreeTabButton;
	private RadioButton _kuibuListTabButton;
	private RadioButton _userCenterTabButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_viewFllipper = (ViewFlipper) findViewById(R.id.view_switcher);
		_leftView = findViewById(R.id.tabviewleft);
		_middleView = findViewById(R.id.tabviewmiddle);
		_rightView = findViewById(R.id.tabviewright);
		
		_actContext = MainActivity.this;
		_appContext = (AppContext)getApplication();
		FileUtils.setGlobalContext(_appContext);
		_sysInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
		initHeaderView();
		initCategoryTreeView();
		initKuibuListView();
		initFooterBar();
		
		initCategoryTreeData();//This is the first show screen.
		initKuibuListData();

	}
	
	private void switchToTabView(View tabView)
	{
		int guard = 15;	
		while (_viewFllipper.getCurrentView() != tabView && guard-- > 0) 
		{
			 _viewFllipper.showNext();
	    }  
	}
	
	private void initKuibuListView() {
		_kuibuListView = (PullToRefreshListView)findViewById(R.id.directorydetail);
		_kuibuListAdapter = new KuiBuListAdapter(this, _kuibuListData, R.layout.kuibulistitem);
		_kuibuListView.setAdapter(_kuibuListAdapter);
		
		_kuibuListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
			{
				KuiBuDict kuibuDict = (KuiBuDict) arg0.getAdapter().getItem(position);
				if(kuibuDict == null)
				{
					Log.e(TAG, "we clicked the header view, do nothing.");
					return;
				}

				Intent intent = new Intent(MainActivity.this, KuiBuDetailActivity.class);			
				Bundle bundle = new Bundle();
				bundle.putSerializable("KUIBULIST", _kuibuListData);
				bundle.putInt("INDEX", position);
				intent.putExtras(bundle);
				
				_actContext.startActivity(intent);
			
			}
		});
		
		_kuibuListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
                Log.e(TAG, "_kuibuListView begin to refresh");
				loadKuiBuListData(0, _kuibuListHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
			}

		});
	}
	
	private void initKuibuListData()
	{
		_kuibuListHandler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) 
	    {
	    	   super.handleMessage(msg);
		        switch(msg.what)
		        {
		        case 0:
		        	KuiBuList retKuibuList = (KuiBuList)msg.obj;
		        	_kuibuListData.setKuiBulist(retKuibuList.getKuiBulist());
		        	_kuibuListAdapter.notifyDataSetChanged();
		        	_kuibuListView.onRefreshComplete();
		        	_headTextView.setText("加载KuiBuDictList完成");
		        	_headProgressBar.setVisibility(View.INVISIBLE);
		        	Log.e(TAG, "We received KuiBulist " + retKuibuList);
		        	break;
		        default:
		        	_kuibuListView.onRefreshComplete();
		        	UIHelper.ToastMessage(_appContext, "Default message handler");
		        }
	    
	    }
		};

	}
	
	private void initCategoryTreeData()
	{
		_categoryTreeHandler = new Handler(){
		    @Override
		    public void handleMessage(Message msg) {
		        super.handleMessage(msg);
		        switch(msg.what)
		        {
		        case 0:
		    		_headProgressBar.setVisibility(View.INVISIBLE);
		    		_headTextView.setText("分类列表");
		    		CategoryTree retCategoryTreeData = (CategoryTree)msg.obj;
		    		
		    		_categoryTreeData.setDirectoryList(retCategoryTreeData.getDirectoryList());
		    		_headProgressBar.setVisibility(View.INVISIBLE);
		    		_categoryTreeAdapter.notifyDataSetChanged();
		    		_categoryTreeCtrl.onRefreshComplete();
		    		break;
		        case -1:
		        	AppException e = (AppException)msg.obj;
		        	
		        	if(AppException.TYPE_HTTP_CODE == e.getType())
		        		UIHelper.ToastMessage(_actContext, "Network issue with status code " + e.getCode());
		        	_categoryTreeCtrl.onRefreshComplete();
		        	_headProgressBar.setVisibility(View.INVISIBLE);
		        	UIHelper.ToastMessage(_actContext, "Network error, get the directory list failed");
		        }
		    }
		};
		
		//Other handler.
		
		if(_categoryTreeData.isEmpty())
		{
			loadDirectoryTreeViewData(0, _categoryTreeHandler, UIHelper.LISTVIEW_ACTION_INIT);
		}
		
	}
	
	
	private void loadKuiBuListData(final int pageIndex, final Handler handler, final int action)
	{
		_headProgressBar.setVisibility(View.VISIBLE);
		new Thread()
		{
			public void run() 
			{
				Message msg = Message.obtain();
				boolean isRefresh = false;
                
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
                    isRefresh = true;
              
				try {
					KuiBuList kuiDictList = _appContext.getKuiBuListData("Type", action, isRefresh);
                    msg.what = kuiDictList.getPageSize();
                    msg.obj = kuiDictList;
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

	private void initHeaderView()
	{
		_headTextView = (TextView)findViewById(R.id.main_head_title);
		_headProgressBar = (ProgressBar)findViewById(R.id.main_head_progress);
		_headProgressBar.setVisibility(View.VISIBLE);
		_headTextView.setText("正在加载目录，请稍后...");
	}


	private void initCategoryTreeView()
	{
		_categoryTreeAdapter = new CategoryTreeAdapter(this, _categoryTreeData, R.layout.treeview_item);
		_categoryTreeCtrl = (PullToRefreshListView) findViewById(R.id.treeview);
		_categoryTreeCtrl.setAdapter(_categoryTreeAdapter);
		
		_categoryTreeCtrl.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				loadDirectoryTreeViewData(0, _categoryTreeHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
			}

		});
		
		_categoryTreeCtrl.setOnItemClickListener(new AdapterView.OnItemClickListener(){
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
					_categoryTreeTabButton.setChecked(false);
					_kuibuListTabButton.setChecked(true);
					_userCenterTabButton.setChecked(false);
					
					loadKuiBuListData(0, _kuibuListHandler, UIHelper.LISTVIEW_ACTION_INIT);
		        	switchToTabView(_middleView);
					
					return;
				}
				
				if (dir.isExpanded()) 
				{
					dir.setExpanded(false);
					ArrayList<Directory> elementsToDel = new ArrayList<Directory>();
					//Fix the click error due to introduce of PullRefreshView-addHeaderView.
					for (int i = position; i < _categoryTreeData.getDisplayDirectoryList().size(); i++) 
					{  
						if (dir.getLevel() >= _categoryTreeData.getDisplayDirectoryList().get(i).getLevel())
							break;
						elementsToDel.add(_categoryTreeData.getDisplayDirectoryList().get(i));
					}
					_categoryTreeData.getDisplayDirectoryList().removeAll(elementsToDel);

				} else {
					dir.setExpanded(true);
					int i = 1;
					for (Directory e : _categoryTreeData.getDirectoryList()) {
						if (e.getParendId() == dir.getId()) {
							e.setExpanded(false);
							//Fix the click error due to introduce of PullRefreshView-addHeaderView.
							_categoryTreeData.getDisplayDirectoryList().add(position -1 + i, e);
							i ++;
						}
					}
					
				}
				_categoryTreeAdapter.notifyDataSetChanged();
			}
		});
	}
	
	private void initFooterBar()
	{
		_categoryTreeTabButton = (RadioButton) findViewById(R.id.main_footbar_news);
		_kuibuListTabButton = (RadioButton) findViewById(R.id.main_footbar_question);
		_userCenterTabButton = (RadioButton) findViewById(R.id.main_footbar_active);
		_categoryTreeTabButton.setChecked(true);
		
		_categoryTreeTabButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchToTabView(_leftView);
				
				_categoryTreeTabButton.setChecked(true);
				_kuibuListTabButton.setChecked(false);
				_userCenterTabButton.setChecked(false);
			}
		});
		
		_kuibuListTabButton.setOnClickListener( new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchToTabView(_middleView);
				
				_categoryTreeTabButton.setChecked(false);
				_kuibuListTabButton.setChecked(true);
				_userCenterTabButton.setChecked(false);
			}
			
		});
		
		_userCenterTabButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchToTabView(_rightView);
				
				_categoryTreeTabButton.setChecked(false);
				_kuibuListTabButton.setChecked(false);
				_userCenterTabButton.setChecked(true);
			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{ 
			if((System.currentTimeMillis()- _exitTime) > 2000)
			{ 
				UIHelper.ToastMessage(_appContext, "再按一次退出程序");
				_exitTime = System.currentTimeMillis(); 
			} else 
			{ 
				finish(); 
				System.exit(0); 
			} 
			return true; 
		} 
		
		return super.onKeyDown(keyCode, event);
	}
	
	private void loadDirectoryTreeViewData(final int pageIndex, final Handler handler, final int action)
	{
		_headProgressBar.setVisibility(View.VISIBLE);
		new Thread()
		{
			public void run() 
			{
				Message msg = Message.obtain();
				boolean isRefresh = false;
                
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
                    isRefresh = true;
              
				try {
                	CategoryTree retCategoryTreeData  = _appContext.getCategoryTreeData(pageIndex, isRefresh);
                    msg.what = retCategoryTreeData.getPageSize();
                    msg.obj = retCategoryTreeData;
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
