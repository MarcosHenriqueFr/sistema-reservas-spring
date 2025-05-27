# Sistema de Reservas de Restaurante 🖥️ 🍽

## Tecnologias 🖥️
<ul>
    <li>Java 21
    <li>Maven 4
    <li>Spring Boot 3.4.5
    <li>Spring Security 6
    <li>JPA (Hibernate)
    <li>PostgreSQL
    <li>Apache Commons Lang3
</ul>

## Como testar o projeto 🚀

### Pré-requisitos

Antes de iniciar o projeto, é necessário baixar os itens a seguir:
<ul>
    <li>JDK 21
    <li>Git
    <li>Banco PostgreSQL
</ul>

### Clonando

Primeiro clone o projeto para uma pasta da sua máquina:

```bash
git clone https://github.com/MarcosHenriqueFr/sistema-reservas-spring
```
Depois entre na pasta criada:

```bash 
cd sistema-reservas-spring
```

### Configurando as variáveis de ambiente

#### 1. application.properties

Dentro de **backend/src/main/resources** configure  `application.properties` de acordo com os dados do seu banco de dados. Desconsidere os comentários a seguir e use o código já presente do arquivo.

```bash
spring.application.name=booking-restaurant

spring.datasource.url=jdbc:postgresql://localhost:5432/[seu_banco] //Coloque uma database do PostgreSQL
spring.datasource.username=[seu_usuario_do_banco]
spring.datasource.password=[sua_senha_do_banco]
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.show-sql=true // Exclua essa linha para não ver as queries SQL
spring.jpa.hibernate.ddl-auto=create-drop // Mude essa linha caso não queira que o hibernate derrube o banco
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

jwt.issuer=booking-restaurant

// Essas chaves tem que ser criadas para o uso do JWT
jwt.public.key=classpath:app.pub
jwt.private.key=classpath:app.key
```

#### 2. Chaves públicas e privadas

Para configurar as chaves é preciso usar o **openssl**.
Primeiro no seu terminal vá até a pasta de resources:

```bash
cd backend/src/main/resources
```

Depois gere a chave privada usando o openssl:

```bash
openssl genrsa -out app.key 2048
```

Por fim, extraia a chave pública da chave privada:

```bash
openssl rsa -in app.key -pubout -out app.pub
```

Os caminhos das chaves já estão configuradas no application.properties.

Para voltar para a raiz do projeto backend digite:

```bash
cd ../../..
```

### Começando o projeto

Agora é só rodar o projeto, o maven precisa estar instalado globalmente:

```bash
mvn spring-boot:run
```

Caso não possua o maven digite:
```bash
./mvnw spring-boot:run
```

## Endpoints 🚩

| Endpoint               | Descrição                                          
|----------------------|-----------------------------------------------------
| <kbd>POST /registrar</kbd>     | Registra o usuário no banco de dados
| <kbd>POST /login</kbd>     | Autentica o usuário na API
| <kbd>POST /mesas</kbd>     | Registra uma nova Mesa de Restaurante
| <kbd>GET /mesas</kbd>     | Retorna todas as Mesas do Restaurante
| <kbd>PATCH /mesas/:{id} </kbd>     | Modifica os atributos de uma Mesa existente
| <kbd>DELETE /mesas/:{id}</kbd>     | Apaga o registro de uma Mesa existente do banco de dados.
| <kbd>GET /reservas</kbd>     | Pega todas as Reservas do usuário que realizou a Request
| <kbd>POST /reservas</kbd>     | Registra uma nova Reserva com os dados de Usuário, Mesa e horário.
| <kbd>PATCH /reservas/:{id}/cancelar</kbd>     | Altera o status da Reserva de ATIVA para CANCELADA

<br>

O conjunto de Requisições do Postman estará na **pasta** de `sistema-reservas-spring` do projeto, para testar com mais facilidade. Simplesmente importe o arquivo JSON para o Postman.

## O que foi aprendido 📝

<ul>
    <li> Como fazer uma estruturação de logs.
    <li> O uso de Spring Boot para Web Servers.
    <li> A aplicação de autenticação e autorização via Spring security com o uso de JWT.
    <li> Uso de uma estrutura semelhante ao MVC, com a interação do model com os controllers.
    <li> Aplicação de validações simples usando o Bean Validation com padrões RegEx.
    <li> Criação de banco de dados PostgreSQL via Hibernate.
    <li> Gerenciamento de Exceções e respostas para o usuário.
</ul>

## Possíveis Evoluções 📈


<ul>
    <li> Uso de testes unitários para projetos Spring Boot.
    <li> Docker para facilitar a demonstração da API.
    <li> A criação de um frontend para fácil visualização dos recursos da API.
    <li> Uso de cache com Spring Boot.
</ul>
<br><br>

**Obrigado pela sua atenção. Qualquer feedback é bem-vindo!**