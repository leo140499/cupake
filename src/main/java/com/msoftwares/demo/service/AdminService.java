package com.msoftwares.demo.service;

import com.msoftwares.demo.dto.AdminDTO;
import com.msoftwares.demo.dto.ClienteDTO;
import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.model.DadosPessoais;
import com.msoftwares.demo.model.Endereco;
import com.msoftwares.demo.repository.AdminRepository;
import com.msoftwares.demo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    public void cadastrar(AdminDTO dto) throws Exception {
        Administrador administrador = new Administrador();
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
        administrador.setDadosPessoais(dadosPessoais);

        if (verificarEmailExistente(dadosPessoais.getEmail())){
            throw new Exception("Email já existente");
        }else if (verificarDocumentoExistente(dadosPessoais.getDocumento())){
            throw new Exception("CPF já existente");
        }else if (verificarSenhaExistente(dadosPessoais.getSenha())){
            throw new Exception("Senha já existente");
        }

        adminRepository.save(administrador);
    }

    public Administrador login(String email, String senha){
        Administrador administrador = adminRepository.findByDadosPessoaisEmail(email);
        if(administrador != null && administrador.getDadosPessoais().getSenha().matches(senha)){
            return administrador;
        }else{
            return null;
        }
    }

    public boolean verificarEmailExistente(String email){
        Administrador administrador = adminRepository.findByDadosPessoaisEmail(email);
        if(administrador != null){
            return true;
        }else{
            return false;
        }
    }

    public boolean verificarSenhaExistente(String senha){
        Administrador administrador = adminRepository.findByDadosPessoaisEmail(senha);
        if(administrador != null){
            return true;
        }else{
            return false;
        }
    }

    public boolean verificarDocumentoExistente(String documento){
        Administrador administrador = adminRepository.findByDadosPessoaisDocumento(documento);
        if(administrador != null){
            return true;
        }else{
            return false;
        }
    }

}
