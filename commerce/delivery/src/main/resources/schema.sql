DROP TABLE IF EXISTS deliveries, addresses CASCADE;

CREATE TABLE IF NOT EXISTS addresses
(
    address_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country    VARCHAR(50),
    city       VARCHAR(50),
    street     VARCHAR(50),
    house      VARCHAR(50),
    flat       VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS deliveries
(
    delivery_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    from_address_id UUID NOT NULL REFERENCES addresses (address_id),
    to_address_id   UUID NOT NULL REFERENCES addresses (address_id),
    order_id        UUID NOT NULL,
    delivery_state  VARCHAR(50)
);