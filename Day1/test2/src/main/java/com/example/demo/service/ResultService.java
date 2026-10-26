package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.TestResultRequest;
import com.example.demo.entity.TestResult;
import com.example.demo.repo.TestResultRepository;

@Service
public class ResultService {

	private final TestResultRepository repository;

	public ResultService(TestResultRepository repository) {
		this.repository = repository;
	}

	public List<TestResult> getAll() {
		return repository.findAll();
	}

	public TestResult getById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));
	}

	public TestResult create(TestResultRequest request) {
		validate(request);

		TestResult result = new TestResult();
		copy(request, result);
		return repository.save(result);
	}

	public TestResult update(Long id, TestResultRequest request) {
		validate(request);

		TestResult result = getById(id);
		copy(request, result);
		return repository.save(result);
	}

	public void delete(Long id) {
		repository.delete(getById(id));
	}

	private void validate(TestResultRequest request) {
		if (request.studentName() == null || request.studentName().isBlank() || request.subject() == null
				|| request.subject().isBlank() || request.marks() < 0 || request.totalMarks() <= 0
				|| request.marks() > request.totalMarks()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter valid student, subject and marks");
		}
	}

	private void copy(TestResultRequest request, TestResult result) {
		result.setStudentName(request.studentName().trim());
		result.setSubject(request.subject().trim());
		result.setMarks(request.marks());
		result.setTotalMarks(request.totalMarks());
	}
}
