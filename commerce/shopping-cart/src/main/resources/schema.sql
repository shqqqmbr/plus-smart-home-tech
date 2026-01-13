DROP TABLE IF EXISTS carts_products CASCADE;
DROP TABLE IF EXISTS shopping_carts CASCADE;

CREATE TABLE IF NOT EXISTS shopping_carts
(
    shopping_cart_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username         VARCHAR,
    activated        BOOLEAN
);

CREATE TABLE IF NOT EXISTS carts_products
(
    shopping_cart_id UUID REFERENCES shopping_carts (shopping_cart_id),
    product_id       UUID NOT NULL,
    quantity         INT,
    PRIMARY KEY (shopping_cart_id, product_id)
);