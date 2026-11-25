# 001 — Consultórios, fila e versões

## Contexto

A clínica deixa de ser um único banco compartilhado. Há um master, vários consultórios e a possibilidade de fila em comum, sem prontuário em comum.

## Decisão

- Tenant é o consultório. Paciente, empresa, exame e documento pertencem a um tenant.
- CPF é obrigatório e único por tenant.
- Empresa é obrigatória no paciente, por seleção ou cadastro rápido na mesma tela.
- Um atendimento tem uma ficha clínica e um ASO, ligados a ele.
- A ficha e o ASO são versionados. O vínculo guarda o id da versão corrente.
- A fila pode ser de um grupo de consultórios. A autorização de leitura clínica continua no tenant do paciente.
- A assinatura impressa vem do cadastro do médico que atendeu.

## Consequência

Toda consulta de exame filtra pelo tenant do usuário. O master não entra nessa consulta. Médico de outro consultório vê a linha da fila compartilhada e não a ficha.
