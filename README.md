# Produto Service

Serviço Java baseado em Spring Boot para gerenciamento de produtos, utilizando Postgres e arquitetura hexagonal (Ports & Adapters).

---

## 🏗️ Arquitetura

O projeto segue o padrão de arquitetura hexagonal, separando as regras de negócio (domínio) das implementações externas (gateways/adapters):

- **API**: Endpoints REST para operações de produtos.
- **Domain**: Modelos, exceções e portas (interfaces) do domínio.
- **Gateway**: Implementações de acesso a dados (JPA).
- **Service**: Lógica de negócio central.
- **Utils**: Utilitários.

---

## 📁 Estrutura de Pastas
```
src/main/java/com/fiap/postech/produto_service/
├── api/
│   ├── controller/
│   │   └── ProdutoController.java
│   ├── dto/
│   │   ├── ProdutoAtualizaRequest.java
│   │   ├── ProdutoDto.java
│   │   ├── ProdutoRequest.java
│   │   └── ResponseDto.java
│   └── mapper/
│       └── ProdutoMapper.java
├── domain/
│   ├── exceptions/
│   │   ├── ErroInternoException.java
│   │   ├── GlobalHandlerException.java
│   │   └── internal/
│   │       ├── InvalidPrecoException.java
│   │       ├── InvalidSkuException.java
│   │       ├── ProdutoExistsException.java
│   │       └── ProdutoNotFoundException.java
│   └── model/
│       └── Produto.java
├── gateway/
│   └── database/
│       ├── ProdutoRepositoryImpl.java
│       ├── entity/
│       │   └── ProdutoEntity.java
│       └── repository/
│           └── ProdutoRepositoryJPA.java
├── port/
│   ├── ProdutoRepositoryPort.java
│   └── ProdutoServicePort.java
├── service/
│   └── ProdutoServiceImpl.java
├── utils/
│   └── ConstantUtils.java
└── ProdutoServiceApplication.java
```
---

## 🧩 Principais Classes

- **ProdutoController**: Endpoints REST para cadastro, atualização, busca e remoção de produtos.
- **ProdutoServiceImpl**: Implementação da lógica de negócio.
- **ProdutoRepositoryPort**: Interface para chamada de endpoints na implementação de persistência dos produtos.
- **ProdutoRepositoryImpl**: Implementação do repositório usando JPA.
- **ProdutoEntity**: Entidade JPA para persistência.
- **Produto**: Modelo de domínio.
- **DTOs**: Objetos para transferência de dados entre camadas.
- **Exceções**: Tratamento centralizado de erros e validações.

---

## ⚙️ Configuração

O arquivo `src/main/resources/application.yml` define:

- Conexão com Postgres (variáveis necessárias em um arquivo .env).
- JPA configurado para atualização automática do schema e exibição de SQL.

---

## ▶️ Executando o Projeto

1. Configure o banco de dados e ajuste as configurações se necessário.
2. Rode o projeto com: `mvn spring-boot:run`
3. A aplicação estará disponível em `http://localhost:8080`.

---

## 📚 Documentação
A documentação dos endpoints pode ser acessada via Swagger em `/swagger-ui.html` (caso habilitado).
