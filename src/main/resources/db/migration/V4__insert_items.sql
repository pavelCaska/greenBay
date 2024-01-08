-- Insert new items - this script can't be run if the database is empty
-- When populating the item table for the first time, the randomly created user_ids must be inserted as seller_ids)

insert into items (id, name, description, photourl, starting_price, purchase_price, last_bid, sellable, seller_id)
values
    (UUID_TO_BIN(UUID()), 'Motorola', 'mobile phone', '/img/green_fox_logo.png', 75, 150, 0, true, UUID_TO_BIN('291866e6-ae21-11ee-95d2-088fc37f00c6')), -- user1
    (UUID_TO_BIN(UUID()), 'HP Elite', 'notebook', '/img/green_fox_logo.png', 100, 150, 0, true, UUID_TO_BIN('29186a45-ae21-11ee-95d2-088fc37f00c6')), -- user2
    (UUID_TO_BIN(UUID()), 'iPhone', 'mobile phone', '/img/green_fox_logo.png', 100, 150, 0, true, UUID_TO_BIN('29186b4f-ae21-11ee-95d2-088fc37f00c6')), -- user3
    (UUID_TO_BIN(UUID()), 'HP Pavillon', 'notebook', '/img/green_fox_logo.png', 200, 350, 0, true, UUID_TO_BIN('29186bd9-ae21-11ee-95d2-088fc37f00c6')), -- user4 ... and so on
    (UUID_TO_BIN(UUID()), 'Asus', 'notebook', '/img/green_fox_logo.png', 100, 150, 0, true, UUID_TO_BIN('291866e6-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'iPhone', 'mobile phone', '/img/green_fox_logo.png', 125, 200, 0, true, UUID_TO_BIN('29186a45-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'Samsung', 'mobile phone', '/img/green_fox_logo.png', 100, 150, 0, true, UUID_TO_BIN('29186b4f-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'Lenovo', 'mobile phone', '/img/green_fox_logo.png', 50, 100, 0, true, UUID_TO_BIN('29186bd9-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'Samsung', 'mobile phone', '/img/green_fox_logo.png', 75, 125, 0, true, UUID_TO_BIN('291866e6-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'Acer', 'notebook', '/img/green_fox_logo.png', 75, 150, 0, true, UUID_TO_BIN('29186a45-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'Asus', 'notebook', '/img/green_fox_logo.png', 150, 250, 0, true, UUID_TO_BIN('29186b4f-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'MacBook', 'notebook', '/img/green_fox_logo.png', 200, 300, 0, true, UUID_TO_BIN('29186bd9-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'iPhone', 'mobile phone', '/img/green_fox_logo.png', 150, 250, 0, true, UUID_TO_BIN('291866e6-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'Samsung', 'mobile phone', '/img/green_fox_logo.png', 50, 150, 0, true, UUID_TO_BIN('29186a45-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'Lenovo', 'tablet', '/img/green_fox_logo.png', 50, 100, 0, true, UUID_TO_BIN('29186b4f-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'iPad', 'tablet', '/img/green_fox_logo.png', 100, 150, 0, true, UUID_TO_BIN('29186bd9-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'Lenovo', 'tablet', '/img/green_fox_logo.png', 75, 125, 0, true, UUID_TO_BIN('291866e6-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'iPad', 'tablet', '/img/green_fox_logo.png', 150, 250, 0, true, UUID_TO_BIN('29186a45-ae21-11ee-95d2-088fc37f00c6')),
    (UUID_TO_BIN(UUID()), 'Lenovo', 'tablet', '/img/green_fox_logo.png', 10, 50, 0, true, UUID_TO_BIN('29186b4f-ae21-11ee-95d2-088fc37f00c6'));
