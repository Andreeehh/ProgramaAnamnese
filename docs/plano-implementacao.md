# Plano de implementação

A implementação só começa depois da aprovação deste levantamento e das decisões pendentes que afetam campos.

## Etapa 1 — Levantamento

Concluída nos documentos desta pasta. Sem código de aplicação.

## Etapa 2 — Arquitetura

Decisões fechadas. Antes de codar, falta só `docs/api.md` com os contratos já delimitados: master, tenant, CPF, empresa obrigatória, versões e fila compartilhada sem prontuário cruzado.

## Etapa 3 — Infraestrutura e acesso

Spring Boot, React/Vite, Compose, PostgreSQL, Flyway, healthcheck, usuários iniciais, login e autorização dos dois perfis. Testes de autenticação e de rota negada.

## Etapa 4 — Cadastros

Paciente e empresa com os campos do inventário. Pesquisa por nome e RG; CNPJ e nome para empresa. Sem exclusão física se houver atendimento. Testes de duplicidade na regra que for aprovada.

## Etapa 5 — Recepção e fila

Chegada, tipo de exame, empresa opcional, estados e transições, painel do médico, SSE, teste de concorrência de chamada.

## Etapa 6 — Ficha médica

Anamnese e exame clínico fieis ao inventário, inclusive condicionais de antecedentes e hábitos. Retrato cadastral. Histórico somente leitura. Testes de validação e de imutabilidade após finalizar.

## Etapa 7 — ASO e PDF

Geração independente da persistência da ficha. Comparar campo a campo com `Exame.jrxml` e `ASO.jrxml` (textos, X dos riscos, quebra, assinatura). Não alterar layout sem autorização.

## Etapa 8 — Fechamento

Fluxo completo da secretaria ao histórico, backup restaurável em ambiente local, README de instalação. Sem funcionalidade fora do escopo.
