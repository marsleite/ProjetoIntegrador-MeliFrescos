INSERT INTO warehouse(warehouse_code) VALUES(1);

INSERT INTO section(current_capacity, max_capacity, storage_type, warehouse_warehouse_code) VALUES(0, 100, 'FRESH', 1);
INSERT INTO section(current_capacity, max_capacity, storage_type, warehouse_warehouse_code) VALUES(0, 100, 'REFRIGERATED', 1);
INSERT INTO section(current_capacity, max_capacity, storage_type, warehouse_warehouse_code) VALUES(0, 100, 'FROZEN', 1);

-- products
-- FRESH
INSERT INTO products(product_name, product_type, product_description) VALUES ('banana', 'FRESH', 'banana productDescription');
INSERT INTO products(product_name, product_type, product_description) VALUES ('uva', 'FRESH', 'uva productDescription');
INSERT INTO products(product_name, product_type, product_description) VALUES ('tomate', 'FRESH', 'tomate productDescription');
-- REFRIGERATED
INSERT INTO products(product_name, product_type, product_description) VALUES ('manteiga', 'REFRIGERATED', 'manteiga productDescription');
INSERT INTO products(product_name, product_type, product_description) VALUES ('iogurte', 'REFRIGERATED', 'iogurte productDescription');
INSERT INTO products(product_name, product_type, product_description) VALUES ('queijo', 'REFRIGERATED', 'queijo productDescription');
-- FROZEN
INSERT INTO products(product_name, product_type, product_description) VALUES ('lasanha', 'FROZEN', 'lasanha productDescription');
INSERT INTO products(product_name, product_type, product_description) VALUES ('carne', 'FROZEN', 'carne productDescription');
INSERT INTO products(product_name, product_type, product_description) VALUES ('peixe', 'FROZEN', 'peixe productDescription');

-- inbound_order
-- FRESH
INSERT INTO inbound_order(order_date, section_section_code) VALUES ('2022-04-25', 1);
-- REFRIGERATED
INSERT INTO inbound_order(order_date, section_section_code) VALUES ('2022-04-25', 2);
-- FROZEN
INSERT INTO inbound_order(order_date, section_section_code) VALUES ('2022-04-25', 3);

-- batch
-- FRESH
INSERT INTO batch(current_temperature, minimum_temperature, unit_price, initial_quantity, current_quantity, manufacturing_date, due_date, inbound_order_order_number, product_product_id) VALUES (15, 10, 2, 5, 5, '2022-03-25', '2022-04-25', 1, 1);
-- REFRIGERATED
INSERT INTO batch(current_temperature, minimum_temperature, unit_price, initial_quantity, current_quantity, manufacturing_date, due_date, inbound_order_order_number, product_product_id) VALUES (10, 8, 2, 5, 5, '2022-03-25', '2022-04-25', 2, 4);
-- FROZEN
INSERT INTO batch(current_temperature, minimum_temperature, unit_price, initial_quantity, current_quantity, manufacturing_date, due_date, inbound_order_order_number, product_product_id) VALUES (-15, -20, 2, 5, 5, '2022-03-25', '2022-04-25', 3, 7);

-- USERS
INSERT INTO users(fullname, email, password) VALUES('Jose Alfredo', 'jose@gmail.com', '$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy');
INSERT INTO users(fullname, email, password) VALUES('Marcos Silva', 'marcos@gmail.com', '$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy');
INSERT INTO users(fullname, email, password) VALUES('Geovane Marquies', 'geovane@gmail.com', '$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy');
INSERT INTO users(fullname, email, password) VALUES('Meli', 'meli@gmail.com', '$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy');

-- PROFILE
INSERT INTO profile(name) VALUES('SUPERVISOR');
INSERT INTO profile(name) VALUES('BUYER');
INSERT INTO profile(name) VALUES('SELLER');
INSERT INTO profile(name) VALUES('ADMIN');

-- USERS_PROFILE
INSERT INTO users_profiles(user_id, profiles_id) VALUES(1, 1);
INSERT INTO users_profiles(user_id, profiles_id) VALUES(3, 3);
INSERT INTO users_profiles(user_id, profiles_id) VALUES(2, 2);
INSERT INTO users_profiles(user_id, profiles_id) VALUES(4, 4);

