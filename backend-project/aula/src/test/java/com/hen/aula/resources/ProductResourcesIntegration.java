package com.hen.aula.resources;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hen.aula.dto.ProductDTO;
import com.hen.aula.tests.Factory;
import com.hen.aula.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc /*Essa é usado quando faz o teste
de todo o contexto na camada web*/
@Transactional
public class ProductResourcesIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private TokenUtil tokenUtil;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    private String username, password, bearerToken;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L; /*
        Quantidade de produto no si*/

        username = "maria@gmail.com";
        password = "123456";

        bearerToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        /*Test se o Json na resposta da requisição está vindo ordenado
         * por nome*/

        ResultActions result =
                mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        /*valida se o total de elementos no JSon da paginado
         * é 25*/
        result.andExpect(jsonPath("$.content").exists());
        /*Acima testamos se a lista content existe e
         * abaixo testamos se o macBook está na posição 0 que é a primeira
         * e assim por diante*/
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));


    }

    @Test
    public void updateShouldReturnProducDTOWhenIdExists() throws Exception {

        ProductDTO productDTO = Factory.createProductDTO();

        /*Vamos converter o objeto java productDTO para texto/json para pssar
         * no corpo da requisição http em update e insert
         * o objectMapper tem um método que converte o objeto java em um string*/
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        /*depois só passar o objeto como string no content*/

        /*como é um teste de integração seu eu passar um valor no produto
        * ele vai atualizar, vamos criar uma variavel
        * para pegar o nome esperado depois de atualizar*/

        String expectedName = productDTO.getName();
        String expectedDescriptions = productDTO.getDescription();



        ResultActions result =
                mockMvc.perform(put("/products/{id}", existingId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .content(jsonBody)/*aqui é passado
                                 o corpo da requisição que o objeto
                                 productDTO com strin*/
                        .contentType(MediaType.APPLICATION_JSON)/*
                                aqui é o tipo do conteúdo passo no corpo
                                da requisição qu Json*/
                        .accept(MediaType.APPLICATION_JSON));
            /*MediaType vocÊ define  o tipo que a requisição vai aceitar como
            reposta no caso estamos colocando tipo JSON*/


        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.description").value(expectedDescriptions));


    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotexists() throws Exception {

        ProductDTO productDTO = Factory.createProductDTO();

        /*Vamos converter o objeto java productDTO para texto/json para pssar
         * no corpo da requisição http em update e insert
         * o objectMapper tem um método que converte o objeto java em um string*/
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        /*depois só passar o objeto como string no content*/

        /*como é um teste de integração seu eu passar um valor no produto
         * ele vai atualizar, vamos criar uma variavel
         * para pegar o nome esperado depois de atualizar*/

        ResultActions result =
                mockMvc.perform(put("/products/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .content(jsonBody)/*aqui é passado
                                 o corpo da requisição que o objeto
                                 productDTO com strin*/
                        .contentType(MediaType.APPLICATION_JSON)/*
                                aqui é o tipo do conteúdo passo no corpo
                                da requisição qu Json*/
                        .accept(MediaType.APPLICATION_JSON));
            /*MediaType vocÊ define  o tipo que a requisição vai aceitar como
            reposta no caso estamos colocando tipo JSON*/


        result.andExpect(status().isNotFound());

    }
}
