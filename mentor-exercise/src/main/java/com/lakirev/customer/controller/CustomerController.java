package com.lakirev.customer.controller;

import com.lakirev.json.exception.JsonParseException;
import com.lakirev.json.parser.CustomJsonParser;
import com.lakirev.json.util.JsonParseUtility;
import com.lakirev.customer.model.Customer;
import com.lakirev.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService service;
    private final CustomJsonParser parser;
    private final JsonParseUtility parseUtility;

    public CustomerController(CustomerService service, CustomJsonParser parser, JsonParseUtility parseUtility) {
        this.service = service;
        this.parser = parser;
        this.parseUtility = parseUtility;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insert(@RequestBody String json) {
        List<String> jsons = parseUtility.getMajorJsonObjects(json);
        if (jsons.isEmpty()) return;
        try {
            if (jsons.size() == 1) {
                service.insert(parser.fromJson(jsons.get(0), Customer.class));
            } else {
                for (String jsonObject : jsons) {
                    service.insert(parser.fromJson(jsonObject, Customer.class));
                }
            }
        } catch (JsonParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect json", e);
        }
    }
}
