package com.stellar.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PwdDto implements Serializable {
    @NotBlank(message = "The new password cannot be empty.")
    private String newPassword;
    @NotBlank(message = "The old password cannot be empty.")
    private String currentPassword;

}
