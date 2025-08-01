package com.hen.aula.dto;

import com.hen.aula.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO /*
herança, ou seja ele é filho do UserDTO, ele vai ter tudo que o userDTO tem*/{

    @NotBlank(message = "Campo obrigatório")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
    message = "Deve ter no mínimo 8 caracteres \n" +
            "Ao menos um número \n" +
            "Ao menos 1 maiúsculo e mínusculo \n " +
            "Deve um caractere especial")
    @Size(min = 8, message = "Deve ter no mínimo 8 caracteres")
    private String password;

    public UserInsertDTO(){

    }

    public String getPassword() {
        return password;
    }
}
