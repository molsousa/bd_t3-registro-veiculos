/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro_veiculo.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import registro_veiculo.connection.*;
import registro_veiculo.model.*;

/**
 *
 * @author molsousa
 */
public class VeiculoDAO {

    private static final String SQL_BASE_VEICULO =
        "SELECT " +
        "  v.\"idVeiculo_PK\", v.\"placa\", v.\"chassi\", v.\"renevam\", v.\"anoFabricacao\", " +
        "  cor.\"descricaoCor\", cat.\"descricaoCategoria\", " +
        "  mar.\"descricaoMarca\", mod.\"descricaoModelo\", mod.\"anoLancamento\" " +
        "FROM \"Veiculo\" v " +
        "JOIN \"Cor\" cor ON cor.\"idCor_PK\" = v.\"idCor_FK\" " +
        "JOIN \"Categoria\" cat ON cat.\"idCategoria_PK\" = v.\"idCategoria_FK\" " +
        "JOIN \"Modelo\" mod ON mod.\"idModelo_PK\" = v.\"idModelo_FK\" " +
        "JOIN \"Marca\" mar ON mar.\"idMarca_PK\" = mod.\"idMarca_FK\" ";

    //Todos os veículos com os dados simples (id/cpf/cnpj/nome) de todos os
    //proprietários (atuais e antigos) agregados em arrays via LATERAL,
    //para não duplicar a linha do veículo quando ele tiver mais de um dono (1 SQL)
    private static final String SQL_VEICULOS_COM_PROPRIETARIOS =
        "SELECT " +
        "  v.\"idVeiculo_PK\", v.\"placa\", v.\"chassi\", v.\"renevam\", v.\"anoFabricacao\", " +
        "  cor.\"descricaoCor\", cat.\"descricaoCategoria\", " +
        "  mar.\"descricaoMarca\", mod.\"descricaoModelo\", mod.\"anoLancamento\", " +
        "  propAgg.\"ids\", propAgg.\"cpfs\", propAgg.\"cnpjs\", propAgg.\"nomes\" " +
        "FROM \"Veiculo\" v " +
        "JOIN \"Cor\" cor ON cor.\"idCor_PK\" = v.\"idCor_FK\" " +
        "JOIN \"Categoria\" cat ON cat.\"idCategoria_PK\" = v.\"idCategoria_FK\" " +
        "JOIN \"Modelo\" mod ON mod.\"idModelo_PK\" = v.\"idModelo_FK\" " +
        "JOIN \"Marca\" mar ON mar.\"idMarca_PK\" = mod.\"idMarca_FK\" " +
        "LEFT JOIN LATERAL ( " +
        "  SELECT ARRAY_AGG(dono.\"idMotorista_PK\") AS \"ids\", " +
        "         ARRAY_AGG(dono.\"cpf\") AS \"cpfs\", " +
        "         ARRAY_AGG(dono.\"cnpj\") AS \"cnpjs\", " +
        "         ARRAY_AGG(dono.\"nome\") AS \"nomes\" " +
        "  FROM ( " +
        "    SELECT DISTINCT m.\"idMotorista_PK\", m.\"cpf\", m.\"cnpj\", m.\"nome\" " +
        "    FROM \"Proprietario_Veiculo\" pv " +
        "    JOIN \"Motorista\" m ON m.\"idMotorista_PK\" = pv.\"idProprietario_Veiculo_FK_PK\" " +
        "    WHERE pv.\"idVeiculo_FK_PK\" = v.\"idVeiculo_PK\" " +
        "  ) dono " +
        ") propAgg ON true";

    //Busca, pelo id do proprietário, os veículos que estão no nome dele com
    //posse em data válida (já iniciada e ainda não encerrada)
    public List<Veiculo> buscarPorProprietario(int idMotorista) throws SQLException {
        List<Veiculo> lista = new ArrayList<>();

        String sql = SQL_BASE_VEICULO +
            "JOIN \"Proprietario_Veiculo\" pv ON pv.\"idVeiculo_FK_PK\" = v.\"idVeiculo_PK\" " +
            "WHERE pv.\"idProprietario_Veiculo_FK_PK\" = ? " +
            "AND pv.\"dtInicio\" <= CURRENT_DATE " +
            "AND (pv.\"dtFim\" IS NULL OR pv.\"dtFim\" >= CURRENT_DATE)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMotorista);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapVeiculo(rs));
                }
            }
        }

        return lista;
    }

    //Busca todos os veículos cadastrados, já trazendo os dados simples
    //(id/cpf/cnpj/nome) de todos os proprietários (atuais e antigos) de cada um
    public List<Veiculo> buscarTodos() throws SQLException {
        List<Veiculo> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_VEICULOS_COM_PROPRIETARIOS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapVeiculoComProprietarios(rs));
            }
        }

        return lista;
    }

    private Veiculo mapVeiculoComProprietarios(ResultSet rs) throws SQLException {
        Veiculo veiculo = mapVeiculo(rs);

        List<Integer> ids = arrayToListInteger(rs.getArray("ids"));
        List<String> cpfs = arrayToListString(rs.getArray("cpfs"));
        List<String> cnpjs = arrayToListString(rs.getArray("cnpjs"));
        List<String> nomes = arrayToListString(rs.getArray("nomes"));

        List<Motorista> proprietarios = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            Motorista proprietario = new Motorista();
            proprietario.setIdMotorista(ids.get(i));
            proprietario.setCpf(cpfs.get(i));
            proprietario.setCnpj(cnpjs.get(i));
            proprietario.setNome(nomes.get(i));
            proprietarios.add(proprietario);
        }
        veiculo.setProprietarios(proprietarios);

        return veiculo;
    }

    private List<Integer> arrayToListInteger(Array array) throws SQLException {
        List<Integer> lista = new ArrayList<>();
        if (array == null) {
            return lista;
        }

        Object[] bruto = (Object[]) array.getArray();
        for (Object valor : bruto) {
            lista.add((Integer) valor);
        }

        return lista;
    }

    private List<String> arrayToListString(Array array) throws SQLException {
        List<String> lista = new ArrayList<>();
        if (array == null) {
            return lista;
        }

        Object[] bruto = (Object[]) array.getArray();
        for (Object valor : bruto) {
            lista.add((String) valor);
        }

        return lista;
    }

    private Veiculo mapVeiculo(ResultSet rs) throws SQLException {
        Marca marca = new Marca();
        marca.setDescricaoMarca(rs.getString("descricaoMarca"));

        Modelo modelo = new Modelo();
        modelo.setDescricaoModelo(rs.getString("descricaoModelo"));
        modelo.setDataLancamento(rs.getDate("anoLancamento"));

        Veiculo veiculo = new Veiculo();
        veiculo.setIdVeiculo(rs.getInt("idVeiculo_PK"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setChassi(rs.getString("chassi"));
        veiculo.setRenavam(rs.getString("renevam"));
        veiculo.setAnoFabricacao(rs.getDate("anoFabricacao"));
        veiculo.setCor(rs.getString("descricaoCor"));
        veiculo.setCategoriaVeiculo(rs.getString("descricaoCategoria"));
        veiculo.setMarca(marca);
        veiculo.setModelo(modelo);

        return veiculo;
    }
}
