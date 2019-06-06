package models;

import io.ebean.Ebean;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;
import scala.concurrent.java8.FuturesConvertersImpl;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "front_user")
public class FrontUser extends Model {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_gender")
    private String userGender;
    @Column(name = "user_uuid")
    private String userUUID;
    @Column(name = "user_account")
    private String userAccount;
    @Column(name = "user_password")
    private String userPassword;
    @CreatedTimestamp
    @Column(name = "create_datetime")
    private Date createDatetime;
    @UpdatedTimestamp
    @Column(name = "modify_datetime")
    private Date modifyDatetime;

    public FrontUser(){

    }

    public FrontUser(String inputUserId,String inputUserName,String inputUserGender,String inputUserUUID,String inputUserAcc,String inputUserPwd){
        this.userId=inputUserId;
        this.userName=inputUserName;
        this.userGender=inputUserGender;
        this.userUUID=inputUserUUID;
        this.userAccount=inputUserAcc;
        this.userPassword=inputUserPwd;
    }

    public static FrontUser findFrontUserByUUID(String userUUID){
        return Ebean.getServer("default").find(FrontUser.class).where().eq("user_uuid",userUUID).findOne();
    }

    public static FrontUser findFrontUserByAccount(String userAccount){
        return Ebean.getServer("default").find(FrontUser.class).where().eq("user_account",userAccount).findOne();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId(){
        return userId;

    }

    public void setUserId(String userId){
        this.userId=userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getUserGender(){return userGender;}

    public void setUserGender(String gender){this.userGender=gender;}

    public String getUserUUID(){return userUUID;}

    public void setUserUUID(String userUUID){this.userUUID=userUUID;}

    public String getAccount(){return userAccount;}

    public void setUserAccount(String account){this.userAccount=account;}

    public String getPassword(){return userPassword;}

    public void  setUserPassword(String password){this.userPassword=password;}

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getModifyDatetime() {
        return modifyDatetime;
    }

    public void setModifyDatetime(Date modifyDatetime) {
        this.modifyDatetime = modifyDatetime;
    }
}
