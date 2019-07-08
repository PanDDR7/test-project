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
@Table(name = "shopping_cart")
public class ShoppingCart extends Model {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private int price;
    @Column(name = "total_amount")
    private int totalAmount;
    @CreatedTimestamp
    @Column(name = "create_datetime")
    private Date createDatetime;

    public static ShoppingCart findShoppingCartById(int id) {
        return Ebean.getServer("default").find(ShoppingCart.class).where().eq("id", id).findOne();
    }

    public ShoppingCart(){

    }

    public ShoppingCart(String inputUserId,int inputProductId,int inputQuantity,int inputTotalAmount){
        this.userId=inputUserId;
        this.productId=inputProductId;
        this.quantity=inputQuantity;
        this.totalAmount=inputTotalAmount;
    }

    public static List<ShoppingCart> shoppingCartListByUserId(String userId){
        return Ebean.getServer("default").find(ShoppingCart.class).where().eq("user_id",userId).findList();
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName=productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalAmount(){
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount){this.totalAmount = totalAmount;}

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}
