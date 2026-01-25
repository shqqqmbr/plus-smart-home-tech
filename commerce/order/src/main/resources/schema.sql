DROP TABLE IF EXISTS order_products;
DROP TABLE IF EXISTS orders;

CREATE TABLE IF NOT EXISTS orders
(
    order_id         VARCHAR(36) PRIMARY KEY,
    shopping_cart_id UUID             NOT NULL,
    username         VARCHAR(255),
    payment_id       UUID,
    delivery_id      UUID,
    state            VARCHAR(50)      NOT NULL DEFAULT 'NEW',
    delivery_weight  DOUBLE PRECISION NOT NULL,
    delivery_volume  DOUBLE PRECISION NOT NULL,
    fragile          BOOLEAN          NOT NULL,
    total_price      DOUBLE PRECISION NOT NULL,
    delivery_price   DOUBLE PRECISION NOT NULL,
    product_price    DOUBLE PRECISION NOT NULL
);


CREATE TABLE IF NOT EXISTS order_products
(
    order_id   VARCHAR(36) NOT NULL,
    product_id UUID        NOT NULL,
    quantity   INTEGER     NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE
);