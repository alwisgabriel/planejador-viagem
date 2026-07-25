# Planejador de Viagens — Fonte Única de Verdade

> Status: MVP completo — sistema funcional com autenticação opcional, IA via Groq + Wikipedia RAG  
> Última atualização: 2026-07-25  
> Regra: nenhuma implementação deve divergir deste documento sem uma decisão registrada aqui.

## 1. Visão geral

O sistema receberá origem, destino e período da viagem e produzirá um planejamento composto por:

- itinerário diário;
- informações climáticas por local e período;
- dados de segurança e violência;
- recomendação do melhor modal de transporte;
- plano consolidado gerado por uma camada de integração com LLM.

O produto será tratado como um projeto **Build to Learn**: cada incremento deverá ser pequeno, testável e acompanhado da decisão técnica correspondente.

## 2. Objetivos e limites do MVP

### Objetivos

- Permitir cadastro e autenticação de usuários.
- Permitir criar, consultar, atualizar e excluir viagens.
- Associar destinos e períodos às viagens.
- Gerar e armazenar um plano de viagem.
- Manter integrações externas isoladas atrás de portas (interfaces).
- Executar testes contra PostgreSQL real por meio de Testcontainers.

### Fora do MVP — a validar

- Reserva de hotéis, voos ou ingressos.
- Pagamentos.
- Aplicativo mobile.
- Dados em tempo real garantidos por provedores externos.
- Recomendação personalizada baseada em histórico comportamental.

## 3. Requisitos funcionais

1. Usuários podem se registrar e autenticar.
2. Usuários autenticados podem gerenciar suas próprias viagens.
3. Uma viagem possui origem, destino(s), data inicial e data final.
4. O sistema pode solicitar a geração de um plano para uma viagem.
5. O plano contém itinerário, clima, segurança e transporte.
6. O sistema deve registrar o estado da geração e eventuais falhas de integração.
7. A API deve expor documentação OpenAPI.

## 4. Requisitos não funcionais

- Java 21 e Spring Boot 3.x.
- Persistência em PostgreSQL via Spring Data JPA.
- Evolução de schema exclusivamente por Flyway.
- Autenticação e autorização com Spring Security e JWT.
- Validação de entrada na borda da aplicação.
- Integrações externas com timeout, tratamento de erro e observabilidade básica.
- Testes unitários, de integração e de persistência.
- Ambiente reproduzível com Docker e Docker Compose.
- Segredos fornecidos por variáveis de ambiente; nunca versionados.
- Meta inicial de qualidade: toda regra de negócio nova deve nascer com teste.

## 5. Decisão arquitetural proposta

### Arquitetura escolhida: Hexagonal (Ports and Adapters)

A regra de negócio ficará no núcleo da aplicação e não dependerá de Spring, PostgreSQL, HTTP, LLM ou fornecedores externos.

- **Domínio:** entidades, objetos de valor e regras invariantes.
- **Aplicação:** casos de uso, portas de entrada e portas de saída.
- **Adaptadores de entrada:** controllers REST e configuração de segurança.
- **Adaptadores de saída:** JPA, Flyway, clientes de clima/segurança/transporte e cliente LLM.
- **Infraestrutura:** configuração técnica e composição dos adaptadores.

Essa escolha será revisada antes do primeiro caso de uso. A dependência deve apontar sempre para dentro: adaptadores dependem das portas; o domínio não conhece adaptadores.

## 6. Estrutura de diretórios proposta

```text
src/
├── main/java/<package-base>/
│   ├── domain/
│   │   ├── model/
│   │   └── exception/
│   ├── application/
│   │   ├── port/in/
│   │   ├── port/out/
│   │   └── service/
│   ├── adapter/in/web/
│   ├── adapter/out/persistence/
│   ├── adapter/out/integration/
│   └── infrastructure/
│       ├── config/
│       └── security/
├── main/resources/
│   ├── application.yml
│   └── db/migration/
└── test/
    ├── unit/
    └── integration/
```

> `package-base` será definido antes da criação do projeto Maven.

## 7. Modelo conceitual inicial

### Usuário

- `id: UUID`
- `email: String` único
- `passwordHash: String`
- `createdAt` e `updatedAt`

### Viagem

- `id: UUID`
- `userId: UUID`
- `origin: String`
- `startDate` e `endDate`
- `status` da geração/planejamento
- `createdAt` e `updatedAt`

### Destino

- `id: UUID`
- `tripId: UUID`
- `name: String`
- `country: String` opcional na primeira versão
- ordem no roteiro

### Plano de viagem

Entidade ou componente persistido a ser decidido após definir o contrato do caso de uso. A decisão deverá considerar versionamento do resultado da LLM, reprocessamento e auditoria.

### Decisões pendentes

- Nome e valor do `package-base`.
- Se uma viagem terá um ou vários destinos no MVP.
- Persistência do plano como estrutura normalizada, JSONB ou combinação dos dois.
- Provedor(es) reais para clima, segurança, transporte e LLM.
- Estratégia de atualização assíncrona da geração do plano.

## 8. Convenções de desenvolvimento

### Código

- Classes e métodos devem ter uma responsabilidade clara.
- Nomes devem expressar intenção; evitar abreviações.
- Regras de negócio não devem ser duplicadas entre controller e service.
- DTOs não devem vazar para o domínio.
- Exceções de negócio devem ser explícitas e mapeadas para respostas HTTP consistentes.
- Validação deve ocorrer na entrada e invariantes devem ser protegidas no domínio.
- Não adicionar abstrações sem uma necessidade concreta.

### Testes

- TDD: teste primeiro, implementação mínima depois, refatoração por último.
- Unitários para regras isoladas, Mockito somente nas fronteiras necessárias.
- Testes de integração para persistência e composição real dos componentes.
- Testcontainers para PostgreSQL.
- MockMvc para contratos HTTP e segurança.
- Cada teste deve validar comportamento observável, não detalhes acidentais de implementação.

### Git e commits

Formato proposto: **Conventional Commits**.

Exemplos: `feat(trip): create trip`, `test(trip): add creation use case`, `fix(auth): reject expired token`, `docs: define persistence model`.

Commits devem ser pequenos, compiláveis quando possível e representar uma decisão ou incremento coerente.

## 9. Dependências planejadas para o `pom.xml`

Versões serão alinhadas ao Spring Boot 3.x e registradas quando o projeto Maven for criado.

- `spring-boot-starter-web`
- `spring-boot-starter-validation`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-security`
- `spring-boot-starter-actuator`
- `postgresql` — runtime
- `flyway-core`
- `flyway-database-postgresql`
- biblioteca JWT — decisão entre Nimbus e JJWT a validar
- `springdoc-openapi-starter-webmvc-ui`
- `spring-boot-starter-test`
- `spring-security-test`
- `testcontainers-junit-jupiter`
- `testcontainers-postgresql`
- `mockito` — via `spring-boot-starter-test`, salvo necessidade específica
- plugin `spring-boot-maven-plugin`
- plugin `maven-failsafe-plugin` para testes de integração — a validar
- plugin de cobertura/linting — a escolher antes do CI

## 10. Sequência de execução

1. Aprovar ou alterar este documento.
2. Definir package base e decisões pendentes do MVP.
3. Criar o esqueleto Maven e a infraestrutura Docker.
4. Criar migrations após aprovar o modelo relacional.
5. Executar cada caso de uso em ciclo TDD.
6. Adicionar integrações externas por portas e adaptadores.
7. Implementar segurança e endpoints com testes de integração.
8. Finalizar OpenAPI, CI/CD e documentação operacional.

## 11. Registro de decisões

| Data | Decisão | Motivo | Status |
|---|---|---|---|
| 2026-07-24 | Usar arquitetura Hexagonal | Isolar regras de negócio e integrações externas | Proposta |
| 2026-07-24 | Usar UUIDs como identificadores | Evitar IDs previsíveis e facilitar distribuição | Proposta |
| 2026-07-24 | Usar TDD incremental | Garantir feedback curto e comportamento especificado | Proposta |
| 2026-07-24 | PostgreSQL 16 via Docker Compose | Reproduzir localmente a dependência de persistência | Implementado |
| 2026-07-24 | Compose inicia somente o banco no Passo 2 | O projeto ainda não possui `pom.xml` nem aplicação para construir | Implementado |
| 2026-07-24 | Viagem possui vários destinos (1:N) | Suportar escalas e roteiros com múltiplas cidades | Implementado |
| 2026-07-24 | Persistir planos gerados pela IA | Permitir histórico, edição e comparação entre versões | Implementado |
| 2026-07-24 | Armazenar conteúdo do plano como JSONB | Permitir evolução do formato sem migration a cada mudança | Implementado |
| 2026-07-24 | Usar `display_order` exclusivo por viagem | Preservar a ordem do roteiro | Implementado |
| 2026-07-24 | Criar o esqueleto Maven com Java 21 | Habilitar compilação e execução dos testes | Implementado |
| 2026-07-24 | Escrever o teste de criação antes da implementação | Aplicar TDD e definir o comportamento esperado | Implementado |
| 2026-07-24 | Usar Mockito para isolar `TripRepository` no teste unitário | Testar o caso de uso sem depender do banco | Implementado |
| 2026-07-24 | Implementar criação mínima de viagem | Fazer o primeiro teste passar antes de refatorar | Implementado |
| 2026-07-24 | Testar data final anterior à inicial | Impedir viagens com intervalo inválido | Implementado |
| 2026-07-24 | Testar orçamento negativo | Impedir valores de orçamento inválidos | Implementado |
| 2026-07-24 | Validar invariantes no domínio `Trip` | Manter regras essenciais protegidas independentemente do adaptador | Implementado |
| 2026-07-24 | Suíte TDD verde após as validações | Confirmar criação válida e rejeição dos casos inválidos | Implementado |
| 2026-07-24 | Extrair a porta de entrada `CreateTripUseCase` | Explicitar o contrato do caso de uso na arquitetura Hexagonal | Implementado |
| 2026-07-24 | Validar a refatoração com `mvn clean test` | Garantir que a mudança estrutural preservou o comportamento | Implementado |
| 2026-07-24 | Criar teste de persistência com Testcontainers e Flyway | Validar migrations e PostgreSQL real antes do adaptador | Implementado |
| 2026-07-24 | Adicionar dependências PostgreSQL, Flyway e Testcontainers | Preparar o teste de integração do banco | Implementado |
| 2026-07-24 | Execução bloqueada por incompatibilidade Docker API 1.32/1.44 | O cliente disponível é antigo para o daemon atual | Bloqueado no ambiente |
| 2026-07-25 | Definir `api.version=1.44` via system property no surefire | docker-java 3.4.2 não lê a env var `DOCKER_API_VERSION`; só lê system property `api.version` | Implementado |
| 2026-07-25 | Criar `GetTripsUseCase` | Listar viagens por usuário | Implementado |
| 2026-07-25 | Criar `DeleteTripUseCase` | Remover viagem por ID | Implementado |
| 2026-07-25 | Criar `AddDestinationUseCase` | Adicionar destino com cidade, país e ordem | Implementado |
| 2026-07-25 | Criar `Destination` como entidade de domínio | Isolar regras de validação de destino | Implementado |
| 2026-07-25 | Suíte com 11 testes TDD verde | Validar criação, listagem, deleção e adição de destino | Implementado |
| 2026-07-25 | Criar porta `WeatherPort` + adapter `InMemoryWeatherAdapter` | Isolar integração de clima atrás de porta | Implementado |
| 2026-07-25 | Criar porta `SecurityPort` + adapter `InMemorySecurityAdapter` | Isolar integração de segurança atrás de porta | Implementado |
| 2026-07-25 | Criar porta `TransportPort` + adapter `InMemoryTransportAdapter` | Isolar integração de transporte atrás de porta | Implementado |
| 2026-07-25 | Criar porta `LlmPort` + adapter `InMemoryLlmAdapter` | Isolar integração de LLM atrás de porta | Implementado |
| 2026-07-25 | Suíte completa com 15 testes verdes | Validar domínio, casos de uso e adapters in-memory | Implementado |
| 2026-07-25 | Adicionar dependências web, security, validation, JWT, OpenAPI | Preparar camada REST e segurança | Implementado |
| 2026-07-25 | Criar entidades JPA e adapters de persistência | TripJpaAdapter, UserJpaAdapter, DestinationJpaAdapter | Implementado |
| 2026-07-25 | Criar `User` como entidade de domínio + `UserRepository` port | Registrar e autenticar usuários | Implementado |
| 2026-07-25 | Criar `JwtService`, `JwtAuthenticationFilter`, `SecurityConfig` | Autenticação stateless com JWT | Implementado |
| 2026-07-25 | Criar `AuthController` (register/login) + `TripController` (CRUD + destinos) | API REST completa do MVP | Implementado |
| 2026-07-25 | Suíte de testes de integração com MockMvc + Testcontainers | 6 cenários HTTP validando auth, CRUD e destinos | Implementado |
| 2026-07-25 | Suíte completa com 22 testes verdes | Validar toda a aplicação | Implementado |
| 2026-07-25 | Trocar JWT por modo sem autenticação | Simplificar uso | Implementado |
| 2026-07-25 | Adicionar `GroqLlmAdapter` | Usar Groq API como LLM (gratuito e rápido) | Implementado |
| 2026-07-25 | Adicionar `WikipediaAdapter` (RAG) | Buscar dados reais das cidades antes de gerar roteiro | Implementado |
| 2026-07-25 | Alterar coluna `content` de JSONB para TEXT | Evitar erro de validação JSON | Implementado |
| 2026-07-25 | Trocar modelo Groq para `llama-3.3-70b-versatile` | Melhor precisão em cidades brasileiras | Implementado |
| 2026-07-25 | Criar script interativo `interativo.sh` | Interface via terminal para usuário final | Implementado |
| 2026-07-25 | Criar `.gitignore` e atualizar README | Preparar para publicação no repositório | Implementado |

