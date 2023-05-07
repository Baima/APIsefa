package com.example.demo.controller;


import com.example.demo.model.Pagamento;
import com.example.demo.repositorio.PagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PagController {

    @Autowired
    private PagRepo pagRepo;

    @GetMapping("/getTodos")
    public ResponseEntity<List<Pagamento>> exibeTodosPagamentos(){
    try{
        List<Pagamento> pagamentos = new ArrayList<>();
        pagRepo.findAll().forEach(pagamentos::add);

        if(pagamentos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pagamentos, HttpStatus.OK);
    }catch (Exception e){
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    }

    @GetMapping("/getPagamentoPorId/{id}")
    public ResponseEntity<Pagamento> exibePagamentoPorId(@PathVariable Long id){
        try{
            Optional<Pagamento> dadosPagamento = pagRepo.findById(id);
            if(dadosPagamento.isPresent()) {
                return new ResponseEntity<>(dadosPagamento.get(),HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/getPagamentoPorDocumento/{doc}")
    public ResponseEntity<List<Pagamento>> exibePagamentoPorDocumento(@PathVariable String doc){
        try{
            List<Pagamento> dadosPagamento = pagRepo.findByCpfCnpj(doc);

            if(!dadosPagamento.isEmpty()) {

                return new ResponseEntity<>(dadosPagamento,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getPagamentoPorStatus/{status}")
    public ResponseEntity<List<Pagamento>> exibePagamentoPorStatus(@PathVariable String status){
        try{
            List<Pagamento> dadosPagamento = pagRepo.findByStatusProcessamento(status);
            if(!dadosPagamento.isEmpty()) {
                return new ResponseEntity<>(dadosPagamento,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getPagamentoPorCodigo/{cod}")
    public ResponseEntity<Pagamento> exibePagamentoPorDocumento(@PathVariable Long cod){
        try{
            Optional<Pagamento> dadosPagamento = pagRepo.findByCodigo(cod);
            if(dadosPagamento.isPresent()) {
                return new ResponseEntity<>(dadosPagamento.get(),HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/adicionarPagamento")
    public ResponseEntity<Pagamento> adicionaPagamento(@RequestBody Pagamento pagamento){
        try{
            if(pagamento.getMetodoPagamento().equals("cartao_debito") || pagamento.getMetodoPagamento().equals("cartao_credito")){
                if(pagamento.getNumeroCartao()==null){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                pagamento.setStatusProcessamento("Pendente");
                Pagamento respostaADD = pagRepo.save(pagamento);
                return new ResponseEntity<>(respostaADD, HttpStatus.OK);
            }
            else if(pagamento.getMetodoPagamento().equals("pix") || pagamento.getMetodoPagamento().equals("boleto")) {
                pagamento.setNumeroCartao(null);
                pagamento.setStatusProcessamento("Pendente");
                Pagamento respostaADD = pagRepo.save(pagamento);
                return new ResponseEntity<>(respostaADD, HttpStatus.OK);
            }

        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/atualizaPagamentoPorId/{id}")
    public ResponseEntity<Pagamento> atualizaPagamentoPorId(@PathVariable Long id, @RequestBody Pagamento NovosDadosPagamento){
        try{
            Optional<Pagamento> pagamentoAntigo = pagRepo.findById(id);
            if(pagamentoAntigo.isPresent()) {
                Pagamento pagamentoAtualizado = pagamentoAntigo.get();
                switch (pagamentoAtualizado.getStatusProcessamento()){
                    case "Pendente":
                        Pagamento respostaATTPendente = atualizaStatus(NovosDadosPagamento,pagamentoAtualizado);
                        return new ResponseEntity<>(respostaATTPendente,HttpStatus.OK);
                    case "Sucesso":
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    case "Falha":
                        if(NovosDadosPagamento.getStatusProcessamento().equals("Pendente")){
                            Pagamento respostaATTFalha = atualizaStatus(NovosDadosPagamento,pagamentoAtualizado);
                            return new ResponseEntity<>(respostaATTFalha,HttpStatus.OK);
                        }
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    default:
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Pagamento atualizaStatus(Pagamento novosDadosPagamento, Pagamento pagamentoAtualizado ){

        pagamentoAtualizado.setStatusProcessamento(novosDadosPagamento.getStatusProcessamento());
        Pagamento respostaATT = pagRepo.save(pagamentoAtualizado);
        return respostaATT;

    }

    @DeleteMapping("/removePagamentoPorId/{id}")
    public ResponseEntity<HttpStatus> removePagamentoPorId(@PathVariable Long id){
        try{
            Optional<Pagamento> pagamentoAntigo = pagRepo.findById(id);
            if(pagamentoAntigo.isPresent()) {
                Pagamento pagamento = pagamentoAntigo.get();
                if(pagamento.getStatusProcessamento().equals("Pendente")){
                    pagRepo.deleteById(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
