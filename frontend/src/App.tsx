import { useState } from 'react'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { AppShell } from './layout/AppShell'
import { ClinicsPage } from './pages/ClinicsPage'
import { CompaniesPage } from './pages/CompaniesPage'
import { LoginPage } from './pages/LoginPage'
import { PatientsPage } from './pages/PatientsPage'
import { ExamPage } from './pages/ExamPage'
import { ReceptionPage } from './pages/ReceptionPage'
import { UsersPage } from './pages/UsersPage'
import { loadSession, type Session } from './session'

export default function App() {
  const [session, setSession] = useState<Session | null>(loadSession)

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/entrar" element={<LoginPage onLogin={setSession} />} />
        <Route element={session ? <AppShell session={session} /> : <Navigate to="/entrar" replace />}>
          <Route path="/clinicas" element={session?.role === 'MASTER' ? <ClinicsPage session={session} /> : <Navigate to="/recepcao" replace />} />
          <Route path="/usuarios" element={session?.role === 'MASTER' ? <UsersPage session={session} /> : <Navigate to="/recepcao" replace />} />
          <Route path="/recepcao" element={session && session.role !== 'MASTER' ? <ReceptionPage session={session} /> : <Navigate to="/clinicas" replace />} />
          <Route path="/atendimento/:queueId" element={session && session.role !== 'MASTER' ? <ExamPage session={session} /> : <Navigate to="/clinicas" replace />} />
          <Route path="/pacientes" element={session && session.role !== 'MASTER' ? <PatientsPage session={session} /> : <Navigate to="/clinicas" replace />} />
          <Route path="/empresas" element={session && session.role !== 'MASTER' ? <CompaniesPage session={session} /> : <Navigate to="/clinicas" replace />} />
        </Route>
        <Route path="*" element={<Navigate to={session?.role === 'MASTER' ? '/clinicas' : session ? '/recepcao' : '/entrar'} replace />} />
      </Routes>
    </BrowserRouter>
  )
}
