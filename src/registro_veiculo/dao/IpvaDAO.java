package registro_veiculo.dao;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
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
public class IpvaDAO {

    //Veículos já associados ao proprietário, com todos os dados de IPVA
    private static final String SQL_VEICULOS_COM_IPVA_DO_PROPRIETARIO =
        "SELECT " +
        "  v.\"idVeiculo_PK\", v.\"placa\", v.\"chassi\", v.\"renevam\", v.\"anoFabricacao\", " +
        "  cor.\"descricaoCor\", cat.\"descricaoCategoria\", " +
        "  mar.\"descricaoMarca\", mod.\"descricaoModelo\", mod.\"anoLancamento\", " +
        "  ipvaAgg.\"anos\", ipvaAgg.\"precos\", ipvaAgg.\"aliquotas\", " +
        "  ipvaAgg.\"vencimentos\", ipvaAgg.\"pagamentos\", ipvaAgg.\"status\" " +
        "FROM (SELECT DISTINCT \"idVeiculo_FK_PK\" FROM \"Proprietario_Veiculo\" WHERE \"idProprietario_Veiculo_FK_PK\" = ?) pv " +
        "JOIN \"Veiculo\" v ON v.\"idVeiculo_PK\" = pv.\"idVeiculo_FK_PK\" " +
        "JOIN \"Cor\" cor ON cor.\"idCor_PK\" = v.\"idCor_FK\" " +
        "JOIN \"Categoria\" cat ON cat.\"idCategoria_PK\" = v.\"idCategoria_FK\" " +
        "JOIN \"Modelo\" mod ON mod.\"idModelo_PK\" = v.\"idModelo_FK\" " +
        "JOIN \"Marca\" mar ON mar.\"idMarca_PK\" = mod.\"idMarca_FK\" " +
        "LEFT JOIN LATERAL ( " +
        "  SELECT ARRAY_AGG(ipva.\"anoFiscal\" ORDER BY ipva.\"anoFiscal\" DESC) AS \"anos\", " +
        "         ARRAY_AGG(ipva.\"precoTabelaFIPE\" ORDER BY ipva.\"anoFiscal\" DESC) AS \"precos\", " +
        "         ARRAY_AGG(ipva.\"aliquota\" ORDER BY ipva.\"anoFiscal\" DESC) AS \"aliquotas\", " +
        "         ARRAY_AGG(ipva.\"dtVencimento\" ORDER BY ipva.\"anoFiscal\" DESC) AS \"vencimentos\", " +
        "         ARRAY_AGG(ipva.\"dtPagamento\" ORDER BY ipva.\"anoFiscal\" DESC) AS \"pagamentos\", " +
        "         ARRAY_AGG(sp.\"descricaoStatusPagamento\" ORDER BY ipva.\"anoFiscal\" DESC) AS \"status\" " +
        "  FROM \"IPVA\" ipva " +
        "  JOIN \"StatusPagamento\" sp ON sp.\"idStatusPagamento_PK\" = ipva.\"idStatusPagamento_FK\" " +
        "  WHERE ipva.\"idVeiculo_FK\" = v.\"idVeiculo_PK\" " +
        ") ipvaAgg ON true";

    //Busca, a partir do id do proprietário, todos os veículos já associados
    //a ele e para cada um com todos os dados da tabela IPVA
    public List<Veiculo> buscarPorProprietario(int idMotorista) throws SQLException {
        List<Veiculo> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_VEICULOS_COM_IPVA_DO_PROPRIETARIO)) {

            stmt.setInt(1, idMotorista);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapVeiculoComIpva(rs));
                }
            }
        }

        return lista;
    }

    private Veiculo mapVeiculoComIpva(ResultSet rs) throws SQLException {
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

        List<Date> anos = arrayToListDate(rs.getArray("anos"));
        List<BigDecimal> precos = arrayToListBigDecimal(rs.getArray("precos"));
        List<BigDecimal> aliquotas = arrayToListBigDecimal(rs.getArray("aliquotas"));
        List<Date> vencimentos = arrayToListDate(rs.getArray("vencimentos"));
        List<Date> pagamentos = arrayToListDate(rs.getArray("pagamentos"));
        List<String> status = arrayToListString(rs.getArray("status"));

        List<IPVA> ipvas = new ArrayList<>();
        for (int i = 0; i < anos.size(); i++) {
            IPVA ipva = new IPVA();
            ipva.setAnoFiscal(anos.get(i));
            ipva.setPrecoTabelaFIPE(precos.get(i) != null ? precos.get(i).floatValue() : 0f);
            ipva.setAliquota(aliquotas.get(i) != null ? aliquotas.get(i).floatValue() : 0f);
            ipva.setDataVencimento(vencimentos.get(i));
            ipva.setDataPagamento(pagamentos.get(i));
            ipva.setStatusPagamento(status.get(i));
            ipvas.add(ipva);
        }
        veiculo.setIpvas(ipvas);

        return veiculo;
    }

    private List<Date> arrayToListDate(Array array) throws SQLException {
        List<Date> lista = new ArrayList<>();
        if (array == null) {
            return lista;
        }

        Object[] bruto = (Object[]) array.getArray();
        for (Object valor : bruto) {
            lista.add((Date) valor);
        }

        return lista;
    }

    private List<BigDecimal> arrayToListBigDecimal(Array array) throws SQLException {
        List<BigDecimal> lista = new ArrayList<>();
        if (array == null) {
            return lista;
        }

        Object[] bruto = (Object[]) array.getArray();
        for (Object valor : bruto) {
            lista.add((BigDecimal) valor);
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
}
