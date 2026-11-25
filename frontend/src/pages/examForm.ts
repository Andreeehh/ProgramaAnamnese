export type FillMode = 'text' | 'select'

export type FormField = {
  key: string
  label: string
  required?: boolean
  mode?: FillMode
  options?: string[]
}

export type FormGroup = {
  id: string
  title: string
  femaleOnly?: boolean
  fields: FormField[]
}

export const defaultExamGroups: FormGroup[] = [
  { id: 'pessoais', title: 'Antecedentes pessoais', fields: [['nefrit', 'Nefrit.'], ['tbc', 'Tbc.'], ['maleita', 'Maleita'], ['parasitoses', 'Parasitoses'], ['hepatite', 'Hepatite'], ['reum', 'Reum Poliart.'], ['pleuriz', 'Pleuriz.'], ['venereas', 'Venéreas'], ['outras', 'Outras'], ['operacao', 'Operação'], ['acidentes', 'Acidentes']].map(field) },
  { id: 'obstetrico', title: 'Obstétrico e ginecológico', femaleOnly: true, fields: [['menarca', 'Menarca'], ['partosAbortos', 'Partos e Abortos'], ['ciclo', 'Ciclo']].map(field) },
  { id: 'familiares', title: 'Antecedentes familiares', fields: [['antecedenteFamiliar', 'Antecedente familiar'], ['hpma', 'HPMA']].map(field) },
  { id: 'habitos', title: 'Hábitos', fields: [['fumo', 'Fumo'], ['alcool', 'Álcool'], ['toxicos', 'Tóxicos'], ['alimentacao', 'Alimentação'], ['horasSono', 'Horas de sono']].map(field) },
  { id: 'medidas', title: 'Medidas', fields: [['peso', 'Peso'], ['altura', 'Altura'], ['pa', 'PA'], ['pulso', 'Pulso']].map(field) },
  { id: 'geral', title: 'Exame geral', fields: [['mucosas', 'Mucosas'], ['garganta', 'Garganta'], ['tegumentos', 'Tegumentos'], ['ganglios', 'Gânglios'], ['dentes', 'Dentes'], ['olhos', 'Olhos']].map(field) },
  { id: 'visao-sem', title: 'Visão sem óculos', fields: [['semPertoEsq', 'Perto esquerdo'], ['semPertoDir', 'Perto direito'], ['semLongeEsq', 'Longe esquerdo'], ['semLongeDir', 'Longe direito']].map(field) },
  { id: 'visao-com', title: 'Visão com óculos', fields: [['comPertoEsq', 'Perto esquerdo'], ['comPertoDir', 'Perto direito'], ['comLongeEsq', 'Longe esquerdo'], ['comLongeDir', 'Longe direito'], ['cores', 'Cores'], ['ouvidos', 'Ouvidos']].map(field) },
  { id: 'aparelhos', title: 'Aparelhos', fields: [['respiratorio', 'Aparelho respiratório'], ['coracao', 'Coração'], ['varizes', 'Varizes'], ['circulatorioColateral', 'Circulatório colateral'], ['abdomen', 'Abdômen'], ['cicatrizes', 'Cicatrizes'], ['aneis', 'Anéis heminários'], ['outrosAchados', 'Outros achados'], ['genito', 'Gênito urinário'], ['osteo', 'Ósteo articular'], ['nervoso', 'Sistema nervoso'], ['romberg', 'Romberg'], ['psiquismo', 'Psiquismo'], ['endocrino', 'Endócrino']].map(field) },
  { id: 'vacinas', title: 'Vacinas', fields: [['antivariolica', 'Antivariólica'], ['antitetanica', 'Antitetânica'], ['outrasVacinas', 'Outras']].map(field) },
  { id: 'resultado', title: 'Resultado', fields: [{ key: 'fit', label: 'Aptidão', required: true, mode: 'select', options: ['APTO', 'INAPTO'] }] },
]

function field(pair: string[]): FormField {
  return { key: pair[0], label: pair[1], mode: 'text' }
}

export function visibleGroups(groups: FormGroup[], sex: string) {
  return groups.filter((group) => !group.femaleOnly || sex === 'F')
}

export function formatDateBr(iso: string) {
  const [year, month, day] = iso.split('-')
  if (!year || !month || !day) return iso
  return `${day}/${month}/${year}`
}
