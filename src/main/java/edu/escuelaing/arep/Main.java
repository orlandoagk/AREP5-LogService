package edu.escuelaing.arep;

import edu.escuelaing.arep.components.Persistence;


import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {

        port(getPort());
        get("/",(req,res)->{
            Persistence persistence = new Persistence();
            persistence.putMessage(req.queryParams("message"));
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
