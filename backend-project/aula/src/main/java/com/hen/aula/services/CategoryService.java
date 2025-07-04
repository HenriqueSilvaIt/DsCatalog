package com.hen.aula.services;

import com.hen.aula.CategoryDTO;
import com.hen.aula.entities.Category;
import com.hen.aula.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service /*Essa anotation registra essa classe  como um componente
que vai particiar do sistema de depência automatizado do spring, se for
um component genérico que não tem um significado especifico pode colocar Component*/
public class CategoryService {

    @Autowired /* Injeção de dependencia automatica*/
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {

        // Busca lista de categoria no banco de dados e salva nessa list
        List<Category> list = repository.findAll();

        List<CategoryDTO> listDto = new ArrayList<>();

        /*Para cada elemento da lista de categoria, para cada item da lista
        eu crio um CategoryDTO  e passo todos eles dentro da listDto
        que ai eu tenho todos elementos da entidade dentro da lista de DTO*/
        for (Category cat : list) {
            listDto.add(new CategoryDTO(cat));
        }

        /*        List<CategoryDTO> listDto  = .
        Stream é um recurso do java 8 em diante
        que permit vocÊ trabalhar com funções de alta ordem
         com funções inclusiv com expressões lambda que é um recurso
          de programação funcional

          o map aplica uma função com cada item da sua lista;
          ele transforma um elemento x que era de list para um novo elemento
          dto de categoryDTo para cada item da list de category
          ai tem que usar o collect para transformar a stream em lista denovo */

        return listDto; /* o repository já tem o método
        find all*/

    }
}
