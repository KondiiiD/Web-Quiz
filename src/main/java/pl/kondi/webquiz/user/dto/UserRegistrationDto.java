package pl.kondi.webquiz.user.dto;

import jakarta.validation.constraints.Size;


public record UserRegistrationDto(String email,
                                  @Size(min = 5) String password) {
}
