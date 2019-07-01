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

import play.Logger;
import play.libs.F;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.Json;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.nio.file.WatchService;
import java.util.*;
import java.util.concurrent.CompletionStage;

public class OrdersDetailsController extends Controller {

    @Inject
    WSClient wsClient;

    public CompletionStage<Result> apiTest(JsonNode jsonNode) {
        JsonNode request = Json.newObject().put("total_amount", 100);
        return wsClient.url("http://nas.ecloudmobile.com:9091/eic/api/payTest").post(request).thenApply(wsResponse -> {
            Logger.debug("{}", wsResponse.asJson());
            if (wsResponse.asJson().has("message")) {
                return ok("success");
                /*
                String message = wsResponse.asJson().get("message").asText();
                if(message.equals("success")){
                    return ok("success");
                }else {
                    return ok("success");
                }
                 */
            } else {
                return ok("fail");
            }
        });
    }

    public Result searchOrderDetailsForFront() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid") || !parameter.has("order_id")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        int orderId = parameter.get("order_id").asInt();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        Orders orders = Orders.findOrdersById(orderId);
        if (orders == null) {
            return ok(Json.newObject().put("message", "order id is not exist"));
        }
        if (!frontUser.getUserId().equals(orders.getUserID())) {
            return ok(Json.newObject().put("message", "user do not have such order"));
        }
        List<OrdersDetails> ordersDetailsList = OrdersDetails.findOrdersDetailsListByOrderId(orders.getId());
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode arrayNode = response.putArray("data");
        for (OrdersDetails ordersDetails : ordersDetailsList) {
            arrayNode.addObject().put("id", ordersDetails.getId()).put("order_id", ordersDetails.getOrderId()).put("product_id", ordersDetails.getProductId()).put("quantity", ordersDetails.getQuantity()).put("price", ordersDetails.getPrice()).put("total_amount", ordersDetails.getTotalAmount());
        }
        return ok(response);
    }

    public Result searchOrderDetailsForBackend() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid") || !parameter.has("order_id")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        int orderId = parameter.get("order_id").asInt();
        BackendUser backendUser = BackendUser.findBackendUserByUUID(userUUID);
        if (backendUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        Orders orders = Orders.findOrdersById(orderId);
        if (orders == null) {
            return ok(Json.newObject().put("message", "order id is not exist"));
        }
        List<OrdersDetails> ordersDetailsList = OrdersDetails.findOrdersDetailsListByOrderId(orders.getId());
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode arrayNode = response.putArray("data");
        for (OrdersDetails ordersDetails : ordersDetailsList) {
            arrayNode.addObject().put("id", ordersDetails.getId()).put("order_id", ordersDetails.getOrderId()).put("product_id", ordersDetails.getProductId()).put("quantity", ordersDetails.getQuantity()).put("price", ordersDetails.getPrice()).put("total_amount", ordersDetails.getTotalAmount());
        }
        return ok(response);
    }
}
