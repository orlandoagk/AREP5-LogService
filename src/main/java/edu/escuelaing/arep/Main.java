package edu.escuelaing.arep;

import edu.escuelaing.arep.components.Persistence;
import org.json.JSONObject;


import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        Persistence persistence = new Persistence();
        port(getPort());
        get("/getMessages",(req,res)->{
            return persistence.getMessages(10);
        });
        post("/putMessage",(req,res)->{
            JSONObject body = new JSONObject(req.body());
            persistence.putMessage(body.get("message").toString());
            return persistence.getMessages(10);
        });
    }

    private static int getPort(){
        if(System.getenv("PORT")!=null){
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
