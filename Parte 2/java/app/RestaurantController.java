package app;
import org.bson.Document;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Building a RESTful Web Service retrieved from: https://spring.io/guides/gs/rest-service/#scratch
 * https://spring.io/guides/gs/rest-service/#scratch
 * Additionally see: https://spring.io/guides/gs/accessing-mongodb-data-rest/
 */
@RestController
public class RestaurantController {

    @RequestMapping("/getRestaurants")
    public String getRestaurants(@RequestParam(value="value") String value) {
        MongoConnector mongo = new MongoConnector();
        String res = mongo.getData("PEIProject", "SalesDetails", "borough", value);
        return res;
    }

    @RequestMapping("/aggregateRestaurantsByQueryString")
    public String aggregateRestaurantsByQueryString(@RequestParam(value="query") String query) {
        //Example: %5B%7B%24group%3A%7B%22_id%22%3Anull%2Ccount%3A%7B%24sum%3A1%7D%7D%7D%5D para [{$group:{"_id":null,count:{$sum:1}}}] -> utilizar: https://meyerweb.com/eric/tools/dencoder/
        MongoConnector mongo = new MongoConnector();
        String res = mongo.aggregateDataByQueryString("restaurantsDB", "restaurants", query);
        return res;
    }

    @RequestMapping("/getdata")
    public String getData(@RequestParam(value="store") String store,@RequestParam(value="mes") int mes,@RequestParam(value="ano")int ano){
        String out="";
        MongoConnector mongo = new MongoConnector();
        String data=""+ano+"-0"+mes+"";
        String query="{$match:{StoreName: {$eq: '"+store+"'},OrderDate:{$gte:'"+data+"-01 00:00:00.000',$lte:'"+data+"-31 00:00:00.000'}}}";
        String query2 = "[{$match:{StoreName: {$eq: '" + store +"'},OrderDate:{$gte:'"+data+"-01 00:00:00.000',$lte:'"+data+"-31 00:00:00.000'}}},{$group:{_id:\"$StoreName\",MediaPrice:{$avg:\"$UnitPrice\"}}}]"; //
        String query3 = "[{$match:{StoreName: {$eq: '"+ store + "',OrderDate:{$gte:'"+data+"-01 00:00:00.000',$lte:'"+data+"-31 00:00:00.000'}}}},{$group:{_id:\"$Customer\"}},{$group:{_id:1,count:{$sum:1}}}]";



        out=mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query);
        String res = mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query);
        String res2 = mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query2);
        return query+" - "+out + res +res2;

    }


    @RequestMapping("/sales")
    public String sales() {
        //Example: %5B%7B%24group%3A%7B%22_id%22%3Anull%2Ccount%3A%7B%24sum%3A1%7D%7D%7D%5D para [{$group:{"_id":null,count:{$sum:1}}}] -> utilizar: https://meyerweb.com/eric/tools/dencoder/
        MongoConnector mongo = new MongoConnector();
        String query = "[{$match:{StoreName: {$in: [\"Better Bike Shop\"]}}},{$group:{_id:\"$StoreName\",MediaPrice:{$avg:\"$UnitPrice\"}}}]"; //
        String query2 = "[{$match:{StoreName: {$in: [\"Better Bike Shop\"]}}},{$group:{_id:\"$Customer\"}},{$group:{_id:1,count:{$sum:1}}}]";
        String res = mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query);
        String res2 = mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query2);
        return res + res2;
    }

}