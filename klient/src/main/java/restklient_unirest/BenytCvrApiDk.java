package restklient_unirest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class BenytCvrApiDk {
    public static void main(String[] args) throws Exception {

        String url = "https://cvrapi.dk/api?search=nordfalken&country=dk";
        HttpResponse<JsonNode> response = Unirest.get(url).asJson();
        JSONObject json = response.getBody().getObject();
        System.out.println("json = "+json);

        System.out.println("Adressen er: "+json.getString("address"));
        
    }
}
