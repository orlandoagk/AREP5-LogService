package edu.escuelaing.arep.components;

import org.json.*;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.*;

public class Persistence {

    private Jedis jedis;

    public Persistence(){
        try {
            jedis = new Jedis(getEnvironment());
            jedis.auth("prueba123");
            System.out.println("Me conecté " + jedis.ping());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get the environment but if not it configured return a Server in the Cloud
     * @return The environment that the app use
     */
    public String getEnvironment(){
        if(System.getenv("Environment")!=null){
            System.out.println(System.getenv("Environment"));
            return System.getenv("Environment");
        }
        return "18.212.123.33";
    }

    /**
     * This method save the message in the DB
     * @param message String the message that you want to save in the DB
     */
    public void putMessage(String message){
        JSONObject json = new JSONObject();
        Long millis = System.currentTimeMillis();
        json.put("message",message);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(millis);
        System.out.println(sdf.format(resultdate));
        json.put("dateHuman",sdf.format(resultdate));
        jedis.set(String.valueOf(System.currentTimeMillis()),json.toString());

    }


    /**
     * This method get the last 10 messages that are in the DB
     * @param nMessages int the N's message(s) that you want to return
     * @return ArrayList that have the N message(s) in Json object
     */
    public List<JSONObject> getMessages(int nMessages){
        List<JSONObject> jsonObjects = new ArrayList<>();
        try {

            for(String s :jedis.keys("*")){
                JSONObject jsonObject = new JSONObject(jedis.get(s));
                jsonObjects.add(jsonObject);
                jsonObject.put("computerDate",s);
                System.out.println(s+" "+jedis.get(s));

            }

            jsonObjects = bubbleSort(jsonObjects);
            int size = jsonObjects.size();
            if(jsonObjects.size()>nMessages){
                jsonObjects = jsonObjects.subList(size-nMessages,size);
            }
            System.out.println(jsonObjects.size());
            Collections.reverse(jsonObjects);
            for(JSONObject j:jsonObjects){
                System.out.println(j.toString());
            }



        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return jsonObjects;
    }


    /**
     * This method is a implementation of the common Bubble Sort with List
     * @param array List of Json that you want to sort
     * @return The sort Array
     */
    private List<JSONObject> bubbleSort(List<JSONObject> array) {
        int n = array.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (Long.parseLong(array.get(j).get("computerDate").toString()) > Long.parseLong(array.get(j + 1).get("computerDate").toString())) {


                    array.add(j,array.remove(j+1));



                }
            }
        }
        return array;
    }

}
