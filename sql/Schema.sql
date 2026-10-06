Schema · SQL

CREATE TABLE usuario (
    id        SERIAL PRIMARY KEY,
    tipo      VARCHAR(20)  NOT NULL CHECK (tipo IN ('CLIENTE', 'FUNCIONARIO')),
    nome      VARCHAR(100) NOT NULL,
    email     VARCHAR(150) NOT NULL UNIQUE,
    senha     VARCHAR(255) NOT NULL,
    cpf       VARCHAR(14)  NOT NULL UNIQUE,
    endereco  VARCHAR(255),   -- só Cliente
    cargo     VARCHAR(100),   -- só Funcionario
    CONSTRAINT ck_usuario_campos CHECK (
        (tipo = 'CLIENTE'     AND endereco IS NOT NULL AND cargo IS NULL) OR
        (tipo = 'FUNCIONARIO' AND cargo    IS NOT NULL AND endereco IS NULL)
    )
);
 
CREATE TABLE produto (
    id                    SERIAL PRIMARY KEY,
    tipo                  VARCHAR(20)   NOT NULL CHECK (tipo IN ('NACIONAL', 'INTERNACIONAL')),
    nome                  VARCHAR(150)  NOT NULL,
    descricao             TEXT,
    preco                 NUMERIC(12,2) NOT NULL CHECK (preco >= 0),
    estoque               INT           NOT NULL CHECK (estoque >= 0),
    distancia             NUMERIC(10,2) NOT NULL DEFAULT 0,
    categoria             VARCHAR(50)   NOT NULL,
    percentual_importacao NUMERIC(5,2),   -- só ProdutoInternacional
    CONSTRAINT ck_produto_campos CHECK (
        (tipo = 'INTERNACIONAL' AND percentual_importacao IS NOT NULL) OR
        (tipo = 'NACIONAL'      AND percentual_importacao IS NULL)
    )
);
 
CREATE TABLE pedido (
    id         SERIAL PRIMARY KEY,
    cliente_id INT         NOT NULL REFERENCES usuario(id),
    status     VARCHAR(30) NOT NULL,
    criado_em  TIMESTAMP   NOT NULL DEFAULT now()
);
 
-- O Carrinho não vira tabela: seus itens são gravados como itens do pedido.
-- preco_unitario guarda o preço no momento da compra (o preço do produto pode mudar depois).
CREATE TABLE item_pedido (
    id             SERIAL PRIMARY KEY,
    pedido_id      INT           NOT NULL REFERENCES pedido(id) ON DELETE CASCADE,
    produto_id     INT           NOT NULL REFERENCES produto(id),
    quantidade     INT           NOT NULL CHECK (quantidade > 0),
    preco_unitario NUMERIC(12,2) NOT NULL
);
 
-- Uma linha por forma de pagamento usada: pagamento MIX = 2 linhas no mesmo pedido.
-- (Rascunho: ajustar depois de ver as classes de pagamento/)
CREATE TABLE pagamento (
    id        SERIAL PRIMARY KEY,
    pedido_id INT           NOT NULL REFERENCES pedido(id) ON DELETE CASCADE,
    forma     VARCHAR(20)   NOT NULL,   -- DEBITO, CREDITO, BOLETO
    valor     NUMERIC(12,2) NOT NULL CHECK (valor > 0)
);