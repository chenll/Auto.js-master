package com.stardust.datebase.greenDao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class History {
    @Id(autoincrement = true)
    private Long id;
    private String packageName;
    private String key;
    private long time;
    @Generated(hash = 1871685353)
    public History(Long id, String packageName, String key, long time) {
        this.id = id;
        this.packageName = packageName;
        this.key = key;
        this.time = time;
    }
    @Generated(hash = 869423138)
    public History() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPackageName() {
        return this.packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
  
}
