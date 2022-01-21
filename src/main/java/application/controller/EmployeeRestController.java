package application.controller;

import application.model.Employee;
import application.repository.EmployeeRepositoryImpl;
import application.util.EmployeeSortingUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import application.repository.EmployeeRepository;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeRestController {
    private final EmployeeRepository repository;

    public EmployeeRestController() {
        this.repository = new EmployeeRepositoryImpl();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insert(@RequestBody List<Employee> employees) {
        repository.insert(employees);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAll() {
        return repository.getAll();
    }

    @GetMapping(value = "/sorted", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAllSorted() {
        return EmployeeSortingUtil.sort(repository.getAll());
    }

    @GetMapping(value = "/reverse", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAllSortedReversed() {
        return EmployeeSortingUtil.sortReversed(repository.getAll());
    }

    @PostMapping(value = "/compare", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getCommonPart(@RequestBody List<Employee> employees) {
        employees.retainAll(repository.getAll());
        return employees;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void put(@RequestBody List<Employee> employees) {
        if (employees.size() == 2) {
            repository.change(employees.get(0), employees.get(1));
        } else {
            throw new RestClientException("Неверно указаны параметры запроса");
        }
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody Employee employee) {
        repository.delete(employee);
    }
}
