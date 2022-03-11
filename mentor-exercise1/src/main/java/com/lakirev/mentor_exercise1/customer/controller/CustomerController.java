package com.lakirev.mentor_exercise1.customer.controller;

import com.lakirev.mentor_exercise1.json.service.JsonParser;
import com.lakirev.mentor_exercise1.json.service.JsonStreamReader;
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

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService service;
    private final JsonParser parser;
    private final JsonStreamReader jsonStreamReader;

    @Value("${customer.controller.buffer-size}")
    private int bufferSize;

    public CustomerController(CustomerService service, JsonParser parser, JsonStreamReader jsonStreamReader) {
        this.service = service;
        this.parser = parser;
        this.jsonStreamReader = jsonStreamReader;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insert(@RequestBody String json) {
        service.insert(parser.fromJsonToList(json, Customer.class));
    }

    @PostMapping(value = "/stream", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void insert(HttpServletRequest request) {
        try (InputStream stream = request.getInputStream()) {
            jsonStreamReader.consumeJsonStream(stream, bufferSize, (s) -> service.insert(parser.fromJson(s, Customer.class)));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Json Parsing error", e);
        }
    }
}
