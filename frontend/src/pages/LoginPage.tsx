import { Stethoscope } from 'lucide-react'
import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button } from '../components/ui/button'
import { Field } from '../components/ui/field'
import { Input } from '../components/ui/input'
import { saveSession, type Session } from '../session'

export function LoginPage({ onLogin }: { onLogin: (session: Session) => void }) {
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  async function submit(event: FormEvent) {
    event.preventDefault()
    setError('')
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    })
    if (!response.ok) {
      setError('Usuário ou senha inválidos.')
      return
    }
    const session = (await response.json()) as Session
    saveSession(session)
    onLogin(session)
    navigate(session.role === 'MASTER' ? '/clinicas' : '/recepcao')
  }

  return (
    <main className="grid min-h-screen place-items-center bg-slate-100 p-6">
      <form onSubmit={submit} className="grid w-full max-w-sm gap-4 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        <div className="flex items-center gap-2 text-teal-800">
          <Stethoscope className="h-5 w-5" />
          <h1 className="text-lg font-semibold">Medicina do trabalho</h1>
        </div>
        <Field label="Usuário">
          <Input value={username} onChange={(e) => setUsername(e.target.value)} autoComplete="username" required />
        </Field>
        <Field label="Senha">
          <Input type="password" value={password} onChange={(e) => setPassword(e.target.value)} autoComplete="current-password" required />
        </Field>
        {error && <p className="text-sm text-red-700">{error}</p>}
        <Button type="submit">Entrar</Button>
      </form>
    </main>
  )
}
