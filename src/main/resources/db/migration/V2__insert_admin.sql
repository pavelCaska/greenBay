-- Insert a new user admin
insert into users (id, username, password, balance, roles)
values (UUID_TO_BIN(UUID()), 'admin', '$2a$10$MP7AzukhX7vMfqyxNp31mOGueQfpmT7ZVKMVngegqHrmyJ7STevkO', 0, 'ROLE_ADMIN'); -- password A12345