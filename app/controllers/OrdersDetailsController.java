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
import play.mvc.Controller;
import play.mvc.Result;

import java.lang.reflect.Array;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class OrdersDetailsController extends Controller {

    public Result searchOrderDetails() {
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
}
