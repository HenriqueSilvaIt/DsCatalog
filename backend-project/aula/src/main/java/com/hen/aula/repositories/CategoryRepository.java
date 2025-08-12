package com.hen.aula.repositories;

import com.hen.aula.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*Classe para acesso a banco de dados, isso funciona para qualquer banco de dados
relacional
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


}
