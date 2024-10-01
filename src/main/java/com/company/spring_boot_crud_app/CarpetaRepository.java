package com.company.spring_boot_crud_app;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarpetaRepository extends CrudRepository<Carpeta, Long> {
    List<Carpeta> findByNombre(String nombre);
}