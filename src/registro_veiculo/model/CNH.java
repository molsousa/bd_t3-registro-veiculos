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
public class CNH {
    private int idCNH;
    private String nroRegistro;
    private Date dataEmissao;
    private Date dataValidade;

    public int getIdCNH() {
        return idCNH;
    }

    public void setIdCNH(int idCNH) {
        this.idCNH = idCNH;
    }

    public String getNroRegistro() {
        return nroRegistro;
    }

    public void setNroRegistro(String nroRegistro) {
        this.nroRegistro = nroRegistro;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }
    
    
}
