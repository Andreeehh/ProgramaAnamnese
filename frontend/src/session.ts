export type Session = {
  token: string;
  fullName: string;
  role: "MASTER" | "SECRETARIA" | "MEDICO";
  tenantIds: string[];
};

const key = "session";

export function loadSession(): Session | null {
  const raw = sessionStorage.getItem(key);
  return raw ? (JSON.parse(raw) as Session) : null;
}

export function saveSession(session: Session) {
  sessionStorage.setItem(key, JSON.stringify(session));
}

export function clearSession() {
  sessionStorage.removeItem(key);
}

const apiBase = (import.meta.env.VITE_API_URL ?? "").replace(/\/$/, "");

export function apiUrl(path: string) {
  return `${apiBase}${path}`;
}

export async function api(path: string, token: string, init: RequestInit = {}) {
  const headers = new Headers(init.headers);
  headers.set("Authorization", `Bearer ${token}`);
  if (init.body && !headers.has("Content-Type"))
    headers.set("Content-Type", "application/json");
  const response = await fetch(apiUrl(path), { ...init, headers });
  if (!response.ok) {
    let message = "Não foi possível concluir a operação.";
    try {
      const body = (await response.json()) as {
        message?: string;
        detail?: string;
      };
      if (body.detail || body.message)
        message = body.detail || body.message || message;
    } catch {
      /* resposta sem JSON */
    }
    throw new Error(message);
  }
  if (response.status === 204) return null;
  return response.json();
}
