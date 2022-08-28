package com.stellar.common.lang;

import java.io.Serializable;
import lombok.Data;

@Data
public class Result implements Serializable {
    private int code;// 200: Successful    Others: Exception
    private String msg; // Hint message of status
    private Object data; //Data needed return to frontend

    public static Result succ(int code, String msg, Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result succ(Object data){
        return succ(200,"Successful operation.",data);
    }

    public static Result fail(String msg){
        return fail(400,msg,null);
    }
//    public static Result fail(String msg,Object data){
//        return fail(400,msg,data);
//    }
    public static Result fail(int code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
