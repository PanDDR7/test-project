package dataTypes;

public enum OrderStatus {

    other("0", "確認中"), process("1", "進行中"), success("2", "成功"), fail("3", "失敗");

    private String status;
    private String code;

    OrderStatus(String code, String status) {
        this.status = status;
        this.code = code;
    }

    public static OrderStatus setStatus(String input) {
        if (input == null) {
            return other;
        }
        switch (input) {
            case "0":
                return process;
            case "1":
                return success;
            case "2":
                return fail;
            default:
                return other;
        }
    }

    public String getStatus() {
        return this.status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
