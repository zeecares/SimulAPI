package apiTest;

import java.awt.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.ContentType;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Map;

import org.apache.http.cookie.Cookie;


public class Action {

	public static void main(String[] args) {
		
		HttpClient httpClient = new DefaultHttpClient();

	    try {
	        HttpPost request = new HttpPost("http://simapi.ucd.ie/44/385/setTem");
	        StringEntity params =new StringEntity("{\"TSetHea\":\"5\",\"TSetCoo\":\"26\"}");
	        params.setContentType("application/json");
	        request.setEntity(params);
	        
	        HttpResponse response = httpClient.execute(request);

   
	        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        String line = "";
	        line = rd.readLine();
	        System.out.println("Output from Server:");
	        System.out.println(line);

	    }catch (Exception ex) {
	    	 System.out.println("Null response");
	    } finally {
	        httpClient.getConnectionManager().shutdown();
	    }

}
}
