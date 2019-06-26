package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mysql.cj.xdevapi.JsonArray;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Model;
import models.BackendUser;
import models.ShoppingCart;
import models.User;
import play.libs.Json;
import models.Product;
import play.mvc.Controller;
import play.mvc.Result;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BackendUserController extends Controller {

    public Result login(){
        JsonNode parameter = request().body().asJson();
        if(!parameter.has("account") || !parameter.has("password")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
        BackendUser backendUser = BackendUser.findBackendUserByAccount(parameter.get("account").asText());
        if(backendUser==null){
            return ok(Json.newObject().put("message", "there is no such user"));
        }
        if(!backendUser.getPassword().equals(parameter.get("password").asText())){
            return ok(Json.newObject().put("message", "incorrect user password"));
        }
        backendUser.setUserUUID(UUID.randomUUID().toString().replaceAll("-",""));
        backendUser.update();
        return ok(Json.newObject().put("key",backendUser.getUserUUID()));
    }

    public Result insert(){
        JsonNode parameter = request().body().asJson();
        BackendUser backendUser = new BackendUser(parameter.get("user_id").asText(),parameter.get("user_name").asText(),parameter.get("user_gender").asText(),parameter.get("user_uuid").asText(),parameter.get("user_account").asText(),parameter.get("user_password").asText());
        backendUser.save();
        return ok(Json.newObject().put("insert","success"));
    }
}
