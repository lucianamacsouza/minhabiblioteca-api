alter table livro drop foreign key fk_livro_genero;  
alter table livro drop index fk_livro_genero; 

alter table livro add constraint fk_livro_genero
  foreign key(genero_id) references genero(id);
  