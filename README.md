
# 📦 Sistema de Microsserviços - SP SpringBoot + AWS

## 📝 Descrição

Este projeto demonstra a implementação de uma **Arquitetura de Microsserviços** usando Java, Spring Boot, Docker e AWS. O sistema é composto por três microsserviços: **Pedido**, **Estoque** e **Cliente**. Cada microsserviço se comunica com os outros usando APIs REST, documentadas com **Swagger**, e persiste dados usando bancos de dados **PostgreSQL**.

---

## 📑 Índice

- [Funcionalidades](#funcionalidades)
- [Requisitos](#requisitos)
- [Configuração do Ambiente](#configuracao-do-ambiente)
- [Exemplos de Endpoints](#exemplos-de-endpoints)
- [Como Executar o Projeto](#como-executar-o-projeto)
- [Detalhes do Banco de Dados e Swagger](#detalhes-do-banco-de-dados-e-swagger)

---

## 🚀 Funcionalidades

- **Serviço de Pedido:**
  - Gerencia pedidos, verifica o estoque e reduz os níveis de inventário.
  - Garante que os produtos estejam disponíveis antes de confirmar os pedidos.
  - Retorna mensagens de erro claras para estoque insuficiente.

- **Serviço de Estoque:**
  - Mantém o inventário de produtos.
  - Permite consultar e atualizar as quantidades de produtos.

- **Serviço de Cliente:**
  - Gerencia os detalhes dos clientes.
  - Recupera o histórico de pedidos de clientes, comunicando-se com o serviço Pedido.

- **Integração com Swagger:**
  - Fornece documentação interativa da API para cada microsserviço.

- **Banco de Dados PostgreSQL:**
  - Cada microsserviço possui seu próprio banco de dados PostgreSQL para persistência de dados.
  - Gerenciado e orquestrado via Docker Compose.

---

## 🛠 Requisitos

Certifique-se de que as seguintes ferramentas estejam instaladas em sua máquina:

- [Java JDK 17+](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)
- [Maven](https://maven.apache.org/install.html): Para gerenciamento de dependências e construção do projeto.

---

## 🌟 Configuração do Ambiente

1. **Clone o repositório:**
    ```bash
    git clone git@github.com:VictorHugoCC/SP_SpringBoot_AWS_Desafio_03.git
    cd SP_SpringBoot_AWS_Desafio_03
    ```

2. **Inicie o Docker Compose:**
    Certifique-se de que o Docker esteja em execução e execute o seguinte comando para iniciar todos os serviços:
    ```bash
    docker-compose up -d
    ```

3. **Acesse a Documentação do Swagger:**
   - Serviço de Pedido: `http://<ip-da-instancia>:8090/swagger-ui/index.html`
   - Serviço de Estoque: `http://<ip-da-instancia>:8092/swagger-ui/index.html`
   - Serviço de Cliente: `http://<ip-da-instancia>:8094/swagger-ui/index.html`

---

## 📋 Exemplos de Endpoints

Aqui estão alguns exemplos de endpoints de API para cada serviço:

### Serviço de Pedido
- **Criar Pedido** (POST)
  - URL: `/api/v1/orders`
  - Corpo da Requisição:
    ```json
    {
      "customerId": 1,
      "products": [
        {"productId": 101, "quantity": 2}
      ]
    }
    ```
  - Resposta:
    ```json
    {
      "orderId": 5001,
      "status": "CONFIRMED"
    }
    ```

### Serviço de Estoque
- **Verificar Estoque** (GET)
  - URL: `/api/v1/stock/{productId}`
  - Resposta:
    ```json
    {
      "productId": 101,
      "quantity": 20
    }
    ```

### Serviço de Cliente
- **Obter Pedidos do Cliente** (GET)
  - URL: `/api/v1/customers/{customerId}/orders`
  - Resposta:
    ```json
    [
      {
        "orderId": 5001,
        "products": [{"productId": 101, "quantity": 2}],
        "status": "CONFIRMED"
      }
    ]
    ```

---

## 📂 Detalhes do Banco de Dados e Swagger

### Configuração do PostgreSQL
- Cada microsserviço utiliza seu próprio banco de dados PostgreSQL, configurado no `docker-compose.yml`:
  - **Banco de Dados do Pedido:** Acessível na porta `5434`
  - **Banco de Dados do Estoque:** Acessível na porta `5433`
  - **Banco de Dados do Cliente:** Acessível na porta `5432`

### Documentação do Swagger
- O Swagger está habilitado para todos os microsserviços, fornecendo documentação interativa da API e testes:
  - Navegue até `/swagger-ui/index.html` para cada serviço e explore os endpoints.

---

## 📦 Como Executar o Projeto

1. Certifique-se de que o Docker Compose está em execução de cada microsserviço. Entre em cada microsserviço e execute o comando a seguir:
    ```bash
    docker-compose up -d
    ```

2. Acesse as APIs via Swagger ou teste diretamente os endpoints usando ferramentas como Postman.

3. Monitore os logs de cada serviço para verificar o fluxo de trabalho.

---
## 🎬 Demostração/Evidencias
- Segue abaixo os links para os videos das Demostrações das aplicações/evidencias funcionando dentro da EC2
https://drive.google.com/drive/folders/1PIDsHL0uEsghDS5Lvx640tzPkdbEUYEM?usp=sharing

## 📊 Monitoramento e Logs

- Cada microsserviço registra detalhes das chamadas de API e interações com o banco de dados.
- Monitore os logs para depurar e entender o fluxo de dados entre os serviços.

---

## 📝 Notas

- Certifique-se de que os grupos de segurança da AWS permitem a comunicação entre as instâncias para o funcionamento adequado.
- Se encontrar problemas, verifique os logs do Docker e as configurações do banco de dados.

---
