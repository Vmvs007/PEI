package PEI.PEIProject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SalesDetailsController {
    @RequestMapping("/getSalesDetails")
    public String getSalesDetails() {
        MongoConnector mongo = new MongoConnector();
        String res = mongo.getData("PEIProject", "SalesDetails");
        return res;
    }

    @RequestMapping("/aggregateSalesDetailsByQueryString")
    public String aggregateSalesDetailsByQueryString() {
        //Example: %7B%24group%3A%7B%22_id%22%3Anull%2Ccount%3A%7B%24sum%3A1%7D%7D%7D para {$group:{"_id":null,count:{$sum:1}}} -> utilizar: https://meyerweb.com/eric/tools/dencoder/
        MongoConnector mongo = new MongoConnector();
        String query="{$group:{_id:\"$Customer\"}},{$group:{_id:1,count:{$sum:1}}}";
        String res = mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query);
        return res;
    }
    @RequestMapping("/getdata")
    public String getData(@RequestParam(value="store") String store,@RequestParam(value="mes") int mes,@RequestParam(value="ano")int ano){
        String out="";
        MongoConnector mongo = new MongoConnector();
        String data=""+ano+"-0"+mes+"";
        String query="{$match:{StoreName: {$eq: '"+store+"'},OrderDate:{$gte:'"+data+"-01 00:00:00.000',$lte:'"+data+"-31 00:00:00.000'}}}";



        out=mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query);
        return query+" - "+out;

    }
}
