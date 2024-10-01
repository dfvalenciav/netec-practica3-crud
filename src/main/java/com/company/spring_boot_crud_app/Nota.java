// Esta línea define en qué "carpeta" (o paquete) está esta clase dentro del proyecto.
// Los paquetes ayudan a organizar el código.
package com.company.spring_boot_crud_app;

// A continuación, estamos "importando" o trayendo algunas herramientas que nos ayudarán
// a conectar esta clase con una base de datos. Es como traer ciertas funciones predefinidas 
// que necesitamos para que todo funcione sin tener que escribirlas desde cero.
import jakarta.persistence.*; // Nos trae todo lo necesario para trabajar con bases de datos.
import lombok.Data; // Nos trae una herramienta que nos permite reducir el código que tenemos que escribir.


/**
 * Representa un producto que será almacenado en la base de datos.
 * Cada producto tiene un identificador único, un nombre y un precio.
 * 
 * <p>Esta clase utiliza anotaciones de JPA (Java Persistence API) para mapear
 * los objetos Producto a una tabla de base de datos llamada "productos". 
 * Además, utiliza Lombok para generar automáticamente métodos como getters, 
 * setters, y otros métodos comunes.</p>
 */
@Entity // Indica que esta clase es una entidad que será gestionada por JPA (base de datos).
@Table(name = "notas") // Define que los datos de esta clase se almacenan en una tabla llamada "productos".
@Data 
public class Nota {
    @Id // Indica que este campo es la clave primaria de la entidad (ID único).
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La base de datos genera automáticamente el valor del ID.
    private Long id; // Identificador único del producto.

    @Column(nullable = false) 
    private String nombre; 

    @Column(nullable = false) 
    private Double resultado; 
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne 
    @JoinColumn(name = "carpeta_id",  nullable = false)
    private Carpeta carpeta;
}
