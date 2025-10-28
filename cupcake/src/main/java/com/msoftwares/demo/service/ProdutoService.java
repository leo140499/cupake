package com.msoftwares.demo.service;

import com.msoftwares.demo.dto.ProdutoDTO;
import com.msoftwares.demo.model.Produto;
import com.msoftwares.demo.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public void cadastrar(ProdutoDTO dto){
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setDescricao(dto.getDescricao());
        produtoRepository.save(produto);
    }

    public List<Produto> listar(){
        return produtoRepository.findAll();
    }

    public void deletar(Long id){
        Produto produto  = produtoRepository.findById(id).get();
        produtoRepository.delete(produto);
    }

    @Transactional
    public void editar(ProdutoDTO dto){
        Produto produto = produtoRepository.findById(dto.getId()).get();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produtoRepository.save(produto);
    }

    public Produto buscarPorId(Long id){
        return produtoRepository.findById(id).get();
    }

}
