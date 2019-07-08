package dataTypes;

public enum OrderStatus {

    other("確認中"),process("進行中"),success("成功"),fail("失敗");

    private String status;

    OrderStatus(String status){
        this.status=status;
    }

    public static OrderStatus setStatus(String input){
        if(input==null){
            return other;
        }
        switch (input){
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

    public String getStatus(){
        return this.status;
    }
}
