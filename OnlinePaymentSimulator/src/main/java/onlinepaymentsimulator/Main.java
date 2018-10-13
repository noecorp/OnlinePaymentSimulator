package onlinepaymentsimulator;

import java.util.Date;
import java.util.HashMap;
import onlinepaymentsimulator.domain.Token;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {
    
    public static String BANK_NAME = "Demo Pankki";
    
    public static void main(String[] args) throws Exception {
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("BANK_NAME", Main.BANK_NAME);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
    }
}
