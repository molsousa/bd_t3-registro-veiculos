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
public class IPVA {

    private Date anoFiscal;
    private float precoTabelaFIPE;
    private float aliquota;
    private Date dataVencimento;
    private Date dataPagamento;
    private String statusPagamento;

    public Date getAnoFiscal() {
        return anoFiscal;
    }

    public void setAnoFiscal(Date anoFiscal) {
        this.anoFiscal = anoFiscal;
    }

    public float getPrecoTabelaFIPE() {
        return precoTabelaFIPE;
    }

    public void setPrecoTabelaFIPE(float precoTabelaFIPE) {
        this.precoTabelaFIPE = precoTabelaFIPE;
    }

    public float getAliquota() {
        return aliquota;
    }

    public void setAliquota(float aliquota) {
        this.aliquota = aliquota;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

}
