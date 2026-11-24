# Decisões aprovadas

Registradas em 24/11/2026. A implementação ainda não começou.

1. **Rótulos da anamnese.** Vale o rótulo da tela, não a coluna trocada do sistema antigo. Pleuriz, Venéreas, Outras e Ciclo são gravados e impressos com esse significado. O banco antigo não será migrado, então o bug de mapeamento não é preservado.

2. **Tipos de exame.** Uma lista só, a do ASO, que é a mais ampla: Admissional, Periódico, Demissional, Retorna ao Trabalho, Mudança de cargo/função, Acidente do Trabalho, Doença Ocupacional, Doença/Acidente não Ocupacional, Parto. A impressão usa essas mesmas strings, inclusive o X de retorno. A grafia `De retorna ao Trabalho` da anamnese antiga não entra.

3. **CPF.** Obrigatório no paciente. A pesquisa usa nome, CPF ou RG. A unicidade do CPF é por consultório, não global.

4. **Assinatura.** Sai do texto fixo do Jasper. Nome, CRM e os dados de assinatura ficam no cadastro do médico e são os que o PDF imprime. O médico do atendimento é quem assina.

5. **Empresa.** O paciente precisa de empresa. No cadastro, o campo busca uma empresa já existente ou abre um formulário rápido de empresa sem sair da tela. Não há paciente sem vínculo.

6. **Anamnese e ASO.** Os dois nascem do mesmo atendimento e permanecem ligados a ele.

7. **Edição.** Cada gravação gera uma versão. O atendimento aponta para a versão atual. As anteriores ficam guardadas e não são apagadas.

8. **Outros.** Onde o formulário diz “Outros” e a descrição for necessária, entra campo de texto novo. Isso vale para os riscos do ASO.

9. **Banco antigo.** Ignorado. Não há sistema em produção para migrar.

10. **Acesso e consultórios.** Existe um usuário master, criado pela configuração inicial, sem autocadastro público. O master cadastra médicos e secretárias e faz os vínculos de consultório (tenant).

    - Cada médico tem consultório.
    - Consultórios podem compartilhar a fila de atendimento.
    - O cadastro de paciente é único dentro do consultório. O mesmo CPF e o mesmo RG podem existir em outro consultório, como outro cadastro.
    - Exame, anamnese, ASO e PDF de um consultório não são lidos por outro.
    - A fila compartilhada mostra só o necessário para chamar: nome, tipo de exame, horários e status. Não abre a ficha de outro consultório.

11. **Templates.** O exame ocupacional atual é o template padrão, inapagável. Cada consultório pode ter outros templates, marcar obrigatório ou opcional, ocultar campo, acrescentar campo e adaptar rótulo. A recepção escolhe o template da vinda. Exame já gravado guarda a versão do template e não acompanha edição posterior. O PDF clássico vale para o template padrão; template próprio imprime pela própria estrutura. Detalhe em `docs/expansao-templates.md`.

## LGPD

Não compartilhar o prontuário entre consultórios não contraria a LGPD. Dado de saúde é dado sensível, e o acesso fica limitado a quem atende naquele consultório. Por isso o isolamento pedido permanece. Não se cria prontuário único entre consultórios.

O master administra usuários e vínculos. Não recebe, por esse papel, leitura do conteúdo clínico.

A fila compartilhada expõe dado pessoal operacional apenas aos consultórios que o master vinculou, e não inclui o conteúdo do exame.
