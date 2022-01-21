package application.repository;

import application.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository{
    private static final List<Employee> EMPLOYEE_STORAGE = new ArrayList<>();

    @Override
    public void insert(List<Employee> employees) {
        EMPLOYEE_STORAGE.addAll(employees);
    }

    @Override
    public List<Employee> getAll() {
        return new ArrayList<>(EMPLOYEE_STORAGE);
    }

    @Override
    public void change(Employee oldEmployee, Employee newEmployee) {
        if (EMPLOYEE_STORAGE.remove(oldEmployee)) {
            EMPLOYEE_STORAGE.add(newEmployee);
        }
    }

    @Override
    public void delete(Employee employee) {
        EMPLOYEE_STORAGE.remove(employee);
    }
}
