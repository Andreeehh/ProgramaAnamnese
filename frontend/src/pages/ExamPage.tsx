import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Button } from '../components/ui/button'
import { Card, Field } from '../components/ui/field'
import { Input, Select } from '../components/ui/input'
import { api, type Session } from '../session'

const groups: { title: string; fields: [string, string][]; femaleOnly?: boolean }[] = [
  { title: 'Antecedentes pessoais', fields: [['nefrit', 'Nefrit.'], ['tbc', 'Tbc.'], ['maleita', 'Maleita'], ['parasitoses', 'Parasitoses'], ['hepatite', 'Hepatite'], ['reum', 'Reum Poliart.'], ['pleuriz', 'Pleuriz.'], ['venereas', 'Venéreas'], ['outras', 'Outras'], ['operacao', 'Operação'], ['acidentes', 'Acidentes']] },
  { title: 'Obstétrico e ginecológico', femaleOnly: true, fields: [['menarca', 'Menarca'], ['partosAbortos', 'Partos e Abortos'], ['ciclo', 'Ciclo']] },
  { title: 'Antecedentes familiares', fields: [['antecedenteFamiliar', 'Antecedente familiar'], ['hpma', 'HPMA']] },
  { title: 'Hábitos', fields: [['fumo', 'Fumo'], ['alcool', 'Álcool'], ['toxicos', 'Tóxicos'], ['alimentacao', 'Alimentação'], ['horasSono', 'Horas de sono']] },
  { title: 'Medidas', fields: [['peso', 'Peso'], ['altura', 'Altura'], ['pa', 'PA'], ['pulso', 'Pulso']] },
  { title: 'Exame geral', fields: [['mucosas', 'Mucosas'], ['garganta', 'Garganta'], ['tegumentos', 'Tegumentos'], ['ganglios', 'Gânglios'], ['dentes', 'Dentes'], ['olhos', 'Olhos']] },
  { title: 'Visão sem óculos', fields: [['semPertoEsq', 'Perto esquerdo'], ['semPertoDir', 'Perto direito'], ['semLongeEsq', 'Longe esquerdo'], ['semLongeDir', 'Longe direito']] },
  { title: 'Visão com óculos', fields: [['comPertoEsq', 'Perto esquerdo'], ['comPertoDir', 'Perto direito'], ['comLongeEsq', 'Longe esquerdo'], ['comLongeDir', 'Longe direito'], ['cores', 'Cores'], ['ouvidos', 'Ouvidos']] },
  { title: 'Aparelhos', fields: [['respiratorio', 'Aparelho respiratório'], ['coracao', 'Coração'], ['varizes', 'Varizes'], ['circulatorioColateral', 'Circulatório colateral'], ['abdomen', 'Abdômen'], ['cicatrizes', 'Cicatrizes'], ['aneis', 'Anéis heminários'], ['outrosAchados', 'Outros achados'], ['genito', 'Gênito urinário'], ['osteo', 'Ósteo articular'], ['nervoso', 'Sistema nervoso'], ['romberg', 'Romberg'], ['psiquismo', 'Psiquismo'], ['endocrino', 'Endócrino']] },
  { title: 'Vacinas', fields: [['antivariolica', 'Antivariólica'], ['antitetanica', 'Antitetânica'], ['outrasVacinas', 'Outras']] },
]

type Exam = {
  patientName: string
  birthDate: string
  sex: string
  cpf: string
  companyName: string
  examType: string
  jobTitle: string
  answers: Record<string, string>
  fit: string | null
  status: string
}

export function ExamPage({ session }: { session: Session }) {
  const { queueId } = useParams()
  const navigate = useNavigate()
  const [exam, setExam] = useState<Exam | null>(null)
  const [answers, setAnswers] = useState<Record<string, string>>({})
  const [fit, setFit] = useState('')
  const [message, setMessage] = useState('')

  useEffect(() => {
    void api(`/api/exams/queue/${queueId}`, session.token).then((loaded: Exam) => {
      setExam(loaded)
      setAnswers(loaded.answers ?? {})
      setFit(loaded.fit ?? '')
    })
  }, [queueId, session.token])

  async function persist() {
    const saved = await api(`/api/exams/queue/${queueId}`, session.token, {
      method: 'PUT',
      body: JSON.stringify({ answers, fit: fit || null }),
    })
    setExam(saved)
  }

  async function save() {
    setMessage('')
    try {
      await persist()
      setMessage('Ficha gravada.')
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Falha ao gravar.')
    }
  }

  async function finish() {
    setMessage('')
    try {
      await persist()
      await api(`/api/exams/queue/${queueId}/finish`, session.token, { method: 'POST' })
      navigate('/recepcao')
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Falha ao finalizar.')
    }
  }

  if (!exam) return <p>Carregando ficha...</p>
  const editable = session.role === 'MEDICO' && exam.status === 'EM_ATENDIMENTO'

  return (
    <div className="grid gap-4">
      <Card title={exam.patientName}>
        <p className="text-sm text-slate-600">{exam.examType} — {exam.companyName} — CPF {exam.cpf} — {exam.jobTitle} — nasc. {exam.birthDate}</p>
      </Card>
      {groups.filter((group) => !group.femaleOnly || exam.sex === 'F').map((group) => (
        <Card key={group.title} title={group.title}>
          <div className="grid gap-3 md:grid-cols-2">
            {group.fields.map(([key, label]) => (
              <Field key={key} label={label}>
                <Input value={answers[key] ?? ''} disabled={!editable} onChange={(e) => setAnswers({ ...answers, [key]: e.target.value })} />
              </Field>
            ))}
          </div>
        </Card>
      ))}
      <Card title="Resultado">
        <Field label="Aptidão">
          <Select value={fit} disabled={!editable} onChange={(e) => setFit(e.target.value)}>
            <option value="">Selecione</option>
            <option value="APTO">APTO</option>
            <option value="INAPTO">INAPTO</option>
          </Select>
        </Field>
        {editable && (
          <div className="mt-3 flex gap-2">
            <Button type="button" variant="outline" onClick={() => void save()}>Salvar</Button>
            <Button type="button" onClick={() => void finish()}>Finalizar atendimento</Button>
          </div>
        )}
        {message && <p className="mt-2 text-sm text-slate-600">{message}</p>}
      </Card>
    </div>
  )
}
