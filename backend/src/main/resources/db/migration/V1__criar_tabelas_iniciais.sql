CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    perfil VARCHAR(30) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(150),
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE equipamentos (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES clientes(id),
    tipo VARCHAR(50) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    numero_serie VARCHAR(100) UNIQUE,
    especificacoes_hardware TEXT,
    observacoes_fisicas TEXT
);

CREATE TABLE pecas (
    id BIGSERIAL PRIMARY KEY,
    codigo_sku VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(150) NOT NULL,
    categoria VARCHAR(80),
    quantidade_estoque INT NOT NULL DEFAULT 0,
    estoque_minimo INT NOT NULL DEFAULT 2,
    preco_custo DECIMAL(10,2) NOT NULL,
    preco_venda DECIMAL(10,2) NOT NULL
);

CREATE TABLE ordens_servico (
    id BIGSERIAL PRIMARY KEY,
    protocolo VARCHAR(20) NOT NULL UNIQUE,
    equipamento_id BIGINT NOT NULL REFERENCES equipamentos(id),
    tecnico_id BIGINT REFERENCES usuarios(id),
    status VARCHAR(30) NOT NULL,
    defeito_relatado TEXT NOT NULL,
    diagnostico_tecnico TEXT,
    valor_mao_de_obra DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    valor_total_pecas DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    valor_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    orcamento_aprovado BOOLEAN NOT NULL DEFAULT FALSE,
    data_abertura TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_aprovacao TIMESTAMP,
    data_finalizacao TIMESTAMP
);

CREATE TABLE os_historico_status (
    id BIGSERIAL PRIMARY KEY,
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id),
    usuario_id BIGINT REFERENCES usuarios(id),
    status_anterior VARCHAR(30),
    status_novo VARCHAR(30) NOT NULL,
    observacao TEXT,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE os_pecas_utilizadas (
    id BIGSERIAL PRIMARY KEY,
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id),
    peca_id BIGINT NOT NULL REFERENCES pecas(id),
    quantidade INT NOT NULL,
    preco_unitario_cobrado DECIMAL(10,2) NOT NULL,
    custo_unitario_epoca DECIMAL(10,2) NOT NULL
);
