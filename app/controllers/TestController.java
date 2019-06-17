package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.Model;
import models.ShoppingCart;
import models.User;
import play.libs.Json;
import models.Product;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.function.Consumer;

public class TestController extends Controller {
    /*
    public Result getProduct(int id, String inputAccount, String inputPassword) {
        Product product = Product.findProductById(id);
        if (product == null) {
            return ok(Json.newObject().put("message", "not found"));
        }
        if (!product.getAccount().equals(inputAccount)) {
            return ok(Json.newObject().put("message", "account error"));
        }
        if (!product.getPassword().equals(inputPassword)) {
            return ok(Json.newObject().put("message", "password error"));
        }
        return ok(Json.newObject().put("login success", product.getName()));
    }

     */

    public Result login() {
        JsonNode parameter = request().body().asJson();
        User user = User.findUserById(parameter.get("id").asInt());
        if (user == null) {
            return ok(Json.newObject().put("message", "id not found"));
        }
        if (!user.getAccount().equals(parameter.get("account").asText())) {
            return ok(Json.newObject().put("message", "account error"));
        }
        if (!user.getPassword().equals(parameter.get("password").asText())) {
            return ok(Json.newObject().put("message", "password error"));
        }
        return ok(Json.newObject().put("login success", user.getName()));
    }

    public Result insert() {
        JsonNode parameter = request().body().asJson();
        //參數有沒有缺
        ShoppingCart shoppingCart = ShoppingCart.findShoppingCartById(parameter.get("id").asInt());
        if (shoppingCart != null) {
            return ok(Json.newObject().put("message", "id already exist"));
        }
        //參數的規則
        shoppingCart = new ShoppingCart(parameter.get("user_id").asText(), parameter.get("product_id").asInt(), parameter.get("quantity").asInt());
        /*
        ShoppingCart shoppingCart = ShoppingCart.findShoppingCartById(parameter.get("id").asInt());
        if(shoppingCart!=null){
            return ok(Json.newObject().put("message", "id already exist"));
        }
        shoppingCart.setId(parameter.get("id").asInt());
        shoppingCart.setUserId(parameter.get("user_id").asText());
        shoppingCart.setProductId(parameter.get("product_id").asInt());
        shoppingCart.setQuantity(parameter.get("quantity").asText());
         */
        //shoppingCart.save();
        Ebean.getServer("default").insert(shoppingCart);
        return ok(Json.newObject().put("insert success", "success"));
    }

    public Result update() {
        JsonNode parameter = request().body().asJson();
        ShoppingCart shoppingCart = ShoppingCart.findShoppingCartById(parameter.get("id").asInt());
        if (shoppingCart == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        if (parameter.get("user_id") == null) {
            return ok(Json.newObject().put("message", "please inter user_id"));
        }
        if (parameter.get("product_id") == null) {
            return ok(Json.newObject().put("message", "please inter product_id"));
        }
        if (parameter.get("quantity") == null) {
            return ok(Json.newObject().put("message", "please inter quantity"));
        }
        shoppingCart.setUserId(parameter.get("user_id").asText());
        shoppingCart.setProductId(parameter.get("product_id").asInt());
        shoppingCart.setQuantity(parameter.get("quantity").asInt());
        shoppingCart.update();
        //Ebean.getServer("default").update(shoppingCart);
        return ok(Json.newObject().put("update success", "success"));
    }

    public Result delete() {
        JsonNode parameter = request().body().asJson();
        ShoppingCart shoppingCart = ShoppingCart.findShoppingCartById(parameter.get("id").asInt());
        if (shoppingCart == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        Ebean.getServer("default").delete(shoppingCart);
        return ok(Json.newObject().put("delete success", "success"));

    }

    public Result showAll() {
        ShoppingCart shoppingCart = new ShoppingCart();
        String result = String.valueOf(Ebean.getServer("default").find(ShoppingCart.class).where().eq("id", 1));
        return ok(Json.newObject().put("show all", result));
    }
}
