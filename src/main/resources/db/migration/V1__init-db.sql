-- When new schema has been created, this script can run with V2 and V3 but not V4
-- Before running V4 the seller_ids must be updated

    create table if not exists bids (
        bid_amount integer not null,
        id bigint not null,
        bidder_id binary(16),
        item_id binary(16),
        primary key (id)
    ) engine=InnoDB;

    create table if not exists bids_seq (
        next_val bigint
    ) engine=InnoDB;

    insert into bids_seq values ( 1 );

    create table if not exists items (
        last_bid integer not null,
        purchase_price integer not null,
        sellable bit not null,
        starting_price integer not null,
        id binary(16) not null,
        seller_id binary(16),
        description TEXT,
        name varchar(255),
        photourl varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table if not exists purchases (
        purchase_amount integer not null,
        id bigint not null,
        buyer_id binary(16),
        item_id binary(16),
        primary key (id)
    ) engine=InnoDB;

    create table if not exists purchases_seq (
        next_val bigint
    ) engine=InnoDB;

    insert into purchases_seq values ( 1 );

    create table if not exists users (
        balance float(53) not null,
        id binary(16) not null,
        password varchar(255),
        roles varchar(255),
        username varchar(255),
        primary key (id)
    ) engine=InnoDB;

    alter table users
       add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);

    alter table bids
       add constraint FKmtrc6tnwawlpk1u2km6qnxbha 
       foreign key (bidder_id) 
       references users (id);

    alter table bids
       add constraint FKg1mdb2uha9v6t2ujkvlmj3tuq 
       foreign key (item_id) 
       references items (id);

    alter table items 
       add constraint FKsm9ro5ntn6yaav2m7ydato0fc 
       foreign key (seller_id) 
       references users (id);

    alter table purchases 
       add constraint FKpbgddohncjt0ijwi5pgdjng5u 
       foreign key (buyer_id) 
       references users (id);

    alter table purchases 
       add constraint FKh403vdm50hi3rwf8ukj5xubsc 
       foreign key (item_id) 
       references items (id);
