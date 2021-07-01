# ProgramaAnamnese É um sistema que a parte de Engenharia de Software foi desenvolvida na faculdade com alguns colegas de sala, com os requisitos listados abaixo, porém com desenvolvimento feito sozinho por mim, pois o Cliente entrevistado é meu pai, e queria aplicar os conhecimentos que tive nesses 3 semestres de faculdade e alguns cursos online para ajudar o processo que ele tem hoje.


1. REQUISITOS DO SISTEMA
1.1. Descrição do Sistema
O sistema consiste em um serviço de exames da área da medicina do trabalho.
A clínica denominada Clínica 9 de Julho é a única que atende exames de medicina do trabalho no município de Iacanga, São Paulo. O atendimento é feito por ordem de chegada em horários fixos semanais, às segundas-feiras das 15h às 17h e terças-feiras das 10h às 12h.
Algumas empresas emitem uma solicitação de exame para o paciente, contendo os seguintes dados: identificação da empresa com CPNJ, endereço, nome comercial, nome do paciente, cargo, RG, autorização e tipo de exame.
Em alguns casos, os pacientes podem vir por espontânea vontade. Quando isso ocorre, o médico questiona ao paciente qual empresa o enviou, e anota no sistema os dados do paciente, caso empresa e paciente não estejam previamente cadastrados, serão admitidos no sistema.
O sistema tem por função disponibilizar ao médico as seguintes opções para a execução do exame:
•	Tipo de exame
Podendo ser admissional, demissional, periódico, mudança de função e retorno ao trabalho;

•	Dados do paciente
Data do exame, nome do paciente, data de nascimento, sexo, cor, estado civil, filiação, naturalidade, documentação (CPF e RG), endereço, cidade, cargo ou função, antecedentes pessoais;

•	Especificações do exame
Exame neurológico, de visão, audição, equilíbrio, cardiopulmonar, digestivo, hormonal, motor, infeccioso, antecedentes cirúrgicos, acidentes, e para mulheres adicional de obstétrico e ginecológico, antecedente familiar e um campo de observação a complementar.

•	Resultados do exame clínico
O médico faz o exame clínico e preenche mais dados, e pergunta ao paciente sobre hábitos e vícios, queixas atuais e anota na anamnese. Partindo para o exame físico, registrando pressão arterial, frequência cardíaca, visão, aparelho respiratório e circulatório, abdômen, ossioarticular, sistema nervoso e entre outros. Após o exame físico é anotado o resultado, capacitando o paciente a ser apto ou inapto, com sua assinatura em um campo final.
Ao protocolar todas as opções listadas acima, é respondido sim ou não. Em caso afirmativo, haverá um espaço para demais anotações.
O sistema deve oferecer ao médico um banco de dados com os pacientes e as empresas, que poderá acessar esses registros protegidos por login e senha do médico e impressão de um relatório, constando a assinatura do médico, que ao ser entregue ao paciente, será repassado à empresa.

1.2. Requisitos Funcionais
O cliente deverá ser capaz de:
RF1: Documentar todas as informações adquiridas durante o exame;
RF2: Cadastrar pacientes e empresas e vinculá-los ao banco de dados;
RF3: Imprimir relatório do exame.

1.3. Requisitos não funcionais
São eles, respectivamente:
Requisito de Produto

RFN1: O sistema deverá estar disponível à acesso pelo usuário durante os horários de atendimento pré-estabelecidos.

Requisito Organizacional

RFN2: O cliente/usuário deverá se autenticar com login e senha.

Requisito Externo

RFN3: Os dados dos pacientes devem ser protegidos e com sua devida privacidade, sendo acessados somente pelo cliente/usuário.



2. MODELAGEM DO SISTEMA (UML)
2.1. Descrição de Casos de Uso
Número do Caso de Uso
UC001
Nome do Caso de Uso	Manter Paciente
Ator(es)	Médico
Descrição	Este caso tem como objetivo cadastrar ou editar informações de um paciente já cadastrado.
Pré-Condições	São necessárias as documentações do paciente.
Pós-Condições	Não há.
Cenário Principal	1.	Médico abre o sistema
2.	Sistema Solicita Login
2.1. O sistema exibe a tela de login para o médico.
3.	Inserção das credenciais
3.1. O médico insere suas credenciais de login e senha no sistema.
4.	Sistema de validação do sistema
4.1. Após a inserção das credenciais o sistema faz a validação.
4.2. Caso haja inconsistência nos dados é apresentado uma mensagem de erro e é solicitado novamente as credenciais
4.3. Caso haja uma validação positiva o sistema é aberto para o médico.
5.	Médico solicita a opção de cadastro de pacientes.
5.1. Em caso de “Inclusão”, o sistema habilita a edição de dados.
5.2. Em caso de “Alteração”, o sistema exibe os dados cadastrados e os habilita para edição.
5.3. Em caso de “Consulta” ou “Exclusão”, o sistema exibe os dados cadastrados desabilitados para edição.
5.3.1	No caso de “Exclusão” o sistema solicita a confirmação. Se confirmado (EXC0001).

6.	O Médico informa, no caso de “alteração” ou “inclusão”.
6.1. Dados do paciente.
7.	O Médico confirma a operação realizada.
8.	O sistema realiza o cadastro das informações.
8.1. No caso de inclusão, o sistema gera automaticamente um código de identificação.

Cenário Alternativo	Não há.
Exceções	EXC0001 – Exclusão não permitida
Não é possível excluir um cliente que esteja associado a uma anamnese.
Inclusão(Includes)	Não há.
Extensões	Não há.
Regras do Negocio	

 
Número do Caso de Uso	UC002
Nome do Caso de Uso	Manter empresa
Ator(es)	Médico
Descrição	Este caso tem como objetivo cadastrar ou editar informações de um empresa já cadastrada.
Pré-Condições	São necessárias as documentações da empresa.
Pós-Condições	Não há.
Cenário Principal	1.	Médico abre o sistema
2.	Sistema Solicita Login
2.1. O sistema exibe a tela de login para o médico.
3.	Inserção das credenciais
3.1. O médico insere suas credenciais de login e senha no sistema.
4.	Sistema de validação do sistema
4.1. Após a inserção das credenciais o sistema faz a validação.
4.2. Caso haja inconsistência nos dados é apresentado uma mensagem de erro e é solicitado novamente as credenciais
4.3. Caso haja uma validação positiva o sistema é aberto para o médico.
5.	Médico solicita a opção de cadastro de empresas.
5.1. Em caso de “Inclusão”, o sistema habilita a edição de dados.
5.2. Em caso de “Alteração”, o sistema exibe os dados cadastrados e os habilita para edição.
5.3. Em caso de “Consulta” ou “Exclusão”, o sistema exibe os dados cadastrados desabilitados para edição.
5.3.2	No caso de “Exclusão” o sistema solicita a confirmação. Se confirmado (EXC0001).

6.	O Médico informa, no caso de “alteração” ou “inclusão”.
6.1. Dados do paciente.
7.	O Médico confirma a operação realizada.
8.	O sistema realiza o cadastro das informações.
8.1. No caso de inclusão, o sistema gera automaticamente um código de identificação.

Cenário Alternativo	Não há.
Exceções	EXC0001 – Exclusão não permitida
Não é possível excluir uma empresa que esteja associado a uma anamnese.
Inclusão(Includes)	Não há.
Extensões	Não há.
Regras do Negocio	

 
Número do Caso de Uso	UC003
Nome do Caso de Uso	Manter Anamnese
Ator(es)	Médico
Descrição	Este caso tem como objetivo realizar a anamnese e salvar seus dados.
Pré-Condições	É necessário ter os dados da empresa e dos pacientes previamente cadastrados no sistema.
Pós-Condições	Não há.
Cenário Principal	1.	Médico abre o sistema
2.	Sistema Solicita Login
2.1. O sistema exibe a tela de login para o médico.
3.	Inserção das credenciais
3.1. O médico insere suas credenciais de login e senha no sistema.
4.	Sistema de validação do sistema
4.1. Após a inserção das credenciais o sistema faz a validação.
4.2. Caso haja inconsistência nos dados é apresentado uma mensagem de erro e é solicitado novamente as credenciais
4.3. Caso haja uma validação positiva o sistema é aberto para o médico.
5.	Médico solicita a opção de realizar anamnese
5.1. Em caso de “inclusão”, o sistema habilita a edição de dados.
5.2. Em caso de “Alteração”, o sistema exibe os dados cadastrados e os habilita para edição.
5.3. Em caso de “Consulta”, o sistema exibe os dados cadastrados desabilitados para edição
6.	O Médico informa, no caso de “Inclusão” ou “Alteração”.
6.1. Dados do exame.
6.2. Código do paciente e da empresa.
7.	O médico confirma a operação realizada.
8.	O sistema realiza o cadastro das informações
Cenário Alternativo	Não há.
Exceções	Não há.
Inclusão(Includes)	UC003 – Consultar Pacientes Cadastrados
UC004 – Consultar Empresas Cadastradas
Extensões	Não há.
Regras do Negocio	



