package com.uneb.appsus.Client;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uneb.appsus.DTO.SpecialitiesDTO;

import java.lang.reflect.Type;
import java.util.List;

public class SpecialitiesClient extends BaseClient{


    public SpecialitiesClient(Context context) {
        super(context);
    }

    public List<SpecialitiesDTO> GetSpecialities() {
        String json = loadJSONFromAsset("specialities.json");
        if(json != null){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<SpecialitiesDTO>>() {}.getType();
            return gson.fromJson(json, listType);
        }
        return null;
    }
}
