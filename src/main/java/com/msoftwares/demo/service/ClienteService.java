package com.msoftwares.demo.service;

import com.msoftwares.demo.dto.ClienteDTO;
import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.model.DadosPessoais;
import com.msoftwares.demo.model.Endereco;
import com.msoftwares.demo.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    public void cadastrar(ClienteDTO dto) throws Exception {
        Cliente cliente = new Cliente();
        DadosPessoais dadosPessoais = new DadosPessoais();
        Endereco endereco = new Endereco();
        endereco.setBairro(dto.getEnderecoDTO().getBairro());
        endereco.setComplemento(dto.getEnderecoDTO().getComplemento());
        endereco.setRua(dto.getEnderecoDTO().getRua());
        endereco.setCidade(dto.getEnderecoDTO().getCidade());
        dadosPessoais.setDocumento(dto.getCpf());
        dadosPessoais.setNome(dto.getNome());
        dadosPessoais.setEmail(dto.getEmail());
        dadosPessoais.setEndereco(endereco);
        dadosPessoais.setSenha(dto.getSenha());
        cliente.setDadosPessoais(dadosPessoais);

        if (verificarEmailExistente(dadosPessoais.getEmail())){
            throw new Exception("Email já existente");
        }else if (verificarDocumentoExistente(dadosPessoais.getDocumento())){
            throw new Exception("CPF já existente");
        }else if (verificarSenhaExistente(dadosPessoais.getSenha())){
            throw new Exception("Senha já existente");
        }
        clienteRepository.save(cliente);
    }

    public Cliente login(String email, String senha){
        Cliente cliente = clienteRepository.findByDadosPessoaisEmail(email);
        if(cliente != null && cliente.getDadosPessoais().getSenha().matches(senha)){
            return cliente;
        }else{
            return null;
        }
    }

    public boolean verificarEmailExistente(String email){
        Cliente cliente = clienteRepository.findByDadosPessoaisEmail(email);
        if(cliente != null){
            return true;
        }else{
            return false;
        }
    }

    public boolean verificarSenhaExistente(String senha){
        Cliente cliente = clienteRepository.findByDadosPessoaisEmail(senha);
        if(cliente != null){
            return true;
        }else{
            return false;
        }
    }

    public boolean verificarDocumentoExistente(String documento){
        Cliente cliente = clienteRepository.findByDadosPessoaisDocumento(documento);
        if(cliente != null){
            return true;
        }else{
            return false;
        }
    }

}
