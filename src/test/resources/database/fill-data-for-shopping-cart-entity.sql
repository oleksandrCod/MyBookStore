INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted) VALUES (1, 'bob@example.com', '12345678', 'Bob', 'Bober', 'Vilna 7', false);

INSERT INTO shopping_carts (id, user_id, is_deleted) VALUES (1, 1, false);
