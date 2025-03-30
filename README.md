# API Login e Cadastro de Usuários - Backend

Bem-vindo ao repositório `Backend_Cadastro`! Este projeto é uma aplicação backend desenvolvida como parte de um teste técnico ou exemplo de implementação. Ele inclui endpoints para gerenciamento de usuários e outras funcionalidades básicas, com foco em demonstrar boas práticas de desenvolvimento e organização de código.

## Objetivo
O objetivo deste repositório é fornecer uma base funcional para uma API backend, incluindo operações CRUD (criação, leitura, atualização e exclusão) e autenticação básica com geração de tokens. Este projeto pode ser utilizado como ponto de partida para aprendizado ou como referência em processos seletivos.

## Tecnologias Utilizadas

- **Linguagem**: [Java]
- **Framework**: [Spring Boot]
- **Banco de Dados**: [H2]
- **Outras Dependências**: [JWT para autenticação]

## Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:
- [Linguagem e versão, Java 21]
- [Gerenciador de dependências, Maven]
- [Banco de dados, H2]

## Como Rodar o Projeto

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/arthur-paraibano/Backend_Cadastro.git
   cd Backend_Cadastro
   ```

2. **Instale as dependências**:
   ```bash
   [Comando específico, mvn install]
   ```

3. **Configure o ambiente**:
   - Crie um arquivo `.env` ou ajuste as configurações no arquivo `[nome do arquivo de configuração]` com as seguintes variáveis:
     ```
     DB_URL=[jdbc:h2:mem:duett]
     DB_USERNAME=[root]
     DB_PASSWORD=[root]
     ```

4. **Inicie a aplicação**:
   ```bash
   [Comando específico, mvn spring-boot:run]
   ```

5. **Teste os endpoints**:
   - A API estará disponível em `http://localhost:8080` (ou a porta configurada).

## Estrutura da API

### Endpoints Principais

- **POST /create**: Cria um novo recurso.
  - Exemplo de corpo da requisição:
    ```json
    {
        "name": "string",
        "email": "string",
        "password": "string",
        "cpf": "string",
        "profile": "string"
    }
    ```
  - Resposta de sucesso: `201 - Created with success`.

- **POST /user/id**: Busca um usuário pelo ID.
  - Exemplo de corpo da requisição:
    ```json
    {
        "Id": "0"
    }
    ```
  - Resposta de erro: `404 - User not found`.

- **PUT /user/update**: Atualiza um recurso existente.
    - Exemplo de corpo da requisição:
    ```json
    {
        "id": 0,
        "name": "string",
        "email": "string",
        "password": "string",
        "cpf": "string",
        "profile": "string"
    }
    ```
  - Resposta de sucesso: `200 - Update completed`.

- **DELETE /user/delete**: Remove um recurso.
    - Exemplo de corpo da requisição:
    ```json
    {
        "Id": "0"
    }
    ```
  - Resposta de sucesso: `200 - Successfully deleted`.

### Mensagens de Resposta

A API utiliza mensagens padronizadas para informar o resultado das operações:

- **Sucesso**:
  - `Created with success`
  - `Update completed`
  - `Successfully deleted`
  - `Password changed`

- **Erro**:
  - `The ID cannot be null`
  - `User not found`
  - `Username and password are required`
  - `Error generating token`

## Exemplos de Uso

### Criando um Usuário
```bash
curl -X 'POST' \
  'http://localhost:8080/auth/login' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "arthur",
  "password": "12345"
}'
```

### Resposta Esperada
```json
{
  "token": "eRubB3uKHncVStOCE",
  "id": 1,
  "name": "ARTHUR",
  "email": "ARTHK0@GMAIL.COM",
  "profile": "USUÁRIO",
  "firstLogin": true
}
```

## Contribuição

Contribuições são bem-vindas! Siga os passos abaixo:
1. Faça um fork do repositório.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`).
3. Commit suas mudanças (`git commit -m "Adiciona nova funcionalidade"`).
4. Envie para o repositório remoto (`git push origin feature/nova-funcionalidade`).
5. Abra um Pull Request.

## Contato

Desenvolvido por [Arthur Paraibano](https://github.com/arthur-paraibano). Para dúvidas ou sugestões, abra uma issue ou entre em contato diretamente pelo GitHub.