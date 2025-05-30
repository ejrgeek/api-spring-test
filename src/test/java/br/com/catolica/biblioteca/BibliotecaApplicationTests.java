package br.com.catolica.biblioteca;

import br.com.catolica.biblioteca.controller.LivroController;
import br.com.catolica.biblioteca.entity.Livro;
import br.com.catolica.biblioteca.enums.EnumCategoria;
import br.com.catolica.biblioteca.enums.EnumDistribuicao;
import br.com.catolica.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class BibliotecaApplicationTests {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private Livro livroMock;

    @InjectMocks
    private LivroController livroController;

    private UUID livroId;
    private final String ISBN_VALIDO = "1234567890";
    private final String TITULO_VALIDO = "Neuromancer";
    private final String AUTOR_VALIDO = "William Gibson";
    private final EnumCategoria CATEGORIA = EnumCategoria.ROMANCE;
    private final EnumDistribuicao DISTRIBUICAO = EnumDistribuicao.FISICO;

    @BeforeEach
    void setUp() {
        livroId = UUID.randomUUID();

        when(livroMock.getIsbn()).thenReturn(ISBN_VALIDO);
        when(livroMock.getTitulo()).thenReturn(TITULO_VALIDO);
        when(livroMock.getAutor()).thenReturn(AUTOR_VALIDO);
        when(livroMock.getCategoria()).thenReturn(CATEGORIA);
        when(livroMock.getDistribuicao()).thenReturn(DISTRIBUICAO);
        when(livroMock.getId()).thenReturn(livroId);
    }

    @Test
    void contextLoads() {
        assertNotNull(livroController);
    }

    @Test
    void deveCriarLivroComSucesso() {
        when(livroRepository.save(any(Livro.class))).thenReturn(livroMock);

        ResponseEntity<?> response = livroController.criar(livroMock);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(livroRepository).save(any(Livro.class));
    }

    @Test
    void naoDeveCriarLivroComIsbnInvalido() {
        when(livroMock.getIsbn()).thenReturn("123");

        ResponseEntity<?> response = livroController.criar(livroMock);

        assertEquals(409, response.getStatusCodeValue());
        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    void deveListarLivros() {
        List<Livro> livros = Arrays.asList(livroMock);
        when(livroRepository.findAll()).thenReturn(livros);

        List<Livro> resultado = livroController.listar();

        assertEquals(1, resultado.size());
        assertEquals(livroMock, resultado.get(0));
        verify(livroRepository).findAll();
    }

    @Test
    void deveAtualizarLivroComSucesso() {
        when(livroRepository.existsById(livroId)).thenReturn(true);
        when(livroRepository.save(any(Livro.class))).thenReturn(livroMock);

        ResponseEntity<?> response = livroController.atualizar(livroMock);

        assertEquals(204, response.getStatusCodeValue());
        verify(livroRepository).existsById(livroId);
        verify(livroRepository).save(any(Livro.class));
    }

    @Test
    void naoDeveAtualizarLivroInexistente() {
        when(livroRepository.existsById(livroId)).thenReturn(false);

        ResponseEntity<?> response = livroController.atualizar(livroMock);

        assertEquals(404, response.getStatusCodeValue());
        verify(livroRepository).existsById(livroId);
        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    void deveDeletarLivroComSucesso() {
        when(livroRepository.existsById(livroId)).thenReturn(true);
        doNothing().when(livroRepository).deleteById(livroId);

        ResponseEntity<?> response = livroController.deletar(livroId);

        assertEquals(204, response.getStatusCodeValue());
        verify(livroRepository).existsById(livroId);
        verify(livroRepository).deleteById(livroId);
    }

    @Test
    void naoDeveDeletarLivroInexistente() {
        when(livroRepository.existsById(livroId)).thenReturn(false);

        ResponseEntity<?> response = livroController.deletar(livroId);

        assertEquals(404, response.getStatusCodeValue());
        verify(livroRepository).existsById(livroId);
        verify(livroRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void deveRetornarErroQuandoValidarTituloInvalido() {
        when(livroMock.getTitulo()).thenReturn("Ab");
        when(livroMock.getIsbn()).thenReturn(ISBN_VALIDO);
        when(livroMock.getAutor()).thenReturn(AUTOR_VALIDO);

        ResponseEntity<?> response = livroController.criar(livroMock);

        assertEquals(409, response.getStatusCodeValue());
        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    void deveRetornarErroQuandoValidarAutorInvalido() {
        when(livroMock.getAutor()).thenReturn("A");
        when(livroMock.getIsbn()).thenReturn(ISBN_VALIDO);
        when(livroMock.getTitulo()).thenReturn(TITULO_VALIDO);

        ResponseEntity<?> response = livroController.criar(livroMock);

        assertEquals(409, response.getStatusCodeValue());
        verify(livroRepository, never()).save(any(Livro.class));
    }
}