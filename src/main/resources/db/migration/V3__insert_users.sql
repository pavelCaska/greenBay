-- Insert 4 new users
insert into users (id, username, password, balance, roles)
values
    (UUID_TO_BIN(UUID()), 'user1', '$2a$10$rBGGl.1g43/HIEhcUDDUvOmAGe1Ur0.BFK6lJuuwOM18CfC3SYkje', 100, 'ROLE_USER'), -- password u12345
    (UUID_TO_BIN(UUID()), 'user2', '$2a$10$EMNauqZ6unuvHw5NugaYi.bEdMvAWSVTpr4zwAqRmGEfwF0DcU8Nu', 200, 'ROLE_USER'), -- password u23456
    (UUID_TO_BIN(UUID()), 'user3', '$2a$10$wgOgY7fjhn9veCVQTTnVjewSD4btzo2.AwBJjiXmZ./TbdGok6jsy', 300, 'ROLE_USER'), -- password u34567
    (UUID_TO_BIN(UUID()), 'user4', '$2a$10$gZjPjPeKPneBOb4FDVDEWubESFLp.wGVaoPfR2XY.zMOYsfdSD2sG', 400, 'ROLE_USER'); -- password u45678
