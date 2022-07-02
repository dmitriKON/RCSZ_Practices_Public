package practice3.packet;

import org.json.JSONObject;

public class Processor {

    public static String process(Message message) {
        JSONObject jo = new JSONObject();
        jo.put("response", 200);
        String answer = jo.toString();
        return answer;
    }

}
