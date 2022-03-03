package com.naman.springbootfirestoredemo.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.naman.springbootfirestoredemo.entity.Student;
import com.naman.springbootfirestoredemo.exception.StudentNotFoundException;
import com.naman.springbootfirestoredemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class StudentService {

    @Autowired
    private Firestore firestore;

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Mono<Student> saveStudent(Student student) {
        return repository.save(student);
    }

    public Flux<Student> retrieveAllStudents() {
        return repository.findAll();
    }

    public Mono<Student> retrieveStudentById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new StudentNotFoundException(id)));
    }

    public Mono<Student> updateStudentDetails(Student student, String id) {
        Student studentToUpdate = repository.findById(id)
                .switchIfEmpty(Mono.error(new StudentNotFoundException(id)))
                .block();

        if (student.getFirstName() != null
                && student.getFirstName().length() > 0
                && !Objects.equals(student.getFirstName(), studentToUpdate.getFirstName())) {
            studentToUpdate.setFirstName(student.getFirstName());
        }

        if (student.getLastName() != null
                && student.getLastName().length() > 0
                && !Objects.equals(student.getLastName(), studentToUpdate.getLastName())) {
            studentToUpdate.setLastName(student.getLastName());
        }

        if (student.getRollNumber() != null
                && !Objects.equals(student.getRollNumber(), studentToUpdate.getRollNumber())) {
            studentToUpdate.setRollNumber(student.getRollNumber());
        }

        return repository.save(studentToUpdate);

    }


    public Mono<Student> deleteById(String id) {
        Mono<Student> student = repository.findById(id)
                .switchIfEmpty(Mono.error(new StudentNotFoundException(id)));

        repository.deleteById(id);
        return student;
    }

    public List<Student> fetchStudentsDataWithFirestoreBean() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = firestore.collection("students").get();
        QuerySnapshot queryDocumentSnapshots = query.get();
        List<QueryDocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
        List<Student> students = new ArrayList<>();

        documents.forEach(document -> students.add(document.toObject(Student.class)));
        return students;
    }

}

