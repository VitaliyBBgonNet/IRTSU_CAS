package bbgon.irtsu_cas.dto.request;

import bbgon.irtsu_cas.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateUser {

    @NotBlank
    String id;

    @NotBlank(message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    @Email(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    String email;

    @NotBlank(message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    String lastName;

    @NotBlank(message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    String firstName;

    @Size(min = 3, max = 25, message = ValidationConstants.PASSWORD_NOT_VALID)
    @NotBlank(message = ValidationConstants.PASSWORD_NOT_VALID)
    String password;

    @NotBlank(message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    String surName;

    @NotBlank(message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    String phoneNumber;

    String addInformation;
}
