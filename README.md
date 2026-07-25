# ✈️ Planejador de Viagens

API REST para planejamento de viagens com geração de roteiros personalizados por IA.

> 🎥 **Vídeo do sistema funcionando:**


https://github.com/user-attachments/assets/0adf62b8-6376-4943-b9c2-b3b5c82dd8ed





---

## 🧠 Sobre o Projeto

Este é um projeto **Build to Learn**  o objetivo principal foi aprender na prática os fundamentos de **arquitetura hexagonal (Ports and Adapters)**, **TDD** e integração com **IA**, seguindo o método de desenvolvimento de **Fábio Akita**.

### Inspiração

O projeto foi inspirado por dois vídeos do canal **Mano Deyvin**:

- [🎬 Método Akita + IA — visão geral do método](https://www.youtube.com/watch?v=cWY7iBafw7I&t=1093s)
- [🎬 LiveCode — construção do backend do Planejador de Viagens](https://www.youtube.com/watch?v=h2SgxIqyiAI&t=3628s)

### Método aplicado

- **Arquitetura Hexagonal** — regras de negócio isoladas de frameworks e integrações externas
- **TDD dogmático** — teste primeiro, implementação depois
- **Desenvolvedor como arquiteto** — IA preenche o código, não decide a estrutura
- **Pair Programming com IA** — agente atua como piloto, dev como navegador
- **Documentação viva** — arquivo `cloud.md` como fonte única de verdade
- **Componentes in-memory** — integrações externas isoladas atrás de portas (clima, segurança, transporte)

---

## 🏗️ Stack Tecnológica

| Camada | Tecnologia |
|--------|-----------|
| **Linguagem** | Java 21 |
| **Framework** | Spring Boot 3.4.5 |
| **Arquitetura** | Hexagonal (Ports and Adapters) |
| **Banco** | PostgreSQL 16 |
| **Migrations** | Flyway |
| **Segurança** | JWT (Spring Security) |
| **IA** | Groq API (`llama-3.3-70b-versatile`) |
| **Documentação** | SpringDoc OpenAPI / Swagger |
| **Testes** | JUnit 5, Testcontainers, MockMvc |
| **Container** | Docker / Docker Compose |

---

## ⚙️ Como Rodar

### Pré-requisitos

- JDK 21+
- Docker
- Chave da API Groq (grátis: [console.groq.com](https://console.groq.com))

### Passo a passo

```bash
# 1. Clone o repositório
git clone https://github.com/alwisgabriel/planejador-viagem.git
cd planejador-viagem

# 2. Configure sua chave Groq
export GROQ_API_KEY="sua-chave-aqui"

# 3. Suba o banco de dados e inicie a aplicação
./run.sh
```

A aplicação estará disponível em: **`http://localhost:8080`**

Swagger UI: **`http://localhost:8080/swagger-ui.html`**

### Testar o fluxo completo

```bash
./test-api.sh
```

Ou use o modo interativo (perguntas e respostas):

```bash
./interativo.sh
```

---

## 📡 API Endpoints

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/auth/register` | Registrar novo usuário |
| `POST` | `/auth/login` | Login (retorna JWT) |
| `GET` | `/trips` | Listar viagens |
| `POST` | `/trips` | Criar viagem |
| `DELETE` | `/trips/{id}` | Deletar viagem |
| `POST` | `/trips/{id}/destinations` | Adicionar destino |
| `POST` | `/trips/{id}/plan` | Gerar roteiro com IA |

### Exemplo de payloads

```json
// POST /trips
{
  "title": "Europa 2026",
  "startDate": "2026-08-01",
  "endDate": "2026-08-15",
  "budget": 5000
}

// POST /trips/{id}/destinations
{
  "city": "Paris",
  "country": "França",
  "displayOrder": 1
}
```

---

## 🧪 Testes

```bash
mvn test
```

Usa **Testcontainers** — sobe PostgreSQL automaticamente em container. Não precisa do Docker Compose para rodar os testes.

---

## 🐳 Docker

```bash
# Build da imagem
docker build -t planejador-viagem .

# Rodar com Docker Compose
docker compose up -d
```

---

## 📁 Estrutura do Projeto

```
src/
├── main/java/com/planejadorviagem/
│   ├── domain/model/          → Entidades e regras de negócio
│   ├── application/
│   │   ├── port/in/           → Casos de uso (input ports)
│   │   ├── port/out/          → Portas de integração
│   │   └── service/           → Implementação dos casos de uso
│   ├── adapter/in/web/        → Controllers REST
│   ├── adapter/out/
│   │   ├── persistence/       → JPA + Flyway
│   │   └── integration/       → Climas, segurança, IA (Groq, Wikipedia)
│   └── infrastructure/
│       ├── config/            → Beans e configurações
│       └── security/          → JWT + Spring Security
├── main/resources/
│   ├── application.yml
│   └── db/migration/
└── test/
```

---

## 📝 Licença

Projeto educacional — sinta-se livre para estudar, modificar e compartilhar.
