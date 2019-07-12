package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mysql.cj.xdevapi.JsonArray;
import dataTypes.ErrorMessage;
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
            return ok(Json.toJson(ErrorMessage.NOT_ENOUGH_PARAMETERS.toErrorMap()));
        }
        BackendUser backendUser = BackendUser.findBackendUserByAccount(parameter.get("account").asText());
        if(backendUser==null){
            return ok(Json.toJson(ErrorMessage.ACCOUNT_NOT_FOUNT.toErrorMap()));
        }
        if(!backendUser.getPassword().equals(parameter.get("password").asText())){
            return ok(Json.toJson(ErrorMessage.ACCOUNT_OR_PASSWORD_ERROR.toErrorMap()));
        }
        backendUser.setUserUUID(UUID.randomUUID().toString().replaceAll("-",""));
        backendUser.update();
        return ok(Json.newObject().put("key",backendUser.getUserUUID()));
    }

    public Result insert(){
        JsonNode parameter = request().body().asJson();
        if(!parameter.has("user_id")||!parameter.has("user_name")||!parameter.has("user_gender")||!parameter.has("user_account")||!parameter.has("user_password")){
            return ok(Json.toJson(ErrorMessage.NOT_ENOUGH_PARAMETERS.toErrorMap()));
        }
        String userId = parameter.get("user_id").asText();
        String userName = parameter.get("user_name").asText();
        String gender = parameter.get("user_gender").asText();
        String account = parameter.get("user_account").asText();
        String password = parameter.get("user_password").asText();
        BackendUser backendUser = BackendUser.findBackendUserByAccount(account);
        if(backendUser!=null){
            return ok(Json.toJson(ErrorMessage.ACCOUNT_IS_EXIST.toErrorMap()));
        }
        BackendUser user = new BackendUser();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setUserGender(gender);
        user.setUserUUID(UUID.randomUUID().toString().replaceAll("-",""));
        user.setUserAccount(account);
        user.setUserPassword(password);
        user.save();
        //BackendUser backendUser = new BackendUser(parameter.get("user_id").asText(),parameter.get("user_name").asText(),parameter.get("user_gender").asText(),parameter.get("user_uuid").asText(),parameter.get("user_account").asText(),parameter.get("user_password").asText());
        //backendUser.save();
        return ok(Json.newObject().put("insert","success"));
    }
}
