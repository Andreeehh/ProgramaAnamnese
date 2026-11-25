import { useEffect, useState, type FormEvent } from 'react'
import { Button } from '../components/ui/button'
import { Card, Field } from '../components/ui/field'
import { Input, Select } from '../components/ui/input'
import { Loading } from '../components/Loading'
import { digits, lookupCep, maskCep, maskCnpj, maskCpf, maskPhone, maskRg } from '../components/masks'
import { api, type Session } from '../session'

type Company = { id: string; name: string }
type Patient = { id: string; fullName: string; cpf: string; rg: string; jobTitle: string }

const empty = {
  newCompany: false,
  companyId: '',
  companyName: '',
  companyCnpj: '',
  companyAddress: '',
  fullName: '',
  birthDate: '',
  sex: 'M',
  skinColor: '',
  maritalStatus: '',
  cpf: '',
  rg: '',
  address: '',
  city: '',
  jobTitle: '',
  fatherName: '',
  motherName: '',
  birthplace: '',
  phone: '',
  postalCode: '',
}

export function PatientsPage({ session }: { session: Session }) {
  const canWrite = session.role === 'SECRETARIA'
  const [companies, setCompanies] = useState<Company[]>([])
  const [patients, setPatients] = useState<Patient[]>([])
  const [query, setQuery] = useState('')
  const [form, setForm] = useState(empty)
  const [message, setMessage] = useState('')
  const [loading, setLoading] = useState(false)

  async function load(name = '') {
    const cpf = digits(name)
    const param = cpf.length === 11 ? `cpf=${encodeURIComponent(cpf)}` : `name=${encodeURIComponent(name)}`
    const [people, clinics] = await Promise.all([
      api(`/api/patients?${param}`, session.token),
      api('/api/companies', session.token),
    ])
    setPatients(people)
    setCompanies(clinics)
  }

  useEffect(() => {
    void load()
  }, [])

  async function submit(event: FormEvent) {
    event.preventDefault()
    setMessage('')
    const fields = {
      fullName: form.fullName,
      birthDate: form.birthDate,
      sex: form.sex,
      skinColor: form.skinColor,
      maritalStatus: form.maritalStatus,
      cpf: digits(form.cpf),
      rg: digits(form.rg),
      address: form.address,
      city: form.city,
      jobTitle: form.jobTitle,
      fatherName: form.fatherName,
      motherName: form.motherName,
      birthplace: form.birthplace,
      phone: digits(form.phone),
      postalCode: digits(form.postalCode),
    }
    const body = form.newCompany
      ? { company: { cnpj: digits(form.companyCnpj), name: form.companyName, address: form.companyAddress }, ...fields }
      : { companyId: form.companyId, ...fields }
    try {
      await api('/api/patients', session.token, { method: 'POST', body: JSON.stringify(body) })
      setForm(empty)
      setMessage('Paciente gravado.')
      await load()
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Falha ao gravar.')
    }
  }

  async function fillCep(value: string) {
    const postalCode = maskCep(value)
    setForm((current) => ({ ...current, postalCode }))
    if (postalCode.replace(/\D/g, '').length !== 8) return
    setLoading(true)
    try {
      const found = await lookupCep(postalCode)
      setForm((current) => ({ ...current, postalCode, address: found.address || current.address, city: found.city || current.city }))
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Falha ao consultar CEP.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="grid gap-6">
      <Loading show={loading} />
      <Card title="Pacientes">
        <form className="mb-4 flex gap-2" onSubmit={(event) => { event.preventDefault(); void load(query) }}>
          <Input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Nome ou CPF" />
          <Button type="submit" variant="outline">Pesquisar</Button>
        </form>
        <ul className="divide-y divide-slate-100">
          {patients.map((patient) => (
            <li key={patient.id} className="py-2 text-sm">
              <strong>{patient.fullName}</strong>
              <span className="text-slate-500"> — CPF {maskCpf(patient.cpf)} — RG {maskRg(patient.rg)} — {patient.jobTitle}</span>
            </li>
          ))}
        </ul>
      </Card>
      {canWrite && (
        <Card title="Novo paciente">
          <form onSubmit={submit} className="grid gap-3 md:grid-cols-3">
            <label className="flex items-center gap-2 text-sm md:col-span-3">
              <input type="checkbox" checked={form.newCompany} onChange={(e) => setForm({ ...form, newCompany: e.target.checked })} />
              Cadastrar empresa agora
            </label>
            {form.newCompany ? (
              <>
                <Field label="CNPJ"><Input value={form.companyCnpj} onChange={(e) => setForm({ ...form, companyCnpj: maskCnpj(e.target.value) })} /></Field>
                <Field label="Empresa"><Input required value={form.companyName} onChange={(e) => setForm({ ...form, companyName: e.target.value })} /></Field>
                <Field label="Endereço da empresa"><Input required value={form.companyAddress} onChange={(e) => setForm({ ...form, companyAddress: e.target.value })} /></Field>
              </>
            ) : (
              <Field label="Empresa">
                <Select required value={form.companyId} onChange={(e) => setForm({ ...form, companyId: e.target.value })}>
                  <option value="">Selecione</option>
                  {companies.map((company) => <option key={company.id} value={company.id}>{company.name}</option>)}
                </Select>
              </Field>
            )}
            <Field label="Nome"><Input required value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} /></Field>
            <Field label="Nascimento"><Input required type="date" value={form.birthDate} onChange={(e) => setForm({ ...form, birthDate: e.target.value })} /></Field>
            <Field label="Sexo">
              <Select value={form.sex} onChange={(e) => setForm({ ...form, sex: e.target.value })}>
                <option value="M">Masculino</option>
                <option value="F">Feminino</option>
              </Select>
            </Field>
            <Field label="Cor"><Input value={form.skinColor} onChange={(e) => setForm({ ...form, skinColor: e.target.value })} /></Field>
            <Field label="Estado civil"><Input required value={form.maritalStatus} onChange={(e) => setForm({ ...form, maritalStatus: e.target.value })} /></Field>
            <Field label="CPF"><Input required value={form.cpf} onChange={(e) => setForm({ ...form, cpf: maskCpf(e.target.value) })} /></Field>
            <Field label="RG"><Input required value={form.rg} onChange={(e) => setForm({ ...form, rg: maskRg(e.target.value) })} /></Field>
            <Field label="Telefone"><Input value={form.phone} onChange={(e) => setForm({ ...form, phone: maskPhone(e.target.value) })} /></Field>
            <Field label="CEP"><Input value={form.postalCode} onChange={(e) => void fillCep(e.target.value)} /></Field>
            <Field label="Endereço"><Input required value={form.address} onChange={(e) => setForm({ ...form, address: e.target.value })} /></Field>
            <Field label="Cidade"><Input required value={form.city} onChange={(e) => setForm({ ...form, city: e.target.value })} /></Field>
            <Field label="Cargo ou função"><Input required value={form.jobTitle} onChange={(e) => setForm({ ...form, jobTitle: e.target.value })} /></Field>
            <Field label="Pai"><Input value={form.fatherName} onChange={(e) => setForm({ ...form, fatherName: e.target.value })} /></Field>
            <Field label="Mãe"><Input value={form.motherName} onChange={(e) => setForm({ ...form, motherName: e.target.value })} /></Field>
            <Field label="Naturalidade"><Input required value={form.birthplace} onChange={(e) => setForm({ ...form, birthplace: e.target.value })} /></Field>
            <Button type="submit" className="w-fit">Cadastrar</Button>
            {message && <p className="text-sm text-slate-600 md:col-span-3">{message}</p>}
          </form>
        </Card>
      )}
    </div>
  )
}
