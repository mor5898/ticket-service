/*Testdaten Generierung*/

DROP SCHEMA IF EXISTS public CASCADE;

CREATE SCHEMA public;

CREATE TABLE person(
    id INTEGER,
    first_name VARCHAR(256),
    last_name VARCHAR(256),
    ending VARCHAR(256),
    PRIMARY KEY (id)
);

INSERT INTO person(id, first_name, last_name, ending)
VALUES (1, 'Andreas', 'Dinauer', '@mail.de'),
    (2, 'Felix', 'Sewald', '@gmail.com'),
    (3, 'Andreas', 'Ott', '@gmx.com'),
    (4, 'Maximilian', 'Berger', '@freenet.de'),
    (5, 'Eduard', 'Hauser', '@t-online.de'),
    (6, 'Lukas', 'Dinauer', '@mail.de'),
    (7, 'Emma', 'Müller', '@gmail.com'),
    (8, 'Marie', 'Schmidt', '@gmx.com'),
    (9, 'Mia', 'Weber', '@freenet.de'),
    (10, 'Clara', 'Mayer', '@t-online.de'),
    (11, 'Jan', 'Holtman', '@mail.de'),
    (12, 'Moritz', 'Schwarz', '@gmail.com'),
    (13, 'Linus', 'Baumeister', '@gmx.com'),
    (14, 'Anton', 'Weber', '@freenet.de'),
    (15, 'Tobi', 'Weiß', '@t-online.de');


CREATE TABLE price(
    price INTEGER,
    PRIMARY KEY (price)
);

INSERT INTO price(price)
VALUES (2100), (2050), (3450), (4000), (4280), (5020), (5250), (7080), (7980), (7120), (7770), (10080), (12080), (21080), (20990), (18980), (9080);

CREATE TABLE status(
    status VARCHAR(32),
    PRIMARY KEY (status)
);

INSERT INTO status(status)
VALUES ('VALID'), ('REDEEMED'), ('CANCELLED');
/*Testdaten Generierung*/

CREATE TABLE customer (
    id VARCHAR(256),
    stripe_id VARCHAR(256),
    email VARCHAR(256),
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE refund_link (
     id VARCHAR(256),
     CONSTRAINT pk_refund_link PRIMARY KEY (id)
);

CREATE TABLE booking (
    id VARCHAR(256),
    event_id VARCHAR(256),
    total_price INTEGER,
    booked_at TIMESTAMP,
    customer_id VARCHAR(256),
    refund_link_id VARCHAR(256),
    CONSTRAINT pk_booking PRIMARY KEY (id),
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_refund_link FOREIGN KEY (refund_link_id) REFERENCES refund_link(id)
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

CREATE TABLE cancellation (
    id VARCHAR(256),
    ticket_id VARCHAR(256),
    booking_id VARCHAR(256),
    requested_at TIMESTAMP,
    status VARCHAR(32),
    CONSTRAINT pk_cancellation PRIMARY KEY (id),
    CONSTRAINT fk_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    CONSTRAINT fk_booking FOREIGN KEY (booking_id) REFERENCES booking(id)
);

INSERT INTO customer(id, stripe_id, email)

VALUES ('aa75b92f-510c-44de-9fa0-5523ba6d96c2', '849380', 'a.d@mail.de'),
       ('bb75b92f-510c-44de-9fa0-5523ba6d96c2', '849381', 'a.o@icloud.com'),
       ('cc75b92f-510c-44de-9fa0-5523ba6d96c2', '849382', 'f.s@icloud.com');

INSERT INTO booking(id, event_id, total_price, booked_at, customer_id)
VALUES ('377f870c-5a29-46e4-ac86-706c0ff00dc2', '383f700f-5449-4e40-b509-bee0b5d139d6', 2300, '2024-05-12 10:34:12', 'aa75b92f-510c-44de-9fa0-5523ba6d96c2'),
       ('366f870c-5a29-46e4-ac86-706c0ff00dc2', '383f700f-5449-4e40-b509-bee0b5d139d6', 3350, '2024-05-15 10:34:12', 'cc75b92f-510c-44de-9fa0-5523ba6d96c2');

INSERT INTO booking_item(id, quantity, ticket_type_id, booking_id)
VALUES ('a4a83ecc-098c-445f-9d94-0c98b5c3de0b', 3, '223f700f-5449-4e40-b509-bee0b5d139d6', '377f870c-5a29-46e4-ac86-706c0ff00dc2'),
       ('6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6', 2, '223f700f-5449-4e40-b509-bee0b5d139d6', '377f870c-5a29-46e4-ac86-706c0ff00dc2'),
       ('b4a83ecc-098c-445f-9d94-0c98b5c3de0b', 3, '223f700f-5449-4e40-b509-bee0b5d139d6', '366f870c-5a29-46e4-ac86-706c0ff00dc2'),
       ('b379fd9f-a4d9-4828-a7a1-4bf89f36d0b6', 2, '223f700f-5449-4e40-b509-bee0b5d139d6', '366f870c-5a29-46e4-ac86-706c0ff00dc2');

INSERT INTO ticket(id, public_id, status, price, tax_rate, booking_item_id)
VALUES ('6375b92f-510c-44de-9fa0-5523ba6d96c2', '654927', 'VALID', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('1375b92f-510c-44de-9fa0-5523ba6d96c2', '354927', 'VALID', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('c375b92f-510c-44de-9fa0-5523ba6d96c2', '454927', 'VALID', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('3375b92f-510c-44de-9fa0-5523ba6d96c2', '954927', 'VALID', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('4375b92f-510c-44de-9fa0-5523ba6d96c2', '234927', 'VALID', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('5375b92f-510c-44de-9fa0-5523ba6d96c2', '984927', 'REDEEMED', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('7375b92f-510c-44de-9fa0-5523ba6d96c2', '184927', 'REDEEMED', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('8375b92f-510c-44de-9fa0-5523ba6d96c2', '544927', 'REDEEMED', 2000, 19, 'a4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('9375b92f-510c-44de-9fa0-5523ba6d96c2', '144927', 'REDEEMED', 2000, 19, '6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6'),
       ('0375b92f-510c-44de-9fa0-5523ba6d96c2', '154927', 'CANCELLED', 2000, 19, '6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6'),
       ('b375b92f-510c-44de-9fa0-5523ba6d96c3', '834927', 'CANCELLED', 2000, 19, '6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6'),
       ('b376b92f-510c-44de-9fa0-5523ba6d96c3', '824927', 'CANCELLED', 2000, 19, '6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6'),
       ('c375b92f-510c-44de-9fa0-5523ba6d96c3', '894927', 'CANCELLED', 2000, 19, '6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6'),
       ('d375b92f-510c-44de-9fa0-5523ba6d96c3', '894927', 'CANCELLED', 2000, 19, '6379fd9f-a4d9-4828-a7a1-4bf89f36d0b6'),
       ('a278b92f-510c-44de-9fa0-5523ba6d96c3', '134926', 'CANCELLED', 2000, 19, 'b4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('a375b92f-510c-44de-9fa0-5523ba6d96c3', '124926', 'CANCELLED', 2000, 19, 'b4a83ecc-098c-445f-9d94-0c98b5c3de0b'),
       ('b478b92f-510c-44de-9fa0-5523ba6d96c3', '194926', 'CANCELLED', 2000, 19, 'b379fd9f-a4d9-4828-a7a1-4bf89f36d0b6'),
       ('a575b92f-510c-44de-9fa0-5523ba6d96c3', '194926', 'CANCELLED', 2000, 19, 'b379fd9f-a4d9-4828-a7a1-4bf89f36d0b6');

INSERT INTO cancellation(id, ticket_id, booking_id, requested_at, status)

VALUES ('zn643g8l-510c-44de-9fa0-552m5a6d96c3', '6375b92f-510c-44de-9fa0-5523ba6d96c2', '377f870c-5a29-46e4-ac86-706c0ff00dc2', '2024-05-12 10:34:12', 'PENDING');


INSERT INTO customer(id, stripe_id, email)
SELECT gen_random_uuid(), gen_random_uuid(), CONCAT(LOWER(p.first_name), '.', LOWER(pp.last_name), ppp.ending) FROM person AS p, person AS pp, person AS ppp ORDER BY RANDOM() LIMIT 20;

INSERT INTO booking(id, event_id, total_price, booked_at, customer_id)
SELECT gen_random_uuid(), '383f700f-5449-4e40-b509-bee0b5d139d6', p.price, (NOW() - (floor(random() * 120) || ' days')::interval)::date
     , c.id
FROM generate_series(1, 3) id, price AS p, customer AS c;

INSERT INTO booking_item(id, quantity, ticket_type_id, booking_id)
SELECT gen_random_uuid(), floor(random() * 10) + 1, '223f700f-5449-4e40-b509-bee0b5d139d6', b.id
FROM generate_series(1,2) id, booking AS b;

INSERT INTO ticket(id, public_id, status, price, tax_rate, booking_item_id)
SELECT gen_random_uuid(), floor(random() * 1000000)::text, s.status, 2000, 19, b.id
FROM generate_series(1, 2), booking_item AS b, status AS s;
