package br.com.fiap.venda.infrastructure.config;

import br.com.fiap.venda.application.port.input.CadastrarVendaUseCase;
import br.com.fiap.venda.application.port.output.VendaRepositoryPort;
import br.com.fiap.venda.application.port.output.VeiculoConsultaPort;
import br.com.fiap.venda.application.service.CadastrarVendaService;
import br.com.fiap.venda.infrastructure.security.TokenRelay;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BeanConfiguration {

    @Bean
    CadastrarVendaUseCase cadastrarCompraUseCase(
            VendaRepositoryPort repository,
            VeiculoConsultaPort veiculoConsultaPort
    ) {
        return new CadastrarVendaService(repository, veiculoConsultaPort);
    }

    @Bean
    RestClient veiculoRestClient(
            @Value("${integration.veiculo.base-url}") String baseUrl,
            TokenRelay tokenRelay
    ) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor((request, body, execution) -> {
                    tokenRelay.currentBearerTokenValue()
                            .ifPresent(jwt -> request.getHeaders().setBearerAuth(jwt));
                    return execution.execute(request, body);
                })
                .build();
    }

}
