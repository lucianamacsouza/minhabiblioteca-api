create table livro (
    id bigint not null auto_increment,
    titulo varchar(150) not null,
    subtitulo varchar(150),
    codigo_barras varchar(20) not null,
    ano_edicao int not null,
    numero_edicao int not null,
    isbn10 varchar(20),
    data_inclusao datetime not null,    
    resumo text not null,
    genero_id bigint not null,
    autor_id bigint not null,
    editora_id bigint not null,
    primary key (id)
);

alter table livro add constraint fk_livro_genero
  foreign key(genero_id) references livro(id);
  
alter table livro add constraint fk_livro_autor
  foreign key(autor_id) references autor(id);
  
alter table livro add constraint fk_livro_editora
  foreign key(editora_id) references editora(id);  
  