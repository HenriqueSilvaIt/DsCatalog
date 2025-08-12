package com.hen.aula.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hen.aula.dto.ProductDTO;
import com.hen.aula.entities.Product;
import com.hen.aula.services.ProductService;
import com.hen.aula.services.expections.DatabaseException;
import com.hen.aula.services.expections.ResourceNotFoundException;
import com.hen.aula.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProductResource.class,
excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class ProductsResourcesTests { /*Simula os comportamentos do product
service*/

    @Autowired /*Essa clase  ajuda na */
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    /*Vamos converter o objeto java productDTO para texto/json para pssar
     * no corpo da requisição http em update e insert
     *
     * Não tem problema fazer o autowired no object mapp porque ele é um
     * objeto auxiliar ele n é uma depêncendia do seu productResourco*/


    @Mock/*Mock normalmente utiliza
    quando estamos testando unidade é mais rapido e exunto
    quando a classe de teste carrega o contexto da aplicação
    e precisa mocar algum bin do sistema usamos o mock bin e ele
    vai substituir o componente que você vai colocar no mock bean por um
    compoenete mockado*/
    private ProductService service;

    private PageImpl<ProductDTO> page;
    /*PageImpl é um page que aceita o new e dar para criar
    * um objeto de página*/

    private ProductDTO productDTO;

    private Long existingId;
    private Long dependentId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of());
                /*List.of permite que eu instanci uma lista já
                * com ele mento dentro del*/

        when(service.findAllPaged(any(), "1", page.getPageable())).thenReturn(page);
        /*any é do ArgumentsMatchers.any() que significa qualquer argumento
        , se você preferir
        * estático é só colocar name e garatir que  o import foi
        * do ArgumentsMatchers corretamente*/

        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(service.update(eq(existingId), any())).thenReturn(productDTO);
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        /*para usar o any como argumento qualquer no update
        * tem que colcoar o eq antes do existingid pois os outros
        * argumentos n pode ser argumentos simples eq é do ArgumentsMathers também*/

        /*quando o método é void
        *primeiro colocamos a ação depois o when*/
        doNothing().when(service).delete(existingId); /*FAÇA nada
        quando deleta um id existente*/
        doThrow(DatabaseException.class).when(service).delete(dependentId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        /*no service ele lança exceção qando é id n encontrado é só repository que n lança*/

        when(service.insert(any())).thenReturn(productDTO);

    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }


    @Test
    public void createdShouldReturnCreatedAndProducDTOWhenExistsId() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(post("/products")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                     );

        result.andExpect(status().isCreated());

    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception{

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNoContent());

    }


    @Test
    public void updateShouldReturnProducDTOWhenIdExists() throws Exception {

        /*Vamos converter o objeto java productDTO para texto/json para pssar
        * no corpo da requisição http em update e insert
        * o objectMapper tem um método que converte o objeto java em um string*/
        String jsonBody = objectMapper.writeValueAsString(productDTO);
    /*depois só passar o objeto como string no content*/

        ResultActions result =
                mockMvc.perform(put("/products/{id}", existingId)
                                .content(jsonBody)/*aqui é passado
                                 o corpo da requisição que o objeto
                                 productDTO com strin*/
                                .contentType(MediaType.APPLICATION_JSON)/*
                                aqui é o tipo do conteúdo passo no corpo
                                da requisição qu Json*/
                        .accept(MediaType.APPLICATION_JSON));
            /*MediaType vocÊ define  o tipo que a requisição vai aceitar como
            reposta no caso estamos colocando tipo JSON*/

/* resposta do result acima vai ser um productDTO que passamos
* como string*/

        /*Assertions, quando mais assertions vocÊ tiver
        * mais rigoros fica o seu test*/
        result.andExpect(status().isOk()); /*/
        result.andExpect(jsonPath("$.id").exists()); /*$ acessa
        o objeto da resposta, nesse caso estamos dizendo que
        o campo id deve existir no corpo da resposta*/
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldREturnNotFoundWhenIdDoesNotExist() throws Exception {
        /*Vamos converter o objeto java productDTO para texto/json para pssar
         * no corpo da requisição http em update e insert
         * o objectMapper tem um método que converte o objeto java em um string*/
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        /*depois só passar o objeto como string no content*/

        ResultActions result =
                mockMvc.perform(put("/products/{id}", nonExistingId)
                        .content(jsonBody)/*aqui é passado
                                 o corpo da requisição que o objeto
                                 productDTO com strin*/
                        .contentType(MediaType.APPLICATION_JSON)/*
                                aqui é o tipo do conteúdo passo no corpo
                                da requisição qu Json*/
                        .accept(MediaType.APPLICATION_JSON));
            /*MediaType vocÊ define  o tipo que a requisição vai aceitar como
            reposta no caso estamos colocando tipo JSON*/

        /* resposta do result acima vai ser um productDTO que passamos
         * como string*/

        /*Assertions, quando mais assertions vocÊ tiver
         * mais rigoros fica o seu test*/
        result.andExpect(status().isNotFound()); /*/
        result.andExpect(jsonPath("$.id").exists()); /*$ acessa
        o objeto da resposta, nesse caso estamos dizendo que
        o campo id deve existir no corpo da resposta*/
    }

    @Test
    public void findAllShouldReturnPage() throws Exception{



            ResultActions result =
                    mockMvc.perform(get("/products")
                            .accept(MediaType.APPLICATION_JSON));
            /*MediaType vocÊ define  o tipo que a requisição vai aceitar como
            reposta no caso estamos colocando tipo JSON*/



        result.andExpect(status().isOk());
                /*mockMvc.perform faz uma requisição
                * estamos chamado acima uma requisição com o método
                * http no caminho /products
                * isOk é código 200 de http de resposta o is tem várias resposta
                * que você pode usar
                * andDo - faz alguma coisa
                * andReturn - oque vocÊ espera que retorn*/
    }

     @Test
     public void findByIdShouldReturnProductWhenIdExists() throws Exception {


        ResultActions result =
                mockMvc.perform(get("/products/{id}", existingId) /*
                existingId é o valo que passado dentro do {id que está rota*/
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists()); /*$ acessa
        o objeto da resposta, nesse caso estamos dizendo que
        o campo id deve existir no corpo da resposta*/
         result.andExpect(jsonPath("$.name").exists());
         result.andExpect(jsonPath("$.description").exists());

     }

     @Test
     public void findByIdShouldReturnNotFoundWhenIdDoesExistId() throws Exception /*
     precisa porque o perfom lança exceção ou usa o try catch*/ {
         ResultActions result =
                 mockMvc.perform(get("/products/{id}", nonExistingId) /*
              nonexistingId é o valo que passado dentro do {id que está rota
              no caso configuramo esse nonexistingId para gera execeção*/
                         .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());/*
        not found é o código http 400 e como estamos tratando exceção na classe
        ResourceExceptionHandlerm, tratamos essa execeção*/
     }

}
