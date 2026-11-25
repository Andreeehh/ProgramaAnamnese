import { useEffect, useState, type FormEvent } from 'react'
import { Button } from '../components/ui/button'
import { Card, Field } from '../components/ui/field'
import { Input, Select } from '../components/ui/input'
import { api, type Session } from '../session'

type Tenant = { id: string; name: string }
type User = { id: string; username: string; role: string }

export function UsersPage({ session }: { session: Session }) {
  const [tenants, setTenants] = useState<Tenant[]>([])
  const [users, setUsers] = useState<User[]>([])
  const [form, setForm] = useState({
    username: '',
    password: '',
    fullName: '',
    role: 'SECRETARIA',
    tenantId: '',
    crm: '',
    signatureName: '',
    signatureDetails: '',
  })
  const [message, setMessage] = useState('')

  async function load() {
    const [clinicList, userList] = await Promise.all([
      api('/api/admin/tenants', session.token),
      api('/api/admin/users', session.token),
    ])
    setTenants(clinicList)
    setUsers(userList)
    setForm((current) => ({ ...current, tenantId: current.tenantId || clinicList[0]?.id || '' }))
  }

  useEffect(() => {
    void load()
  }, [])

  async function submit(event: FormEvent) {
    event.preventDefault()
    setMessage('')
    try {
      await api('/api/admin/users', session.token, {
        method: 'POST',
        body: JSON.stringify({
          username: form.username,
          password: form.password,
          fullName: form.fullName,
          role: form.role,
          tenantIds: [form.tenantId],
          crm: form.role === 'MEDICO' ? form.crm : null,
          signatureName: form.role === 'MEDICO' ? form.signatureName : null,
          signatureDetails: form.role === 'MEDICO' ? form.signatureDetails : null,
        }),
      })
      setMessage('Usuário cadastrado.')
      setForm((current) => ({ ...current, username: '', password: '', fullName: '', crm: '', signatureName: '', signatureDetails: '' }))
      await load()
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Falha ao cadastrar.')
    }
  }

  return (
    <div className="grid gap-6 lg:grid-cols-[420px_1fr]">
      <Card title="Novo usuário">
        <form onSubmit={submit} className="grid gap-3">
          <Field label="Nome">
            <Input value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} required />
          </Field>
          <Field label="Usuário">
            <Input value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} required />
          </Field>
          <Field label="Senha">
            <Input type="password" minLength={8} value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required />
          </Field>
          <Field label="Perfil">
            <Select value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })}>
              <option value="SECRETARIA">Secretaria</option>
              <option value="MEDICO">Médico</option>
            </Select>
          </Field>
          <Field label="Clínica">
            <Select value={form.tenantId} onChange={(e) => setForm({ ...form, tenantId: e.target.value })} required>
              {tenants.map((tenant) => <option key={tenant.id} value={tenant.id}>{tenant.name}</option>)}
            </Select>
          </Field>
          {form.role === 'MEDICO' && (
            <>
              <Field label="CRM">
                <Input value={form.crm} onChange={(e) => setForm({ ...form, crm: e.target.value })} required />
              </Field>
              <Field label="Nome na assinatura">
                <Input value={form.signatureName} onChange={(e) => setForm({ ...form, signatureName: e.target.value })} />
              </Field>
              <Field label="Carimbo">
                <Input value={form.signatureDetails} onChange={(e) => setForm({ ...form, signatureDetails: e.target.value })} />
              </Field>
            </>
          )}
          <Button type="submit">Cadastrar</Button>
          {message && <p className="text-sm text-slate-600">{message}</p>}
        </form>
      </Card>
      <Card title="Usuários">
        <ul className="divide-y divide-slate-100">
          {users.map((user) => (
            <li key={user.id} className="flex justify-between py-2 text-sm">
              <span>{user.username}</span>
              <span className="text-slate-500">{user.role}</span>
            </li>
          ))}
        </ul>
      </Card>
    </div>
  )
}
