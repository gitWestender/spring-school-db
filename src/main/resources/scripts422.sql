create table people
(
    id        integer primary key,
    name      varchar(255) unique not null,
    age       integer check ( age > 18 ),
    can_drive boolean default (false),
    car_id integer references cars (id)
);

create table cars
(
    id     integer primary key,
    vendor varchar(255) not null,
    model  varchar(255) not null,
    price  integer check ( price > 0 )
);

