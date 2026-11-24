# Arquitetura

Alinhada às decisões aprovadas. Ainda sem código de aplicação.

## Forma

Monólito modular. Um processo Spring Boot, um frontend React, um PostgreSQL. Sem microsserviços, sem barramento e sem portal externo.

```
frontend (React + Vite + TypeScript)
    REST + SSE
backend (Spring Boot, Java 21, Maven)
    authentication, users, tenants, patients, companies,
    queue, form-templates, medical-exams, documents, audit
PostgreSQL + Flyway
```

Cada módulo de domínio tem controller, serviço e repositório. Controller não concentra regra. Entidade JPA não é o contrato da API.

## Backend

- Java 21, Spring Boot, Spring Web, Spring Security, Spring Data JPA, Bean Validation, springdoc-openapi.
- Senha com hash (BCrypt ou Argon2). Sessão por cookie httpOnly ou JWT de curta duração guardado de forma não exposta a script de terceiros. A escolha fina fica para a etapa 3, desde que não haja senha em claro nem cadastro público.
- Perfis: `MASTER`, `SECRETARIA` e `MEDICO`. Autorização no backend, inclusive o filtro de consultório.
- Só o master, vindo de variável de ambiente, cria usuários e vínculos. Não há autocadastro. O master não lê exame.
- Flyway como única evolução de esquema.
- PDF no servidor (OpenPDF ou Jasper, a escolher na etapa 7). O navegador só exibe e manda imprimir. O gerador não depende de componente React.
- Fila: transições no serviço, com trava para dois médicos não iniciarem o mesmo atendimento. Consultórios do mesmo grupo enxergam a fila; a ficha continua no consultório do paciente. Atualização por SSE.
- Auditoria das operações de exame, conclusão, impressão e retificação. Log de aplicação não recebe conteúdo clínico.

## Frontend

- React, TypeScript, Vite, React Router.
- Formulários com validação espelhada na API. A validação que importa é a do backend.
- Layout: menu lateral, cabeçalho com usuário, área principal.
- Secretaria: pesquisa, cadastro, recepção, fila.
- Médico: paciente atual, próximo, espera, ficha, histórico, documentos.
- Componentes de campo reutilizáveis. Os formulários médicos seguem o inventário, não um desenho novo de perguntas.

## O que não entra

Acesso de paciente ou empresa, autocadastro público, app mobile, fila pública de TV, integrações externas, e-mail e armazenamento em nuvem de terceiros. O master não é um portal externo: é o único usuário interno de provisão.

## Execução

Docker Compose sobe frontend, backend e PostgreSQL com volume. Segredos só em ambiente. `.env.example` sem valores reais. Backup do volume e dos PDFs previstos na etapa de infraestrutura, não como serviço externo.
