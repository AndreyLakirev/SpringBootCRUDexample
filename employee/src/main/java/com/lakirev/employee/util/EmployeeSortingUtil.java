package com.lakirev.employee.util;

import com.lakirev.employee.model.Employee;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeSortingUtil {
    public static List<Employee> sort(List<Employee> list) {
        return list.stream().sorted(Comparator.comparing(Employee::getLastName)
        .thenComparing(Employee::getFirstName)
        .thenComparing(Employee::getAge)
        .thenComparing(Employee::getPosition))
        .collect(Collectors.toList());
    }

    public static List<Employee> sortReversed(List<Employee> list) {
        return list.stream().sorted(Comparator.comparing(Employee::getLastName)
                .thenComparing(Employee::getFirstName)
                .thenComparing(Employee::getAge)
                .thenComparing(Employee::getPosition)
                .reversed())
                .collect(Collectors.toList());
    }
}
