CREATE TABLE tb_rent
(
    id        SERIAL PRIMARY KEY,
    rent_date     DATE NOT NULL,
    devolution_date DATE NOT NULL,
    client_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    FOREIGN KEY (client_id) REFERENCES tb_client(id),
    FOREIGN KEY (book_id) REFERENCES tb_book(id)
);