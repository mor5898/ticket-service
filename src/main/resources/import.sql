CREATE TABLE customer (
    stripe_id VARCHAR(256),
    customer_name VARCHAR(256),
    email VARCHAR(256),
    CONSTRAINT pk_customer PRIMARY KEY (stripe_id)
);

CREATE TABLE ticket (
    id VARCHAR(256),
    status VARCHAR(32),
    price INTEGER,
    tax_rate INTEGER,
    customer_id VARCHAR(256),
    ticket_type_id VARCHAR(256),
    CONSTRAINT pk_ticket PRIMARY KEY (id),
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(stripe_id)
);