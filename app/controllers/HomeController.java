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
    public Result login(Http.Request request){
        JsonNode parameter = request.body().asJson();
        if(!parameter.has("account")){
            return ok(Json.newObject().put("error_code", "00001"));
        }
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        User user = new User();
        if(!parameter.get("account").asText().equals(user.getaccount())){
            return ok(Json.newObject().put("error_code", "login error! incorrect account"));
        }
        if(!parameter.get("pwd").asText().equals(user.getPwd())){
            return ok(Json.newObject().put("error_code", "login error! incorrect password"));
        }
        response.put("login_success", user.getUsername());
        return ok(response);
    }
}
