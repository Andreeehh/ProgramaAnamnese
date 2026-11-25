# Expansão: vários consultórios e exames personalizáveis

Análise de viabilidade. Não é implementação.

## Vários clientes médicos

O modelo de consultório já separa cadastro e prontuário. Incluir mais médicos não exige outro sistema: o master cria o consultório, o médico, a secretaria e, se for o caso, o grupo de fila.

O que escala sem misturar dado clínico:

- cada consultório tem pacientes, empresas, templates e exames próprios;
- o mesmo CPF pode existir em outro consultório, como outro cadastro;
- a fila compartilhada continua só operacional;
- nenhum exame atravessa o consultório.

Não cabe, nesta base, um prontuário único entre clínicas. Isso quebraria a decisão já aprovada e ampliaria acesso a dado de saúde sem necessidade do atendimento.

## Personalizar o exame

É viável. O exame padrão de hoje vira um template de sistema, não uma tela fixa no código.

Nome do template padrão: exame ocupacional padrão. É o formulário atual de anamnese, exame clínico e ASO, com os rótulos da tela e os nove tipos de exame. Todo consultório nasce com ele. Não se apaga.

Um consultório pode ter outros templates, copiados desse ou montados a partir dele. Na recepção, a secretaria escolhe o template daquela vinda. Se não escolher, vale o padrão do consultório, que começa sendo o ocupacional padrão.

O que cada template pode alterar:

- campo obrigatório ou opcional;
- ocultar um campo do padrão, sem apagar o que já foi gravado;
- incluir campo novo: texto, número, data, sim/não ou lista;
- rótulo adaptado à operação daquele consultório;
- ordem e agrupamento na tela.

O que não muda depois de emitido:

- a versão do template usada naquela gravação fica copiada no exame;
- editar o template depois não reescreve exame nem PDF antigo;
- remover um campo da tela não apaga resposta histórica.

## Como guardar

Campos do template padrão continuam identificáveis (`cpf`, `pleuriz`, `apto`, riscos do ASO). Isso permite imprimir o guia e o ASO no layout original quando o template é o padrão.

Campo criado pela clínica não vira coluna nova no banco. Fica na versão do template e a resposta fica na versão do exame. Assim dá para acrescentar e tirar campo sem migração por clínica.

Obrigatório é regra do template, validada no backend, não só na tela.

## Impressão

Dois resultados, de propósito:

- template padrão: PDF no layout do documento original, com a assinatura do médico do atendimento;
- template alterado ou próprio: PDF gerado pela estrutura do template (seções, rótulos e respostas), não encaixado à força nas caixas do Jasper antigo.

Campo novo não entra no ASO clássico se não houver lugar previsto. Se a clínica quiser esse campo no papel, ele sai no documento do template próprio.

No ASO, mesmo num template próprio, permanecem obrigatórios: paciente, empresa, tipo de exame, conclusão e médico assinante. São o núcleo do documento, não preferência de tela.

## Limite

Personalizar não é um construtor genérico de qualquer prontuário, nem editor visual de página. É um conjunto de templates por consultório, com o ocupacional padrão como base e como documento fiel.

Quem edita template na primeira versão: o médico do consultório. O master não altera formulário clínico de outro consultório. A secretaria usa o template; não o redefine.
