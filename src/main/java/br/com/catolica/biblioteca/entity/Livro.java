package br.com.catolica.biblioteca.entity;

import br.com.catolica.biblioteca.enums.EnumCategoria;
import br.com.catolica.biblioteca.enums.EnumDistribuicao;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Livro {
    @Id
    @GeneratedValue
    private UUID id;
    private String isbn;
    private String titulo;
    private String autor;
    @Enumerated(EnumType.STRING)
    private EnumCategoria categoria;
    @Enumerated(EnumType.STRING)
    private EnumDistribuicao distribuicao;
}
