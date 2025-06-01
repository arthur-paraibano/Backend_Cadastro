### Backend (`Backend_Cadastro`)

```markdown
# API Login e Cadastro de Usuários - Backend

Bem-vindo ao repositório `Backend_Cadastro`! Este projeto é uma aplicação backend desenvolvida como parte de um desafio técnico para demonstrar habilidades em desenvolvimento web com Java Spring Boot. Ele implementa uma API REST para autenticação e gerenciamento de usuários, atendendo aos requisitos de um sistema de login seguro e funcional.

## Objetivo
O objetivo deste repositório é fornecer uma API backend funcional para autenticação de usuários, cadastro, troca de senha e gerenciamento de usuários por administradores, com foco em segurança, boas práticas e integração com um frontend React.

## Tecnologias Utilizadas

- **Linguagem**: Java 21
- **Framework**: Spring Boot
- **Banco de Dados**: H2 (in-memory)
- **Segurança**: Spring Security com autenticação básica
- **Outras Dependências**: JPA/Hibernate, Lombok

## Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:
- Java 21 (JDK)
- Maven (gerenciador de dependências)
- Git (para clonar o repositório)

## Como Rodar o Projeto

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/arthur-paraibano/Backend_Cadastro.git
   cd Backend_Cadastro
   ```

2. **Instale as dependências**:
   ```bash
   mvn install
   ```

3. **Configure o ambiente**:
   - O projeto usa um banco H2 in-memory, então não é necessária configuração adicional. As propriedades estão em `src/main/resources/application.properties`:
     ```
     spring.datasource.url=jdbc:h2:mem:testdb
     spring.datasource.driverClassName=org.h2.Driver
     spring.datasource.username=sa
     spring.datasource.password=
     spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
     spring.h2.console.enabled=true
     spring.jpa.hibernate.ddl-auto=update
     ```

4. **Inicie a aplicação**:
   ```bash
   mvn spring-boot:run
   ```

5. **Teste os endpoints**:
   - A API estará disponível em `http://localhost:8080`.
   - Acesse o console H2 em `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, usuário: `sa`, senha: vazia) para verificar os dados.

## Seeding do Banco de Dados
O banco é populado automaticamente ao iniciar a aplicação com dois usuários padrão:
- **Administrador**:
  - Usuário: `ADMIN`
  - Senha: `admin123`
  - Perfil: `ADMINISTRADOR`
- **Usuário Comum**:
  - Usuário: `JOAO`
  - Senha: `joao123`
  - Perfil: `USUÁRIO`

 Esses dados são inseridos via código Java (`DatabaseSeeder.java`), atendendo ao requisito de seeding sem uso de arquivos de backup.

## Estrutura da API

### Endpoints Principais

- **POST /create**  
  Cria um novo usuário.
  - **Corpo da requisição**:
    ```json
    {
        "name": "string",
        "email": "string",
        "password": "string",
        "cpf": "string",
        "profile": "USUÁRIO ou ADMINISTRADOR"
    }
    ```
  - **Resposta de sucesso**: `201 - Created`
  - **Resposta de erro**: `400 - Nome, e-mail ou CPF já cadastrado`

- **POST /auth/login**  
  Realiza o login e retorna dados do usuário.
  - **Corpo da requisição**:
    ```json
    {
        "name": "string",
        "password": "string"
    }
    ```
  - **Resposta de sucesso**:
    ```json
    {
        "token": "eRubB3uKHncVStOCE",
        "id": 1,
        "name": "ADMIN",
        "email": "admin@duett.com",
        "cpf": "123.456.789-00",
        "profile": "ADMINISTRADOR",
        "firstLogin": false
    }
    ```
  - **Resposta de erro**: `401 - Credenciais inválidas`

- **PUT /user/update**  
  Atualiza os dados de um usuário (ex.: troca de senha).
  - **Corpo da requisição**:
    ```json
    {
        "id": 1,
        "name": "string",
        "email": "string",
        "password": "string",
        "cpf": "string",
        "profile": "USUÁRIO ou ADMINISTRADOR"
    }
    ```
  - **Resposta de sucesso**: `200 - Updated`
  - **Resposta de erro**: `404 - User not found`

- **DELETE /user/delete**  
  Remove um usuário pelo ID.
  - **Corpo da requisição**:
    ```json
    {
        "id": 1
    }
    ```
  - **Resposta de sucesso**: `200 - Successfully deleted`
  - **Resposta de erro**: `404 - User not found`

- **GET /user/all**  
  Lista todos os usuários (acesso apenas para administradores).
  - **Resposta de sucesso**:
    ```json
    [
        {
            "id": 1,
            "name": "ADMIN",
            "email": "admin@duett.com",
            "cpf": "123.456.789-00",
            "profile": "ADMINISTRADOR"
        },
        ...
    ]
    ```
  - **Resposta de erro**: `403 - Forbidden`

### Mensagens de Resposta
A API retorna mensagens padronizadas via `RestMessage`:
- **Sucesso**: `"Created"`, `"Updated"`, `"Successfully deleted"`
- **Erro**: `"Nome, e-mail ou CPF já cadastrado"`, `"Credenciais inválidas"`, `"User not found"`, `"Forbidden"`

## Exemplos de Uso

### Login como Administrador
```bash
curl -X POST \
  http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "ADMIN",
    "password": "admin123"
}'
```
**Resposta**:
```json
{
    "token": "eRubB3uKHncVStOCE",
    "id": 1,
    "name": "ADMIN",
    "email": "admin@duett.com",
    "cpf": "123.456.789-00",
    "profile": "ADMINISTRADOR",
    "firstLogin": false
}
```

### Cadastro de Novo Usuário
```bash
curl -X POST \
  http://localhost:8080/create \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "maria",
    "email": "maria@duett.com",
    "password": "maria123",
    "cpf": "111.222.333-44",
    "profile": "USUÁRIO"
}'
```
**Resposta**:
```json
{
    "id": 3,
    "name": "maria",
    "email": "maria@duett.com",
    "cpf": "111.222.333-44",
    "profile": "USUÁRIO"
}
```

## Contribuição
Contribuições são bem-vindas! Siga os passos:
1. Faça um fork do repositório.
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`).
3. Commit suas mudanças (`git commit -m "Adiciona nova funcionalidade"`).
4. Push para o remoto (`git push origin feature/nova-funcionalidade`).
5. Abra um Pull Request.

## Contato
Desenvolvido por [Arthur Paraibano](https://github.com/arthur-paraibano). Para dúvidas ou sugestões, abra uma issue ou entre em contato pelo GitHub.
