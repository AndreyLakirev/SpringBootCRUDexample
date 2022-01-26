package com.lakirev.employee.controller;

import com.lakirev.employee.model.Employee;
import com.lakirev.employee.service.EmployeeService;
import com.lakirev.employee.util.EmployeeSortingUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeRestController {
    private final EmployeeService service;

    public EmployeeRestController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insert(@RequestBody List<Employee> employees) {
        service.save(employees);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAll() {
        return service.getAll();
    }

    @GetMapping(value = "/sorted", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAllSorted() {
        return EmployeeSortingUtil.sort(service.getAll());
    }

    @GetMapping(value = "/reverse", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAllSortedReversed() {
        return EmployeeSortingUtil.sortReversed(service.getAll());
    }

    @PostMapping(value = "/compare", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getCommonPart(@RequestBody List<Employee> employees) {
        employees.retainAll(service.getAll());
        return employees;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void put(@RequestBody Employee employee) {
        service.change(employee);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody Employee employee) {
        service.delete(employee);
    }
}
