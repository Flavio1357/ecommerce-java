# E-commerce Java

Projeto de e-commerce desenvolvido em **Java 17** com foco em Programação Orientada a Objetos, boas práticas de desenvolvimento, testes automatizados e persistência com **PostgreSQL**.

O projeto está sendo desenvolvido de forma incremental, adicionando novas funcionalidades conforme os conceitos são estudados e praticados.

## 🛠️ Tecnologias

- Java 17
- Maven
- JUnit 5
- PostgreSQL
- JDBC (driver `org.postgresql:postgresql`)
- Git e GitHub

## 📚 Conceitos praticados

- Encapsulamento
- Herança
- Abstração
- Polimorfismo
- Interfaces
- Sobrescrita de métodos (`@Override`)
- Sobrecarga de métodos
- Enumerações (`enum`)
- Associação entre classes
- Composição
- Collections (`List` / `ArrayList`)
- Exceções personalizadas
- Testes unitários
- Camada de acesso a dados (DAO)
- Conexão com banco via JDBC e `PreparedStatement`
- Variáveis de ambiente para credenciais

## 🛒 Funcionalidades atuais

### Usuários

- Cadastro de clientes
- Cadastro de funcionários
- Autenticação de usuários
- Diferenciação entre tipos de usuário através de herança
- **Persistência no PostgreSQL** (`UsuarioDAO`): inserir, buscar por id e buscar por e-mail

### Produtos

- Produtos nacionais
- Produtos internacionais
- Categorias de produtos
- Cálculo de frete
- Cálculo de imposto de importação
- Controle de estoque

### Carrinho

- Adição de produtos
- Remoção de itens
- Controle de quantidade
- Cálculo do subtotal
- Cálculo do valor total
- Validação de estoque

### Pedidos

- Criação de pedidos
- Associação entre cliente e pedido
- Associação entre pedido e carrinho
- Controle de status do pedido
- Validação de carrinho vazio
- Associação com formas de pagamento
- Processamento do pagamento
- Controle de pagamento duplicado
- Redução do estoque após pagamento
- Validação do estoque antes do pagamento

### 💳 Formas de pagamento

O sistema possui diferentes formas de pagamento através da interface `FormaPagamento`:

- Débito
- Crédito
- Boleto
- MIX

O pagamento MIX permite dividir o valor do pedido entre duas formas de pagamento.

Exemplo:

```
Total do pedido: R$ 6.000

Débito:  R$ 3.000
Crédito: R$ 3.000
```

## 🗄️ Banco de dados

O banco se chama `EcommerceDataBase` e o script de criação das tabelas está em [`sql/schema.sql`](sql/schema.sql).

| Tabela | Conteúdo |
|---|---|
| `usuario` | Clientes e funcionários na mesma tabela, diferenciados pela coluna `tipo` |
| `produto` | Produtos nacionais e internacionais, diferenciados pela coluna `tipo` |
| `pedido` | Pedido com cliente, status e data de criação |
| `item_pedido` | Itens do pedido, com o preço unitário no momento da compra |
| `pagamento` | Uma linha por forma de pagamento usada (o MIX gera duas linhas) |

Os enums (`CategoriaProduto`, `StatusPedido`) são gravados como texto.

## 🧪 Testes

O projeto utiliza **JUnit 5** para testes automatizados.

Os testes verificam, entre outros comportamentos:

- Criação de usuários
- Criação de produtos
- Funcionamento do carrinho
- Validação de quantidade
- Validação de estoque
- Processamento de pagamentos
- Pagamento MIX
- Criação de pedidos
- Validação de carrinho vazio
- Validação de pagamento
- Prevenção de pagamento duplicado
- Redução de estoque após pagamento

## 📁 Estrutura do projeto

```
ecommerce-java/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── Main.java          (teste manual de gravação no banco)
│   │       ├── model/
│   │       ├── pagamento/
│   │       ├── interfaces/
│   │       ├── enums/
│   │       ├── service/
│   │       ├── exception/
│   │       └── dao/
│   │           ├── ConexaoFactory.java
│   │           └── UsuarioDAO.java
│   │
│   └── test/
│       └── java/
│
├── sql/
│   └── schema.sql
├── pom.xml
└── README.md
```

## ▶️ Como executar

### Pré-requisitos

- Java 17 ou superior
- Maven
- PostgreSQL
- Git

### Clonar o projeto

```
git clone <URL_DO_REPOSITORIO>
cd ecommerce-java
```

### Configurar o banco

1. Crie um banco chamado `EcommerceDataBase` no PostgreSQL.
2. Execute o script `sql/schema.sql` nesse banco (pelo pgAdmin ou pelo `psql`):

```
psql -d EcommerceDataBase -f sql/schema.sql
```

### Configurar a conexão

A conexão é feita pela classe `ConexaoFactory`, que lê os dados de variáveis de ambiente. A senha **não** fica escrita no código.

| Variável | Padrão se não for definida |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/EcommerceDataBase` |
| `DB_USER` | `postgres` |
| `DB_PASSWORD` | vazio (defina a sua) |

Exemplo no terminal (Linux/Mac):

```
export DB_PASSWORD=suasenha
```

No VS Code, as variáveis podem ficar no arquivo `.vscode/launch.json` (na seção `env`), que está no `.gitignore` e não é enviado ao repositório.

### Executar os testes unitários

```
mvn test
```

### Testar a gravação no banco

Execute a classe `Main` (pelo VS Code ou pela sua IDE, com as variáveis de ambiente configuradas). Ela cadastra um cliente de teste, grava no PostgreSQL e busca de volta pelo e-mail.

Saída esperada:

```
Cliente gravado com id: 1
Cliente encontrado no banco:
Cliente: Cliente Teste
...
```

## 🚧 Próximas etapas

- `ProdutoDAO` (produtos nacionais e internacionais)
- `PedidoDAO`, com itens e pagamentos
- Camada de serviços (`Service`) usando os DAOs em vez de listas em memória
- Transações JDBC no pagamento do pedido (estoque, status e pagamento juntos)
- Armazenar a senha com hash em vez de texto puro
- Incluir frete e imposto de importação no total do pedido
- Avaliar o uso de `BigDecimal` para valores monetários
- Testes automatizados para a camada DAO

## 🎯 Objetivo

Este projeto tem como objetivo praticar e consolidar conhecimentos de **Java, Programação Orientada a Objetos, testes automatizados, persistência de dados e desenvolvimento de sistemas**, simulando a construção gradual de uma aplicação de e-commerce.
