CREATE TABLE tb_phone
(
    id        SERIAL PRIMARY KEY,
    phone     VARCHAR(11) NOT NULL,
    client_id INTEGER NOT NULL,
    FOREIGN KEY (client_id) REFERENCES tb_client(id) ON DELETE CASCADE
);