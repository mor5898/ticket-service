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

INSERT INTO customer(stripe_id, customer_name, email)
VALUES ('8738', 'Andreas Dinauer', 'andreas.dinauer@mail.de'),
       ('5338', 'Andreas Ott', 'andreas.ott@mail.de'),
       ('2238', 'Felix Sewald', 'felix.sewald@mail.de'),
       ('1938', 'Random Person', 'random.person@mail.de');

INSERT INTO ticket(id, status, price, tax_rate, customer_id, ticket_type_id)
VALUES ('d703af1c-71f4-4355-82e1-ad3856c237e3', 'VALID', 2500, 7, '8738', '223f700f-5449-4e40-b509-bee0b5d139d6'),
       ('0233998e-0d8c-40d2-914c-cb3462e9958c', 'VALID', 2500, 7, '5338', '223f700f-5449-4e40-b509-bee0b5d139d6'),
       ('99f1eead-c70a-4ea9-a651-91b867b4c00d', 'PENDING', 2500, 7, '2238', 'b23f700f-5449-4e40-b509-bee0b5d139d6'),
       ('9aa0088b-03c6-426d-94e2-eabd325ce89b', 'PENDING', 2500, 7, '1938', 'b23f700f-5449-4e40-b509-bee0b5d139d6'),
       ('4f3d382f-cce5-4291-8443-5926329d4736', 'REDEEMED', 2500, 7, '5338', '223f700f-5449-4e40-b509-bee0b5d139d6'),
       ('1703af1c-71f4-4355-82e1-ad3856c237e3', 'VALID', 2500, 7, '8738', '223f700f-5449-4e40-b509-bee0b5d139d6'),
       ('1233998e-0d8c-40d2-914c-cb3462e9958c', 'REDEEMED', 2500, 7, '5338', '223f700f-5449-4e40-b509-bee0b5d139d6'),
       ('19f1eead-c70a-4ea9-a651-91b867b4c00d', 'REDEEMED', 2500, 7, '2238', 'b23f700f-5449-4e40-b509-bee0b5d139d6'),
       ('1aa0088b-03c6-426d-94e2-eabd325ce89b', 'CANCELLED', 2500, 7, '1938', 'b23f700f-5449-4e40-b509-bee0b5d139d6'),
       ('1f3d382f-cce5-4291-8443-5926329d4736', 'CANCELLED', 2500, 7, '5338', '223f700f-5449-4e40-b509-bee0b5d139d6');