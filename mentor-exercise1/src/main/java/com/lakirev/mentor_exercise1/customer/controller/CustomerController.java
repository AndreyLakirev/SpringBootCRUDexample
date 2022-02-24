package com.lakirev.mentor_exercise1.customer.controller;

import com.lakirev.mentor_exercise1.json.exception.JsonParseException;
import com.lakirev.mentor_exercise1.json.parser.ReflectionJsonParser;
import com.lakirev.mentor_exercise1.json.util.JsonParseUtility;
import com.lakirev.mentor_exercise1.customer.model.Customer;
import com.lakirev.mentor_exercise1.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService service;
    private final ReflectionJsonParser parser;
    private final JsonParseUtility parseUtility;

    @Value("${customer.controller.buffer-size}")
    private int bufferSize;

    public CustomerController(CustomerService service, ReflectionJsonParser parser, JsonParseUtility parseUtility) {
        this.service = service;
        this.parser = parser;
        this.parseUtility = parseUtility;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insert(@RequestBody String json) {
        List<String> jsons = parseUtility.getMajorJsonObjects(json.toCharArray());
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

    @PostMapping(value = "/stream", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void insert(HttpServletRequest request) {
        try (InputStream stream = request.getInputStream()) {
            parser.fromJsonStream(stream, bufferSize, Customer.class, service::insert);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Json Parsing error", e);
        }
    }
}
