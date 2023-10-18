INSERT INTO books (id, title, author, isbn, price, is_deleted) VALUES (1, 'Book A', 'Author A', '10001000', 10, false);
INSERT INTO books (id, title, author, isbn, price, is_deleted) VALUES (2, 'Book D', 'Author D', '20002000', 11, false);
INSERT INTO books (id, title, author, isbn, price, is_deleted) VALUES (3, 'Book B', 'Author B', '30003000', 12, false);

INSERT INTO categories (id, name, is_deleted) VALUES (1, 'a', false);
INSERT INTO categories (id, name, is_deleted) VALUES (2, 'b', false);
INSERT INTO categories (id, name, is_deleted) VALUES (3, 'c', false);

INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (2, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (3, 2);
