package com.interview.iso.models;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lu.nguyenvan2 on 10/28/2015.
 */
public class Answer {

    public long person_id;
    public String result;

    public Answer(long person_id,String res){

        this.person_id = person_id;
        this.result = res;

    }
    public JSONObject convertToJsonArray(){
        JSONObject object = null;
        if(result != null && result.length()>2){
            //convert to Has
            try {
                object = new JSONObject(result);
            }catch (Exception e){}

        }
        return  object;
    }

}
