/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro_veiculo.model;

import java.sql.*;
import java.util.List;

/**
 *
 * @author molsousa
 */
public class Veiculo {
    private int idVeiculo;
    private String placa;
    private String chassi;
    private String renavam;
    private Date anoFabricacao;
    private String cor;
    private String categoriaVeiculo;
    private Marca marca;
    private Modelo modelo;
    private List<Motorista> proprietarios;
    private List<IPVA> ipvas;

    public int getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public Date getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Date anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getCategoriaVeiculo() {
        return categoriaVeiculo;
    }

    public void setCategoriaVeiculo(String categoriaVeiculo) {
        this.categoriaVeiculo = categoriaVeiculo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public List<Motorista> getProprietarios() {
        return proprietarios;
    }

    public void setProprietarios(List<Motorista> proprietarios) {
        this.proprietarios = proprietarios;
    }

    public List<IPVA> getIpvas() {
        return ipvas;
    }

    public void setIpvas(List<IPVA> ipvas) {
        this.ipvas = ipvas;
    }


}
