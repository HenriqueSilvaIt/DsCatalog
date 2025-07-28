package com.hen.aula.dto;

public class UserInsertDTO extends UserDTO /*
herança, ou seja ele é filho do UserDTO, ele vai ter tudo que o userDTO tem*/{

    private String password;

    public UserInsertDTO(){

    }

    public String getPassword() {
        return password;
    }
}
