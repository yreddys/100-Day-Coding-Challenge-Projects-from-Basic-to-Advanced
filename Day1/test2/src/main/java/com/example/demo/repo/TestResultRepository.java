package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TestResult;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
}
