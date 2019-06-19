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
import scala.util.parsing.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class ProductController extends Controller {

    public Result insert(){
        JsonNode parameter = request().body().asJson();
        if(!parameter.has("name")||!parameter.has("price")||!parameter.has("user_uuid")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
        /*
        if(!parameter.has("name")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
        if(!parameter.has("price")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
         */
        if(parameter.get("price").asInt()<0){
            return ok(Json.newObject().put("message", "price must > 0"));
        }
        Product product = Product.findProductByName(parameter.get("name").asText());
        if(product != null){
            return ok(Json.newObject().put("message", "product already exist"));
        }
        BackendUser backendUser = BackendUser.findBackendUserByUUID(parameter.get("user_uuid").asText());
        if(backendUser==null){
            return ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        product = new Product(backendUser.getUserId(),parameter.get("name").asText(),parameter.get("price").asInt());
        product.save();
        return ok(Json.newObject().put("insert success", "success"));
    }

    public Result update(){
        JsonNode parameter = request().body().asJson();
        if(!parameter.has("id")||!parameter.has("name")||!parameter.has("price")||!parameter.has("user_uuid")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
        Product product = Product.findProductById(parameter.get("id").asInt());
        if(product == null){
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        if (parameter.get("name") == null) {
            return ok(Json.newObject().put("message", "please enter name"));
        }
        if (parameter.get("price") == null || parameter.get("price").asInt() < 0) {
            return ok(Json.newObject().put("message", "please enter price & price must > 0"));
        }
        FrontUser frontUser = FrontUser.findFrontUserByUUID(parameter.get("user_uuid").asText());
        if(frontUser==null){
            return ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        product.setName(parameter.get("name").asText());
        product.setPrice(parameter.get("price").asInt());
        product.setUserId(frontUser.getUserId());
        product.update();
        return ok(Json.newObject().put("update", "success"));
    }

    public Result delete(){
        JsonNode parameter = request().body().asJson();
        if(!parameter.has("user_uuid")||!parameter.has("id")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
        BackendUser backendUser = BackendUser.findBackendUserByUUID(parameter.get("user_uuid").asText());
        if(backendUser==null){
            return ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        Product product = Product.findProductById(parameter.get("id").asInt());
        if(product == null){
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        product.delete();
        return ok(Json.newObject().put("delete", "success"));
    }

    public Result showAll(){
        JsonNode parameter  = request().body().asJson();
        if(!parameter.has("user_uuid")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
        BackendUser backendUser = BackendUser.findBackendUserByUUID(parameter.get("user_uuid").asText());
        if(backendUser==null){
            return ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        List<Product> products = Product.findProductList();
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode arrayNode = response.putArray("data");
        for(Product product : products){
            arrayNode.addObject().put("id",product.getId());
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

    public  Result search(){
        JsonNode parameter = request().body().asJson();
        if(!parameter.has("user_uuid")||!parameter.has("id")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
        FrontUser frontUser = FrontUser.findFrontUserByUUID(parameter.get("user_uuid").asText());
        if(frontUser==null){
            return ok(Json.newObject().put("message", "User UUID is not exist"));
        }
        Product product = Product.findProductById(parameter.get("id").asInt());
        if(product==null){
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        ObjectNode respone = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode arrayNode = respone.putArray("data");
        arrayNode.addObject().put("id",product.getId()).put("name",product.getName()).put("price",product.getPrice());
        return ok(respone);
    }

}
