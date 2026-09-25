CREATE TABLE "TipoLogradouro" (
  "siglaTipoLogradouro_PK" varchar(4) PRIMARY KEY,
  "nomeTipoLogradouro" varchar
);

CREATE TABLE "Logradouro" (
  "idLogradouro_PK" integer PRIMARY KEY,
  "nomeLogradouro" varchar,
  "siglaTipoLogradouro_FK" varchar(4) NOT NULL
);

CREATE TABLE "Bairro" (
  "idBairro_PK" integer PRIMARY KEY,
  "nomeBairro" varchar
);

CREATE TABLE "Cidade" (
  "idCidade_PK" integer PRIMARY KEY,
  "nomeCidade" varchar,
  "siglaUF_FK" varchar(3) NOT NULL
);

CREATE TABLE "UF" (
  "siglaUF_PK" varchar(3) PRIMARY KEY,
  "nomeUF" varchar
);

CREATE TABLE "Endereco" (
  "idEndereco_PK" integer PRIMARY KEY,
  "cep" varchar,
  "idBairro_FK" integer NOT NULL,
  "idCidade_Fk" integer NOT NULL,
  "idLogradouro_FK" integer NOT NULL
);

CREATE TABLE "DDI" (
  "nroDDI_PK" integer PRIMARY KEY
);

CREATE TABLE "DDD" (
  "nroDDD_PK" integer PRIMARY KEY
);

CREATE TABLE "FoneMotorista" (
  "idFoneMotorista_PK" integer PRIMARY KEY,
  "nroTelefone" varchar(10),
  "nroDDI_FK" integer NOT NULL,
  "nroDDD_FK" integer NOT NULL,
  "idMotorista_FK" integer NOT NULL
);

CREATE TABLE "EmailMotorista" (
  "idEmailMotorista_PK" integer PRIMARY KEY,
  "enderecoEmail" varchar,
  "idMotorista_FK" integer NOT NULL
);

CREATE TABLE "Sexo" (
  "idSexo_PK" integer PRIMARY KEY,
  "descricaoSexo" varchar
);

CREATE TABLE "CNH" (
  "idCNH_PK" integer PRIMARY KEY,
  "nroRegistro" varchar(12),
  "dtEmissao" date,
  "dtValidade" date,
  "idMotorista_FK" integer NOT NULL
);

CREATE TABLE "Motorista" (
  "idMotorista_PK" integer PRIMARY KEY,
  "cpf" varchar(15),
  "cnpj" varchar(19),
  "nome" varchar,
  "dtNascimento_Fundacao" date,
  "nroEndereco" integer,
  "compEndereco" varchar,
  "idSexo_FK" integer NOT NULL,
  "idEndereco_FK" integer NOT NULL
);

CREATE TABLE "Veiculo" (
  "idVeiculo_PK" integer PRIMARY KEY,
  "placa" varchar(8),
  "chassi" varchar(18),
  "renevam" varchar(13),
  "anoFabricacao" date,
  "idCategoria_FK" integer NOT NULL,
  "idModelo_FK" integer NOT NULL,
  "idCor_FK" integer NOT NULL
);

CREATE TABLE "Categoria" (
  "idCategoria_PK" integer PRIMARY KEY,
  "descricaoCategoria" varchar
);

CREATE TABLE "Cor" (
  "idCor_PK" integer PRIMARY KEY,
  "descricaoCor" varchar
);

CREATE TABLE "Marca" (
  "idMarca_PK" integer PRIMARY KEY,
  "descricaoMarca" varchar
);

CREATE TABLE "Modelo" (
  "idModelo_PK" integer PRIMARY KEY,
  "descricaoModelo" varchar,
  "anoLancamento" date,
  "idMarca_FK" integer NOT NULL
);

CREATE TABLE "Proprietario_Veiculo" (
  "idProprietario_Veiculo_FK_PK" integer NOT NULL,
  "idVeiculo_FK_PK" integer NOT NULL,
  "dtInicio" date,
  "dtFim" date
);

CREATE TABLE "IPVA" (
  "idIPVA_PK" integer PRIMARY KEY,
  "anoFiscal" date,
  "precoTabelaFIPE" decimal,
  "aliquota" decimal,
  "dtVencimento" date,
  "dtPagamento" date,
  "idStatusPagamento_FK" integer NOT NULL,
  "idVeiculo_FK" integer NOT NULL
);

CREATE TABLE "StatusPagamento" (
  "idStatusPagamento_PK" integer PRIMARY KEY,
  "descricaoStatusPagamento" varchar
);

CREATE TABLE "Infracao" (
  "idInfracao_PK" integer PRIMARY KEY,
  "dtInfracao" date,
  "horaInfracao" time,
  "idTipoInfracao_FK" integer NOT NULL,
  "localInfracao_FK" integer NOT NULL,
  "idVeiculo_FK" integer NOT NULL,
  "idMotorista" integer NOT NULL
);

CREATE TABLE "TipoInfracao" (
  "idTipoInfracao_PK" integer PRIMARY KEY,
  "descricaoTipoInfracao" varchar
);

ALTER TABLE "Endereco" ADD FOREIGN KEY ("idBairro_FK") REFERENCES "Bairro" ("idBairro_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Endereco" ADD FOREIGN KEY ("idCidade_Fk") REFERENCES "Cidade" ("idCidade_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Endereco" ADD FOREIGN KEY ("idLogradouro_FK") REFERENCES "Logradouro" ("idLogradouro_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Cidade" ADD FOREIGN KEY ("siglaUF_FK") REFERENCES "UF" ("siglaUF_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Logradouro" ADD FOREIGN KEY ("siglaTipoLogradouro_FK") REFERENCES "TipoLogradouro" ("siglaTipoLogradouro_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Motorista" ADD FOREIGN KEY ("idEndereco_FK") REFERENCES "Endereco" ("idEndereco_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Motorista" ADD FOREIGN KEY ("idSexo_FK") REFERENCES "Sexo" ("idSexo_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "EmailMotorista" ADD FOREIGN KEY ("idMotorista_FK") REFERENCES "Motorista" ("idMotorista_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "FoneMotorista" ADD FOREIGN KEY ("idMotorista_FK") REFERENCES "Motorista" ("idMotorista_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "FoneMotorista" ADD FOREIGN KEY ("nroDDI_FK") REFERENCES "DDI" ("nroDDI_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "FoneMotorista" ADD FOREIGN KEY ("nroDDD_FK") REFERENCES "DDD" ("nroDDD_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "CNH" ADD FOREIGN KEY ("idMotorista_FK") REFERENCES "Motorista" ("idMotorista_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Veiculo" ADD FOREIGN KEY ("idCategoria_FK") REFERENCES "Categoria" ("idCategoria_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Veiculo" ADD FOREIGN KEY ("idModelo_FK") REFERENCES "Modelo" ("idModelo_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Modelo" ADD FOREIGN KEY ("idMarca_FK") REFERENCES "Marca" ("idMarca_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Veiculo" ADD FOREIGN KEY ("idCor_FK") REFERENCES "Cor" ("idCor_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Proprietario_Veiculo" ADD FOREIGN KEY ("idProprietario_Veiculo_FK_PK") REFERENCES "Motorista" ("idMotorista_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Proprietario_Veiculo" ADD FOREIGN KEY ("idVeiculo_FK_PK") REFERENCES "Veiculo" ("idVeiculo_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Infracao" ADD FOREIGN KEY ("idVeiculo_FK") REFERENCES "Veiculo" ("idVeiculo_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Infracao" ADD FOREIGN KEY ("idMotorista") REFERENCES "Motorista" ("idMotorista_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Infracao" ADD FOREIGN KEY ("idTipoInfracao_FK") REFERENCES "TipoInfracao" ("idTipoInfracao_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "Infracao" ADD FOREIGN KEY ("localInfracao_FK") REFERENCES "Endereco" ("idEndereco_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "IPVA" ADD FOREIGN KEY ("idVeiculo_FK") REFERENCES "Veiculo" ("idVeiculo_PK") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "IPVA" ADD FOREIGN KEY ("idStatusPagamento_FK") REFERENCES "StatusPagamento" ("idStatusPagamento_PK") DEFERRABLE INITIALLY IMMEDIATE;
