import { useEffect, useState, type FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { Button } from '../components/ui/button'
import { Card, Field } from '../components/ui/field'
import { Input, Select } from '../components/ui/input'
import { api, type Session } from '../session'

const examTypes = [
  ['ADMISSIONAL', 'Admissional'],
  ['PERIODICO', 'Periódico'],
  ['DEMISSIONAL', 'Demissional'],
  ['RETORNO', 'Retorna ao Trabalho'],
  ['MUDANCA', 'Mudança de cargo/função'],
  ['ACIDENTE', 'Acidente do Trabalho'],
  ['DOENCA_OCUPACIONAL', 'Doença Ocupacional'],
  ['DOENCA_NAO_OCUPACIONAL', 'Doença/Acidente não Ocupacional'],
  ['PARTO', 'Parto'],
] as const

type Patient = { id: string; fullName: string; cpf: string }
type Slot = { id: string; patientName: string; examType: string; status: string; arrivedAt: string; startedAt?: string } | null
type Board = { inCare: Slot; next: Slot; waiting: NonNullable<Slot>[] }

export function ReceptionPage({ session }: { session: Session }) {
  const [query, setQuery] = useState('')
  const [patients, setPatients] = useState<Patient[]>([])
  const [patientId, setPatientId] = useState('')
  const [examType, setExamType] = useState('ADMISSIONAL')
  const [board, setBoard] = useState<Board>({ inCare: null, next: null, waiting: [] })
  const [message, setMessage] = useState('')

  async function loadBoard() {
    setBoard(await api('/api/queue', session.token))
  }

  useEffect(() => {
    void loadBoard()
    const timer = window.setInterval(() => void loadBoard(), 5000)
    return () => window.clearInterval(timer)
  }, [])

  async function search(event: FormEvent) {
    event.preventDefault()
    const param = query.replace(/\D/g, '').length === 11 ? `cpf=${encodeURIComponent(query)}` : `name=${encodeURIComponent(query)}`
    const found = await api(`/api/patients?${param}`, session.token)
    setPatients(found)
    setPatientId(found[0]?.id ?? '')
  }

  async function arrive(event: FormEvent) {
    event.preventDefault()
    setMessage('')
    try {
      await api('/api/queue', session.token, { method: 'POST', body: JSON.stringify({ patientId, examType }) })
      setMessage('Chegada registrada.')
      await loadBoard()
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Falha ao registrar chegada.')
    }
  }

  return (
    <div className="grid gap-6 lg:grid-cols-[420px_1fr]">
      {session.role === 'SECRETARIA' && (
        <Card title="Recepção">
          <form onSubmit={search} className="mb-3 flex gap-2">
            <Input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Nome ou CPF" />
            <Button type="submit" variant="outline">Buscar</Button>
          </form>
          <form onSubmit={arrive} className="grid gap-3">
            <Field label="Paciente">
              <Select required value={patientId} onChange={(e) => setPatientId(e.target.value)}>
                <option value="">Selecione</option>
                {patients.map((patient) => <option key={patient.id} value={patient.id}>{patient.fullName} — {patient.cpf}</option>)}
              </Select>
            </Field>
            <Field label="Tipo de exame">
              <Select value={examType} onChange={(e) => setExamType(e.target.value)}>
                {examTypes.map(([value, label]) => <option key={value} value={value}>{label}</option>)}
              </Select>
            </Field>
            <Button type="submit">Registrar chegada</Button>
            {message && <p className="text-sm text-slate-600">{message}</p>}
          </form>
        </Card>
      )}
      <QueueBoard session={session} board={board} onChange={loadBoard} />
    </div>
  )
}

function QueueBoard({ session, board, onChange }: { session: Session; board: Board; onChange: () => Promise<void> }) {
  async function act(path: string) {
    await api(path, session.token, { method: 'POST' })
    await onChange()
  }

  return (
    <div className="grid gap-4">
      <Card title="Em atendimento">
        <SlotLine slot={board.inCare} />
        {board.inCare && <Link className="mt-2 inline-block text-sm text-teal-800" to={`/atendimento/${board.inCare.id}`}>Abrir ficha</Link>}
      </Card>
      <Card title="Próximo">
        <SlotLine slot={board.next} />
        {session.role === 'MEDICO' && (
          <div className="mt-3 flex gap-2">
            <Button type="button" onClick={() => void act('/api/queue/next')}>Chamar próximo</Button>
            {board.next?.status === 'CHAMADO' && <Button type="button" variant="outline" onClick={() => void act(`/api/queue/${board.next?.id}/start`)}>Iniciar</Button>}
          </div>
        )}
      </Card>
      <Card title="Espera">
        <ul className="divide-y divide-slate-100">
          {board.waiting.map((item) => (
            <li key={item.id} className="flex items-center justify-between py-2 text-sm">
              <span>{item.patientName} — {item.examType}</span>
              <Button type="button" variant="outline" onClick={() => void act(`/api/queue/${item.id}/cancel`)}>Cancelar</Button>
            </li>
          ))}
          {board.waiting.length === 0 && <li className="text-sm text-slate-500">Ninguém aguardando.</li>}
        </ul>
      </Card>
    </div>
  )
}

function SlotLine({ slot }: { slot: Slot }) {
  if (!slot) return <p className="text-sm text-slate-500">Nenhum paciente.</p>
  return <p className="text-sm"><strong>{slot.patientName}</strong> — {slot.examType}</p>
}
