alter table queue_entry add column current_exam_id uuid;

create table medical_exam (
    id uuid primary key,
    tenant_id uuid not null references tenant (id),
    queue_entry_id uuid not null references queue_entry (id),
    version_number int not null,
    patient_name varchar(80) not null,
    birth_date date not null,
    sex varchar(1) not null,
    cpf varchar(11) not null,
    rg varchar(20) not null,
    address varchar(180) not null,
    city varchar(60) not null,
    job_title varchar(40) not null,
    company_name varchar(80) not null,
    exam_type varchar(40) not null,
    exam_date date not null,
    answers text not null,
    fit varchar(10),
    author_id uuid not null references app_user (id),
    created_at timestamp not null,
    unique (queue_entry_id, version_number)
);
