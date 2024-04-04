package com.spring.mvc.chap05.dto.response;

import com.spring.mvc.chap05.entity.Auth;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class LoginUserResponseDTO {

	private String account;
	private String name;
	private String email;
	private String auth;

}