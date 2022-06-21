package com.nju.nnt.common;

public class Response<T> {
    /**
     * 200 访问成功
     * 400 内部错误
     * 401 未授权
     *
     *
     */
    private int code;
    private T data;
    private String msg;

    public Response(int code, T data, String msg){
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    public static <T> Response<T> success(T data){
        return new Response(200, data, "");
    }

    public static <T> Response<T> success(T data, String msg){
        return new Response(200, data, msg);
    }

    public static <T> Response<T> error(T data){
        return new Response(400, data, "");
    }

    public static <T> Response<T> error(T data, String msg){
        return new Response(400, data, msg);
    }

    public static <T> Response<T> error(int code, T data, String msg){ return new Response(code, data, msg); }
    public static <T> Response<T> error(int code, String msg){ return new Response(code,null,msg); }
}
