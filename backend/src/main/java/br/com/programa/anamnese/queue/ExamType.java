package br.com.programa.anamnese.queue;

public enum ExamType {
    ADMISSIONAL("Admissional"),
    PERIODICO("Periódico"),
    DEMISSIONAL("Demissional"),
    RETORNO("Retorna ao Trabalho"),
    MUDANCA("Mudança de cargo/função"),
    ACIDENTE("Acidente do Trabalho"),
    DOENCA_OCUPACIONAL("Doença Ocupacional"),
    DOENCA_NAO_OCUPACIONAL("Doença/Acidente não Ocupacional"),
    PARTO("Parto");

    private final String label;

    ExamType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
