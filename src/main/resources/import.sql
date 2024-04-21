DROP SCHEMA IF EXISTS benevolo_ticket_service CASCADE;
CREATE SCHEMA benevolo_ticket_service;

SET schema 'benevolo_ticket_service';

CREATE TABLE customer (
    id VARCHAR(256),
    stripe_id VARCHAR(256),
    email VARCHAR(256),
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE ticket (
    id VARCHAR(256),
    public_id VARCHAR(6),
    status VARCHAR(32),
    booked_at TIMESTAMP,
    price INTEGER,
    tax_rate INTEGER,
    customer_id VARCHAR(256),
    ticket_type_id VARCHAR(256),
    event_id VARCHAR(256),
    CONSTRAINT pk_ticket PRIMARY KEY (id),
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT uq_public_id UNIQUE (id)
);

INSERT INTO customer(id, stripe_id, email)
VALUES ('aa75b92f-510c-44de-9fa0-5523ba6d96c2', '849380', 'a.d@mail.de');

INSERT INTO ticket(id, public_id, status, booked_at, price, tax_rate, customer_id, ticket_type_id, event_id)
VALUES ('6375b92f-510c-44de-9fa0-5523ba6d96c2', '654927', 'VALID', '2024-04-18 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('1375b92f-510c-44de-9fa0-5523ba6d96c2', '354927', 'VALID', '2024-04-19 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('2375b92f-510c-44de-9fa0-5523ba6d96c2', '454927', 'VALID', '2024-04-20 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('3375b92f-510c-44de-9fa0-5523ba6d96c2', '954927', 'VALID', '2024-04-21 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('4375b92f-510c-44de-9fa0-5523ba6d96c2', '234927', 'VALID', '2024-04-22 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('5375b92f-510c-44de-9fa0-5523ba6d96c2', '984927', 'REDEEMED', '2024-04-23 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('7375b92f-510c-44de-9fa0-5523ba6d96c2', '184927', 'REDEEMED', '2024-04-24 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('8375b92f-510c-44de-9fa0-5523ba6d96c2', '544927', 'REDEEMED', '2024-04-25 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('9375b92f-510c-44de-9fa0-5523ba6d96c2', '144927', 'REDEEMED', '2024-04-26 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('0375b92f-510c-44de-9fa0-5523ba6d96c2', '154927', 'CANCELLED', '2024-04-27 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('a375b92f-510c-44de-9fa0-5523ba6d96c2', '834927', 'CANCELLED', '2024-04-28 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('b375b92f-510c-44de-9fa0-5523ba6d96c2', '824927', 'CANCELLED', '2024-04-29 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6'),
       ('c375b92f-510c-44de-9fa0-5523ba6d96c2', '894927', 'CANCELLED', '2024-04-30 10:34:12', 2000, 19, 'aa75b92f-510c-44de-9fa0-5523ba6d96c2', '223f700f-5449-4e40-b509-bee0b5d139d6', '383f700f-5449-4e40-b509-bee0b5d139d6');