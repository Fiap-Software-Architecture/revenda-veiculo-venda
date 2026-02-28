package br.com.fiap.venda.infrastructure.config;

import br.com.fiap.venda.application.port.input.CadastrarVendaUseCase;
import br.com.fiap.venda.application.port.output.VendaRepositoryPort;
import br.com.fiap.venda.application.port.output.VeiculoConsultaPort;
import br.com.fiap.venda.infrastructure.security.TokenRelay;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class BeanConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(BeanConfiguration.class)
            .withBean(VendaRepositoryPort.class, () -> mock(VendaRepositoryPort.class))
            .withBean(VeiculoConsultaPort.class, () -> mock(VeiculoConsultaPort.class))
            .withBean(TokenRelay.class, () -> Mockito.mock(TokenRelay.class))
            .withPropertyValues("integration.veiculo.base-url=http://localhost:8081");

    @Test
    void deveCriarBeanCadastrarCompraUseCase() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(CadastrarVendaUseCase.class);
            assertThat(context.getBean(CadastrarVendaUseCase.class)).isNotNull();
        });
    }

    @Test
    void deveCriarBeanVeiculoRestClient() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(RestClient.class);
            assertThat(context.getBean(RestClient.class)).isNotNull();
        });
    }

}
