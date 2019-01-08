package PEI.PEIProject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CurrencyDetailsController {

    @RequestMapping("/getCurrencyDetails")
    public String getCurrencyDetails() {
        MongoConnector mongo = new MongoConnector();
        String res = mongo.getData("PEIProject", "CurrencyDetails");
        return res;
    }

    @RequestMapping("/aggregateCurrencyDetailsByQueryString")
    public String aggregateCurrencyDetailsByQueryString(@RequestParam(value="query") String query) {
        //Example: %7B%24group%3A%7B%22_id%22%3Anull%2Ccount%3A%7B%24sum%3A1%7D%7D%7D para {$group:{"_id":null,count:{$sum:1}}} -> utilizar: https://meyerweb.com/eric/tools/dencoder/
        MongoConnector mongo = new MongoConnector();
        String res = mongo.aggregateDataByQueryString("PEIProject", "CurrencyDetails", query);
        return res;
    }

}
