package org.mislab.api.event;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.EventObject;
import java.util.Map;

public class OnlineExamEvent extends EventObject {
    private static final Type STR_STR_MAP_TYPE;
    
    static {
        STR_STR_MAP_TYPE = new TypeToken<Map<String, String>>(){}.getType();
    }
    
    private final EventType type;
    private final EventAction action;
    private final Map<String, String> content;
    
    public OnlineExamEvent(JsonObject json) {
        super(json);
        
        type = EventType.valueOf(json.get("type").getAsString());
        action = EventAction.valueOf(json.get("action").getAsString());
        
        if (!json.has("content")) {
            this.content = null;
        } else {
            Gson gson = new Gson();
            String contentStr = json.get("content").getAsJsonObject().toString();
            
            this.content = gson.fromJson(contentStr, STR_STR_MAP_TYPE);
        }
    }
    
    /**
     * get event action
     * @return the action defined in EventAction class
     */
    public EventAction getAction() {
        return action;
    }
    
    /**
     * get event type
     * @return the type defined in EventType class
     */
    public EventType getType() {
        return type;
    }
    
    public Map<String, String> getContent() {
        return content;
    }
    
    @Override
    public String toString() {
      return String.format("<ev:%s,%s>", type, action);
    }
}
