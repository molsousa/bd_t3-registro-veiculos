/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro_veiculo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import registro_veiculo.model.*;
import registro_veiculo.connection.*;

/**
 *
 * @author molsousa
 */
public class InfracaoDAO {
    // Alterar o URL da chamada no ConnectionFactory, USER e PASS caso necessário
    // Tem algumas classes model também pra facilitar o armazenamento quando puxar o SQL
    // Classe de teste e principal no pacote view

    /**
     * Junta as infos do Motorista + Veiculo + Infracao (e tabelas de apoio)
     * em um único SQL, para não precisar de várias idas ao banco.
     */
    private static final String SQL_BASE =
        "SELECT " +
        "  i.\"dtInfracao\", i.\"horaInfracao\", ti.\"descricaoTipoInfracao\", " +
        "  eInfr.\"cep\" AS infr_cep, bInfr.\"nomeBairro\" AS infr_bairro, " +
        "  cInfr.\"nomeCidade\" AS infr_cidade, ufInfr.\"nomeUF\" AS infr_uf, " +
        "  ufInfr.\"siglaUF_PK\" AS infr_siglaUf, lInfr.\"nomeLogradouro\" AS infr_logradouro, " +
        "  tlInfr.\"nomeTipoLogradouro\" AS infr_tipoLogradouro, " +
        "  tlInfr.\"siglaTipoLogradouro_PK\" AS infr_siglaTipoLogradouro, " +
        "  v.\"placa\", v.\"chassi\", v.\"renevam\", v.\"anoFabricacao\", " +
        "  cor.\"descricaoCor\", cat.\"descricaoCategoria\", " +
        "  mar.\"descricaoMarca\", mod.\"descricaoModeo\", mod.\"anoLancamento\", " +
        "  m.\"idMotorista_PK\", m.\"cpf\", m.\"cnpj\", m.\"nome\", " +
        "  m.\"dtNascimento_Fundacao\", m.\"nroEndereco\", m.\"compEndereco\", sx.\"descricaoSexo\", " +
        "  eMot.\"cep\" AS mot_cep, bMot.\"nomeBairro\" AS mot_bairro, " +
        "  cMot.\"nomeCidade\" AS mot_cidade, ufMot.\"nomeUF\" AS mot_uf, " +
        "  ufMot.\"siglaUF_PK\" AS mot_siglaUf, lMot.\"nomeLogradouro\" AS mot_logradouro, " +
        "  tlMot.\"nomeTipoLogradouro\" AS mot_tipoLogradouro, " +
        "  tlMot.\"siglaTipoLogradouro_PK\" AS mot_siglaTipoLogradouro " +
        "FROM \"Infracao\" i " +
        "JOIN \"TipoInfracao\" ti ON ti.\"idTipoInfracao_PK\" = i.\"idTipoInfracao_FK\" " +
        "JOIN \"Endereco\" eInfr ON eInfr.\"idEndereco_PK\" = i.\"localInfracao_FK\" " +
        "JOIN \"Bairro\" bInfr ON bInfr.\"idBairro_PK\" = eInfr.\"idBairro_FK\" " +
        "JOIN \"Cidade\" cInfr ON cInfr.\"idCidade_PK\" = eInfr.\"idCidade_Fk\" " +
        "JOIN \"UF\" ufInfr ON ufInfr.\"siglaUF_PK\" = cInfr.\"siglaUF_FK\" " +
        "JOIN \"Logradouro\" lInfr ON lInfr.\"idLogradouro_PK\" = eInfr.\"idLogradouro_FK\" " +
        "JOIN \"TipoLogradouro\" tlInfr ON tlInfr.\"siglaTipoLogradouro_PK\" = lInfr.\"siglaTipoLogradouro_FK\" " +
        "JOIN \"Veiculo\" v ON v.\"idVeiculo_PK\" = i.\"idVeiculo_FK\" " +
        "JOIN \"Cor\" cor ON cor.\"idCor_PK\" = v.\"idCor_FK\" " +
        "JOIN \"Categoria\" cat ON cat.\"idCategoria_PK\" = v.\"idCategoria_FK\" " +
        "JOIN \"Modelo\" mod ON mod.\"idModelo_PK\" = v.\"idModelo_FK\" " +
        "JOIN \"Marca\" mar ON mar.\"idMarca_PK\" = mod.\"idMarca_FK\" " +
        "JOIN \"Motorista\" m ON m.\"idMotorista_PK\" = i.\"idMotorista\" " +
        "JOIN \"Sexo\" sx ON sx.\"idSexo_PK\" = m.\"idSexo_FK\" " +
        "JOIN \"Endereco\" eMot ON eMot.\"idEndereco_PK\" = m.\"idEndereco_FK\" " +
        "JOIN \"Bairro\" bMot ON bMot.\"idBairro_PK\" = eMot.\"idBairro_FK\" " +
        "JOIN \"Cidade\" cMot ON cMot.\"idCidade_PK\" = eMot.\"idCidade_Fk\" " +
        "JOIN \"UF\" ufMot ON ufMot.\"siglaUF_PK\" = cMot.\"siglaUF_FK\" " +
        "JOIN \"Logradouro\" lMot ON lMot.\"idLogradouro_PK\" = eMot.\"idLogradouro_FK\" " +
        "JOIN \"TipoLogradouro\" tlMot ON tlMot.\"siglaTipoLogradouro_PK\" = lMot.\"siglaTipoLogradouro_FK\" ";

    /**
     * Busca todas as infrações já com os dados do motorista e do veículo
     * envolvidos, ordenadas da mais recente para a mais antiga.
     */
    public List<InfracaoCompleta> buscarTodas() throws SQLException {
        List<InfracaoCompleta> lista = new ArrayList<>();

        String sql = SQL_BASE + "ORDER BY i.\"dtInfracao\" DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }

        return lista;
    }

    /**
     * Busca as infrações de um único veículo (por placa), já com os dados
     * do motorista e do veículo envolvidos.
     */
    public List<InfracaoCompleta> buscarPorPlaca(String placa) throws SQLException {
        List<InfracaoCompleta> lista = new ArrayList<>();

        String sql = SQL_BASE + "WHERE v.\"placa\" = ? ORDER BY i.\"dtInfracao\" DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }

        return lista;
    }

    /**
     * Monta um Endereco, um Motorista, um Veiculo e uma Infracao a partir da
     * linha atual do ResultSet, agrupando tudo em um InfracaoCompleta.
     */
    private InfracaoCompleta mapRow(ResultSet rs) throws SQLException {
        Endereco enderecoInfracao = new Endereco();
        enderecoInfracao.setCep(rs.getString("infr_cep"));
        enderecoInfracao.setBairro(rs.getString("infr_bairro"));
        enderecoInfracao.setCidade(rs.getString("infr_cidade"));
        enderecoInfracao.setUF(rs.getString("infr_uf"));
        enderecoInfracao.setSiglaUF(rs.getString("infr_siglaUf"));
        enderecoInfracao.setLogradouro(rs.getString("infr_logradouro"));
        enderecoInfracao.setTipoLogradouro(rs.getString("infr_tipoLogradouro"));
        enderecoInfracao.setSiglaTipoLogradouro(rs.getString("infr_siglaTipoLogradouro"));

        Infracao infracao = new Infracao();
        infracao.setDataInfracao(rs.getDate("dtInfracao"));
        infracao.setHoraInfracao(
            rs.getTime("horaInfracao") != null ? rs.getTime("horaInfracao").toString() : null
        );
        infracao.setTipoInfracao(rs.getString("descricaoTipoInfracao"));
        infracao.setLocalInfracao(enderecoInfracao);

        Marca marca = new Marca();
        marca.setDescricaoMarca(rs.getString("descricaoMarca"));

        Modelo modelo = new Modelo();
        modelo.setDescricaoModelo(rs.getString("descricaoModeo"));
        modelo.setDataLancamento(rs.getDate("anoLancamento"));

        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setChassi(rs.getString("chassi"));
        veiculo.setRenavam(rs.getString("renevam"));
        veiculo.setAnoFabricacao(rs.getDate("anoFabricacao"));
        veiculo.setCor(rs.getString("descricaoCor"));
        veiculo.setCategoriaVeiculo(rs.getString("descricaoCategoria"));
        veiculo.setMarca(marca);
        veiculo.setModelo(modelo);

        Endereco enderecoMotorista = new Endereco();
        enderecoMotorista.setCep(rs.getString("mot_cep"));
        enderecoMotorista.setBairro(rs.getString("mot_bairro"));
        enderecoMotorista.setCidade(rs.getString("mot_cidade"));
        enderecoMotorista.setUF(rs.getString("mot_uf"));
        enderecoMotorista.setSiglaUF(rs.getString("mot_siglaUf"));
        enderecoMotorista.setLogradouro(rs.getString("mot_logradouro"));
        enderecoMotorista.setTipoLogradouro(rs.getString("mot_tipoLogradouro"));
        enderecoMotorista.setSiglaTipoLogradouro(rs.getString("mot_siglaTipoLogradouro"));

        Motorista motorista = new Motorista();
        motorista.setIdMotorista(rs.getInt("idMotorista_PK"));
        motorista.setCpf(rs.getString("cpf"));
        motorista.setCnpj(rs.getString("cnpj"));
        motorista.setNome(rs.getString("nome"));
        motorista.setDataNascimento_Fundacao(rs.getDate("dtNascimento_Fundacao"));
        motorista.setNroEndereco(rs.getString("nroEndereco"));
        motorista.setCompEndereco(rs.getString("compEndereco"));
        motorista.setSexo(rs.getString("descricaoSexo"));
        motorista.setEndereco(enderecoMotorista);

        InfracaoCompleta infracaoCompleta = new InfracaoCompleta();
        infracaoCompleta.setInfracao(infracao);
        infracaoCompleta.setMotorista(motorista);
        infracaoCompleta.setVeiculo(veiculo);

        return infracaoCompleta;
    }

    /**
     * Agrupa numa única estrutura o resultado da consulta que junta
     * Motorista + Veiculo + Infracao, sem precisar alterar as classes model.
     */
    public static class InfracaoCompleta {
        private Infracao infracao;
        private Motorista motorista;
        private Veiculo veiculo;

        public Infracao getInfracao() {
            return infracao;
        }

        public void setInfracao(Infracao infracao) {
            this.infracao = infracao;
        }

        public Motorista getMotorista() {
            return motorista;
        }

        public void setMotorista(Motorista motorista) {
            this.motorista = motorista;
        }

        public Veiculo getVeiculo() {
            return veiculo;
        }

        public void setVeiculo(Veiculo veiculo) {
            this.veiculo = veiculo;
        }
    }
}
