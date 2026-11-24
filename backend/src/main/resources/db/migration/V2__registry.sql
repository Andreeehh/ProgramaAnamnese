create table company (
    id uuid primary key,
    tenant_id uuid not null references tenant (id),
    cnpj varchar(20) not null,
    name varchar(80) not null,
    address varchar(80) not null,
    created_at timestamp not null
);

create index company_tenant_cnpj_idx on company (tenant_id, cnpj);

create table patient (
    id uuid primary key,
    tenant_id uuid not null references tenant (id),
    company_id uuid not null references company (id),
    full_name varchar(80) not null,
    birth_date date not null,
    sex varchar(1) not null check (sex in ('M', 'F')),
    skin_color varchar(15),
    marital_status varchar(15) not null,
    cpf varchar(11) not null,
    rg varchar(11) not null,
    address varchar(150) not null,
    city varchar(40) not null,
    job_title varchar(40) not null,
    father_name varchar(100),
    mother_name varchar(100),
    birthplace varchar(40) not null,
    created_at timestamp not null
);

create unique index patient_tenant_cpf_uk on patient (tenant_id, cpf);
create index patient_tenant_name_idx on patient (tenant_id, full_name);
