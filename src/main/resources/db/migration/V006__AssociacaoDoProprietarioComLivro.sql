alter table livro add column proprietario_id bigint not null;

alter table livro add constraint fk_livro_proprietario
  foreign key(proprietario_id) references proprietario(id);
