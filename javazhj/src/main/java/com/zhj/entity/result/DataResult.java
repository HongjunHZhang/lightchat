package com.zhj.entity.result;

/**
 * @author 789
 */
public class DataResult<T> {
    private int code;
    private String msg;
    private T data;

    public DataResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public DataResult() {
    }

    public  static DataResult<Object> successOfNoData(){
        return new DataResult<>(200,"成功",null);
    }

    public  static DataResult<Object> errorOfNoData(){
        return new DataResult<>(400,"失败",null);
    }

    public static<T>  DataResult<T> successOfData(T data){
        return new DataResult<>(200,"成功",data);
    }

    public static  DataResult errorOfData(){
        return new DataResult<>(400,"失败","未知");
    }

    public static<T>  DataResult<T> errorOfData(T data){
        return new DataResult<>(400,"失败",data);
    }

    public static<T>  DataResult<T> errorOfData(int code,T data){
        return new DataResult<>(code,"失败",data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
