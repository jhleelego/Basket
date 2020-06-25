package com.example.basket.logicalclass;

        import android.util.Log;
        import java.util.HashMap;
        import java.util.Map;

        import com.google.gson.Gson;
        import com.google.gson.JsonParseException;

public class Lson {
    public Lson(String responseBody) {
        transaction(responseBody);
    }
    public void transaction(String stringifyJson) throws JsonParseException {
        Gson gson = new Gson();
        String json = gson.toJson(stringifyJson);
        Map<String, String> map = new HashMap<String, String>();
        map = gson.fromJson(json, map.getClass());

        for(Map.Entry entryMap : map.entrySet()){
            Log.i("map.get(entryMap)", entryMap.getKey().toString());
            Log.i("map.get(entryMap)", entryMap.getValue().toString());
        }
    }

    public static void main(String[] args) {
        String responseBody = "{\"resultcode\":\"00\",\"message\":\"success\",\"response\":{\"id\":\"18378841\"}}";
        Lson lson = new Lson(responseBody);
    }
}
