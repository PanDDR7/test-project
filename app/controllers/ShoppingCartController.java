package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.ObjectArrays;
import com.mysql.cj.xdevapi.JsonArray;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Model;

import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ShoppingCartController extends Controller {

    public Result insert() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid") || !parameter.has("product_id") || !parameter.has("quantity")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String user_uuid = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(user_uuid);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        int productId = parameter.get("product_id").asInt();
        int productQuantity = parameter.get("quantity").asInt();
        if (productQuantity < 0) {
            return ok(Json.newObject().put("message", "quantity must > 0"));
        }
        Product product = Product.findProductById(productId);
        if (product == null) {
            return ok(Json.newObject().put("message", "product is not exist"));
        }
        ShoppingCart cart = ShoppingCart.findShoppingCartByProductId(productId);
        if (cart != null) {
            return ok(Json.newObject().put("message", "product is already in shoppingCart"));
        }
        //ShoppingCart shoppingCart = new ShoppingCart(frontUser.getUserId(), product.getId(), productQuantity,product.getPrice()*productQuantity);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(frontUser.getUserId());
        shoppingCart.setProductId(product.getId());
        shoppingCart.setProductName(product.getName());
        shoppingCart.setQuantity(productQuantity);
        shoppingCart.setPrice(product.getPrice());
        shoppingCart.setTotalAmount(product.getPrice() * productQuantity);
        shoppingCart.save();
        return ok(Json.newObject().put("insert", "success"));
    }

    public Result delete() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid") || !parameter.has("id")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        int id = parameter.get("id").asInt();
        ShoppingCart shoppingCart = ShoppingCart.findShoppingCartById(id);
        if (shoppingCart == null) {
            return ok(Json.newObject().put("message", "id is not in shoppingCart"));
        }
        if (!frontUser.getUserId().equals(shoppingCart.getUserId())) {
            return ok(Json.newObject().put("message", "product is not exist"));
        }
        shoppingCart.delete();
        return ok(Json.newObject().put("delete", "success"));
    }

    public Result update() {
        return TODO;
    }

    public Result totalAmount() {
        int sum = 0;
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
        List<ShoppingCart> shoppingCartList = ShoppingCart.shoppingCartListByUserId(frontUser.getUserId());
        for (ShoppingCart shoppingCart : shoppingCartList) {
            sum += shoppingCart.getTotalAmount();
        }
        return ok(Json.newObject().put("Total_Amount", sum));
    }

    public Result clear() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        List<ShoppingCart> shoppingCartList = ShoppingCart.shoppingCartListByUserId(frontUser.getUserId());
        for (ShoppingCart shoppingCart : shoppingCartList) {
            shoppingCart.delete();
        }
        return ok(Json.newObject().put("clear all", "success"));
    }

    public Result showAll() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        List<ShoppingCart> shoppingCartList = ShoppingCart.shoppingCartListByUserId(frontUser.getUserId());
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode arrayNode = response.putArray("data");
        //arrayNode.addObject().put("sadf","sadf");
        //arrayNode.addObject().put("asdf","asdf");
        for (ShoppingCart shoppingCart : shoppingCartList) {
            //response.put();
            //aaa.put(String.valueOf(shoppingCart.getProductId()),shoppingCart.getQuantity());
            arrayNode.addObject().put("id", shoppingCart.getId()).put("product_id", shoppingCart.getProductId()).put("quantity", shoppingCart.getQuantity());
            //arrayNode.addObject().put(String.valueOf(shoppingCart.getProductId()),shoppingCart.getQuantity());
        }
        return ok(response);
    }
}
