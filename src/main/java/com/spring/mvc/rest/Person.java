package com.spring.mvc.rest;

import lombok.*;

import java.util.List;

@Getter
@Setter @ToString
public class Person {
	private String name;
	private int age;
	private List<String> hobby;
}
