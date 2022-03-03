package com.naman.springbootfirestoredemo.controller;

import com.naman.springbootfirestoredemo.entity.Student;
import com.naman.springbootfirestoredemo.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping
    public ResponseEntity<Mono<Student>> saveStudent(@RequestBody Student student) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(studentService.saveStudent(student));
    }

    @GetMapping
    public ResponseEntity<Flux<Student>> retrieveAll() {
        return ResponseEntity.ok(studentService.retrieveAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Student>> retrieveById(@PathVariable("id") String id) {
        return ResponseEntity.ok(studentService.retrieveStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mono<Student>> updateStudent(@RequestBody Student student,
                                                       @PathVariable("id") String id) {
        return ResponseEntity.ok(studentService.updateStudentDetails(student, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<Student>> deleteStudent(@PathVariable("id") String id) {
        return ResponseEntity.ok(studentService.deleteById(id));
    }

    @GetMapping("/core")
    public List<Student> printStudents() throws ExecutionException, InterruptedException {
        return studentService.fetchStudentsDataWithFirestoreBean();
    }

}
