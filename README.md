# E-commerce Java

Projeto de e-commerce desenvolvido em **Java 17** com foco em Programação Orientada a Objetos, boas práticas de desenvolvimento e testes automatizados.

O projeto está sendo desenvolvido de forma incremental, adicionando novas funcionalidades conforme os conceitos são estudados e praticados.

## 🛠️ Tecnologias

* Java 17
* Maven
* JUnit 5
* Git e GitHub

## 📚 Conceitos praticados

O projeto utiliza diversos conceitos de Programação Orientada a Objetos:

* Encapsulamento
* Herança
* Abstração
* Polimorfismo
* Interfaces
* Sobrescrita de métodos (`@Override`)
* Sobrecarga de métodos
* Enumerações (`enum`)
* Associação entre classes
* Composição
* Collections (`List` / `ArrayList`)
* Exceções personalizadas
* Testes unitários

## 🛒 Funcionalidades atuais

### Usuários

* Cadastro de clientes
* Cadastro de funcionários
* Autenticação de usuários
* Diferenciação entre tipos de usuário através de herança

### Produtos

* Produtos nacionais
* Produtos internacionais
* Categorias de produtos
* Cálculo de frete
* Cálculo de imposto de importação
* Controle de estoque

### Carrinho

* Adição de produtos
* Remoção de itens
* Controle de quantidade
* Cálculo do subtotal
* Cálculo do valor total
* Validação de estoque

### Pedidos

* Criação de pedidos
* Associação entre cliente e pedido
* Associação entre pedido e carrinho
* Controle de status do pedido
* Validação de carrinho vazio
* Associação com formas de pagamento
* Processamento do pagamento
* Controle de pagamento duplicado
* Redução do estoque após pagamento
* Validação do estoque antes do pagamento

### 💳 Formas de pagamento

O sistema possui diferentes formas de pagamento através da interface `FormaPagamento`:

* Débito
* Crédito
* Boleto
* MIX

O pagamento MIX permite dividir o valor do pedido entre duas formas de pagamento.

Exemplo:

```text
Total do pedido: R$ 6.000

Débito:  R$ 3.000
Crédito: R$ 3.000
```

## 🧪 Testes

O projeto utiliza **JUnit 5** para testes automatizados.

Os testes verificam, entre outros comportamentos:

* Criação de usuários
* Criação de produtos
* Funcionamento do carrinho
* Validação de quantidade
* Validação de estoque
* Processamento de pagamentos
* Pagamento MIX
* Criação de pedidos
* Validação de carrinho vazio
* Validação de pagamento
* Prevenção de pagamento duplicado
* Redução de estoque após pagamento

## 📁 Estrutura do projeto

```text
ecommerce-java/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── model/
│   │       ├── pagamento/
│   │       ├── interfaces/
│   │       ├── enums/
│   │       ├── service/
│   │       ├── exception/
│   │       └── dao/
│   │
│   └── test/
│       └── java/
│
├── sql/
├── pom.xml
└── README.md
```

## ▶️ Como executar

### Pré-requisitos

* Java 17 ou superior
* Maven
* Git

### Clonar o projeto

```bash
git clone <URL_DO_REPOSITORIO>
cd ecommerce-java
```

### Executar os testes

```bash
mvn test
```

## 🚧 Próximas etapas

O projeto continuará evoluindo com novas funcionalidades, incluindo:

* Camada de serviços (`Service`)
* DAO
* Integração com PostgreSQL
* JDBC
* Persistência de usuários
* Persistência de produtos
* Persistência de pedidos
* Persistência de pagamentos
* Melhorias nas regras de negócio
* Novos testes automatizados

## 🎯 Objetivo

Este projeto tem como objetivo praticar e consolidar conhecimentos de **Java, Programação Orientada a Objetos, testes automatizados, persistência de dados e desenvolvimento de sistemas**, simulando a construção gradual de uma aplicação de e-commerce.
