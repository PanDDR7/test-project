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
@Table(name = "product")
public class User extends Model{
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private int price;
    @CreatedTimestamp
    @Column(name = "create_datetime")
    private Date createDatetime;
    @UpdatedTimestamp
    @Column(name = "modify_datetime")
    private Date modifyDatetime;
    @Column(name = "account")
    private String account;
    @Column(name = "password")
    private String password;

    public static User findUserById(int id) {
        return Ebean.getServer("default").find(User.class).where().eq("id", id).findOne();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount(){return account;}

    public void setAccount(String account){this.account=account;}

    public String getPassword(){return password;}

    public void setPassword(String password){this.password=password;}

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

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
