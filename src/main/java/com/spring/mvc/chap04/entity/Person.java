package com.spring.mvc.chap04.entity;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Person {

	private int id;
	private String personName;
	private int personAge;
}
