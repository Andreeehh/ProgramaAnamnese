export function Loading({ show }: { show: boolean }) {
  if (!show) return null
  return (
    <div className="fixed inset-0 z-50 grid place-items-center bg-slate-950/40">
      <div className="rounded-xl bg-white px-6 py-4 text-sm shadow-lg">Consultando CEP...</div>
    </div>
  )
}
