import { Building2, ClipboardList, LogOut, PanelLeft, Stethoscope, UserRound, Users } from 'lucide-react'
import { useState } from 'react'
import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { Button } from '../components/ui/button'
import { cn } from '../lib/cn'
import { clearSession, type Session } from '../session'

const links = {
  MASTER: [
    { to: '/clinicas', label: 'Clínicas', icon: Building2 },
    { to: '/usuarios', label: 'Usuários', icon: Users },
  ],
  SECRETARIA: [
    { to: '/recepcao', label: 'Recepção', icon: ClipboardList },
    { to: '/pacientes', label: 'Pacientes', icon: UserRound },
    { to: '/empresas', label: 'Empresas', icon: Building2 },
  ],
  MEDICO: [
    { to: '/recepcao', label: 'Fila', icon: ClipboardList },
    { to: '/pacientes', label: 'Pacientes', icon: UserRound },
    { to: '/empresas', label: 'Empresas', icon: Building2 },
  ],
}

export function AppShell({ session }: { session: Session }) {
  const [open, setOpen] = useState(localStorage.getItem('sidebar') !== 'closed')
  const navigate = useNavigate()
  const items = links[session.role]

  function toggle() {
    const next = !open
    setOpen(next)
    localStorage.setItem('sidebar', next ? 'open' : 'closed')
  }

  return (
    <div className="flex min-h-screen">
      <aside className={cn('flex shrink-0 flex-col border-r border-slate-200 bg-slate-950 text-slate-100 transition-all', open ? 'w-60' : 'w-16')}>
        <div className="flex h-14 items-center gap-2 px-3">
          <Stethoscope className="h-5 w-5 shrink-0 text-teal-300" />
          {open && <span className="truncate text-sm font-semibold">Medicina do trabalho</span>}
        </div>
        <nav className="grid gap-1 px-2">
          {items.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              title={item.label}
              className={({ isActive }) =>
                cn('flex h-10 items-center gap-3 rounded-md px-3 text-sm', isActive ? 'bg-teal-800 text-white' : 'text-slate-300 hover:bg-slate-800')
              }
            >
              <item.icon className="h-4 w-4 shrink-0" />
              {open && item.label}
            </NavLink>
          ))}
        </nav>
        <div className="mt-auto p-2">
          <Button
            variant="ghost"
            className="w-full justify-start text-slate-200 hover:bg-slate-800 hover:text-white"
            onClick={() => {
              clearSession()
              navigate('/entrar')
              window.location.reload()
            }}
          >
            <LogOut className="h-4 w-4" />
            {open && 'Sair'}
          </Button>
        </div>
      </aside>
      <div className="min-w-0 flex-1">
        <header className="flex h-14 items-center gap-3 border-b border-slate-200 bg-white px-4">
          <Button variant="outline" size="icon" onClick={toggle} aria-label="Recolher menu">
            <PanelLeft className="h-4 w-4" />
          </Button>
          <div className="min-w-0">
            <p className="truncate text-sm font-medium">{session.fullName}</p>
            <p className="text-xs text-slate-500">{session.role === 'MASTER' ? 'Master' : session.role === 'MEDICO' ? 'Médico' : 'Secretaria'}</p>
          </div>
        </header>
        <main className="p-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
