package registro_veiculo.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import registro_veiculo.connection.*;
import registro_veiculo.model.*;

/**
 *
 * @author molsousa
 */
public class MotoristaDAO {

    //Motorista + endereço + sexo + CNH + fones e emails 
    private static final String SQL_MOTORISTAS =
        "SELECT " +
        "  m.\"idMotorista_PK\", m.\"cpf\", m.\"cnpj\", m.\"nome\", " +
        "  m.\"dtNascimento_Fundacao\", m.\"nroEndereco\", m.\"compEndereco\", sx.\"descricaoSexo\", " +
        "  e.\"cep\", b.\"nomeBairro\", c.\"nomeCidade\", uf.\"nomeUF\", uf.\"siglaUF_PK\", " +
        "  l.\"nomeLogradouro\", tl.\"nomeTipoLogradouro\", tl.\"siglaTipoLogradouro_PK\", " +
        "  cnh.\"idCNH_PK\", cnh.\"nroRegistro\", cnh.\"dtEmissao\", cnh.\"dtValidade\", " +
        "  foneAgg.\"telefones\", foneAgg.\"ddis\", foneAgg.\"ddds\", " +
        "  emailAgg.\"ids\" AS \"emailIds\", emailAgg.\"enderecos\" AS \"emailEnderecos\" " +
        "FROM \"Motorista\" m " +
        "JOIN \"Sexo\" sx ON sx.\"idSexo_PK\" = m.\"idSexo_FK\" " +
        "JOIN \"Endereco\" e ON e.\"idEndereco_PK\" = m.\"idEndereco_FK\" " +
        "JOIN \"Bairro\" b ON b.\"idBairro_PK\" = e.\"idBairro_FK\" " +
        "JOIN \"Cidade\" c ON c.\"idCidade_PK\" = e.\"idCidade_Fk\" " +
        "JOIN \"UF\" uf ON uf.\"siglaUF_PK\" = c.\"siglaUF_FK\" " +
        "JOIN \"Logradouro\" l ON l.\"idLogradouro_PK\" = e.\"idLogradouro_FK\" " +
        "JOIN \"TipoLogradouro\" tl ON tl.\"siglaTipoLogradouro_PK\" = l.\"siglaTipoLogradouro_FK\" " +
        "LEFT JOIN \"CNH\" cnh ON cnh.\"idMotorista_FK\" = m.\"idMotorista_PK\" " +
        "LEFT JOIN LATERAL ( " +
        "  SELECT ARRAY_AGG(fm.\"nroTelefone\" ORDER BY fm.\"idFoneMotorista_PK\") AS \"telefones\", " +
        "         ARRAY_AGG(fm.\"nroDDI_FK\" ORDER BY fm.\"idFoneMotorista_PK\") AS \"ddis\", " +
        "         ARRAY_AGG(fm.\"nroDDD_FK\" ORDER BY fm.\"idFoneMotorista_PK\") AS \"ddds\" " +
        "  FROM \"FoneMotorista\" fm WHERE fm.\"idMotorista_FK\" = m.\"idMotorista_PK\" " +
        ") foneAgg ON true " +
        "LEFT JOIN LATERAL ( " +
        "  SELECT ARRAY_AGG(em.\"idEmailMotorista_PK\" ORDER BY em.\"idEmailMotorista_PK\") AS \"ids\", " +
        "         ARRAY_AGG(em.\"enderecoEmail\" ORDER BY em.\"idEmailMotorista_PK\") AS \"enderecos\" " +
        "  FROM \"EmailMotorista\" em WHERE em.\"idMotorista_FK\" = m.\"idMotorista_PK\" " +
        ") emailAgg ON true";


    private static final String SQL_VEICULOS_DOS_MOTORISTAS =
        "SELECT " +
        "  pv.\"idProprietario_Veiculo_FK_PK\" AS \"idMotorista\", " +
        "  v.\"idVeiculo_PK\", v.\"placa\", v.\"chassi\", v.\"renevam\", v.\"anoFabricacao\", " +
        "  cor.\"descricaoCor\", cat.\"descricaoCategoria\", " +
        "  mar.\"descricaoMarca\", mod.\"descricaoModelo\", mod.\"anoLancamento\", " +
        "  ipvaAgg.\"anos\", ipvaAgg.\"status\" " +
        "FROM (SELECT DISTINCT \"idProprietario_Veiculo_FK_PK\", \"idVeiculo_FK_PK\" FROM \"Proprietario_Veiculo\") pv " +
        "JOIN \"Veiculo\" v ON v.\"idVeiculo_PK\" = pv.\"idVeiculo_FK_PK\" " +
        "JOIN \"Cor\" cor ON cor.\"idCor_PK\" = v.\"idCor_FK\" " +
        "JOIN \"Categoria\" cat ON cat.\"idCategoria_PK\" = v.\"idCategoria_FK\" " +
        "JOIN \"Modelo\" mod ON mod.\"idModelo_PK\" = v.\"idModelo_FK\" " +
        "JOIN \"Marca\" mar ON mar.\"idMarca_PK\" = mod.\"idMarca_FK\" " +
        "LEFT JOIN LATERAL ( " +
        "  SELECT ARRAY_AGG(ipva.\"anoFiscal\" ORDER BY ipva.\"anoFiscal\" DESC) AS \"anos\", " +
        "         ARRAY_AGG(sp.\"descricaoStatusPagamento\" ORDER BY ipva.\"anoFiscal\" DESC) AS \"status\" " +
        "  FROM \"IPVA\" ipva " +
        "  JOIN \"StatusPagamento\" sp ON sp.\"idStatusPagamento_PK\" = ipva.\"idStatusPagamento_FK\" " +
        "  WHERE ipva.\"idVeiculo_FK\" = v.\"idVeiculo_PK\" " +
        ") ipvaAgg ON true";

    //Busca todos os motoristas com todos os dados cadastrais, a CNH, 
    //todos os veículos associados a ele e, para cada veículo,
    //apenas a situação de pagamento do IPVA 
    public List<Motorista> buscarTodos() throws SQLException {
        Map<Integer, Motorista> porId = new HashMap<>();
        List<Motorista> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection()) {

            try (PreparedStatement stmt = conn.prepareStatement(SQL_MOTORISTAS);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Motorista motorista = mapMotorista(rs);
                    motorista.setVeiculos(new ArrayList<>());
                    porId.put(motorista.getIdMotorista(), motorista);
                    lista.add(motorista);
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(SQL_VEICULOS_DOS_MOTORISTAS);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Motorista motorista = porId.get(rs.getInt("idMotorista"));
                    if (motorista != null) {
                        motorista.getVeiculos().add(mapVeiculoComStatusIpva(rs));
                    }
                }
            }
        }

        return lista;
    }

    private Veiculo mapVeiculoComStatusIpva(ResultSet rs) throws SQLException {
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

        List<java.sql.Date> anos = arrayToListDate(rs.getArray("anos"));
        List<String> status = arrayToListString(rs.getArray("status"));

        List<IPVA> ipvas = new ArrayList<>();
        for (int i = 0; i < anos.size(); i++) {
            IPVA ipva = new IPVA();
            ipva.setAnoFiscal(anos.get(i));
            ipva.setStatusPagamento(status.get(i));
            ipvas.add(ipva);
        }
        veiculo.setIpvas(ipvas);

        return veiculo;
    }

    private Motorista mapMotorista(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setCep(rs.getString("cep"));
        endereco.setBairro(rs.getString("nomeBairro"));
        endereco.setCidade(rs.getString("nomeCidade"));
        endereco.setUF(rs.getString("nomeUF"));
        endereco.setSiglaUF(rs.getString("siglaUF_PK"));
        endereco.setLogradouro(rs.getString("nomeLogradouro"));
        endereco.setTipoLogradouro(rs.getString("nomeTipoLogradouro"));
        endereco.setSiglaTipoLogradouro(rs.getString("siglaTipoLogradouro_PK"));

        Motorista motorista = new Motorista();
        motorista.setIdMotorista(rs.getInt("idMotorista_PK"));
        motorista.setCpf(rs.getString("cpf"));
        motorista.setCnpj(rs.getString("cnpj"));
        motorista.setNome(rs.getString("nome"));
        motorista.setDataNascimento_Fundacao(rs.getDate("dtNascimento_Fundacao"));
        motorista.setNroEndereco(rs.getString("nroEndereco"));
        motorista.setCompEndereco(rs.getString("compEndereco"));
        motorista.setSexo(rs.getString("descricaoSexo"));
        motorista.setEndereco(endereco);

        int idCnh = rs.getInt("idCNH_PK");
        if (!rs.wasNull()) {
            CNH cnh = new CNH();
            cnh.setIdCNH(idCnh);
            cnh.setNroRegistro(rs.getString("nroRegistro"));
            cnh.setDataEmissao(rs.getDate("dtEmissao"));
            cnh.setDataValidade(rs.getDate("dtValidade"));
            motorista.setCnh(cnh);
        }

        List<String> telefones = arrayToListString(rs.getArray("telefones"));
        List<Integer> ddis = arrayToListInteger(rs.getArray("ddis"));
        List<Integer> ddds = arrayToListInteger(rs.getArray("ddds"));

        List<FoneMotorista> fones = new ArrayList<>();
        for (int i = 0; i < telefones.size(); i++) {
            FoneMotorista fone = new FoneMotorista();
            fone.setNroFone(telefones.get(i));
            fone.setDDI(ddis.get(i));
            fone.setDDD(ddds.get(i));
            fones.add(fone);
        }
        motorista.setFones(fones);

        List<Integer> emailIds = arrayToListInteger(rs.getArray("emailIds"));
        List<String> emailEnderecos = arrayToListString(rs.getArray("emailEnderecos"));

        List<EmailMotorista> emails = new ArrayList<>();
        for (int i = 0; i < emailIds.size(); i++) {
            EmailMotorista email = new EmailMotorista();
            email.setIdEmailMotorista(emailIds.get(i));
            email.setEnderecoEmail(emailEnderecos.get(i));
            emails.add(email);
        }
        motorista.setEmails(emails);

        return motorista;
    }

    private List<java.sql.Date> arrayToListDate(Array array) throws SQLException {
        List<java.sql.Date> lista = new ArrayList<>();
        if (array == null) {
            return lista;
        }

        Object[] bruto = (Object[]) array.getArray();
        for (Object valor : bruto) {
            lista.add((java.sql.Date) valor);
        }

        return lista;
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
}
