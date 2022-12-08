package com.example.cache.springcaffeinecache.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cache.springcaffeinecache.customKey.CustomKeyGenerator;
import com.example.cache.springcaffeinecache.model.Employee;
import com.example.cache.springcaffeinecache.repository.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/employees")
@Slf4j
@CacheConfig(cacheNames = {"employees"})
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomKeyGenerator customKeyGenerator;

    @PostMapping
    public ResponseEntity<Employee> save(@RequestBody Employee employee){
        return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    public ResponseEntity<Optional<Employee>> find(@PathVariable(value = "id") Integer id){
        log.info("Employee data fetched from database:: "+id);
        return ResponseEntity.ok(employeeRepository.findById(id));
    }



    @PutMapping("/{id}")
    @CachePut(key = "#id")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Integer id,
                                                   @RequestBody Employee employeeDetails) {
        Optional<Employee> employee = employeeRepository.findById(id);
        employee.get().setName(employeeDetails.getName());
        final Employee updatedEmployee = employeeRepository.save(employee.get());
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(key = "#id", allEntries = true)
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable(value = "id") Integer id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employeeRepository.delete(employee.get());
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }
}
