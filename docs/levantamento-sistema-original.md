# Levantamento do sistema original — ProgramaAnamnese

Análise do código, dos formulários NetBeans (`.form`), dos dumps MariaDB e dos relatórios Jasper (`Exame.jrxml`, `ASO.jrxml`). Nenhuma implementação do sistema novo foi iniciada.

O sistema antigo é referência de campos, opções, regras e documentos. A arquitetura Swing/JDBC não deve ser preservada.

## 1. Arquitetura atual

Aplicação desktop Java (Swing, gerada pelo NetBeans Form Editor), pacotes `formularioInicial` e `BD`. Não há camadas, DTOs, serviços nem controle de perfil.

| Aspecto | Situação |
| --- | --- |
| Interface | `JFrame` / `Frame`, estado global em `Variaveis` (campos `static`) |
| Persistência | JDBC direto, `PreparedStatement`, banco `anamnese` em MariaDB/MySQL (`jdbc:mysql://localhost:3306/anamnese`, usuário `root`, senha vazia) |
| Modelo | Não há entidades. Cada tela monta o SQL |
| Segurança | Login compara senha em texto puro na tabela `medico`. O login não grava qual médico entrou. Cadastro de médico é público na tela de login |
| Impressão | JasperReports 6.x, arquivos `Exame.jasper` e `ASO.jasper` na raiz, visualização em `JasperViewer` |
| Fila / secretaria | Inexistentes. O README descreve ordem de chegada na clínica, mas o software não registra chegada nem status |
| Exclusão | Os casos de uso falam em exclusão bloqueada se houver anamnese. Não há `DELETE` no código |

Fluxo real: Login → `MenuOpcoes` → cadastrar/editar paciente, empresa, anamnese (tela `Exame`) ou ASO → imprimir pelo id numérico.

Modos da anamnese e do ASO usam o inteiro global `Variaveis.cadConExame` (`1` inclusão, `0` consulta/edição).

## 2. Inventário de formulários

| Tela | Classe | Função |
| --- | --- | --- |
| Login | `Login` | Autentica médico; abre cadastro de médico |
| Médico | `Medico` | Inclusão de CRM, nome, login e senha |
| Menu | `MenuOpcoes` | Paciente, empresa, anamnese e ASO (cadastrar, editar, pesquisar, imprimir) |
| Paciente | `Paciente` | Inclusão e atualização |
| Pesquisar paciente | `PesquisarPaciente` | Nome e/ou RG, `LIKE` |
| Empresa | `Empresa` | Inclusão e atualização |
| Pesquisar empresa | `PesquisarEmpresa` | Nome e/ou CNPJ |
| Anamnese / exame clínico | `Exame` | Um único formulário para história, exame físico e aptidão |
| Pesquisar anamnese | `PesquisarAnamnese` | Por nome do paciente |
| ASO | `aso` | Riscos, exames complementares, conclusão |
| Pesquisar ASO | `PesquisarAso` | Por nome do paciente |
| Sair | `ConfirmarSaida` / `Funcoes.sair` | Confirma encerramento |

Não existem telas separadas de “exame neurológico”, “visão”, etc. Esses itens são campos da mesma ficha `Exame`.

## 3. Campos

Tipos abaixo são os do formulário e da coluna. Quase tudo é `varchar`. Não há máscara de CPF. O README cita CPF; o sistema persiste apenas RG.

### 3.1 Paciente (`paciente`)

| Campo de tela | Coluna | Tipo persistido | Obrigatório no banco | Opções / regra |
| --- | --- | --- | --- | --- |
| idPaciente | `idPaciente` | int, auto | sim | gerado; digitável na pesquisa |
| Nome | `Nome` | varchar(80) | sim | texto livre |
| Data Nascimento | `dataNasc` | varchar(15) | sim | texto, exemplo `13/08/1995` |
| Sexo | `Sexo` | varchar(1) | sim | rádio `M` ou `F` |
| Cor | `Cor` | varchar(15) | não | texto livre |
| Estado Civil | `estadoCivil` | varchar(15) | sim | texto livre (não é lista) |
| RG | `RG` | varchar(11) | sim | texto; pesquisa usa RG, não CPF |
| Endereço | `Endereco` | varchar(150) | sim | texto |
| Cidade | `Cidade` | varchar(15) | sim | texto |
| Cargo | `Cargo` | varchar(20) | sim | texto |
| Pai | `pai` | varchar(100) | não | texto |
| Mãe | `mae` | varchar(100) | não | texto |
| Naturalidade | `naturalidade` | varchar(25) | sim | texto |
| idEmpresa | `id_empresa` | int FK | sim | paciente sempre aponta para uma empresa |

Não há coluna de CPF, telefone, e-mail ou CEP.

### 3.2 Empresa (`empresa`)

| Campo | Coluna | Tipo | Obrigatório |
| --- | --- | --- | --- |
| idEmpresa | `idEmpresa` | int auto | sim |
| CNPJ | `CNPJ` | varchar(20) | sim na definição, mas o dump tem linha vazia |
| Nome | `nome` | varchar(80) | sim |
| Endereco | `endereco` | varchar(80) | sim |

Não há razão social separada, nome fantasia, cidade, telefone nem responsável. A linha `idEmpresa = 2` está vazia e é o único indício de atendimento sem empresa.

### 3.3 Médico (`medico`)

| Campo | Coluna | Tipo |
| --- | --- | --- |
| idMedico | `idMedico` | int auto |
| CRM | `CRM` | varchar(20) |
| Nome | `Nome` | varchar(80) |
| Login | `login` | varchar(15) |
| Senha / confirmar | `senha` | varchar(15), texto puro |

Não há perfil secretaria. Não há vínculo do exame ou do ASO com o médico logado.

### 3.4 Anamnese e exame clínico (tabela única `exame`)

Identificação do exame:

| Campo | Coluna | Opções / observação |
| --- | --- | --- |
| idAnamnese | `idAnamnese` | int auto; visível só na consulta |
| idPaciente, nome, nascimento, RG, sexo, cor, estado civil, endereço, cidade, cargo, pai, mãe, naturalidade, CNPJ | lidos de `vabainicial` | somente leitura na ficha |
| Tipo Exame | `tipoExame` varchar(40) | `Admissional`, `Periódico`, `Demissional`, `De retorna ao Trabalho`, `de Mudança de cargo/função` |
| Data Exame | `data` varchar(10) | texto, não `date` |

Antecedentes pessoais. Rádio “Existe antecedentes pessoais?” não é gravado. “Não” desabilita os campos; “Sim” habilita. Default é “Não”.

| Rótulo na tela | Componente | Coluna gravada |
| --- | --- | --- |
| Nefrit. | `apNefrit` | `apNefrit` |
| Tbc. | `apTbc` | `apTbc` |
| Maleita | `apMaleita` | `apMaleita` |
| Parasitoses | `apParasita` | `apParasita` |
| Menarca | `apMenarca` | `apMenarca` |
| Partos e Abortos | `apPartos` | `apPartos` |
| Operação | `apOpera` | `apOpera` |
| Acidentes | `apAcidente` | `apAcidente` |
| Hepatite | `apHep` | `apHep` |
| Reum Poliart. | `apReum` | `apReum` |
| Pleuriz. | `apVenereas` | `apVenereas` |
| Venéreas | `apCiclo` | `apCiclo` |
| Outras | `apPleuriz` | `apPleuriz` |
| Ciclo | `apOutras` | `apOutras` |

Os quatro últimos rótulos estão trocados em relação ao nome do campo e à coluna. A impressão usa o nome da coluna (`Pleuriz` imprime `apPleuriz`, que na tela está sob o rótulo “Outras”). Isso precisa de validação antes de qualquer correção: preservar o que o médico vê, o que o papel imprime, ou os dois com migração explícita.

Antecedentes familiares:

| Rótulo | Componente | Coluna | Condicional |
| --- | --- | --- | --- |
| Antecedente Familiar | `af` | `ap` | “Não” grava `Nada a Declarar` e desabilita |
| HPMA | `afHpma` | `apHpma` | sempre editável; não entra no rádio familiar |

Hábitos. Rádio não é gravado. “Não” preenche e bloqueia: Fumo `Não`, Álcool `Não`, Tóxicos `Não`, Alimentação `Normal`.

| Rótulo | Componente | Coluna | Entra no rádio? |
| --- | --- | --- | --- |
| Fumo | `fumo` | `fumo` | sim |
| Álcool | `alcool` | `alcool` | sim |
| Tóxicos | `tox` | `toxicos` | sim |
| Alimentação | `alimentacao` | `alimentacao` | sim |
| Horas de sono | `hrsSono` | `hrSono` | não |

Exame físico (todos texto livre, sem unidade obrigatória):

| Grupo | Rótulos | Colunas |
| --- | --- | --- |
| Medidas | Peso, Altura, PA, Pulso | `peso`, `altura`, `PA`, `pulso` |
| Geral | Mucosas, Garganta, Tegumentos, Gânglios, Dentes, Olhos | `mucosas`, `garganta`, `tegumentos`, `ganglios`, `dentes`, `olhos` |
| Visão sem óculos | Perto/Longe × Esquerdo/Direito | `semPerEsq`, `semPerDir`, `semLonEsq`, `semLonDir` |
| Visão com óculos | idem | `comPerEsq`, `comPerDir`, `comLonEsq`, `comLonDir` |
| Visão / audição | Cores, Ouvidos | `cores`, `ouvidos` |
| Respiratório | Aparelho Respiratório | `apResp` |
| Circulatório | Coração, Varizes, Circulatório Colateral | `coracao`, `varizes`, `circCol` |
| Abdômen | Abdômen, Cicatrizes, Anéis Heminários, Outros achados | `abdomen`, `cicatrizes`, `aneis`, `outrosAchados` |
| Demais | Gênito urinário, Ósteo articular, Sistema nervoso, Romberg, Psiquismo, Endócrino | `apGen`, `apOst`, `sisNerv`, `roemberg`, `psiquismo`, `sistEndo` |
| Vacinas | Antivariólica, Antitetânica, Outras | `antivari`, `antite`, `outrasVacinas` |
| Resultado | APTO Sim/Não | `apto`: `APTO` ou `INAPTO` |

Não há campo de queixa atual, observação livre da anamnese, nem assinatura digital. “Olhos” é texto além da grade de acuidade. A coluna `olhos` existe no banco e na query do Jasper, mas some da view `pesquisaanamnese`.

A ficha de exame não tem `UPDATE`. “Editar” carrega o registro e troca o botão para imprimir. Alterar anamnese já salva não está implementado.

### 3.5 ASO (`aso`)

Cabeçalho: `idAso`, `idPaciente`, nome, nascimento, RG, cargo (somente leitura), `id_Empresa`, nome da empresa, data do exame (`data` date), tipo de exame.

Tipos no combo do ASO, diferentes dos da anamnese:

`Admissional`, `Periódico`, `Demissional`, `Retorna ao Trabalho`, `Mudança de cargo/função`, `Acidente do Trabalho`, `Doença Ocupacional`, `Doença/Acidente não Ocupacional`, `Parto`.

Riscos, inteiros 0/1, sem texto complementar apesar do rótulo “Outros:”:

- Físicos: Calor, Frio, Ruído, Umidade, Radiação não-ionizante, Outros.
- Químicos: Gases, Poeiras, Tintas, Solventes, Produtos de limpeza uso doméstico, Outros.
- Biológicos: Microorganismos em geral, Outros.
- Ergonômicos: Movimentação de pesos, Postura inadequada, Esforço físico, Trabalho predominantemente sentado, Trabalho predominantemente em pé, Movimentos repetitivos, Outros.
- Ausência de riscos ocupacionais.

Exames complementares: checkbox de realização mais data. Datas aceitas em `dd/mm/aaaa`, `dd-mm-aaaa` ou `ddmmaaaa`, gravadas como `date`.

| Exame | Data | Texto livre |
| --- | --- | --- |
| Avaliação clínica | `exCli` | — |
| Audiometria | `exAud` | — |
| Espirometria | `exEspi` | — |
| ECG | `exECG` | — |
| Hemograma | `exHemo` | — |
| Chagas | `exChagas` | — |
| Glicemia | `exGlice` | — |
| RX do tórax | `exRX` | — |
| Acuidade visual | `exAcui` | — |
| Coproparasitológico | `exCopropar` | — |
| Coprocultura | `exCoprocult` | — |
| VDRL | `exVDRL` | — |
| Outras 1, 2 e 3 | `exOutras1..3` | `exOutras1Texto..3` varchar(20) |

Conclusão: `Apto`, `Apto com Restrições`, `Inapto`. Restrições: varchar(50). O ASO tem inclusão e atualização.

O checkbox do exame complementar não é coluna. Só a data indica que o exame foi feito. Data inválida pode ser gravada como nula (há registro `0010-08-20` no dump).

## 4. Classes e objetos

Não há modelo de domínio. Mapeamento proposto, sem copiar a estrutura antiga:

| Antigo | Papel | Novo (proposta, ainda não aprovada) |
| --- | --- | --- |
| `Login`, `Medico`, tabela `medico` | credencial | `User` + perfil `MEDICO` ou `SECRETARIA` |
| `Paciente` | cadastro | `Patient` |
| `Empresa` | cadastro | `Company`, associação opcional no atendimento |
| `Exame` + tabela `exame` | anamnese e clínico juntos | `MedicalExam` com `Anamnesis` e `ClinicalExamination`, retrato cadastral |
| `aso` + tabela `aso` | documento e riscos | `OccupationalCertificate` (ASO), separado da ficha clínica |
| `MenuOpcoes` impressão | relatório Jasper | geração de PDF no backend |
| `Pesquisar*` | busca | consultas da API |
| `Variaveis` | sessão global | sessão autenticada, sem estado estático |
| — | não existe | `QueueEntry` / `Appointment` |

## 5. Persistência

Tabelas: `medico`, `empresa`, `paciente`, `exame`, `aso`. Views: `vabainicial` (paciente + CNPJ) e `pesquisaanamnese` (view + exame, sem `olhos`).

Relacionamentos reais:

- `paciente.id_empresa` → `empresa.idEmpresa` (obrigatório, FK duplicada no dump).
- `exame.id_Paciente` → `paciente.idPaciente`. O exame não guarda empresa nem médico.
- `aso.id_Paciente` e `aso.id_Empresa` têm FK próprias. A query de impressão ainda exige que a empresa do ASO seja a mesma empresa atual do paciente.

Não há transação de negócio, histórico de alteração, nem bloqueio de registro finalizado. Datas clínicas da anamnese são texto. Charset do dump é `latin1`.

Dados de exemplo no dump incluem credenciais em texto puro. Não devem ser reutilizados. O novo sistema não deve importar senhas.

## 6. Impressão

Dois documentos A4 (595×842), margens 20, Jasper preenchido com a conexão JDBC e um parâmetro de id.

### Guia para exame médico geral (`Exame.jrxml`)

Título fixo “Guia p/ Exame Médico Geral”. Junta `exame`, `paciente` e `empresa` por `idAnamnese`. Imprime identificação, antecedentes, hábitos, exame físico, visão em duas colunas (com e sem óculos), resultado (`exame_apto`) e data.

Assinatura impressa, estática, não é campo nem usuário logado:

- Dr. Roberto Ribeiro de Carvalho
- Médico do Trabalho - CRM 63783 - Rua Nove de Julho, 994 - Iacanga/SP - Fone (14) 991711162

O rodapé da segunda página repete a data do exame. Não há imagem de assinatura.

### Atestado de Saúde Ocupacional (`ASO.jrxml`)

Cabeçalho fixo com o mesmo médico, CRM, “Médico do Trabalho” e endereço “Rua Nove de Julho, 994 - Centro - Iacanga - SP”. Marca com “X” o tipo de exame e cada risco quando o inteiro é 1. Lista exames complementares com data. Conclusão, restrições e linha “Recebi a 2ª via - Assinatura do Funcionário”.

Condição de “X” que não bate com o combo:

- Combo grava `Retorna ao Trabalho`. O relatório só marca `Retorno ao Trabalho`. O X de retorno nunca aparece para registros novos da tela.
- Combo da anamnese grava `De retorna ao Trabalho` e `de Mudança de cargo/função`, também diferentes das strings do ASO.

A query do ASO faz join da empresa do ASO com `paciente.id_empresa`. Se o paciente mudar de empresa, o ASO antigo deixa de imprimir.

## 7. Documentos gerados

Só esses dois. Não há receita, encaminhamento, declaração avulsa nem segunda via armazenada. O PDF/visualização é gerado na hora; nada é arquivado em arquivo.

## 8. Regras a preservar até validação em contrário

- Campos e rótulos médicos listados acima, inclusive menarca, ciclo, partos e vacinas.
- Tipos de exame de cada tela, até decidir a lista única.
- Textos livres, sem transformar em sim/não o que hoje é descrição.
- Resultado da ficha: APTO/INAPTO. Resultado do ASO: Apto / Apto com Restrições / Inapto.
- Bloqueio visual dos antecedentes e hábitos, com os textos default de “Não”.
- Paciente pode chegar sem empresa: hoje isso depende de uma empresa vazia. O requisito novo pede empresa opcional de verdade.
- Não excluir paciente ou empresa ligados a exame.
- Documentos devem continuar reconhecíveis frente aos Jasper atuais.

## 9. Lacunas do original (não inventar conteúdo médico para cobri-las)

- CPF citado no README e ausente na ficha.
- Fila, chamada e status não existem no código.
- Secretaria não existe.
- Exame não grava médico, empresa nem snapshot. ASO depende do cadastro atual na impressão.
- Anamnese não é atualizável depois de salva; ASO é.
- Assinatura não pertence ao usuário logado.
- Não há auditoria, hash de senha, backup nem LGPD operacional.

Decisões que bloqueiam a modelagem fina estão em `docs/decisoes-pendentes.md`.
