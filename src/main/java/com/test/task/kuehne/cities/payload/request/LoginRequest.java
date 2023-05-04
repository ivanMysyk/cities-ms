package com.test.task.kuehne.cities.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
	@NotBlank
  	private String username;

	@NotBlank
	private String password;

}