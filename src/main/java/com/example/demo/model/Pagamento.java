package com.example.demo.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name="pagamentos")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private Integer codigo;
    @NotNull
    private Float valor;
    @NotNull
    private String cpfCnpj;
    @NotNull
    private String metodoPagamento;
    private Integer numeroCartao;
    private String statusProcessamento;




}
