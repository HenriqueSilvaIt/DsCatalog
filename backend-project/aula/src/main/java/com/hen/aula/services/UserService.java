package com.hen.aula.services;

import com.hen.aula.dto.*;
import com.hen.aula.entities.Category;
import com.hen.aula.entities.Role;
import com.hen.aula.entities.User;
import com.hen.aula.projections.UserDetailsProjection;
import com.hen.aula.repositories.CategoryRepository;
import com.hen.aula.repositories.RoleRepository;
import com.hen.aula.repositories.UserRepository;
import com.hen.aula.services.expections.DatabaseException;
import com.hen.aula.services.expections.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service /*Essa anotation registra essa classe  como um componente
que vai particiar do sistema de depência automatizado do spring, se for
um component genérico que não tem um significado especifico pode colocar Component*/
public class UserService implements UserDetailsService {

    @Autowired /* Injeção de dependencia automatica*/
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {

        // Busca lista de categoria no banco de dados e salva nessa list
        Page<User> list = repository.findAll(pageable);

        /*        List<UserDTO> listDto  = .
        Stream é um recurso do java 8 em diante
        que permit vocÊ trabalhar com funções de alta ordem
         com funções inclusiv com expressões lambda que é um recurso
          de programação funcional

          o map aplica uma função com cada item da sua lista;
          ele transforma um elemento x que era de list para um novo elemento
          dto de UserDTo para cada item da list de User
          ai tem que usar o collect para transformar a stream em lista denovo */

        return list.map(x -> new UserDTO(x)); /* o repository já tem o método
        find all*/

    }

    @Transactional
    public UserDTO findById(Long id) {
        /*Optional é uma abordagem que veio desde do Java 8
        para evitar você trabalhar com valor nulo, então esse variavel
        que criamos nunca vai ser um objeto nulo, ela vai ser objeto Optional
        e dentro desse Optional pode ter ou não um objeto User por exemplo
        dentro dele, tem gente que defende
        utilizar esse Optional tem gente que fala que não precisa
        desde que você faça uma programação certinho pra checar se é nulo
         */
        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not " +
                "found")); /* Se o User n existir lança exceção, o método  get do optional
        obttem a entdidade  que está dentro do optinal*/

        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto)
    /* esse DTO específico t    em todos
    * os atributos da super classe  UserDTO e mais a senha*/{

        User entity = new User();
       /* entity.setName(dto.getName());*/
        copyDtotoEntity(dto, entity);



        /*agora considerando a criação do usuário
         * vamos ignorar o DTO que inclui os roles (porque o usuário n vai mais
         * digitar os roles por padrão ele vai ter role operator) e colocar
         * direto o usuário que se cadastrar como role operator */

        entity.getRoles().clear(); /*limpa a lista de perfis no caso
            de alteração ou inserçã*/

        Role role = roleRepository.findByAuthority("ROLE_OPERATOR");
        entity.getRoles().add(role); /*agora estamos inserindo um usuário
        que vai ter somente o role de operato*/

        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        /*para pegar especificamente a senha que o usuário digitar,
         como a senha está especificamento no UserInsertDto, não
         colocamos no método copyDtoToEntity, porque aquele método
         pega o UserDTO e dentro desse dto n tem a senha
        * mas no caso ele vai pegara senha criptrografada
        * usando passwordEncoder que criamos no método BCryptEnconder
        * e injetamos esse método aqui, .encode é que transforma
        * o string em um código criptografado */

       entity =  repository.save(entity); /*o save retorna uma referencia
       para entidade salva por isso fazemos entity = recebe uma referencia
       para entidade dele*/

        /*retornando entidade convertidade para um User dto*/

        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {

        try { /*Esse método pode gerar uma exceção se o Id N exist na hora de atualzia*/
            User entity = repository.getReferenceById(id); /*
        O findById ele consulta o id no banco de dados e quando vocÊ manda salvar
        você acessa o banco de dados 2 vezes
        Para atualizar sem precisar ir no banco de dados duas vezes
        vocÊ usa esse método getReferenceById que ele não vai no banco de dados
        ele instancia um objeto provisório com o id e só quando vocÊ manda
        salvar que aí sim ele vai no banco de dados */


            /*Atualiza a entidade com o nome que vir no DTO pela requisição */
            //entity.setName(dto.getName());

            copyDtotoEntity(dto, entity);
            /*salva no banco de dados*/


            entity = repository.save(entity);

            /*retorna objeto atualizad*/
            return new UserDTO(entity);
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

        public void copyDtotoEntity(UserDTO dto, User entity) {
            entity.setFirstName(dto.getFirstName());
            /* o id n entra porque a gente não coloca ele na hora de atualizar ou inserir*/
            entity.setLastName(dto.getLastName());
            entity.setEmail(dto.getEmail());


            /*agora considerando a criação do usuário
            * vamos ignorar o DTO que inclui os roles (porque o usuário n vai mais
            * digitar os roles por padrão ele vai ter role operator) e colocar
            * direto o usuário que se cadastrar como role operator */

                entity.getRoles().clear(); /*limpa a lista de perfis no caso
            de alteração ou inserçã*/


            for (RoleDTO roleDto: dto.getRoles()) {
                /*getReferenceByid instancia uma entidade sem precisa acessar
                *  o banco só acessa o banco quando salv*/

                Role entityRole = roleRepository.getReferenceById(roleDto.getId());
                /*eu acesso a lista de Role do produto e faço ess for para cada item da lista de
                * ROLE associada ao produto e  pego o id  da ROLE e instancio uma nova role
                *  com esse id sem acessar o banco de dados ainda (por conta do getReference by id */

                entity.getRoles().add(entityRole); /*ADICIOno
                todos os perfils que usuário digitou no DTO pelo id
                 a entidade getRoles, para
                 depois salvar essa nova lista de perfil do usuário
                 no banco
                 por isso que antes de adionar novos perfis a lista de perfis do
                 usuário eu do clear lá em cima para limpar a lista*/

            }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       /*
         User user = repository.findByEmail(username); aqui o reposítory não vai trazer a lista de roles (perfs
        associado ao usuário porque o relaciomaneto entre User e Role é
        muitos para muitos, dessa forma o carregamento é lazy, ele não trás
        direto o objeto associado como no relacionamento que tem
         uma classe para um associada qu é EAGER

        Não podemos ir na classe User e colcoar @ManyToMany(fetch = FetchType.EAGER)
        porque não é uma boa prática trazer todos objetos associados em um
        relacionamento muitos para muitos

        Para resolver isso, vamos criar uma  consulta customizada no SQL raiz
        que já vai trazer os roles associados ao usuário
         */

        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
        if (result.size() == 0) /*se n tiver
        nada na lista, quer dizer que n encontrou esse usuário  passado como argumento
        na lista*/ {
            throw new UsernameNotFoundException("User not found");
        }

        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword())/*
        result acessa o resultado da lista
        get(0), acessa o primeiro elemento da lista
        getPassword pega a senha desse primeiro elemento*/;

        /*vamos adicionar a lista de roles a objeto desse usuário*/

        for (UserDetailsProjection projection: result) {

            user.addRoles(new Role(projection.getRoleId(), projection.getAuthority()));
            /*Para cada elemento do tipo UserDetailsProjection que tiver
             * na lista result, eu vou adicionar esse perfil ao meu usuário
             * com o método addRole que criei nele, dando new Role e pegando o id e o nome do perfil
             *          pela projection*/
        }

        return user;
    }
}
