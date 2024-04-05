package com.spring.mvc.chap05.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AutoLoginRequestDTO {

	private String sessionId;
	private LocalDateTime limitTime;
	private String account;
}
