create table owner (
uuid varchar(255) not null,
username varchar(255) not null,
password varchar(255) not null,
role varchar(255) not null,
owned_animals varchar(255),
UNIQUE (username),
primary key(uuid)
);

create table animals (
id varchar(255) not null,
uuid varchar(255) not null,
type varchar(255) not null,
breed varchar(255) not null,
age integer not null,
treatment varchar(255),
primary key(id)
);

alter table animals add constraint animals_uuid_unq foreign key (uuid) references owner;
alter table owner add constraint animals_id foreign key (owned_animals) references animals;

create table vet (
uuid varchar(255) not null,
username varchar(255) not null,
password varchar(255) not null,
role varchar(255) not null,
specialization varchar(255),
primary key(uuid)
);

create table appointment(
uuid varchar(255) not null,
animal_id varchar(255) not null,
vet_id varchar(255) not null,
date varchar(255) not null,
status varchar(255) not null,
type varchar(255) not null,
primary key(uuid)
);

alter table appointment add constraint appointment_animals_uuid_unq foreign key (animal_id) references animals;
alter table appointment add constraint appointment_vet_uuid_unq foreign key (vet_id) references vet;

--insert into owner values ('test','test','test','test');
