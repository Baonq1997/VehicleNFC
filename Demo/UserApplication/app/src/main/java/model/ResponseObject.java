package model;

import okhttp3.ResponseBody;

public class ResponseObject {

    private int status;
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

//    public static ResponseObject ConvertError(ResponseBody responseBody){
//        ResponseObject responseObject = new ResponseObject();
//        responseBody.
//        data = responseBody;
//    }
}
