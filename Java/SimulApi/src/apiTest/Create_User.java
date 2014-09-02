package apiTest;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;



public class Create_User {

	public static void main(String[] args) {
		
		HttpClient httpClient = new DefaultHttpClient();

	    try {
	
	        HttpPost request = new HttpPost("http://simapi.ucd.ie/accounts/create/");
	       
	        StringEntity params =new StringEntity("{\"email\":\"Fiora@gmail.com\",\"username\":\"apachewang_new\",\"password\":\"123456\"}");
            params.setContentType("application/json");
	        request.setEntity(params);	        
            
	        HttpResponse response = httpClient.execute(request);
	       // System.out.println(response);
	        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	        String line = "";
	        line = rd.readLine();
	        System.out.println("Output from Server:");
	        System.out.println(line);
	        httpClient.getConnectionManager().shutdown();
	        /* while ((line = rd.readLine()) != null) {
	               //Parse our JSON response               
	               JSONParser j = new JSONParser();
	               JSONObject o = (JSONObject)j.parse(line);
	               Map response_get = (Map)o.get("response");
	   	           System.out.println("Real Output from Server:");
	               System.out.println(response_get.get("username"));
	        }
	        System.out.println("Null response");*/
	      }catch (IOException e) {
	        System.err.println("Fatal transport error: " + e.getMessage());
	        e.printStackTrace();
	      }

}
}
