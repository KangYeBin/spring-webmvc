package com.spring.mvc.chap05.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class LoginRequestDto {

	private String account;
	private String password;
	private boolean autoLogin;	// 자동 로그인 체크 여부
}
