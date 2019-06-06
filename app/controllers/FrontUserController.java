package controllers;

import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.List;
import java.util.UUID;

public class FrontUserController extends Controller{

    public Result login(){
        JsonNode parameter = request().body().asJson();
        if(!parameter.has("account") || !parameter.has("password")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
        FrontUser frontUser = FrontUser.findFrontUserByAccount(parameter.get("account").asText());
        if(frontUser==null){
            return ok(Json.newObject().put("message", "there is no such user"));
        }
        if(!frontUser.getPassword().equals(parameter.get("password").asText())){
            return ok(Json.newObject().put("message", "incorrect user password"));
        }
        frontUser.setUserUUID(UUID.randomUUID().toString().replaceAll("-",""));
        frontUser.update();
        return ok(Json.newObject().put("key",frontUser.getUserUUID()));
    }

    public Result insert(){
        JsonNode parameter = request().body().asJson();
        FrontUser frontUser = new FrontUser(parameter.get("user_id").asText(),parameter.get("user_name").asText(),parameter.get("user_gender").asText(),parameter.get("user_uuid").asText(),parameter.get("user_account").asText(),parameter.get("user_password").asText());
        frontUser.save();
        return ok(Json.newObject().put("insert","success"));
    }
}
