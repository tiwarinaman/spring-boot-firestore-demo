package com.naman.springbootfirestoredemo.repository;

import com.naman.springbootfirestoredemo.entity.Student;
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StudentRepository extends FirestoreReactiveRepository<Student> {

    Flux<Student> findStudentByFirstName(String firstName);

}