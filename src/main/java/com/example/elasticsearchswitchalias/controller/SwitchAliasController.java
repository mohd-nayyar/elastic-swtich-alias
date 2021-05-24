package com.example.elasticsearchswitchalias.controller;

import com.example.elasticsearchswitchalias.config.RestClient;
import com.example.elasticsearchswitchalias.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SwitchAliasController {

    @Autowired
    private RestClient restClient;

    @PostMapping("/es/switchAlias")
    public ResponseEntity switchAlias(){
        String url =  "http://localhost:9200/_aliases";

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("pretty", "true");


        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        String body =  "{\n" +
                "    \"actions\" : [\n" +
                "    \t{ \"remove\" : { \"index\" : \"l3_v1\", \"alias\" : \"l3_update\" } },\n" +
                "        { \"add\" : { \"index\" : \"l3_v2\", \"alias\" : \"l3_update\" } },\n" +
                "        { \"remove\" : { \"index\" : \"l3_v2\", \"alias\" : \"l3_insert\" } },\n" +
                "\t\t{ \"add\" : { \"index\" : \"l3_v1\", \"alias\" : \"l3_insert\" } }\n" +
                "\n" +
                "    ]\n" +
                "}";

        RestResponse restResponse = restClient.post(url, paramMap, headerMap, body);
        return ResponseEntity.ok(restResponse);
    }
}
