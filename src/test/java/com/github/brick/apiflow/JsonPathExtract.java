package com.github.brick.apiflow;

import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class JsonPathExtract implements Extract {
    Gson gson = new Gson();
    @Override
    public Object extract(Object o, String el) {
        String json = gson.toJson(o);
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        try {
            return JsonPath.read(document, el);
        } catch (Exception e) {
            return null;

        }
    }

    @Override
    public Object extract(String json, String el) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        try {
            return JsonPath.read(document, el);
        } catch (Exception e) {
            return null;
        }
    }
}
