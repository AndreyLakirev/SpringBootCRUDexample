package com.lakirev.employee.service;

import com.lakirev.employee.model.Employee;
import com.lakirev.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(List<Employee> employees) {
        repository.saveAll(employees);
    }

    @Override
    public List<Employee> getAll() {
        return (List<Employee>) repository.findAll();
    }

    @Transactional
    @Override
    public void change(Employee employee) {
        if (repository.existsById(employee.getId())) {
            repository.save(employee);
        }
    }

    @Override
    public void delete(Employee employee) {
        repository.delete(employee);
    }
}
