package br.com.fiap.venda.infrastructure.adapter.input.rest;

import br.com.fiap.venda.infrastructure.adapter.input.rest.request.CadastrarVendaRequest;
import br.com.fiap.venda.application.port.input.CadastrarVendaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendas")
@AllArgsConstructor
public class VendaController {

    private final CadastrarVendaUseCase cadastrarVenda;

    @Operation(summary = "Cadastrar venda de um veículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
            @ApiResponse(responseCode = "409", description = "Veículo indisponível para venda")
    })
    @PostMapping
    public ResponseEntity<Object> cadastrar(
            @RequestBody @Valid CadastrarVendaRequest request
    ) {
        cadastrarVenda.executar(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
