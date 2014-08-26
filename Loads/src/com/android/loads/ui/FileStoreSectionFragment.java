package com.android.loads.ui;

import java.util.ArrayList;
import com.android.loads.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FileStoreSectionFragment extends Fragment {

    private ArrayList<String> filesArray;
    private ListView fileList;
    private OnFileStoreListener fileStoreListener;
    View rootView;
    public FileStoreSectionFragment () {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.loads_store_layout, container, false);
            fileList = (ListView)rootView.findViewById(R.id.file_list);
            if(savedInstanceState == null)
                filesArray = getArguments().getStringArrayList("key");
            else
                filesArray = savedInstanceState.getStringArrayList("string_key");
            if(filesArray != null) {
                fileList.setAdapter(new ArrayAdapter<String>(rootView.getContext(), R.layout.file_list_item,filesArray));
                fileList.setOnItemClickListener(new OnItemClickListener() {
                
                @Override
                public void onItemClick(AdapterView<?> arg0, View view,
                            int position, long id) {
                        if(fileStoreListener != null)
                            fileStoreListener.onListFileClick(position);
                    }
                });
                fileList.setOnItemLongClickListener(new OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0,
                            View arg1, int position, long id) {
                        if(fileStoreListener != null)
                            fileStoreListener.onLongListFileClick(position);
                        return false;
                    }
                });
                registerForContextMenu(fileList);
        }
        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.file_list_menu, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fileList = (ListView)rootView.findViewById(R.id.file_list);
        int count = fileList.getAdapter().getCount();
        ArrayList<String> array = new ArrayList<String>();
        for(int i = 0; i < count; ++i)
            array.add((String) fileList.getItemAtPosition(i));
        outState.putStringArrayList("string_key", array);
    };

    public void setOnFileStoreListener(OnFileStoreListener listener) {
        fileStoreListener = listener;
    }

    public static interface OnFileStoreListener {

        public void onListFileClick(int position);

        public void onLongListFileClick(int position);

    }
}