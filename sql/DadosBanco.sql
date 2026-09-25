-- Tipo Logradouro

INSERT INTO "TipoLogradouro"
("siglaTipoLogradouro_PK", "nomeTipoLogradouro")
VALUES
('RUA', 'Rua'),
('AV', 'Avenida'),
('AL', 'Alameda');

--UF

INSERT INTO "UF"
("siglaUF_PK", "nomeUF")
VALUES
('PR', 'Paraná'),
('SC', 'Santa Catarina'),
('SP', 'São Paulo');

-- BAirros
INSERT INTO "Bairro"
("idBairro_PK", "nomeBairro")
VALUES
(1, 'Centro'),
(2, 'Coqueiral'),
(3, 'Neva'),
(4, 'Country'),
(5, 'Pioneiros'),
(6, 'Universitário');


-- Cidades
INSERT INTO "Cidade"
("idCidade_PK", "nomeCidade", "siglaUF_FK")
VALUES
(1, 'Cascavel', 'PR'),
(2, 'Foz do Iguaçu', 'PR'),
(3, 'Toledo', 'PR');


-- Logradouros

INSERT INTO "Logradouro"
("idLogradouro_PK", "nomeLogradouro", "siglaTipoLogradouro_FK")
VALUES
(1, 'Brasil', 'AV'),
(2, 'Paraná', 'RUA'),
(3, 'São Paulo', 'RUA'),
(4, 'Juscelino Kubitschek', 'AV'),
(5, 'Rio Grande do Sul', 'RUA'),
(6, 'Carlos Gomes', 'RUA');


-- Enderecos
INSERT INTO "Endereco"
(
    "idEndereco_PK",
    "cep",
    "idBairro_FK",
    "idCidade_Fk",
    "idLogradouro_FK"
)
VALUES
(1, '85801-000', 1, 1, 1),
(2, '85810-000', 2, 1, 2),
(3, '85802-000', 3, 1, 3),
(4, '85807-000', 4, 1, 4),
(5, '85900-000', 5, 3, 5),
(6, '85819-000', 6, 1, 6);


-- DDI
INSERT INTO "DDI"
("nroDDI_PK")
VALUES
(55);


-- DDD
INSERT INTO "DDD"
("nroDDD_PK")
VALUES
(45);



-- sexo
INSERT INTO "Sexo"
("idSexo_PK", "descricaoSexo")
VALUES
(1, 'Feminino'),
(2, 'Masculino');


-- Motoristas
INSERT INTO "Motorista"
(
    "idMotorista_PK",
    "cpf",
    "cnpj",
    "nome",
    "dtNascimento_Fundacao",
    "nroEndereco",
    "compEndereco",
    "idSexo_FK",
    "idEndereco_FK"
)
VALUES
(
    1,
    '123.456.789-01',
    NULL,
    'Ana Carolina Martins',
    '1995-04-15',
    120,
    'Apto 201',
    1,
    1
),
(
    2,
    '234.567.890-12',
    NULL,
    'Bruno Henrique Silva',
    '1990-09-22',
    450,
    NULL,
    2,
    2
),
(
    3,
    '345.678.901-23',
    NULL,
    'Camila Ferreira Souza',
    '1998-12-10',
    85,
    'Casa',
    1,
    3
);


-- Telefones

INSERT INTO "FoneMotorista"
(
    "idFoneMotorista_PK",
    "nroTelefone",
    "nroDDI_FK",
    "nroDDD_FK",
    "idMotorista_FK"
)
VALUES
(1, '999991111', 55, 45, 1),
(2, '999992222', 55, 45, 2),
(3, '999993333', 55, 45, 3);


-- emails
INSERT INTO "EmailMotorista"
(
    "idEmailMotorista_PK",
    "enderecoEmail",
    "idMotorista_FK"
)
VALUES
(1, 'ana.martins@email.com', 1),
(2, 'bruno.silva@email.com', 2),
(3, 'camila.souza@email.com', 3);


-- CNH
INSERT INTO "CNH"
(
    "idCNH_PK",
    "nroRegistro",
    "dtEmissao",
    "dtValidade",
    "idMotorista_FK"
)
VALUES
(
    1,
    'PR123456789',
    '2019-06-10',
    '2029-06-10',
    1
),
(
    2,
    'PR234567890',
    '2018-03-20',
    '2028-03-20',
    2
),
(
    3,
    'PR345678901',
    '2020-01-15',
    '2030-01-15',
    3
);


-- Categoria
INSERT INTO "Categoria"
("idCategoria_PK", "descricaoCategoria")
VALUES
(1, 'Automóvel'),
(2, 'Motocicleta'),
(3, 'Caminhonete');


-- cores
INSERT INTO "Cor"
("idCor_PK", "descricaoCor")
VALUES
(1, 'Branco'),
(2, 'Preto'),
(3, 'Prata'),
(4, 'Vermelho'),
(5, 'Cinza'),
(6, 'Azul'),
(7, 'Verde');


--Marcas
INSERT INTO "Marca"
("idMarca_PK", "descricaoMarca")
VALUES
(1, 'Toyota'),
(2, 'Honda'),
(3, 'Fiat'),
(4, 'Volkswagen'),
(5, 'Chevrolet'),
(6, 'Hyundai'),
(7, 'Jeep'),
(8, 'Nissan'),
(9, 'Renault');


--Modelos

INSERT INTO "Modelo"
(
    "idModelo_PK",
    "descricaoModelo",
    "anoLancamento",
    "idMarca_FK"
)
VALUES
(1, 'Corolla', '2019-01-01', 1),
(2, 'Civic', '2019-01-01', 2),
(3, 'Argo', '2019-01-01', 3),
(4, 'T-Cross', '2019-01-01', 4),
(5, 'Onix', '2019-01-01', 5),
(6, 'HB20', '2019-01-01', 6),
(7, 'Renegade', '2019-01-01', 7),
(8, 'Kicks', '2019-01-01', 8),
(9, 'Sandero', '2019-01-01', 9);

--Veiculos

INSERT INTO "Veiculo"
(
    "idVeiculo_PK",
    "placa",
    "chassi",
    "renevam",
    "anoFabricacao",
    "idCategoria_FK",
    "idModelo_FK",
    "idCor_FK"
)
VALUES
-- Ana Carolina
(
    1,
    'ABC1D23',
    '9BRBLWHE0K1234567',
    '01234567890',
    '2019-01-01',
    1,
    1,
    1
),
(
    2,
    'DEF2E34',
    '9C2FC1650K2345678',
    '01234567891',
    '2019-01-01',
    1,
    2,
    2
),
(
    3,
    'FGH3F45',
    '9BD358A9K03456789',
    '01234567892',
    '2020-01-01',
    1,
    3,
    3
),

-- Bruno Henrique
(
    4,
    'GHI4G56',
    '9BWBH6BF0K4567890',
    '01234567893',
    '2020-01-01',
    1,
    4,
    5
),
(
    5,
    'JKL5H67',
    '9BGKS48U0K5678901',
    '01234567894',
    '2020-01-01',
    1,
    5,
    4
),
(
    6,
    'MNO6I78',
    '9BHBG51CAK6789012',
    '01234567895',
    '2021-01-01',
    1,
    6,
    6
),

-- Camila Ferreira
(
    7,
    'PQR7J89',
    '9BJAB7AU0K7890123',
    '01234567896',
    '2020-01-01',
    1,
    7,
    7
),
(
    8,
    'STU8K90',
    '94DCTCAL0K8901234',
    '01234567897',
    '2021-01-01',
    1,
    8,
    1
),
(
    9,
    'VWX9L01',
    '93Y5SRD0K9012345',
    '01234567898',
    '2020-01-01',
    1,
    9,
    3
);


-- proprietario dos veiculos

-- Ana Carolina -> 3 veículos
INSERT INTO "Proprietario_Veiculo"
(
    "idProprietario_Veiculo_FK_PK",
    "idVeiculo_FK_PK",
    "dtInicio",
    "dtFim"
)
VALUES
(1, 1, '2019-05-10', NULL),
(1, 2, '2020-02-15', NULL),
(1, 3, '2021-06-20', NULL);


-- Bruno Henrique -> 3 veículos
INSERT INTO "Proprietario_Veiculo"
(
    "idProprietario_Veiculo_FK_PK",
    "idVeiculo_FK_PK",
    "dtInicio",
    "dtFim"
)
VALUES
(2, 4, '2020-03-15', NULL),
(2, 5, '2021-04-10', NULL),
(2, 6, '2022-01-20', NULL);


-- Camila Ferreira -> 3 veículos
INSERT INTO "Proprietario_Veiculo"
(
    "idProprietario_Veiculo_FK_PK",
    "idVeiculo_FK_PK",
    "dtInicio",
    "dtFim"
)
VALUES
(3, 7, '2020-08-20', NULL),
(3, 8, '2021-09-12', NULL),
(3, 9, '2022-02-05', NULL);


-- status de pagamento

INSERT INTO "StatusPagamento"
(
    "idStatusPagamento_PK",
    "descricaoStatusPagamento"
)
VALUES
(1, 'Pago'),
(2, 'Pendente'),
(3, 'Atrasado');


--IPVA por veiculo
 
 -- Ana -- Corolla
INSERT INTO "IPVA"
VALUES
(1,  '2022-01-01', 95000.00, 0.032, '2022-03-31', '2022-03-20', 1, 1),
(2,  '2023-01-01', 98000.00, 0.032, '2023-03-31', '2023-03-25', 1, 1),
(3,  '2024-01-01', 101000.00, 0.032, '2024-03-31', '2024-04-05', 3, 1),
(4,  '2025-01-01', 105000.00, 0.032, '2025-03-31', '2025-03-18', 1, 1);


-- Ana - civic

INSERT INTO "IPVA"
VALUES
(5,  '2022-01-01', 100000.00, 0.032, '2022-03-31', '2022-03-22', 1, 2),
(6,  '2023-01-01', 103000.00, 0.032, '2023-03-31', '2023-03-29', 1, 2),
(7,  '2024-01-01', 106000.00, 0.032, '2024-03-31', '2024-04-02', 3, 2),
(8,  '2025-01-01', 109000.00, 0.032, '2025-03-31', '2025-03-15', 1, 2);


-- Ana - argo

INSERT INTO "IPVA"
VALUES
(9,  '2022-01-01', 70000.00, 0.032, '2022-03-31', '2022-03-18', 1, 3),
(10, '2023-01-01', 73000.00, 0.032, '2023-03-31', '2023-03-27', 1, 3),
(11, '2024-01-01', 76000.00, 0.032, '2024-03-31', '2024-03-30', 1, 3),
(12, '2025-01-01', 79000.00, 0.032, '2025-03-31', NULL, 2, 3);


-- Bruno - t cross

INSERT INTO "IPVA"
VALUES
(13, '2022-01-01', 110000.00, 0.032, '2022-03-31', '2022-03-15', 1, 4),
(14, '2023-01-01', 114000.00, 0.032, '2023-03-31', '2023-03-28', 1, 4),
(15, '2024-01-01', 118000.00, 0.032, '2024-03-31', '2024-04-10', 3, 4),
(16, '2025-01-01', 121000.00, 0.032, '2025-03-31', NULL, 2, 4);


-- Bruno -- onix

INSERT INTO "IPVA"
VALUES
(17, '2022-01-01', 68000.00, 0.032, '2022-03-31', '2022-03-20', 1, 5),
(18, '2023-01-01', 71000.00, 0.032, '2023-03-31', '2023-03-25', 1, 5),
(19, '2024-01-01', 74000.00, 0.032, '2024-03-31', '2024-04-05', 3, 5),
(20, '2025-01-01', 77000.00, 0.032, '2025-03-31', '2025-03-19', 1, 5);


-- Bruno - HB20
INSERT INTO "IPVA"
VALUES
(21, '2022-01-01', 65000.00, 0.032, '2022-03-31', '2022-03-17', 1, 6),
(22, '2023-01-01', 69000.00, 0.032, '2023-03-31', '2023-03-23', 1, 6),
(23, '2024-01-01', 72000.00, 0.032, '2024-03-31', '2024-04-01', 3, 6),
(24, '2025-01-01', 75000.00, 0.032, '2025-03-31', NULL, 2, 6);


--Camila - Renegade
INSERT INTO "IPVA"
VALUES
(25, '2022-01-01', 90000.00, 0.032, '2022-03-31', '2022-03-16', 1, 7),
(26, '2023-01-01', 94000.00, 0.032, '2023-03-31', '2023-03-24', 1, 7),
(27, '2024-01-01', 98000.00, 0.032, '2024-03-31', '2024-04-03', 3, 7),
(28, '2025-01-01', 101000.00, 0.032, '2025-03-31', '2025-03-20', 1, 7);


--Camila - Kicks
INSERT INTO "IPVA"
VALUES
(29, '2022-01-01', 85000.00, 0.032, '2022-03-31', '2022-03-19', 1, 8),
(30, '2023-01-01', 88000.00, 0.032, '2023-03-31', '2023-03-26', 1, 8),
(31, '2024-01-01', 92000.00, 0.032, '2024-03-31', '2024-04-06', 3, 8),
(32, '2025-01-01', 95000.00, 0.032, '2025-03-31', NULL, 2, 8);


--Camila - Sandero

INSERT INTO "IPVA"
VALUES
(33, '2022-01-01', 60000.00, 0.032, '2022-03-31', '2022-03-14', 1, 9),
(34, '2023-01-01', 63000.00, 0.032, '2023-03-31', '2023-03-21', 1, 9),
(35, '2024-01-01', 66000.00, 0.032, '2024-03-31', '2024-04-04', 3, 9),
(36, '2025-01-01', 69000.00, 0.032, '2025-03-31', '2025-03-17', 1, 9);


-- Tipos de infração

INSERT INTO "TipoInfracao"
(
    "idTipoInfracao_PK",
    "descricaoTipoInfracao"
)
VALUES
(1, 'Excesso de velocidade'),
(2, 'Estacionamento irregular'),
(3, 'Avançar sinal vermelho'),
(4, 'Uso do celular ao dirigir'),
(5, 'Não utilização do cinto de segurança');


--infrações por motorista
-- Ana Carolina

INSERT INTO "Infracao"
(
    "idInfracao_PK",
    "dtInfracao",
    "horaInfracao",
    "idTipoInfracao_FK",
    "localInfracao_FK",
    "idVeiculo_FK",
    "idMotorista"
)
VALUES
(1, '2022-05-12', '08:30:00', 1, 1, 1, 1),
(2, '2023-07-21', '14:15:00', 2, 2, 2, 1),
(3, '2024-09-03', '18:40:00', 3, 3, 3, 1),
(4, '2025-11-15', '10:20:00', 4, 4, 1, 1);


--Bruno Henrique

INSERT INTO "Infracao"
(
    "idInfracao_PK",
    "dtInfracao",
    "horaInfracao",
    "idTipoInfracao_FK",
    "localInfracao_FK",
    "idVeiculo_FK",
    "idMotorista"
)
VALUES
(5, '2022-03-18', '09:10:00', 5, 2, 4, 2),
(6, '2023-06-25', '16:45:00', 1, 3, 5, 2),
(7, '2024-08-14', '11:30:00', 2, 4, 6, 2),
(8, '2025-10-02', '19:20:00', 3, 5, 4, 2);


--Camila Ferreira
INSERT INTO "Infracao"
(
    "idInfracao_PK",
    "dtInfracao",
    "horaInfracao",
    "idTipoInfracao_FK",
    "localInfracao_FK",
    "idVeiculo_FK",
    "idMotorista"
)
VALUES
(9, '2022-02-10', '07:50:00', 2, 3, 7, 3),
(10, '2023-05-17', '13:25:00', 4, 4, 8, 3),
(11, '2024-07-29', '17:10:00', 1, 1, 9, 3),
(12, '2025-12-05', '20:15:00', 5, 5, 7, 3);