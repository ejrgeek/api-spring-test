package br.com.catolica.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.catolica.biblioteca.entity.Livro;

import java.util.UUID;

@Repository
public interface LivroRepository extends JpaRepository<Livro, UUID> {

}
