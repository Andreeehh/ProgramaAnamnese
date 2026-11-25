alter table company add column phone varchar(11);
alter table company add column postal_code varchar(8);
alter table company alter column address type varchar(150);

alter table patient add column phone varchar(11);
alter table patient add column postal_code varchar(8);
alter table patient alter column city type varchar(60);

create table queue_entry (
    id uuid primary key,
    tenant_id uuid not null references tenant (id),
    patient_id uuid not null references patient (id),
    company_id uuid not null references company (id),
    exam_type varchar(40) not null,
    status varchar(20) not null,
    arrived_at timestamp not null,
    called_at timestamp,
    started_at timestamp,
    finished_at timestamp
);

create index queue_tenant_status_idx on queue_entry (tenant_id, status, arrived_at);
