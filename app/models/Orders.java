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
import java.util.List;

@Entity
@Table(name = "orders")
public class Orders extends Model{

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_uuid")
    private String userUUID;
    @Column(name = "total_amount")
    private int totalAmount;
    @Column(name = "status")
    private String status;
    @CreatedTimestamp
    @Column(name = "create_datetime")
    private Date createDatetime;


    public static Orders findOrdersById(int id){
        return Ebean.getServer("default").find(Orders.class).where().eq("id",id).findOne();
    }

    public static List<Orders> findOrdersListByUserId(String userId){
        return Ebean.getServer("default").find(Orders.class).where().eq("user_id",userId).findList();
    }

    public static List<Orders> findOrdersList(){
        return Ebean.getServer("default").find(Orders.class).findList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID (){
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserUUID (){
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID=userUUID;
    }

    public int getTotalAmount(){return totalAmount;}

    public void setTotalAmount(int totalAmount){this.totalAmount=totalAmount;}

    public String getStatus(){return status;}

    public void setStatus(String status){this.status=status;}

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

}
