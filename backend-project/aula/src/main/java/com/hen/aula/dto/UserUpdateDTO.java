package com.hen.aula.dto;

import com.hen.aula.services.validation.UserInsertValid;
import com.hen.aula.services.validation.UserUpdateValid;
import jakarta.validation.Valid;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO /*
herança, ou seja ele é filho do UserDTO, ele vai ter tudo que o userDTO tem*/{


}
