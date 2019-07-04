package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.Student;
import com.lambdaschool.school.service.StudentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController
{
    @Autowired
    private StudentService studentService;

    // Please note there is no way to add students to course yet!

    @ApiOperation(value = "Lists all the students in the database", response = Student.class, responseContainer = "List")
    @GetMapping(value = "/students", produces = {"application/json"})
    public ResponseEntity<?> listAllStudents( @PageableDefault(page = 0,
            size = 5)
                                                          Pageable pageable)
    {
        List<Student> myStudents = studentService.findAll(pageable);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    @ApiOperation(value = "Returns a student by their id number", response = Student.class)
    @GetMapping(value = "/Student/{StudentId}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentById(
            @PathVariable
                    Long StudentId)
    {
        Student r = studentService.findStudentById(StudentId);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @ApiOperation(value = "Returns a student by comparing their name to the searched value", response = Student.class)
    @GetMapping(value = "/student/namelike/{name}",
            produces = {"application/json"})
    public ResponseEntity<?> getStudentByNameContaining(
            @PathVariable String name, @PageableDefault(page = 0, size = 5) Pageable pageable)
    {
        List<Student> theStudent = studentService.findStudentByNameLike(name,pageable);
        return new ResponseEntity<>(theStudent, HttpStatus.OK);
    }


//    @GetMapping(value = "/student/namelike/{name}",
//                produces = {"application/json"})
//    public ResponseEntity<?> getStudentByNameContaining(
//            @PathVariable String name,  @PageableDefault(page = 0, size = 5) Pageable pageable)
//    {
//        List<Student> myStudents = studentService.findStudentByNameLike(name ,pageable);
//        return new ResponseEntity<>(myStudents, HttpStatus.OK);
//    }


    @ApiOperation(value = "Adds a new student object to the database", response = void.class)
    @PostMapping(value = "/Student",
                 consumes = {"application/json"},
                 produces = {"application/json"})
    public ResponseEntity<?> addNewStudent(@Valid
                                           @RequestBody
                                                   Student newStudent) throws URISyntaxException
    {
        newStudent = studentService.save(newStudent);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Studentid}").buildAndExpand(newStudent.getStudid()).toUri();
        responseHeaders.setLocation(newStudentURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @ApiOperation(value = "Updates a new student object to the database", response = void.class)
    @PutMapping(value = "/Student/{Studentid}")
    public ResponseEntity<?> updateStudent(
            @RequestBody
                    Student updateStudent,
            @PathVariable
                    long Studentid)
    {
        studentService.update(updateStudent, Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "Deletes a new student object to the database", response = void.class)
    @DeleteMapping("/Student/{Studentid}")
    public ResponseEntity<?> deleteStudentById(
            @PathVariable
                    long Studentid)
    {
        studentService.delete(Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
