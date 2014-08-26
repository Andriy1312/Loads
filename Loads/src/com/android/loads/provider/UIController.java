package com.android.loads.provider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.android.loads.R;
import com.android.loads.data.LoadsDataManager;
import com.android.loads.data.OnLoadsDataListener;
import com.android.loads.ui.FileStoreSectionFragment;
import com.android.loads.ui.LoadsSectionFragment;
import com.android.loads.ui.PagerSactionsAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class UIController implements ActionBar.TabListener, IUiController, OnLoadsDataListener {

    private Activity mCurrentActivity;
    private PagerSactionsAdapter mSectionsAdapter;
    private ViewPager mViewPager;
    private LoadsDataManager dataManager;
    private ArrayList<File> fileList;
    private FileStoreSectionFragment fileStoreFragment;
    private LoadsSectionFragment loadsFragment;
    private ListView listView;
    private int fileIdForDeletion = -1;
    private IDownloadController downloadController;

    public UIController(Activity activity) {
        mCurrentActivity = activity;
    }

    public void setActivityUI(FragmentManager fragmentManager, FileStoreSectionFragment.OnFileStoreListener listener) {
/**Tabs adding*/
        final ActionBar actionBar = mCurrentActivity.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

/**Fragment adapter initialization*/
        fileStoreFragment = new FileStoreSectionFragment();
        fileStoreFragment.setOnFileStoreListener(listener);
        loadsFragment = new LoadsSectionFragment();
        List<Fragment> fragmentsList = new ArrayList<Fragment>();
        fragmentsList.add((Fragment)fileStoreFragment);
        fragmentsList.add((Fragment)loadsFragment);
        mSectionsAdapter = new PagerSactionsAdapter(fragmentManager, mCurrentActivity, fragmentsList);

/**Setup files data*/
        dataManager = new LoadsDataManager(Environment.getExternalStorageDirectory());
        dataManager.setOnLoadDataListener(this);
        mSectionsAdapter.setFilesArray(getFileNamesList());

/**View Pager Initialization*/
        mViewPager = (ViewPager) mCurrentActivity.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
        for (int i = 0; i < mSectionsAdapter.getCount(); i++)
            actionBar.addTab(actionBar.newTab().setText(mSectionsAdapter.getPageTitle(i)).setTabListener(this));

/**Registering of DownloadManager*/
        downloadController = new DownloadController(mCurrentActivity);
        downloadController.registerDownloadReceiver();
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

    @Override
    public void onFileStarted(Intent intent) {
        if(intent != null)
            mCurrentActivity.startActivity(intent);
    }

    @Override
    public void onFolderChange() {
        refreshFileStoreList();
    }

    public void refreshFileStoreList() {
        listView = (ListView)mCurrentActivity.findViewById(R.id.file_list);
        listView.setAdapter(new ArrayAdapter<String>(mCurrentActivity, R.layout.file_list_item, getFileNamesList()));
    }

    private ArrayList<String> getFileNamesList() {
        fileList = dataManager.getFileList();
        ArrayList<String> fileNamesList = new ArrayList<String>();
        if(!dataManager.getCurrentRootPath().equals(dataManager.getRootFilePath()))
            fileNamesList.add("../");
        for(File file: fileList)
            fileNamesList.add(file.getName());
        return fileNamesList;
    }

    public void clickToFile(int position) {
      if(!dataManager.getCurrentRootPath().equals(dataManager.getRootFilePath())) {
          if(position == 0) {
              dataManager.backToPreviouseFolder();
              return;
          }
          dataManager.startFile(fileList.get(position-1));
      }
      else
          dataManager.startFile(fileList.get(position));
    }

    public void setFileForDeletion(int fileId) {
        fileIdForDeletion = fileId;
    }

    public void deleteFile() {
        if(fileIdForDeletion != -1) {
            File file;
            if(!dataManager.getCurrentRootPath().equals(dataManager.getRootFilePath()))
                file = fileList.get(fileIdForDeletion-1);
            else
                file = fileList.get(fileIdForDeletion);
            dataManager.removeFile(file);
        }
    }
    @Override
    public void onFileSaved() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onFileRemoved(boolean isRemoved) {
        if(isRemoved) {
            refreshFileStoreList();
            String fileName;
            if(!dataManager.getCurrentRootPath().equals(dataManager.getRootFilePath()))
                fileName = fileList.get(fileIdForDeletion-1).getName();
            else
                fileName = fileList.get(fileIdForDeletion).getName();
            Toast.makeText(mCurrentActivity, "The file" + fileName + "was succesfuly deleted", Toast.LENGTH_LONG).show();
            fileIdForDeletion = -1;
        }
        else
            Toast.makeText(mCurrentActivity, "You have no access to delete this file", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorMessage() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void startDownloading() {
        Toast.makeText(mCurrentActivity, "Load", Toast.LENGTH_LONG).show();
        RelativeLayout load_layout =(RelativeLayout)mCurrentActivity.findViewById(R.id.progress_layout);
        RelativeLayout main_layout =(RelativeLayout)mCurrentActivity.findViewById(R.id.main_load_layout);
        main_layout.setVisibility(RelativeLayout.GONE);
        load_layout.setVisibility(RelativeLayout.VISIBLE);
        EditText inputField = (EditText)mCurrentActivity.findViewById(R.id.input_text_field);
        downloadController.startDownloading(inputField.getText().toString(), dataManager.getDownloadFolderPath());
    }

    @Override
    public void stopDownloading() {
        Toast.makeText(mCurrentActivity, "Cancel", Toast.LENGTH_LONG).show();
        RelativeLayout load_layout =(RelativeLayout)mCurrentActivity.findViewById(R.id.progress_layout);
        RelativeLayout main_layout =(RelativeLayout)mCurrentActivity.findViewById(R.id.main_load_layout);
        main_layout.setVisibility(RelativeLayout.VISIBLE);
        load_layout.setVisibility(RelativeLayout.GONE);
        downloadController.stopDownloading();
    }
}