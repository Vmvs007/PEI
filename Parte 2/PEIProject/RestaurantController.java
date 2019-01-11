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
    public String getData(@RequestParam(value="store") String store,@RequestParam(value="month") int mes,@RequestParam(value="year")int ano){
        String out="";
        MongoConnector mongo = new MongoConnector();
        String data=""+ano+"-0"+mes+"";
        String query="[{$match:{StoreName: {$eq: '"+store+"'},OrderDate:{$gte:'"+data+"-01 00:00:00.000',$lte:'"+data+"-31 00:00:00.000'}}}]"; // Todas as vendas de uma loja num determinado exercicio
        String query2 = "[{$match:{StoreName: {$eq: '" + store +"'}}},{$group:{_id:'$ReceiptID',AveragePrice:{$avg:'$UnitPrice'}}}]"; // Media de preço de produto vendidos por sale
        String query4 ="[{$match:{StoreName:{$eq: '"+store+"'}}},{$group:{_id:'$ReceiptID',count:{$sum:'$Quantity'}}}]"; // total por sale
        String query5 ="[{$match:{StoreName:{$eq: '"+store+"'}}},{$group:{_id:'$ReceiptID',count:{$sum:1}}}]"; // total produtos diferentes por sale
        String query6 ="[{$match:{StoreName:{$eq: '"+store+"'}}},{$group:{_id:quantidade,count:{$sum:'$Quantity'}}}]";//Total de Produtos por store
        String query7 ="[{$match:{StoreName:{$eq: '"+store+"'}}},{$group:{_id:'$ReceiptID',first:{$first:'$SubTotal'}}},{$group:{_id:soma,count:{$sum:'$first'}}}]";//Total total das vendas por loja
        String query8 = "[{$match:{StoreName: {$eq: '" + store +"'}}},{$group:{_id:'$StoreName',AveragePrice:{$avg:'$UnitPrice'}}}]";// Valor médio do preço de venda dos produtos por loja
        String query9 = "[{$match:{StoreName: {$eq: '" + store + "'},OrderDate:{$gte:'"+data+"-01 00:00:00.000',$lte:'"+data+"-31 00:00:00.000'}}},{$group:{_id:\"$ProductID\"}},{$group:{_id:1,count:{$sum:1}}}]";// Número total de produtos diferentes por exercicio
        String query10= "[{$match:{StoreName: {$eq: '" + store + "'},OrderDate:{$gte:'"+data+"-01 00:00:00.000',$lte:'"+data+"-31 00:00:00.000'}}},{$group:{_id:\"$Customer\"}},{$group:{_id:1,count:{$sum:1}}}]";//Número total de clientes diferentes por exercicio
        String query11 = "[{$match:{StoreName: {$eq: '" + store + "'},OrderDate:{$gte:'"+data+"-01 00:00:00.000',$lte:'"+data+"-31 00:00:00.000'}}},{$group:{_id:'$Customer',count:{$sum:'$LineTotal'}}},{$sort:{count: -1}}]"; //Valor vendido por cada cliente
        String query12 = "[{$match:{StoreName: {$eq: '" + store + "'},OrderDate:{$gte:'"+data+"-01 00:00:00.000',$lte:'"+data+"-31 00:00:00.000'}}},{$group:{_id:'$ProductID',count:{$sum:'$Quantity'}}},{$sort:{count: -1}}]"; //Por produto, apresentar o total de unidade vendidas
        String query13 = "[{$match:{StoreName: {$eq: '" + store + "'},OrderDate:{$gte:'"+data+"-01 00:00:00.000',$lte:'"+data+"-31 00:00:00.000'}}},{$group:{_id:'$CurrencyRateID',count:{$sum:'$LineTotal'}}},{$sort:{count: -1}}]"; //Valor total da venda por cada moeda utilizada

        out ="Query 1 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query)+"\n";
        out += "Query 2 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query2)+"\n";
        out += "Query 4 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query4)+"\n";
        out += "Query 5 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query5)+"\n";
        out += "Query 6 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query6)+"\n";
        out += "Query 7 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query7)+"\n";
        out += "Query 8 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query8)+"\n";
        out += "Query 9 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query9)+"\n";
        out += "Query 10 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query10)+"\n";
        out += "Query 11 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query11)+"\n";
        out += "Query 12 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query12)+"\n";
        out += "Query 13 - " +mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query13)+"\n";


        //out += mongo.aggregateDataByQueryString("PEIProject", "SalesDetails", query4);

        return out;

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