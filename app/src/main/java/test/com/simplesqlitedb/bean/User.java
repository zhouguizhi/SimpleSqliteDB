package test.com.simplesqlitedb.bean;
import android.support.annotation.NonNull;

import com.simple.annotations.DBField;
import com.simple.annotations.DBTable;
/**
 * Created by zhouguizhi on 2017/11/8.
 */
@DBTable("user")
public class User {
    @DBField("name")
    private String name;
    @DBField("password")
    private String password;
    @DBField("user_id")
    private String user_id;
    @DBField("status")
    private  Integer status;
    @DBField("sarary")
    private Double sarary;
    @DBField("date")
    private Long date;
    @DBField("isVip")
    private Boolean isVip;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(@NonNull String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getSarary() {
        return sarary;
    }

    public void setSarary(Double sarary) {
        this.sarary = sarary;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Boolean getVip() {
        return isVip;
    }

    public void setVip(Boolean vip) {
        isVip = vip;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", user_id='" + user_id + '\'' +
                ", status=" + status +
                ", sarary=" + sarary +
                ", date=" + date +
                ", isVip=" + isVip +
                '}';
    }
}
