package controllers;

import models.Product;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class TestController extends Controller {
    public Result getProduct(int id) {
        Product product = Product.findProductById(id);
        if (product == null) {
            return ok(Json.newObject().put("message", "not found"));
        }
        return ok(Json.newObject().put("product_name", product.getName()));
    }
}
