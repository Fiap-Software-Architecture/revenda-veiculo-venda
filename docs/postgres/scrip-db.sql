CREATE TABLE IF NOT EXISTS vendas (
    cliente_id UUID NOT NULL,
    veiculo_id UUID NOT NULL,
    preco NUMERIC(15, 2) NOT NULL,
    status_pagamento VARCHAR(30) NULL,
    data_venda TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_vendas PRIMARY KEY (cliente_id, veiculo_id)
);
