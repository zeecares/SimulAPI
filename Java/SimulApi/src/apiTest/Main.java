package apiTest;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class Main {

	public static void main(String[] args) throws ParseException, InterruptedException  {
		String user_id=null;
		String instance_id=null;
		int timestep=0;
		HttpClient httpClient = new DefaultHttpClient();
		
		/*
		 * Create a new user with response of user id
		 */
		
	    try {
	        HttpPost request1 = new HttpPost("http://simapi.ucd.ie/accounts/create/");	       
	        StringEntity params1 =new StringEntity("{\"email\":\"NewUser@gmail.com\","
	        		+ "\"username\":\"apachewang_new\",\"password\":\"123456\"}");
            params1.setContentType("application/json");
	        request1.setEntity(params1);	                    
	        HttpResponse response1 = httpClient.execute(request1);
	       // System.out.println(response);
	        BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
	        String line1 = "";
	        line1 = rd1.readLine();
	        System.out.println("Output from Server:");
	        System.out.println("     ");
	        System.out.println("Create a new user completed");
	        System.out.println("Details :");
	        System.out.println(line1);
	        System.out.println("-------------------------");
	        JSONParser j1 = new JSONParser();
            JSONObject o1 = (JSONObject)j1.parse(line1);      
	        user_id =  o1.get("user_id").toString();

	      }catch (IOException e) {
	        System.err.println("Fatal transport error: " + e.getMessage());
	        e.printStackTrace();
	      }
	    
	     /*
	     * Create a new instance with response of instance id
	     */
	    
	    try {
	        HttpPost request2 = new HttpPost("http://simapi.ucd.ie/"+user_id+"/create_instance/");
	        StringEntity params2 =new StringEntity("{\"name\":\"Instance_new\"}");
	        params2.setContentType("application/json");
	        request2.setEntity(params2);

	        HttpResponse response2 = httpClient.execute(request2);
	       // System.out.println(response);
	        BufferedReader rd2 = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
	        String line2 = "";
	        line2 = rd2.readLine();
	        System.out.println("Create instance completed");
	        System.out.println("Details:");
	        System.out.println(line2);
	        System.out.println("-------------------------");
	        JSONParser j2 = new JSONParser();
            JSONObject o2 = (JSONObject)j2.parse(line2);
      
	        instance_id =  o2.get("instance_id").toString();

	    }catch (IOException e) {
	        System.err.println("Fatal transport error: " + e.getMessage());
	        e.printStackTrace();
	      }
	    
	    /*
	     * Create a new action 
	     * Set heating temperature and cooling temperature
	     * Time step id is 1
	     */
	    
	    try {
	        HttpPost request3 = new HttpPost("http://simapi.ucd.ie/"+instance_id+"/1/setTem");
	        StringEntity params3 =new StringEntity("{\"TSetHea\":\"10\",\"TSetCoo\":\"26\"}");
	        params3.setContentType("application/json");
	        request3.setEntity(params3);
	        
	        HttpResponse response3 = httpClient.execute(request3);

   
	        BufferedReader rd3 = new BufferedReader(new InputStreamReader(response3.getEntity().getContent()));
	        String line3 = "";
	        line3 = rd3.readLine();
	        System.out.println("Create Action completed");
	        System.out.println("Details:");
	        System.out.println(line3);
	        System.out.println("-------------------------");

	    }catch (IOException e) {
	        System.err.println("Fatal transport error: " + e.getMessage());
	        e.printStackTrace();
	      }
	    /*
	     * Begin the simulation
	     * Set column begin in instance to 1
	     */
	    try {
	        HttpPost request4 = new HttpPost("http://simapi.ucd.ie/"+instance_id+"/begin");
	        HttpResponse response4 = httpClient.execute(request4);
	        BufferedReader rd4 = new BufferedReader(new InputStreamReader(response4.getEntity().getContent()));
	        String line4 = "";
	        line4 = rd4.readLine();
	        System.out.println("Ok Simulation starts");
	        System.out.println(line4);
	        System.out.println("-------------------------");

	    }catch (IOException e) {
	        System.err.println("Fatal transport error: " + e.getMessage());
	        e.printStackTrace();
	    }
	    /*
	     * Get sensor value
	     * Sensor id is 1 by default
	     */
	    try {
	    	timestep=1;
	        int wait_status=0;
            String output_sensor;
            String empty = "Sensor Not found";
	    	while(timestep!=0){
			HttpGet getRequest5 = new HttpGet("http://simapi.ucd.ie/"+instance_id+"/1/"+timestep+"/get_sensor");
			getRequest5.addHeader("accept", "application/json");	 
			HttpResponse response5 = httpClient.execute(getRequest5);
			BufferedReader br5 = new BufferedReader( new InputStreamReader((response5.getEntity().getContent())));
            output_sensor = br5.readLine();

			if (response5.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				   + response5.getStatusLine().getStatusCode());
				}
			  else if(output_sensor.equals(empty)){
				    HttpGet getRequest6 = new HttpGet("http://simapi.ucd.ie/"+instance_id+"/get_begin");
					getRequest6.addHeader("accept", "application/json");	 
					HttpResponse response6 = httpClient.execute(getRequest6);
					if (response6.getStatusLine().getStatusCode() != 200) {
						throw new RuntimeException("Failed : HTTP error code : "
						   + response6.getStatusLine().getStatusCode());
						}else{
						        BufferedReader br = new BufferedReader(
				                new InputStreamReader((response6.getEntity().getContent())));
						        String begin;
						        begin = br.readLine();						        
						        if(begin.equals("2")){
						        	 System.out.println("");
						             System.out.println("--------------------------------");
						             System.out.println("");
						             System.out.println("End of simulation");
						             System.out.println("");
						           	break;
						        }					        						
						}		        	   
				       if(wait_status==0){
				    	   String wait="Waiting for new sensor value";		        	    
	                       System.out.println(wait);
	                       wait_status=1;	
	                       }else{ 
	                    	   continue;
	                    	   }					
			     }else {
				   System.out.println("Out put of sensor: ");
	               System.out.println("Time step <"+timestep+">: \n");
                   System.out.println(output_sensor);
                   System.out.println("");
	               timestep=timestep+1;
	               wait_status=0;				
			      }
			
	    	  }
	    	
		  } catch (ClientProtocolException e) {
	 
			e.printStackTrace();
	 
		  } catch (IOException e) {
	 
			e.printStackTrace();
		  }finally{

		httpClient.getConnectionManager().shutdown();

		  }
    }
}
