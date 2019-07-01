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
import play.api.libs.ws.*;
import play.libs.F;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;


public class OrdersController extends Controller {
    @Inject
    WSClient wsClient;

    public Result produceOrder() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        List<ShoppingCart> shoppingCartList = ShoppingCart.shoppingCartList(frontUser.getUserId());
        if (shoppingCartList.size() == 0) {
            return ok(Json.newObject().put("message", "there is no product in shoppingCart"));
        }
        int sum = 0;
        for (ShoppingCart shoppingCart : shoppingCartList) {
            sum += shoppingCart.getTotalAmount();
        }
        Orders orders = new Orders();
        orders.setUserId(frontUser.getUserId());
        orders.setUserUUID(frontUser.getUserUUID());
        orders.setTotalAmount(sum);
        orders.setStatus("1");
        orders.save();
        int orderId = orders.getId();
        OrdersDetails ordersDetails = new OrdersDetails();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            ordersDetails.setOrderId(orderId);
            ordersDetails.setProductId(shoppingCart.getProductId());
            ordersDetails.setProductName(shoppingCart.getProductName());
            ordersDetails.setQuantity(shoppingCart.getQuantity());
            ordersDetails.setPrice(shoppingCart.getPrice());
            ordersDetails.setTotalAmount(shoppingCart.getTotalAmount());
            ordersDetails.save();
            ordersDetails = new OrdersDetails();
            shoppingCart.delete();
        }
        return ok(Json.newObject().put("message", "success"));
    }

    public CompletionStage<Result> checkOutShoppingCart() {
        JsonNode parameter = request().body().asJson();
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        List<ShoppingCart> shoppingCartList = ShoppingCart.shoppingCartList(frontUser.getUserId());
        int sum = 0;
        for (ShoppingCart shoppingCart : shoppingCartList) {
            sum += shoppingCart.getTotalAmount();
        }
        JsonNode request = Json.newObject().put("total_amount", sum);
        return wsClient.url("http://nas.ecloudmobile.com:9091/eic/api/payTest").post(request).thenApply(wsResponse -> {
            if (wsResponse.asJson().has("message")) {
                //return ok("success");
                String message = wsResponse.asJson().get("message").asText();
                if (message.equals("success")) {
                    Orders orders = Orders.findOrdersByUserId(frontUser.getUserId());
                    orders.setStatus("1");
                    orders.save();
                    return ok("check out success");
                } else {
                    Orders orders = Orders.findOrdersByUserId(frontUser.getUserId());
                    orders.setStatus("2");
                    orders.save();
                    return ok("check out fail");
                }
            } else {
                return ok("fail");
            }
        });

    }

    public Result cancelOrder() {
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
            return ok(Json.newObject().put("message", "there is no such order"));
        }
        if (!frontUser.getUserId().equals(orders.getUserID())) {
            return ok(Json.newObject().put("message", "user do not have such order"));
        }
        orders.setStatus("2");
        orders.update();
        return ok(Json.newObject().put("message", "cancel Order success"));
    }

    public Result showOrderListForFront() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        FrontUser frontUser = FrontUser.findFrontUserByUUID(userUUID);
        if (frontUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        List<Orders> ordersList = Orders.findOrdersListByUserId(frontUser.getUserId());
        if (ordersList.size() == 0) {
            return ok(Json.newObject().put("message", "user do not have order"));
        }
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode arrayNode = response.putArray("data");
        for (Orders orders : ordersList) {
            arrayNode.addObject().put("id", orders.getId()).put("user_id", orders.getUserID()).put("total_amount", orders.getTotalAmount()).put("status", orders.getStatus());
        }
        return ok(response);

        /*
        WSRequest wsRequest = request().addAttr();
        wsRequest.body();
         */
    }

    public Result showOrderListForBackend() {
        JsonNode parameter = request().body().asJson();
        if (!parameter.has("user_uuid")) {
            return ok(Json.newObject().put("error_code", "00001"));
        }
        String userUUID = parameter.get("user_uuid").asText();
        BackendUser backendUser = BackendUser.findBackendUserByUUID(userUUID);
        if (backendUser == null) {
            return ok(Json.newObject().put("message", "id is not exist"));
        }
        List<Orders> ordersList = Orders.findOrdersList();
        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode arrayNode = response.putArray("data");
        for (Orders orders : ordersList) {
            arrayNode.addObject().put("id", orders.getId()).put("user_id", orders.getUserID()).put("total_amount", orders.getTotalAmount()).put("status", orders.getStatus());
        }
        return ok(response);
    }
}
