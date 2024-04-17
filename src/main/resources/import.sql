DROP SCHEMA IF EXISTS benevolo_ticket_service CASCADE;
CREATE SCHEMA benevolo_ticket_service;

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
    event_id VARCHAR(256),
    CONSTRAINT pk_ticket PRIMARY KEY (id),
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(stripe_id)
);

INSERT INTO customer(stripe_id, customer_name, email)
VALUES ('849380', 'Andreas Dinauer', 'a.d@mail.de');

INSERT INTO ticket(id, status, price, tax_rate, customer_id, ticket_type_id, event_id)
VALUES ('6375b92f-510c-44de-9fa0-5523ba6d96c2', 'VALID', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('1375b92f-510c-44de-9fa0-5523ba6d96c2', 'VALID', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('2375b92f-510c-44de-9fa0-5523ba6d96c2', 'VALID', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('3375b92f-510c-44de-9fa0-5523ba6d96c2', 'VALID', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('4375b92f-510c-44de-9fa0-5523ba6d96c2', 'VALID', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('5375b92f-510c-44de-9fa0-5523ba6d96c2', 'REDEEMED', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('7375b92f-510c-44de-9fa0-5523ba6d96c2', 'REDEEMED', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('8375b92f-510c-44de-9fa0-5523ba6d96c2', 'REDEEMED', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('9375b92f-510c-44de-9fa0-5523ba6d96c2', 'CANCELLED', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('0375b92f-510c-44de-9fa0-5523ba6d96c2', 'CANCELLED', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('a375b92f-510c-44de-9fa0-5523ba6d96c2', 'CANCELLED', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('b375b92f-510c-44de-9fa0-5523ba6d96c2', 'CANCELLED', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('c375b92f-510c-44de-9fa0-5523ba6d96c2', 'CANCELLED', 2000, 19, '849380', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6');