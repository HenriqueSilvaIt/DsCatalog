package com.hen.aula.services;

import com.hen.aula.CategoryDTO;
import com.hen.aula.entities.Category;
import com.hen.aula.repositories.CategoryRepository;
import com.hen.aula.services.expections.DatabaseException;
import com.hen.aula.services.expections.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service /*Essa anotation registra essa classe  como um componente
que vai particiar do sistema de depência automatizado do spring, se for
um component genérico que não tem um significado especifico pode colocar Component*/
public class CategoryService {

    @Autowired /* Injeção de dependencia automatica*/
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(PageRequest pageRequest) {

        // Busca lista de categoria no banco de dados e salva nessa list
        Page<Category> list = repository.findAll(pageRequest);

        /*        List<CategoryDTO> listDto  = .
        Stream é um recurso do java 8 em diante
        que permit vocÊ trabalhar com funções de alta ordem
         com funções inclusiv com expressões lambda que é um recurso
          de programação funcional

          o map aplica uma função com cada item da sua lista;
          ele transforma um elemento x que era de list para um novo elemento
          dto de categoryDTo para cada item da list de category
          ai tem que usar o collect para transformar a stream em lista denovo */

        return list.map(x -> new CategoryDTO(x)); /* o repository já tem o método
        find all*/

    }

    @Transactional
    public CategoryDTO findById(Long id) {
        /*Optional é uma abordagem que veio desde do Java 8
        para evitar você trabalhar com valor nulo, então esse variavel
        que criamos nunca vai ser um objeto nulo, ela vai ser objeto Optional
        e dentro desse Optional pode ter ou não um objeto Category por exemplo
        dentro dele, tem gente que defende
        utilizar esse Optional tem gente que fala que não precisa
        desde que você faça uma programação certinho pra checar se é nulo
         */
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not " +
                "found")); /* Se o category n existir lança exceção, o método  get do optional
        obttem a entdidade  que está dentro do optinal*/

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {

        Category entity = new Category();
        entity.setName(dto.getName());

       entity =  repository.save(entity); /*o save retorna uma referencia
       para entidade salva por isso fazemos entity = recebe uma referencia
       para entidade dele*/

        /*retornando entidade convertidade para um category dto*/

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {

        try { /*Esse método pode gerar uma exceção se o Id N exist na hora de atualzia*/
            Category entity = repository.getReferenceById(id); /*
        O findById ele consulta o id no banco de dados e quando vocÊ manda salvar
        você acessa o banco de dados 2 vezes
        Para atualizar sem precisar ir no banco de dados duas vezes
        vocÊ usa esse método getReferenceById que ele não vai no banco de dados
        ele instancia um objeto provisório com o id e só quando vocÊ manda
        salvar que aí sim ele vai no banco de dados */


            /*Atualiza a entidade com o nome que vir no DTO pela requisição */
            entity.setName(dto.getName());

            /*salva no banco de dados*/

            entity = repository.save(entity);

            return new CategoryDTO(entity);
        } catch (ResourceNotFoundException e) {
                throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    /*Essa exceçã DataIntegrityViolationException é na camada do banco de dado, o spring n consegue capturar essa
    execeção corretamente se tiver o Transaction, mas você pode colocar o transaction desde de que tenha o argumento Propagation.Supports( se você executar
    esse método isoladamente ele n coloca transação ele executa só, porém se esse método tiver dentro de outro ele entra na transação ai não funciona ai tem que tirar o transactio*/
    public void delete(Long id) {

            if (!repository.existsById(id)) { /*Se o Id não existir lança exceção abaixo*/
                throw new ResourceNotFoundException("Recurso não encontrado");
            }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
                /*Essa exceção é integridade referencial se eu tentar deletar um objeto
    	que é um atributo de outro objeto existente da falha de integridade referencial*/
            throw new DatabaseException("Falha de integridade referencial");/*Data
            base exception é nossa execeção personalizada que criamos dentro do pacote
            exception do services*/
        }
        }
}
