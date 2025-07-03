package com.hen.aula.services;

import com.hen.aula.entities.Category;
import com.hen.aula.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service /*Essa anotation registra essa classe  como um componente
que vai particiar do sistema de depência automatizado do spring, se for
um component genérico que não tem um significado especifico pode colocar Component*/
public class CategoryService {

    @Autowired /* Injeção de dependencia automatica*/
    private CategoryRepository repository;

    public List<Category> findAll() {

        return repository.findAll(); /* o repository já tem o método
        find all*/

    }
}
