package br.com.programa.anamnese.exam;

import java.util.Set;

public final class ExamFields {
    public static final Set<String> KEYS = Set.of(
            "nefrit", "tbc", "maleita", "parasitoses", "menarca", "partosAbortos", "operacao", "acidentes",
            "hepatite", "reum", "pleuriz", "venereas", "outras", "ciclo", "antecedenteFamiliar", "hpma",
            "fumo", "alcool", "toxicos", "alimentacao", "horasSono",
            "peso", "altura", "pa", "pulso", "mucosas", "garganta", "tegumentos", "ganglios", "dentes", "olhos",
            "semPertoEsq", "semPertoDir", "semLongeEsq", "semLongeDir", "comPertoEsq", "comPertoDir", "comLongeEsq", "comLongeDir",
            "cores", "ouvidos", "respiratorio", "coracao", "varizes", "circulatorioColateral",
            "abdomen", "cicatrizes", "aneis", "outrosAchados", "genito", "osteo", "nervoso", "romberg", "psiquismo", "endocrino",
            "antivariolica", "antitetanica", "outrasVacinas");

    private ExamFields() {}
}
