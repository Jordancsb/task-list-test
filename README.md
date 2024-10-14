# Impulso Team - Desafio técnico

Objetivo: criação de uma aplicação web com backend em Java e frontend em ReactJS

### Tecnologias e Ferramentas
- Java 17
- SpringBoot (Maven)
- ReactTs- Vite (yarn)
- JPA/Hibernate
- MySql
- Docker

![Doc1](https://github.com/user-attachments/assets/32bb8d0a-912a-4180-bcb4-77f37da96900)

## Arquitetura 
- Eureka Server: Todos os microsserviços registrados no Eureka.✅
- API Gateway: Ideia inicial é utilizar como loadBalancer e centralizados das rotas.✅
- Task Service: Serviço responsável por gerenciar tarefas (to-do list). Ele permite criar, editar, excluir e buscar tarefas.✅
- Submission Service: Gerencia submissões de tarefas. Ele registra quando uma tarefa foi submetida e processada.⌛
- User Service: Responsável pela criação e autenticação de usuários. Ele gerencia o registro de usuários, login e informações do perfil. ✅

*******
#### Tabela de conteúdo

1. [Infraestrutura](__#Infraestrutura__)

   - [1. MySQL](__#MySQL)

   - [2. Iniciando projeto](__#projeto)

   - [3. Rodando projeto](__#portas)

*******

<div _id_="Infraestrutura"_/_><br/><br/>
## ToDoList - Infraestrutura
Este repositório contém a configuração de infraestrutura utilizando Docker Compose,
configurada para executar múltiplos serviços em containers, incluindo PostgreSQL, RabbitMQ,
Graylog, Grafana, Prometheus, Loki e outros componentes para monitoramento e logging.
<br/><br/>
<div _id_='requisitos'_/_>
### Pré-requisitos
Antes de iniciar a configuração, certifique-se de ter os seguintes pré-requisitos instalados:

- Docker
- Docker Compose
- Serviços Configurados

<div _id_='MySQL'_/_>
### 1. MySQL
Cada aplicação tem um compose para o respectivo banco:

Imagem: mysql:8.0

Portas: 3307 a 3309

Volumes:

- mysql-data:/var/lib/mysql

Variáveis de Ambiente:

MYSQL_DATABASE: 
MYSQL_ROOT_PASSWORD: 

<div _id_='projeto'_/_>
### 1. Iniciando projeto
Para subir todos os containers e serviços configurados, execute o comando:

```
docker-compose up -d
```

### 2. Rodando projeto
Serviços do backend todos são startados via:

```
mvn spring-boot:run
```

Já o front end [task-management-front]() são iniciados via:

```
yarn & yarn run dev --host
```

<div _id_='portas'_/_><br/><br/>

#### 3. Acessos e Portas

```
cloud:
    gateway:
      routes:
        - id: USER-SERVICE
          uri: lb://USER-SERVICE
          predicates:
            - Path=/auth/**

        - id: TASK-SERVICE
          uri: lb://TASK-SERVICE
          predicates:
            - Path=/api/tasks/**

        - id: SUBMISSION-SERVICE
          uri: lb://SUBMISSION-SERVICE
          predicates:
            - Path=/submissions/**
```
Ao provisionar todos servidores a rota do Gateway distribui acesso conforme os prefixos do .yaml acima!

Eureka: http://localhost:8070

Gateway: http://localhost:5000

UserService: http://localhost:5001

TaskService: http://localhost:5002

SubmissionService: http://localhost:5002

Frontend (React): http://localhost:5173

Swagger: http://localhost/${PORT}/swagger-ui/index.html

## Suporte