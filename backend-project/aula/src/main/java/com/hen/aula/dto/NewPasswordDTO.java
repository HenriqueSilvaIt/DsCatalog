package com.hen.aula.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class NewPasswordDTO {

    @NotBlank(message = "Campo obrigatório")
    private String token;

    @NotBlank(message = "Campo obrigatório")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Deve ter no mínimo 8 caracteres \n" +
                    "Ao menos um número \n" +
                    "Ao menos 1 maiúsculo e mínusculo \n " +
                    "Deve um caractere especial")
    @Size(min = 8, message = "Deve ter no mínimo 8 caracteres")
    private String password;

    public NewPasswordDTO() {

    }

    public NewPasswordDTO(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }
}
