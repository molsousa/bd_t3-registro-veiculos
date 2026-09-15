/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro_veiculo.model;

import java.sql.*;

/**
 *
 * @author molsousa
 */
public class Motorista {
    private int idMotorista;
    private String cpf;
    private String cnpj;
    private String nome;
    private Date dataNascimento_Fundacao;
    private String nroEndereco;
    private String compEndereco;
    private String sexo;
    private Endereco endereco;

    public int getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(int idMotorista) {
        this.idMotorista = idMotorista;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento_Fundacao() {
        return dataNascimento_Fundacao;
    }

    public void setDataNascimento_Fundacao(Date dataNascimento_Fundacao) {
        this.dataNascimento_Fundacao = dataNascimento_Fundacao;
    }

    public String getNroEndereco() {
        return nroEndereco;
    }

    public void setNroEndereco(String nroEndereco) {
        this.nroEndereco = nroEndereco;
    }

    public String getCompEndereco() {
        return compEndereco;
    }

    public void setCompEndereco(String compEndereco) {
        this.compEndereco = compEndereco;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Endereco getEnderecoCompleto() {
        return endereco;
    }

    public void setEnderecoCompleto(Endereco endereco) {
        this.endereco = endereco;
    }
    
    
}
