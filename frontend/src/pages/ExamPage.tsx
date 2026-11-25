import { useEffect, useRef, useState, type KeyboardEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Button } from '../components/ui/button'
import { Card, Field } from '../components/ui/field'
import { Input, Select } from '../components/ui/input'
import { api, type Session } from '../session'
import { defaultExamGroups, formatDateBr, visibleGroups, type FormGroup } from './examForm'

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
  const [tab, setTab] = useState(0)
  const panel = useRef<HTMLDivElement>(null)
  const focusEnd = useRef(false)

  useEffect(() => {
    void api(`/api/exams/queue/${queueId}`, session.token).then((loaded: Exam) => {
      setExam(loaded)
      setAnswers(loaded.answers ?? {})
      setFit(loaded.fit ?? '')
    })
  }, [queueId, session.token])

  const groups = exam ? visibleGroups(defaultExamGroups, exam.sex) : []
  const current = groups[tab]

  useEffect(() => {
    const nodes = panel.current?.querySelectorAll<HTMLElement>('[data-nav]')
    const target = focusEnd.current ? nodes?.[nodes.length - 1] : nodes?.[0]
    focusEnd.current = false
    target?.focus()
  }, [tab, exam])

  function valueOf(key: string) {
    return key === 'fit' ? fit : (answers[key] ?? '')
  }

  function setValue(key: string, value: string) {
    if (key === 'fit') setFit(value)
    else setAnswers({ ...answers, [key]: value })
  }

  function moveTab(next: number) {
    setTab(Math.min(Math.max(next, 0), Math.max(groups.length - 1, 0)))
  }

  function onFieldKey(event: KeyboardEvent, index: number, count: number) {
    if (event.key !== 'Tab') return
    if (!event.shiftKey && index === count - 1 && tab < groups.length - 1) {
      event.preventDefault()
      moveTab(tab + 1)
    }
    if (event.shiftKey && index === 0 && tab > 0) {
      event.preventDefault()
      focusEnd.current = true
      moveTab(tab - 1)
    }
  }

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

  if (!exam || !current) return <p>Carregando ficha...</p>
  const editable = session.role === 'MEDICO' && exam.status === 'EM_ATENDIMENTO'

  return (
    <div className="grid gap-4">
      <Card title={exam.patientName}>
        <p className="text-sm text-slate-600">
          {exam.examType} — {exam.companyName} — CPF {exam.cpf} — {exam.jobTitle} — nasc. {formatDateBr(exam.birthDate)}
        </p>
      </Card>
      <section className="rounded-xl border border-slate-200 bg-white shadow-sm">
        <div className="flex gap-1 overflow-x-auto border-b border-slate-200 px-2 pt-2" role="tablist">
          {groups.map((group, index) => (
            <button
              key={group.id}
              type="button"
              role="tab"
              aria-selected={index === tab}
              tabIndex={-1}
              className={`shrink-0 rounded-t-md px-3 py-2 text-sm ${index === tab ? 'bg-teal-800 text-white' : 'text-slate-600 hover:bg-slate-100'}`}
              onClick={() => setTab(index)}
            >
              {group.title}
            </button>
          ))}
        </div>
        <div ref={panel} className="p-5" role="tabpanel">
          <GroupFields group={current} editable={editable} valueOf={valueOf} setValue={setValue} onFieldKey={onFieldKey} />
          <div className="mt-4 flex gap-2">
            <Button type="button" variant="outline" disabled={tab === 0} onClick={() => moveTab(tab - 1)}>Anterior</Button>
            <Button type="button" variant="outline" disabled={tab === groups.length - 1} onClick={() => moveTab(tab + 1)}>Próxima</Button>
            {editable && <Button type="button" variant="outline" onClick={() => void save()}>Salvar</Button>}
            {editable && tab === groups.length - 1 && <Button type="button" onClick={() => void finish()}>Finalizar atendimento</Button>}
          </div>
          {message && <p className="mt-2 text-sm text-slate-600">{message}</p>}
        </div>
      </section>
    </div>
  )
}

function GroupFields({ group, editable, valueOf, setValue, onFieldKey }: {
  group: FormGroup
  editable: boolean
  valueOf: (key: string) => string
  setValue: (key: string, value: string) => void
  onFieldKey: (event: KeyboardEvent, index: number, count: number) => void
}) {
  return (
    <div className="grid gap-3 md:grid-cols-2">
      {group.fields.map((field, index) => (
        <Field key={field.key} label={field.required ? `${field.label} *` : field.label}>
          {field.mode === 'select' ? (
            <Select data-nav={index} value={valueOf(field.key)} disabled={!editable} onKeyDown={(event) => onFieldKey(event, index, group.fields.length)} onChange={(event) => setValue(field.key, event.target.value)}>
              <option value="">Selecione</option>
              {field.options?.map((option) => <option key={option} value={option}>{option}</option>)}
            </Select>
          ) : (
            <Input data-nav={index} value={valueOf(field.key)} disabled={!editable} onKeyDown={(event) => onFieldKey(event, index, group.fields.length)} onChange={(event) => setValue(field.key, event.target.value)} />
          )}
        </Field>
      ))}
    </div>
  )
}
