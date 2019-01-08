package Converter;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.json.XML;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.text;

/**
 * Exemplo baseado no exemplo:MongoJavaExample:
 * É exemplificado de forma simples (apenas com uma main class) como podemos retornar documentos a partir do
 * MongoDB no formato JSON e realizar a sua conversão para XML (sem regras de schema) para suportar o seu
 * mapeamento para um XML específico de acordo com um vocabulário (este exemplo não contempla
 * a validação do documento XML com XSD).
 *
 * Run the example using: gradle run
 */
public class Demo {
    public static void main(String args[]) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("PEIProject");
        MongoCollection<Document> collection = database.getCollection("CurrencyDetails");
        Bson filter = eq("ToCurrencyCode", "USD");
        Bson filter2 = text("");

        //como exemplo apenas estão a ser retornados dois documentos
        FindIterable<Document> cursor = collection.find("").limit(10);
        MongoCursor it = cursor.iterator();
        int i=1;
        String xml="<root>";
        String aux;
        while(it.hasNext()){
            Document obj = (Document) it.next();
            //Demo.processDate(obj);

            JSONObject json = new JSONObject(obj.toJson());
            aux= XML.toString(json, "root").replace("$","").replace("<root>","").replace("</root>","");
            System.out.println(aux);
            xml+=aux;
            //invocação da classe responsável por aplicar o XSL
            //XSLTransformer.transform("resources/", xml,"transformationRules.xsl", "teste"+i+".xml");
            i++;
        }
        xml+="</root>";
        System.out.println(xml);
        XSLTransformer.transform("resources/", xml,"transformationRules.xsl", "testefinal.xml");
        //fechar a ligação ao MongoDB
        mongoClient.close();

    }

    /**
     * Método responsável por tratar o formato das datas do MongoDB. O método utiliza o formato ISODate
     *  (exemplo:ISODate("2014-03-03T00:00:00Z")) para uma string que respeite o formato datetime
     *  do XSD(Exemplo: 2012-06-15T00:00:00Z)
     *
     * @param obj Documento a processar
     */
    public static void processDate(Document obj){
        List<Document> grades = (List<Document>) obj.get("grades");
        for (Document grade : grades) {
            SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            formattedDate.setTimeZone(TimeZone.getTimeZone("UTC"));
            //novo campo newDate para armazenar a conversão da data
            grade.append("newdate",formattedDate.format(grade.getDate("date")).toString());
        }
    }
}