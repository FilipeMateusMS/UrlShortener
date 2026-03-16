# 🔗 URL Shortener

Um encurtador de URLs simples desenvolvido com **Spring Boot**, permitindo gerar links curtos que redirecionam para URLs originais.

## 📌 Funcionalidades

- Criar URL curta
- Redirecionar automaticamente para a URL original
- Persistência em banco de dados
- Validação de URLs
- Conversão de DTOs com MapStruct

---

## 🛠️ Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- MapStruct
- Maven
- Jakarta Validation

---

---

## ⚙️ Como executar o projeto

### 1️⃣ Clonar o repositório
```bash
git clone https://github.com/seu-usuario/url-shortener.git
```

### 2️⃣ Entrar na pasta do projeto
```bash
cd url-shortener
```

### 3️⃣ Rodar a aplicação
```bash
mvn spring-boot:run
```

A aplicação iniciará em:
http://localhost:8080/api


## Endpoints

| Método | Endpoint                         | Descrição                       |
| ------ | -------------------------------- | ------------------------------- |
| POST   | `/api/shorten`                   | Cria uma URL encurtada          |
| GET    | `/api/shorten/{shortCode}`       | Retorna a URL original          |
| GET    | `/{shortCode}`                   | Redireciona para a URL original |
| PUT    | `/api/shorten/{shortCode}`       | Atualiza a URL original         |
| DELETE | `/api/shorten/{shortCode}`       | Remove uma URL encurtada        |
| GET    | `/api/shorten/{shortCode}/stats` | Retorna estatísticas da URL     |
