package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
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


public class ProductController extends Controller {

    public Result addNewProduct() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("name") || !parameter.has("price") || !parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String name = parameter.get("name").asText();
        String userUUID = parameter.get("user_uuid").asText();
        int price = parameter.get("price").asInt();
        /*
        if(!parameter.has("name")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
        if(!parameter.has("price")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
         */
        if (price < 0) {
            return ok(Json.newObject().put("message", "price must > 0"));
        }
        Product product = Product.findProductByName(name);
        if (product != null) {
            return ok(Json.newObject().put("message", "product already exist"));
        }
        BackendUser backendUser = BackendUser.findBackendUserByUUID(userUUID);
        if (backendUser == null) {
            return ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        product = new Product(backendUser.getUserId(), name, price);
        product.save();
        return ok(Json.newObject().put("insert success", "success"));
    }

    public Result updateProductData() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("id") || !parameter.has("name") || !parameter.has("price") || !parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String name = parameter.get("name").asText();
        int productId = parameter.get("id").asInt();
        int price = parameter.get("price").asInt();
        String userUUID = parameter.get("user_uuid").asText();
        Product product = Product.findProductById(productId);
        if (product == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        if (name == null) {
            return ok(Json.newObject().put("message", "please enter name"));
        }
        if (price < 0) {
            return ok(Json.newObject().put("message", "please enter price & price must > 0"));
        }
        BackendUser backendUser = BackendUser.findBackendUserByUUID(userUUID);
        if (backendUser == null) {
            return ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        product.setName(name);
        product.setPrice(price);
        product.setUserId(backendUser.getUserId());
        product.update();
        return ok(Json.newObject().put("message", "success"));
    }

    public Result deleteProduct() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid") || !parameter.has("id")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        int productId = parameter.get("id").asInt();
        String userUUID = parameter.get("user_uuid").asText();
        BackendUser backendUser = BackendUser.findBackendUserByUUID(userUUID);
        if (backendUser == null) {
            return ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        Product product = Product.findProductById(productId);
        if (product == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        product.delete();
        return ok(Json.newObject().put("message", "success"));
    }

    public Result frontShowAll() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        List<Product> products = Product.findProductList();
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode arrayNode = response.putArray("data");
        for (Product product : products) {
            arrayNode.addObject().put("id", product.getId()).put("name", product.getName()).put("price", product.getPrice());
        }
        /*
        StringBuffer stringBuffer = new StringBuffer();
        for (Product product : products){
            stringBuffer.append(product.getName() + "=" + product.getPrice());
        }
        for(int i=0;i<products.size();i++){
            return ok(Json.newObject().put(products.get(i).getName(),products.get(i).getPrice()));
        }
        for(Product product : products){
            return ok(Json.newObject().put(product.getName(), product.getPrice()));
        }
         */
        return ok(response);
    }

    public Result backendShowAll() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        BackendUser backendUser = BackendUser.findBackendUserByUUID(userUUID);
        if (backendUser == null) {
            ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        List<Product> productList = Product.findProductList();
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode arrayNode = response.putArray("data");
        for (Product product : productList) {
            arrayNode.addObject().put("id", product.getId()).put("name", product.getName()).put("price", product.getPrice());
        }
        return ok(response);
    }

    public Result frontSearchProduct() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid") || !parameter.has("id")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        int productId = parameter.get("id").asInt();
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        Product product = Product.findProductById(productId);
        if (product == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode arrayNode = response.putArray("data");
        arrayNode.addObject().put("id", product.getId()).put("name", product.getName()).put("price", product.getPrice());
        return ok(response);
    }

    public Result backendSearchProduct() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid") || !parameter.has("id")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        int productId = parameter.get("id").asInt();
        String userUUID = parameter.get("user_uuid").asText();
        BackendUser backendUser = BackendUser.findBackendUserByUUID(userUUID);
        if (backendUser == null) {
            return ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        Product product = Product.findProductById(productId);
        if (product == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode arrayNode = response.putArray("data");
        arrayNode.addObject().put("id", product.getId()).put("name", product.getName()).put("price", product.getPrice());
        return ok(response);
    }

}
