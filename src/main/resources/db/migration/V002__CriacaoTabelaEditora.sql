create table editora (
    id bigint not null auto_increment,
    nome varchar(60) not null,
    cidade varchar(100) not null,
    estado varchar(02) not null,
    primary key (id)
);
