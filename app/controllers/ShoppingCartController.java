package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.mysql.cj.xdevapi.JsonArray;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Model;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scala.util.parsing.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ShoppingCartController extends Controller {

    public Result insert() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid") || !parameter.has("name") || !parameter.has("quantity")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String UUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(UUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        String productName = parameter.get("name").asText();
        int productQuantity = parameter.get("quantity").asInt();
        if (productQuantity < 0) {
            return ok(Json.newObject().put("message", "quantity must > 0"));
        }
        Product product = Product.findProductByName(productName);
        if (product == null) {
            return ok(Json.newObject().put("message", "product is not exist"));
        }
        ShoppingCart shoppingCart = new ShoppingCart(frontUser.getUserId(), product.getId(), productQuantity);
        shoppingCart.save();
        return ok(Json.newObject().put("insert", "success"));
    }

    public Result delete() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid") || !parameter.has("name")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        String productName = parameter.get("name").asText();
        Product product = Product.findProductByName(productName);
        if (product == null) {
            return ok(Json.newObject().put("message", "product is not exist"));
        }
        ShoppingCart shoppingCart = ShoppingCart.findShoppingCartByProductId(product.getId());
        if (shoppingCart == null) {
            return ok(Json.newObject().put("message", "product is not in shoppingCart"));
        }
        shoppingCart.delete();
        return ok(Json.newObject().put("delete", "success"));
    }

    public Result update(){
        return TODO;
    }

    public Result totalAmount() {
        int sum=0;
        //List shoppingCartList = ShoppingCart.ShoppingCartList();
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        List<ShoppingCart> shoppingCartList = Ebean.getServer("default").find(ShoppingCart.class).where().eq("user_id",frontUser.getUserId()).findList();
        for (ShoppingCart shoppingCart : shoppingCartList){
            Product product = Product.findProductById(shoppingCart.getProductId());
            sum+=product.getPrice()*shoppingCart.getQuantity();
        }
        return ok(Json.newObject().put("Total Amount", sum));
    }

    public Result clear(){
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        List<ShoppingCart> shoppingCartList = Ebean.getServer("default").find(ShoppingCart.class).where().eq("user_id",frontUser.getUserId()).findList();
        for (ShoppingCart shoppingCart : shoppingCartList){
            shoppingCart.delete();
        }
        return ok(Json.newObject().put("clear all", "success"));
    }

    public Result showAll(){
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        List<ShoppingCart> shoppingCartList = Ebean.getServer("default").find(ShoppingCart.class).where().eq("user_id",frontUser.getUserId()).findList();
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        for (ShoppingCart shoppingCart : shoppingCartList){
            Product product = Product.findProductById(shoppingCart.getProductId());
            response.put(product.getName(),shoppingCart.getQuantity());
        }
        return ok(Json.newObject().pojoNode(response));
    }
}
