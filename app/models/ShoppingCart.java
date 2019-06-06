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
@Table(name = "shopping_cart")
public class ShoppingCart extends Model {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "quantity")
    private String quantity;
    @CreatedTimestamp
    @Column(name = "create_datetime")
    private Date createDatetime;

    public static ShoppingCart findShoppingCartById(int id) {
        return Ebean.getServer("default").find(ShoppingCart.class).where().eq("id", id).findOne();
    }

    public ShoppingCart(){

    }

    public ShoppingCart(String inputUserId,int inputProductId,String inputQuantity){
        this.userId=inputUserId;
        this.productId=inputProductId;
        this.quantity=inputQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}
