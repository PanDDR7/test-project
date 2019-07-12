package dataTypes;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import java.util.HashMap;
import java.util.Map;

public enum ErrorMessage {
    //共通
    NOT_ENOUGH_PARAMETERS(10000, "參數不足"),
    ACCESS_TOKEN_EXPIRED(10001, "驗證失敗"),
    ACCOUNT_OR_PASSWORD_ERROR(10002, "帳號或密碼錯誤"),
    API_VERIFY_ERROR(10003, "API驗證錯誤"),
    PROCESS_CODE_ERROR(10004, ""),
    PERMISSION_ERROR(10005, "權限錯誤"),
    TOKEN_IS_EXPIRED(10006, "驗證到期"),

    //前台
    API_KEY_NOT_FOUND(20001, "API KEY 不存在"),
    API_KEY_IS_DELETE(20002, "API KEY 已刪除"),
    API_KEY_DISABLE(20003, "API KEY 已停用"),

    //後台
    OLD_PASSWORD_ERROR(30001, "舊密碼錯誤"),
    PASSWORD_IS_SHORT(30002, "密碼長度過短"),
    API_USER_NOT_FOUND(30003, "API USER 不存在"),
    EMAIL_IS_EXIST(30004, "Email已存在"),
    ACCOUNT_IS_EXIST(30005, "帳號已存在"),
    ACCOUNT_NOT_FOUNT(30006, "帳號不存在"),
    ACCOUNT_DISABLE(30007, "帳號停用中"),
    SERVICE_NOT_FOUND(30008, "服務不存在"),
    PERMISSION_KEY_IS_EXIST(30009, "權限鍵值已存在"),
    PERMISSION_NOT_FOUNT(30010, "該權限不存在"),

    //其它
    SYSTEM_EXCEPTION(90000, "系統執行錯誤");


    private final int code;
    private final String description;

    ErrorMessage(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("ErrorMessage %d => %s", code, description);
    }

    public Map<String, Object> toErrorMap() {
        return toErrorMap(null);
    }

    public Map<String, Object> toErrorMap(String detail) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> error = new HashMap<String, String>();
        result.put("error", error);
        error.put("code", String.valueOf(this.getCode()));
        error.put("message", this.getDescription());
        if (detail != null) {
            error.put("message", detail);
        }
        return result;
    }

    public JsonNode toJson() {
        return Json.toJson(toErrorMap());

    }
}
