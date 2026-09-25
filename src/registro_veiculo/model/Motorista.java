/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro_veiculo.model;

import java.sql.Date;

import java.util.*;

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
    private List<FoneMotorista> fones;
    private List<EmailMotorista> emails;
    private CNH cnh;
    private List<Veiculo> veiculos;

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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<FoneMotorista> getFones() {
        return fones;
    }

    public void setFones(List<FoneMotorista> fones) {
        this.fones = fones;
    }

    public List<EmailMotorista> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailMotorista> emails) {
        this.emails = emails;
    }

    public CNH getCnh() {
        return cnh;
    }

    public void setCnh(CNH cnh) {
        this.cnh = cnh;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }



}
