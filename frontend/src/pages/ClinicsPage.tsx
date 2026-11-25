import { useEffect, useState, type FormEvent } from 'react'
import { Button } from '../components/ui/button'
import { Card, Field } from '../components/ui/field'
import { Input } from '../components/ui/input'
import { api, type Session } from '../session'

type Tenant = { id: string; name: string }

export function ClinicsPage({ session }: { session: Session }) {
  const [tenants, setTenants] = useState<Tenant[]>([])
  const [name, setName] = useState('')
  const [message, setMessage] = useState('')

  async function load() {
    setTenants(await api('/api/admin/tenants', session.token))
  }

  useEffect(() => {
    void load()
  }, [])

  async function submit(event: FormEvent) {
    event.preventDefault()
    setMessage('')
    try {
      await api('/api/admin/tenants', session.token, { method: 'POST', body: JSON.stringify({ name }) })
      setName('')
      setMessage('Clínica cadastrada.')
      await load()
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Falha ao cadastrar.')
    }
  }

  return (
    <div className="grid gap-6 lg:grid-cols-[360px_1fr]">
      <Card title="Nova clínica">
        <form onSubmit={submit} className="grid gap-3">
          <Field label="Nome">
            <Input value={name} onChange={(e) => setName(e.target.value)} required />
          </Field>
          <Button type="submit">Cadastrar</Button>
          {message && <p className="text-sm text-slate-600">{message}</p>}
        </form>
      </Card>
      <Card title="Clínicas">
        <ul className="divide-y divide-slate-100">
          {tenants.map((tenant) => (
            <li key={tenant.id} className="py-2 text-sm">{tenant.name}</li>
          ))}
          {tenants.length === 0 && <li className="text-sm text-slate-500">Nenhuma clínica cadastrada.</li>}
        </ul>
      </Card>
    </div>
  )
}
