package application.repository;

import application.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    void insert(List<Employee> employees);

    List<Employee> getAll();

    void change(Employee oldEmployee, Employee newEmployee);

    void delete(Employee employee);
}
