# Modelo de domínio e PostgreSQL

Decisões fechadas em `decisoes-pendentes.md`. Nomes em inglês no código; rótulos de tela em português, iguais aos da tela original.

## Separação

- `Tenant` é o consultório. Quase todo registro clínico e cadastral aponta para um.
- `QueueGroup` liga consultórios que compartilham a mesma fila. Sem grupo, a fila é só do consultório.
- `User` tem papel `MASTER`, `SECRETARIA` ou `MEDICO`. Master não tem prontuário. Médico tem CRM, nome de assinatura e texto do carimbo. Secretaria e médico atuam em um ou mais consultórios, pelo vínculo que o master grava.
- `Patient` pertence a um consultório. CPF obrigatório, único nesse consultório. O mesmo CPF pode existir em outro consultório, como outra linha. RG continua no cadastro. Empresa é obrigatória.
- `Company` pertence ao consultório. Não se apaga se houver paciente ou exame. O cadastro rápido do paciente cria a empresa nesse mesmo consultório.
- `QueueEntry` é a chegada: paciente, tipo de exame da lista única, horário, status, consultório dono do paciente. Se o consultório está num grupo, a linha aparece para os outros do grupo, sem a ficha.
- `Appointment` é o ato sobre essa entrada: médico, início, fim. O médico que conclui é o autor da assinatura.
- `MedicalExam` é a ficha daquela vinda, no consultório do paciente. Guarda retrato cadastral (nome, nascimento, sexo, CPF, RG, endereço, empresa, cargo). Cada gravação é uma versão; o atendimento aponta para a versão atual.
- `Anamnesis` e `ClinicalExamination` são partes dessa versão. Pleuriz, Venéreas, Outras e Ciclo seguem o rótulo da tela.
- `OccupationalCertificate` é o ASO da mesma versão de atendimento: riscos com texto em “Outros”, exames complementares, conclusão e restrições. Não substitui o APTO/INAPTO da ficha.
- `MedicalDocument` guarda o PDF da versão emitida, o tipo e o médico assinante.
- `AuditLog` registra ator, consultório, instante, operação e identificador. Não grava o texto clínico.

Status da fila: `AGUARDANDO` → `CHAMADO` → `EM_ATENDIMENTO` → `FINALIZADO`, com `CANCELADO` a partir de aguardando ou chamado. Não se finaliza sem ficha. Não se inicia duas vezes.

## PostgreSQL (esboço)

Tipos de verdade, não varchar universal:

- datas como `date`; horários de fila como `timestamptz`;
- sexo, tipo de exame, status, papel e conclusão como valores fechados;
- tipos de exame: os nove da lista do ASO, com a grafia aprovada;
- riscos do ASO como `boolean`, mais texto opcional em cada “Outros”;
- acuidade e sinais vitais como texto, porque o original não tem formato numérico estável (`81kg`, `1,81`, `1.81`);
- `unique (tenant_id, cpf)` em paciente; CNPJ único dentro do consultório quando preenchido;
- exame, ASO e documento com FK para o atendimento e para o tenant do paciente;
- versão imutável após gravar; o vínculo do atendimento só troca o ponteiro da versão corrente;
- proibir `DELETE` de paciente ou empresa referenciados;
- nenhuma consulta clínica sem `tenant_id` do usuário, exceto a projeção operacional da fila compartilhada.

Não copiar as views `vabainicial` e `pesquisaanamnese`. A consulta sai do modelo novo.

Não há script Flyway nesta etapa.
