package com.generation.blogpessoal.controller;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repositories.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioService usuarioService;

   @Autowired
    private UsuarioRepository usuarioRepository;

   @BeforeAll
    void start(){
       usuarioRepository.deleteAll();

       usuarioService.cadastrarUsuario(new Usuario(
               0L, "Root", "root@root.com", "rootroot", " "));
   }

   @Test
    @DisplayName("Cadastrar um usuario")
    public void deveCriarUmUsuario(){

       HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(
               0L,"Paulo Antunes", "paulo@email.com", "13465278", "http://xyz"));

       ResponseEntity<Usuario> corpoResposta = testRestTemplate
               .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

       assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
       assertEquals(corpoRequisicao.getBody().getNome(), corpoRequisicao.getBody().getNome());
       assertEquals(corpoRequisicao.getBody().getUsuario(), corpoRequisicao.getBody().getUsuario());
   }

   @Test
   @DisplayName("Não deve permitir duplicação de usuário")
    public void naoDeveDuplicarUsuario(){
       usuarioService.cadastrarUsuario(new Usuario(
               0L, "Maria da Silva", "maria@email.com", "13465278", "http://xyz"));

       HttpEntity<Usuario> corpoRequicao = new HttpEntity<Usuario>(new Usuario(
               0L, "Maria da Silva", "maria@email.com", "13465278", "http://xyz"));

       ResponseEntity<Usuario> corpoResposta = testRestTemplate
               .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequicao, Usuario.class);

       assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
   }

    @Test
    @DisplayName("Deve atualizar um usuario")
    public void deveAtualizarUmUsuario(){

        Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(
                0L, "Juliana Andrewa", "juliana@email.com", "juliana134", "http://xyz"));

        Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),
                "Juliana Andrews Ramos", "juliana@email.com", "juliana134", "http://xyz");

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);

        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .withBasicAuth("root@root.com", "rootroot")
                .exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), corpoRequisicao.getBody().getNome());
        assertEquals(corpoRequisicao.getBody().getUsuario(), corpoRequisicao.getBody().getUsuario());
    }

    @Test
    @DisplayName("Listar todos usuarios")
    public void ListarTodosUsuarios() {

        usuarioService.cadastrarUsuario(new Usuario(
                0L, "Sabrina Sanches", "sabrina@email.com", "sabrina278", "http://xyz"));

        usuarioService.cadastrarUsuario(new Usuario(
                0L, "Ricardo Marques", "ricardo@email.com", "ricardo278", "http://xyz"));

        ResponseEntity<String> corpoResposta = testRestTemplate
                .withBasicAuth("root@root.com", "rootroot")
                .exchange("/usuarios", HttpMethod.GET, null, String.class);
    }
}
