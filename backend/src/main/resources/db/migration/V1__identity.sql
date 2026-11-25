create table tenant (
    id uuid primary key,
    name varchar(120) not null,
    created_at timestamp not null
);

create table app_user (
    id uuid primary key,
    username varchar(80) not null unique,
    password_hash varchar(100) not null,
    full_name varchar(120) not null,
    role varchar(20) not null check (role in ('MASTER', 'SECRETARIA', 'MEDICO')),
    crm varchar(30),
    signature_name varchar(120),
    signature_details varchar(300),
    active boolean not null,
    created_at timestamp not null
);

create table user_tenant (
    user_id uuid not null references app_user (id),
    tenant_id uuid not null references tenant (id),
    primary key (user_id, tenant_id)
);

create index user_tenant_tenant_idx on user_tenant (tenant_id);
