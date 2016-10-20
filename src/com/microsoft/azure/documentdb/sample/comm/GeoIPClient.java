package com.microsoft.azure.documentdb.sample.comm;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

/**
 * Created by vazvadsk on 2016-10-19.
 */
public class GeoIPClient {

    //ip = "46.228.25.202";

    public String query(String ip){
        String ret = "";
        if(ip == null){
            ip = "46.228.25.202";
        }
        try {
            // REST client for GeoIP service
            // ### please change IP address to VM ip address
            HttpResponse<JsonNode> jrequest = Unirest.get("http://0.0.0.0:5000/" + ip)
                    //.queryString("name", "Mark")
                    //.field("last", "Polo")
                    .asJson();
            // retrieve the parsed JSONObject from the response
            JSONObject myObj = jrequest.getBody().getObject();

            // extract fields from the object
            ret = ip + " - " + myObj.getJSONObject("Data").getJSONObject("Country").getJSONObject("Names").getString("en");
            if(myObj.getJSONObject("Data").getJSONObject("City").getJSONObject("Names") == null){
                ret += ", Unknown City";
            }else{
                ret += ", " + myObj.getJSONObject("Data").getJSONObject("City").getJSONObject("Names").getString("en");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
