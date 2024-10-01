package com.company.spring_boot_crud_app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotaRepository extends CrudRepository<Nota, Long> {
    List<Nota> findByNombre(String nombre);

    List<Nota> findByUsuarioId(Long usuarioId);

    List<Nota> findByResultadoGreaterThan(Double resultado);

    List<Nota> findByResultadoLessThan(Double resultado);

    List<Nota> findByCarpetaId(Long carpetaId);

}
