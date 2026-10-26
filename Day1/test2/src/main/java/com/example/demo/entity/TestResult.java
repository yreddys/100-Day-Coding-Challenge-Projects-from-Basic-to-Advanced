package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_results")
public class TestResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String studentName;

	@Column(nullable = false)
	private String subject;

	@Column(nullable = false)
	private double marks;

	@Column(nullable = false)
	private double totalMarks;

	public Long getId() {
		return id;
	}

	public String getStudentName() {
		return studentName;
	}

	public String getSubject() {
		return subject;
	}

	public double getMarks() {
		return marks;
	}

	public double getTotalMarks() {
		return totalMarks;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setMarks(double marks) {
		this.marks = marks;
	}

	public void setTotalMarks(double totalMarks) {
		this.totalMarks = totalMarks;
	}
}
