package com.generation.blogpessoal.repositories;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.model.Tema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {

    List<Tema> findAllByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);
}
