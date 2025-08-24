package entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @OneToMany(mappedBy = "categoria",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Producto> productos = new ArrayList<>();
}
