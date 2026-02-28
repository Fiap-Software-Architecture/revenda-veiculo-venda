# venda-service

Serviço do ecossistema **Revenda de Veículos** responsável por **registro de vendas**.

Este projeto foi desenvolvido em **Java 21 + Spring Boot** e segue o padrão de **Arquitetura Hexagonal (Ports & Adapters)**.

---

## 📦 O que é o projeto

O serviço **venda-service** expõe uma API REST para registrar vendas de clientes e orquestrar validações e atualizações relacionadas ao veículo..  
Ele persiste dados em **PostgreSQL** e (opcionalmente) protege endpoints usando **Keycloak** como servidor OAuth2/OIDC.

Recursos auxiliares para executar e testar estão em **`docs/`**:
- `docs/docker-compose/`: ambiente local (Postgres + Keycloak + serviços)
- `docs/postgres/`: script SQL do banco
- `docs/postman/`: collection do Postman
- `docs/openapi/`: especificação OpenAPI (JSON)
- `docs/keycloak/`: realm do Keycloak (`realm-revenda.json`)

---

## 🧱 Arquitetura (Hexagonal)

Estrutura principal do código:

- `domain/`  
  Regras de negócio, entidades/VOs e exceções de domínio (não depende de frameworks).
- `application/`  
  Casos de uso (ports de entrada), ports de saída e orquestração do fluxo.
- `infrastructure/`  
  Adaptadores e detalhes técnicos (REST Controllers, JPA, integrações externas, configurações Spring).

A ideia central é manter o **domínio isolado**, com dependências apontando **para dentro**:
**infra → application → domain**.

---

## ▶️ Como rodar localmente

### Opção A) Subir o ambiente completo via Docker Compose (recomendado)

1. Acesse a pasta do compose:
   ```bash
   cd docs/docker-compose
   ```

2. Suba os containers:
   ```bash
   docker compose --env-file .env up -d
   ```

3. A API ficará disponível em:
   - Serviço: `http://localhost:8082`
   - Swagger UI: `http://localhost:8082/swagger`

> Observação: o compose assume que você já tem as imagens locais (ex.: `client-service:0.0.1-SNAPSHOT`).  
> Para gerar a imagem via Maven (sem CI), rode:
> ```bash
> mvn -DskipTests package
> docker build -t venda-service:0.0.1-SNAPSHOT .
> ```

### Opção B) Rodar somente o serviço (sem Docker)

1. Suba um PostgreSQL local (ou use o `docs/postgres/scrip-db.sql`).
2. Exporte variáveis de ambiente (exemplos):
   ```bash
   export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/revenda_veiculos_db"
   export SPRING_DATASOURCE_USERNAME="postgres"
   export SPRING_DATASOURCE_PASSWORD="p0stgr3s"

   # (opcional) Keycloak (JWK Set URI)
   export KEYCLOAK_JWK_SET_URI="http://localhost:8089/realms/revenda/protocol/openid-connect/certs"
   ```

3. Execute:
   ```bash
   mvn spring-boot:run
   ```

---

## ✅ Como testar

### Testes unitários/integrados
```bash
mvn test
```

### Cobertura (JaCoCo) com gate mínimo de 80%
```bash
mvn verify
```

- O relatório JaCoCo é gerado em: `target/site/jacoco/index.html`
- O build falha se a cobertura mínima (80%) não for atendida.

---

## 🚀 Como funciona o deploy (CI/CD)

O pipeline está definido em **`.github/workflows/ci.yml`** e executa em **push** (branches `main`, `master`, `develop`) e **pull requests**.

Fluxo do pipeline:

1. **Build (sem testes)**  
   Compila e empacota o JAR com Maven:
   - `mvn -DskipTests package`

2. **Testes + Cobertura (>= 80%)**  
   Executa testes e aplica o “quality gate” de cobertura via JaCoCo:
   - `mvn verify`  
   Também publica o relatório como artifact (`jacoco-report`).

3. **Build da imagem Docker**  
   Gera a imagem do serviço:
   - `docker build -t venda-service:latest -t venda-service:${{ github.sha }} .`

> Observação: este pipeline **constrói** a imagem, mas **não publica** em registry e **não faz deploy** em runtime (EKS/EC2/etc).  
> Para deploy real, normalmente você adiciona:
> - Login e push para um registry (ex.: Amazon ECR / GHCR)
> - Etapa de deploy (Helm/Kustomize/kubectl, ou outro mecanismo)

---

## 🔎 Documentação

- OpenAPI: `docs/openapi/openapi.json`
- Postman: `docs/postman/revenda_veiculos.postman_collection.json`
- Script do banco: `docs/postgres/scrip-db.sql`
- Docker Compose: `docs/docker-compose/docker-compose.yml`
