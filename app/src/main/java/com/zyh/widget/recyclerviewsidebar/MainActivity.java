package com.zyh.widget.recyclerviewsidebar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;


import com.zyh.widget.recyclerviewsidebar.sidebar.RecyclerViewSidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mListRv;
    private RecyclerViewSidebar mSidebar;
    private TextView mFloatLetterTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListRv = (RecyclerView) findViewById(R.id.main_list_rv);
        mSidebar = (RecyclerViewSidebar) findViewById(R.id.sidebar);
        mFloatLetterTv = (TextView) findViewById(R.id.floating_header);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListRv.setLayoutManager(linearLayoutManager);

        MyListAdapter myListAdapter = new MyListAdapter(this, new ArrayList<MyItemEntity>());
        mListRv.setAdapter(myListAdapter);

        mSidebar.setFloatLetterTextView(mFloatLetterTv);
        mSidebar.setSelectedSideBarColor(R.drawable.sidebar_background_pressed);
        mSidebar.setRecyclerView(mListRv);

        // test code
        String[] nameList = new String[] {"abc","aac","abb","acc","bcd","bdd","bcc","bee","def","dee",
                "dff","bsp","bgg","bgp","fgk","fgl","fgkl","fkk","fll","xyz","yzz","yzx","xzz","zzz"
                ,"caa","cbb","ccc","cdd","cee", "123", "456", "567", "465", "*(&", "$#@JL", "090fojs"
                , "09546", "89jklsj", "^%$HHKI"};

        List<MyItemEntity> data = new ArrayList<>();
        for (int i = 0; i < nameList.length; i++) {
            MyItemEntity myItemEntity = new MyItemEntity();
            String name = nameList[i];
            myItemEntity.setName(name);
            String headLetter = name.substring(0, 1);
            String reg = "[a-zA-Z]";
            if (headLetter.matches(reg)) {
                myItemEntity.setHeader(headLetter.toUpperCase());
            } else {
                myItemEntity.setHeader("#");
            }
            data.add(myItemEntity);
        }

        // sort it at first
        Collections.sort(data);

        // add index item
        List<MyItemEntity> indexData = new ArrayList<>();
        List<MyItemEntity> nonLetterData = new ArrayList<>();
        String lastHeader = null;
        for (int i = 0; i < data.size(); i++) {
            MyItemEntity dataItemEntity = data.get(i);
            if (dataItemEntity.getHeader().equals("#")) {
                nonLetterData.add(dataItemEntity);
            } else {
                if (i == 0) {
                    indexData.add(createIndexItemEntity(dataItemEntity));
                } else {
                    lastHeader = data.get(i - 1).getHeader();
                    if (!lastHeader.equals(dataItemEntity.getHeader())) {
                        indexData.add(createIndexItemEntity(dataItemEntity));
                    }
                }
                indexData.add(dataItemEntity);
            }
        }

        if (nonLetterData.size() > 0) { // add all the non-letter data into the list so that make it at the last.
            indexData.add(createIndexItemEntity(nonLetterData.get(0)));
            indexData.addAll(nonLetterData);
        }

        // refresh list
        myListAdapter.refreshData(indexData);
    }

    private MyItemEntity createIndexItemEntity(MyItemEntity dataItemEntity) {
        MyItemEntity indexItemEntity = new MyItemEntity();
        indexItemEntity.setType(MyItemEntity.TYPE_INDEX);
        indexItemEntity.setName(dataItemEntity.getHeader());
        indexItemEntity.setHeader(dataItemEntity.getHeader());
        return indexItemEntity;
    }
}
