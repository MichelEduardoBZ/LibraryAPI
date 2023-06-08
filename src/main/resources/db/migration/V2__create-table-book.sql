CREATE TABLE tb_book
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    year_of_publication DATE NOT NULL
);