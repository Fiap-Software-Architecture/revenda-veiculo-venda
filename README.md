# 💰 venda-service

Serviço responsável pelo gerenciamento de **Vendas** no projeto **Revenda Veículos**.

Implementado em **Spring Boot** com arquitetura **Hexagonal (Ports & Adapters)** e deploy automatizado na **AWS ECS (Fargate)**.

---

# 📌 Visão Geral

O `venda-service` é um microserviço responsável por:

- Registrar venda de veículo
- Associar cliente e veículo
- Registrar valor da venda
- Controlar status de pagamento
- Consultar vendas por cliente
- Consultar vendas por veículo

Durante o fluxo de venda, o serviço pode interagir com:

- cliente-service
- veiculo-service (para atualizar status para VENDIDO)

Ecossistema completo:

- cliente-service
- veiculo-service
- venda-service
- PostgreSQL (RDS)
- Keycloak
- AWS ECR
- AWS ECS
- Terraform (Infraestrutura como código)

---

# 🏗 Arquitetura

Este serviço segue **Arquitetura Hexagonal**.

```
src/main/java
 └── br.com.revenda.venda
     ├── domain
     ├── application
     ├── adapters
     │    ├── in
     │    └── out
     └── infrastructure
```

---

# ☁️ Infraestrutura AWS

Provisionada via Terraform:

- VPC
- Subnets públicas e privadas
- Security Groups
- RDS PostgreSQL
- ECR
- ECS (Fargate)
- Load Balancer
- Keycloak

---

# 🐳 Containerização

Build local:

```bash
mvn clean package
docker build -t venda-service .
```

---

# 📦 ECR

Padrão do repositório:

```
<project>-<environment>-venda
```

Exemplo:

```
revenda-veiculos-dev-venda
```

Tags publicadas:

```
revenda-veiculos-dev-venda:latest
revenda-veiculos-dev-venda:<commit-sha>
```

---

# 🚀 ECS (Elastic Container Service)

Deploy realizado via GitHub Actions.

## Componentes

- ECS Cluster
- Task Definition
- ECS Service
- Load Balancer
- Target Group

Arquivo de Task Definition:

```
infra/ecs/task-definition-venda.json
```

---

# 🔁 Fluxo CI/CD

```
Push na main
        ↓
GitHub Actions
        ↓
Build Maven
        ↓
Testes + Cobertura
        ↓
Build Docker
        ↓
Push para ECR
        ↓
Nova Task Definition
        ↓
Deploy no ECS
        ↓
Load Balancer
        ↓
Produção
```

---

# 🔐 Secrets necessários no GitHub

## AWS

- AWS_REGION
- AWS_ACCESS_KEY_ID
- AWS_SECRET_ACCESS_KEY

## ECR

- ECR_REPOSITORY_VENDA

## ECS

- ECS_CLUSTER
- ECS_SERVICE_VENDA

---

# 🌍 Variáveis de Ambiente

- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- KEYCLOAK_ISSUER_URI

---

# 🖥 Execução Local

```bash
docker-compose up -d
mvn spring-boot:run
```

---

# 🧠 Tecnologias

- Java 21
- Spring Boot
- Spring Security
- JPA / Hibernate
- PostgreSQL
- Docker
- GitHub Actions
- AWS (ECR, ECS, RDS, ALB)
- Terraform
