package com.data_service.data_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(name = "fecha_actualizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaActualizacion;

    @OneToOne(mappedBy = "inventario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Producto producto;
}