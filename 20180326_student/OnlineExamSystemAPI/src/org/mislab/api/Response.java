package org.mislab.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Map;

public class Response {
    private final ErrorCode errorCode;
    private Map<String, Object> content;
    
    public Response(JsonObject json) {
        this.errorCode = ErrorCode.values()[json.get("errorCode").getAsInt()];
        
        if (errorCode == ErrorCode.OK) {
            try {
                JsonObject contentJson = json.get("content").getAsJsonObject();
                
                Gson gson = new Gson();
                this.content = (Map) gson.fromJson(contentJson, Object.class);
                this.content = Utils.convert2Integer(this.content);
            } catch (NullPointerException ex) {
                this.content = null;
            }
        } else {
            this.content = null;
        }
    }
    
    public Response(ErrorCode code) {
        this.errorCode = code;
        this.content = null;
    }
    
    public boolean success() {
        return errorCode == ErrorCode.OK;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public Map<String, Object> getContent() {
        return content;
    }
    
    protected void addContent(String key, Object obj) {
        content.put(key, obj);
    }
}
