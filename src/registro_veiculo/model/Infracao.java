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
public class Infracao {
    private Date dataInfracao;
    private String horaInfracao;
    private String tipoInfracao;
    private Endereco localInfracao;

    public Date getDataInfracao() {
        return dataInfracao;
    }

    public void setDataInfracao(Date dataInfracao) {
        this.dataInfracao = dataInfracao;
    }

    public String getHoraInfracao() {
        return horaInfracao;
    }

    public void setHoraInfracao(String horaInfracao) {
        this.horaInfracao = horaInfracao;
    }

    public String getTipoInfracao() {
        return tipoInfracao;
    }

    public void setTipoInfracao(String tipoInfracao) {
        this.tipoInfracao = tipoInfracao;
    }

    public Endereco getLocalInfracao() {
        return localInfracao;
    }

    public void setLocalInfracao(Endereco localInfracao) {
        this.localInfracao = localInfracao;
    }
    
    
}
