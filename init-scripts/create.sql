create table assets(
                       id int primary key generated by default as identity,
                       name varchar(30) not null unique
);

create table rates(
                      id int primary key generated by default as identity,
                      value double precision not null,
                      asset_id int unique null references assets(id) on delete cascade
);
