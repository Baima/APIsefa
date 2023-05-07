package com.example.demo.repositorio;

import com.example.demo.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagRepo extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByCpfCnpj(String cpfCnpj);

    List<Pagamento> findByStatusProcessamento(String status);

    Optional<Pagamento> findByCodigo(Long codigo);


}
