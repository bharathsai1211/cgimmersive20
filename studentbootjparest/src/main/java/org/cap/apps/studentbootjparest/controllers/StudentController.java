package org.cap.apps.studentbootjparest.controllers;

import org.cap.apps.studentbootjparest.dto.CreateStudentRequest;
import org.cap.apps.studentbootjparest.dto.StudentDetails;
import org.cap.apps.studentbootjparest.dto.UpdateStudentRequest;
import org.cap.apps.studentbootjparest.entities.Student;
import org.cap.apps.studentbootjparest.exceptions.StudentNotFoundException;
import org.cap.apps.studentbootjparest.service.IStudentService;
import org.cap.apps.studentbootjparest.util.StudentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @NotBlank on string to validate string is not empty
 * @Size(min=2, max=5) to validate string is of length greater than equal to two and smaller than equal to 5
 * @Min(5) to validate number whether number is minimum 5
 * @Max(5) to validate number whether number is maximum 5
 * @Validated on class will help triggering validation on methods
 * @Valid if you require to trigger validations on the fields of object
 */
@Validated
@RequestMapping("/students")
@RestController
public class StudentController {

    @Autowired
    private IStudentService service;

    @Autowired
    private StudentUtil studentUtil;

    /**
     * effective url will be http://localhost:8585/students/add
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public StudentDetails add(@RequestBody @Valid CreateStudentRequest requestData) {
        Student student = new Student(requestData.getName(), requestData.getAge());
        student = service.save(student);
        StudentDetails details = studentUtil.toDetails(student);
        return details;
    }

    @PutMapping("/update")
    public StudentDetails update(@RequestBody @Valid UpdateStudentRequest requestData) {
        Student student = new Student(requestData.getName(), requestData.getAge());
        student.setId(requestData.getId());
        student = service.update(student);
        StudentDetails details = studentUtil.toDetails(student);
        return details;
    }


    @GetMapping("/get/{id}")
    public StudentDetails fetchStudent(@PathVariable("id") Integer id) {
        Student student = service.findById(id);
        StudentDetails details = studentUtil.toDetails(student);
        return details;
    }


    @GetMapping("/by/name/{name}")
    public List<StudentDetails> findStudentByName(@PathVariable("name") @NotBlank @Size(min = 2, max = 10) String name) {
        List<Student> students = service.findByName(name);
        List<StudentDetails> response = studentUtil.toDetails(students);
        return response;
    }



    @GetMapping
    public List<StudentDetails> fetchAll() {
        List<Student> students = service.findAll();
        // List<StudentDetails>response= students.stream().map(student->toDetails(student)).collect(Collectors.toList());
        List<StudentDetails> response = studentUtil.toDetails(students);
        return response;
    }


    @DeleteMapping("/remove/{id}")
    public String removeStudent(@PathVariable("id") Integer id) {
        service.deleteById(id);
        String response = "removed student with id=" + id;
        return response;
    }

}
