import { useEffect, useState, type FormEvent } from 'react'
import { Button } from '../components/ui/button'
import { Card, Field } from '../components/ui/field'
import { Input } from '../components/ui/input'
import { Loading } from '../components/Loading'
import { lookupCep, maskCep, maskCnpj, maskPhone } from '../components/masks'
import { api, type Session } from '../session'

type Company = { id: string; cnpj: string; name: string; address: string; phone?: string; postalCode?: string }

export function CompaniesPage({ session }: { session: Session }) {
  const canWrite = session.role === 'SECRETARIA'
  const [items, setItems] = useState<Company[]>([])
  const [query, setQuery] = useState('')
  const [form, setForm] = useState({ cnpj: '', name: '', address: '', phone: '', postalCode: '', city: '' })
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState('')

  async function load(q = query) {
    setItems(await api(`/api/companies?q=${encodeURIComponent(q)}`, session.token))
  }

  useEffect(() => {
    void load('')
  }, [])

  async function submit(event: FormEvent) {
    event.preventDefault()
    setMessage('')
    try {
      const address = form.city && !form.address.includes(form.city) ? `${form.address} - ${form.city}` : form.address
      await api('/api/companies', session.token, { method: 'POST', body: JSON.stringify({ ...form, address }) })
      setForm({ cnpj: '', name: '', address: '', phone: '', postalCode: '', city: '' })
      setMessage('Empresa gravada.')
      await load('')
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Falha ao gravar.')
    }
  }

  async function fillCep(value: string) {
    const postalCode = maskCep(value)
    setForm((current) => ({ ...current, postalCode }))
    if (postalCode.replace(/\D/g, '').length !== 8) return
    setLoading(true)
    setMessage('')
    try {
      const found = await lookupCep(postalCode)
      setForm((current) => ({ ...current, postalCode, address: found.address, city: found.city }))
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Falha ao consultar CEP.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="grid gap-6">
      <Loading show={loading} />
      <Card title="Empresas">
        <form className="mb-4 flex gap-2" onSubmit={(event) => { event.preventDefault(); void load(query) }}>
          <Input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Pesquisar por nome" />
          <Button type="submit" variant="outline">Pesquisar</Button>
        </form>
        <ul className="divide-y divide-slate-100">
          {items.map((item) => (
            <li key={item.id} className="py-2 text-sm">
              <strong>{item.name}</strong>
              <span className="text-slate-500"> — {item.cnpj || 'sem CNPJ'} — {item.address}</span>
            </li>
          ))}
        </ul>
      </Card>
      {canWrite && (
        <Card title="Nova empresa">
          <form onSubmit={submit} className="grid gap-3 md:grid-cols-3">
            <Field label="CNPJ"><Input value={form.cnpj} onChange={(e) => setForm({ ...form, cnpj: maskCnpj(e.target.value) })} /></Field>
            <Field label="Telefone"><Input value={form.phone} onChange={(e) => setForm({ ...form, phone: maskPhone(e.target.value) })} /></Field>
            <Field label="CEP"><Input value={form.postalCode} onChange={(e) => void fillCep(e.target.value)} /></Field>
            <Field label="Nome"><Input required value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} /></Field>
            <Field label="Endereço"><Input required value={form.address} onChange={(e) => setForm({ ...form, address: e.target.value })} /></Field>
            <Field label="Cidade"><Input value={form.city} onChange={(e) => setForm({ ...form, city: e.target.value })} /></Field>
            <Button type="submit" className="md:col-span-3 w-fit">Cadastrar</Button>
            {message && <p className="text-sm text-slate-600 md:col-span-3">{message}</p>}
          </form>
        </Card>
      )}
    </div>
  )
}
