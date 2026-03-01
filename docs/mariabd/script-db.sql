CREATE TABLE vendas (
  cliente_id CHAR(36) NOT NULL,
  veiculo_id CHAR(36) NOT NULL,
  preco DECIMAL(19,2) NOT NULL,
  data_venda DATETIME NOT NULL,
  status_pagamento VARCHAR(255),
  PRIMARY KEY (cliente_id, veiculo_id)
);