package br.com.fiap.venda.infrastructure.adapter.output.rest.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class VeiculoResponse {

    UUID id;
    String status;

}
