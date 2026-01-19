-- Extensão para UUID
create extension if not exists "uuid-ossp";

-- Pauta
create table pauta (
                       id uuid primary key default uuid_generate_v4(),
                       titulo varchar(200) not null,
                       descricao varchar(1000),
                       criada_em timestamptz not null default now()
);

-- Sessão de votação
create table sessao_votacao (
                                id uuid primary key default uuid_generate_v4(),
                                pauta_id uuid not null,
                                aberta_em timestamptz not null,
                                fecha_em timestamptz not null,
                                status varchar(20) not null,
                                constraint fk_sessao_pauta foreign key (pauta_id) references pauta(id),
                                constraint uk_sessao_pauta unique (pauta_id)
);

create index idx_sessao_pauta on sessao_votacao(pauta_id);

-- Voto
create table voto (
                      id uuid primary key default uuid_generate_v4(),
                      pauta_id uuid not null,
                      associado_cpf varchar(11) not null,
                      valor varchar(3) not null,
                      criado_em timestamptz not null default now(),
                      constraint fk_voto_pauta foreign key (pauta_id) references pauta(id),
                      constraint uk_voto_pauta_cpf unique (pauta_id, associado_cpf),
                      constraint ck_voto_valor check (valor in ('SIM', 'NAO'))
);

create index idx_voto_pauta on voto(pauta_id);
