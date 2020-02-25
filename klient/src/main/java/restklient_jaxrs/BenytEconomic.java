package restklient_jaxrs;

import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class BenytEconomic
{
    public static void main(String[] args)
    {
        Client client = ClientBuilder.newClient();
        Response res = client.target("https://restapi.e-conomic.com/?demo=true")
                .request(MediaType.APPLICATION_JSON).get();
        String svar = res.readEntity(String.class);
        System.out.println(svar);

        try {
            //Parse svar som et JSON-objekt
            JSONObject json = new JSONObject(svar);
            System.out.println("json=" + json);

            System.out.println("apiName=" + json.getString("apiName"));
            System.out.println("version=" + json.getString("version"));
            System.out.println("serverTime=" + json.optString("serverTime"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
