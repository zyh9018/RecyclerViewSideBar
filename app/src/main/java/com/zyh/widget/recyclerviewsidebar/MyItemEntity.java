package com.zyh.widget.recyclerviewsidebar;

import android.support.annotation.NonNull;

/**
 * data entity
 * <p>
 * Created by zyh on 2017/12/19.
 */
public class MyItemEntity implements Comparable<MyItemEntity> {
    public static final int TYPE_DATA = 0;
    public static final int TYPE_INDEX = 1;

    private String name;
    private String header;
    private int type = TYPE_DATA;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(@NonNull MyItemEntity t2) {
        if (this.getName().equals(t2.getName())) {
            return -1;
        }
        return this.getName().compareTo(t2.getName());
    }
}
