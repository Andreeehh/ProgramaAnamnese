export function digits(value: string) {
  return value.replace(/\D/g, '')
}

export function maskCpf(value: string) {
  const d = digits(value).slice(0, 11)
  return d
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d{1,2})$/, '$1-$2')
}

export function maskCnpj(value: string) {
  const d = digits(value).slice(0, 14)
  return d
    .replace(/(\d{2})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d)/, '$1/$2')
    .replace(/(\d{4})(\d{1,2})$/, '$1-$2')
}

export function maskRg(value: string) {
  const d = digits(value).slice(0, 11)
  if (d.length <= 9) {
    return d.replace(/(\d{2})(\d)/, '$1.$2').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d{1})$/, '$1-$2')
  }
  return d
}

export function maskPhone(value: string) {
  const d = digits(value).slice(0, 11)
  if (d.length <= 10) {
    return d.replace(/(\d{2})(\d)/, '($1) $2').replace(/(\d{4})(\d{1,4})$/, '$1-$2')
  }
  return d.replace(/(\d{2})(\d)/, '($1) $2').replace(/(\d{5})(\d{1,4})$/, '$1-$2')
}

export function maskCep(value: string) {
  const d = digits(value).slice(0, 8)
  return d.replace(/(\d{5})(\d{1,3})$/, '$1-$2')
}

export type CepResult = { address: string; city: string }

export async function lookupCep(value: string): Promise<CepResult> {
  const cep = digits(value)
  const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`)
  if (!response.ok) throw new Error('Não foi possível consultar o CEP.')
  const body = (await response.json()) as { erro?: boolean; logradouro?: string; bairro?: string; localidade?: string; uf?: string }
  if (body.erro) throw new Error('CEP não encontrado.')
  const address = [body.logradouro, body.bairro].filter(Boolean).join(', ')
  return { address, city: [body.localidade, body.uf].filter(Boolean).join('/') }
}
