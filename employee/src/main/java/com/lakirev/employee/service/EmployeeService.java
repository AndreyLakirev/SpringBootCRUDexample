package com.lakirev.employee.service;

import com.lakirev.employee.model.Employee;

import java.util.List;

public interface EmployeeService {
    void save(List<Employee> employees);

    List<Employee> getAll();

    void change(Employee employee);

    void delete(Employee employee);
}
