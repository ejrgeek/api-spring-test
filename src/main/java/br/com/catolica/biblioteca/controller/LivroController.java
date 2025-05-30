package br.com.catolica.biblioteca.controller;

import java.util.List;
import java.util.UUID;

import br.com.catolica.biblioteca.entity.Livro;
import br.com.catolica.biblioteca.repository.LivroRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    private boolean validarIsbn(String isbn){
        return isbn.length() >= 10;
    }

    private boolean validarAutor(String autor){
        return autor.length() >= 3;
    }

    private boolean validarTitulo(String titulo){
        return titulo.length() > 3;
    }

    private boolean validarId(UUID id){
        return livroRepository.existsById(id);
    }

    @ResponseBody
    @Transactional
    @RequestMapping(path="/livros", method = RequestMethod.POST)
    public ResponseEntity<?> criar(@RequestBody Livro livro){
        if (!validarAutor(livro.getAutor())){
            return ResponseEntity.status(409).body("Nome do autor não possui 3 ou mais caracteres.");
        }
        if (!validarTitulo(livro.getTitulo())){
            return ResponseEntity.status(409).body("Titulo não possui 4 ou mais caracteres.");
        }
        if (!validarIsbn(livro.getIsbn())){
            return ResponseEntity.status(409).body("Isbn não possui 10 ou mais caracteres.");
        }
        try {
            Livro criado = livroRepository.save(livro);
            return ResponseEntity.status(200).body(criado);
        }catch (Exception e){
            return ResponseEntity.status(400).body(String.format("Erro ao criar: %s", e));
        }
    }

    @ResponseBody
    @RequestMapping(path="/livros", method=RequestMethod.GET)
    public List<Livro> listar(){
        return livroRepository.findAll();
    }

    @ResponseBody
    @Transactional
    @RequestMapping(path="/livros", method=RequestMethod.PUT)
    public ResponseEntity<?> atualizar(@RequestBody Livro livro) {
        if (!validarId(livro.getId())) {
            return ResponseEntity.status(404).body("ID não encontrado.");
        }
        if (!validarTitulo(livro.getTitulo())){
            return ResponseEntity.status(409).body("Titulo não possui 4 ou mais caracteres.");
        }
        if (!validarAutor(livro.getAutor())){
            return ResponseEntity.status(409).body("Nome do autor não possui 3 ou mais caracteres.");
        }
        if (!validarIsbn(livro.getIsbn())){
            return ResponseEntity.status(409).body("Isbn não possui 10 ou mais caracteres.");
        }

        try {
            livroRepository.save(livro);
            return ResponseEntity.status(204).body("");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(String.format("Erro ao atualizar: %s", e));
        }
    }

    @ResponseBody
    @Transactional
    @RequestMapping(path="/livros/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deletar(@PathVariable UUID id){
        if (!validarId(id)){
            return ResponseEntity.status(404).body("ID não encontrado.");
        }
        try {
            livroRepository.deleteById(id);
            return ResponseEntity.status(204).body("");
        } catch ( Exception e ) {
            return ResponseEntity.status(400).body(String.format("Erro ao deletar: %s", e));
        }
    }

}
