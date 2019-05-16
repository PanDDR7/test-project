package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.libs.Json;
import play.mvc.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }
    /*
    public Result test(Http.Request request) {
        JsonNode parameter = request.body().asJson();
        if (!parameter.has("request_name")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        response.put("response_name", parameter.get("request_name").asText());
        return ok(response);
    }
     */
    public Result test(Http.Request request){
        JsonNode parameter = request.body().asJson();
        User user = new User();
        if(!parameter.has("account")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        if(parameter.get("account").asText().equals(user.getAccount())){
            if(parameter.get("pwd").asText().equals(user.getPwd())){
                response.put("user_name",user.getUsername());
                return ok(response);
            }else {
                return ok("login fail ! password error");
            }
        }
        return ok("login fail ! account error");
    }
}
