DROP SCHEMA IF EXISTS benevolo_ticket_service CASCADE;
CREATE SCHEMA benevolo_ticket_service;

SET schema 'benevolo_ticket_service';

CREATE TABLE customer (
    id VARCHAR(256),
    stripe_id VARCHAR(256),
    email VARCHAR(256),
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE booking (
    id VARCHAR(256),
    event_id VARCHAR(256),
    booked_at TIMESTAMP,
    customer_id VARCHAR(256),
    CONSTRAINT pk_booking PRIMARY KEY (id),
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE booking_item (
    id VARCHAR(256),
    quantity INTEGER,
    ticket_type_id VARCHAR(256),
    booking_id VARCHAR(256),
    CONSTRAINT pk_booking_item PRIMARY KEY (id),
    CONSTRAINT fk_booking FOREIGN KEY (booking_id) REFERENCES booking(id)
);

CREATE TABLE ticket (
    id VARCHAR(256),
    public_id VARCHAR(6),
    status VARCHAR(32),
    price INTEGER,
    tax_rate INTEGER,
    booking_item_id VARCHAR(256),
    CONSTRAINT pk_ticket PRIMARY KEY (id),
    CONSTRAINT uq_public_id UNIQUE (id)
);

INSERT INTO customer(id, stripe_id, email)
VALUES ('aa75b92f-510c-44de-9fa0-5523ba6d96c2', '849380', 'a.d@mail.de');

INSERT INTO booking(id, event_id, booked_at, customer_id)
VALUES ('377f870c-5a29-46e4-ac86-706c0ff00dc2', '383f700f-5449-4e40-b509-bee0b5d139d6', '2024-04-18 10:34:12', 'aa75b92f-510c-44de-9fa0-5523ba6d96c2');

INSERT INTO booking_item(id, quantity, ticket_type_id, booking_id)
VALUES ('a4a83ecc-098c-445f-9d94-0c98b5c3de0b', 3, '223f700f-5449-4e40-b509-bee0b5d139d6', '377f870c-5a29-46e4-ac86-706c0ff00dc2'),
       ('6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6', 2, '223f700f-5449-4e40-b509-bee0b5d139d6', '377f870c-5a29-46e4-ac86-706c0ff00dc2');

INSERT INTO ticket(id, public_id, status, price, tax_rate, booking_item_id)
VALUES ('6375b92f-510c-44de-9fa0-5523ba6d96c2', '654927', 'VALID', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('1375b92f-510c-44de-9fa0-5523ba6d96c2', '354927', 'VALID', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('2375b92f-510c-44de-9fa0-5523ba6d96c2', '454927', 'VALID', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('3375b92f-510c-44de-9fa0-5523ba6d96c2', '954927', 'VALID', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('4375b92f-510c-44de-9fa0-5523ba6d96c2', '234927', 'VALID', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('5375b92f-510c-44de-9fa0-5523ba6d96c2', '984927', 'REDEEMED', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('7375b92f-510c-44de-9fa0-5523ba6d96c2', '184927', 'REDEEMED', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('8375b92f-510c-44de-9fa0-5523ba6d96c2', '544927', 'REDEEMED', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('9375b92f-510c-44de-9fa0-5523ba6d96c2', '144927', 'REDEEMED', 2000, 19, '6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6'),
       ('0375b92f-510c-44de-9fa0-5523ba6d96c2', '154927', 'CANCELLED', 2000, 19, '6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6'),
       ('a375b92f-510c-44de-9fa0-5523ba6d96c2', '834927', 'CANCELLED', 2000, 19, '6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6'),
       ('b375b92f-510c-44de-9fa0-5523ba6d96c2', '824927', 'CANCELLED', 2000, 19, '6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6'),
       ('c375b92f-510c-44de-9fa0-5523ba6d96c2', '894927', 'CANCELLED', 2000, 19, '6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6');