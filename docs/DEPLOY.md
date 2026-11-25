# Deploy de teste (Vercel + Render)

Versão de teste com dados fictícios. Publicar não torna o sistema apto para dados médicos reais. Faltam ASO, PDF, backup clínico, auditoria completa e revisão de segurança para uso real.

O `docker compose up --build` local não muda. Produção usa variáveis diferentes.

## Variáveis

Backend no Render:

| Variável | Uso |
| --- | --- |
| `SPRING_DATASOURCE_URL` | JDBC da URL **interna** do Postgres |
| `SPRING_DATASOURCE_USERNAME` | usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | senha do banco |
| `PORT` | injetada pelo Render; a API escuta nela |
| `JWT_SECRET` | pelo menos 32 caracteres, só no Render |
| `MASTER_USERNAME` | usuário master inicial |
| `MASTER_PASSWORD` | senha master inicial |
| `MASTER_NAME` | nome exibido |
| `CORS_ALLOWED_ORIGINS` | origem HTTPS da Vercel, sem barra no final |

Frontend na Vercel:

| Variável | Uso |
| --- | --- |
| `VITE_API_URL` | URL HTTPS da API, sem barra no final |

O Spring Boot lê `SPRING_DATASOURCE_*` por cima de `DB_URL`/`DB_USER`/`DB_PASSWORD`. O Compose local continua com essas três.

## 1. PostgreSQL no Render

1. Crie um PostgreSQL na mesma região em que o Web Service ficará.
2. No plano gratuito, escolha a região antes de criar. Serviço e banco precisam estar na mesma região e no mesmo workspace para a rede privada.
3. Abra **Connect** e copie a **Internal Database URL**. Não use a External URL na API.
4. Em **Access Control**, remova `0.0.0.0/0`. Isso bloqueia a internet. A URL interna não passa por essa lista.
5. Converta a URL interna para JDBC. De `postgres://USUARIO:SENHA@HOST/BANCO` para `jdbc:postgresql://HOST:5432/BANCO`. A senha vai em `SPRING_DATASOURCE_PASSWORD`, não na URL versionada.

O Flyway roda na subida. Não há comando manual de migration.

## 2. Web Service do backend

1. New → Web Service, repositório deste projeto, ambiente Docker.
2. Dockerfile: `backend/Dockerfile`. Root directory: `backend`, ou contexto de build `backend` se o painel pedir o caminho do Dockerfile.
3. Health check: `/actuator/health`.
4. Defina as variáveis da tabela. Não cole segredo no repositório.
5. O primeiro start cria o usuário master se o username ainda não existir.

A API não grava arquivo em disco. Não crie volume.

## 3. Frontend na Vercel

1. Importe o repositório. Root Directory: `frontend`.
2. Framework: Vite. Build: `npm run build`. Output: `dist`.
3. Defina `VITE_API_URL=https://sua-api.onrender.com` e faça o deploy. A variável entra no build. Trocar a URL exige novo build.
4. Copie o domínio `https://....vercel.app` para `CORS_ALLOWED_ORIGINS` no Render e reinicie a API.

O frontend só chama a API. Não há cliente de banco no React.

## 4. Conferir

1. `https://sua-api.onrender.com/actuator/health` responde `UP`.
2. Login em `https://seu-projeto.vercel.app` com o master.
3. No navegador, a chamada vai para a URL da API, não para localhost.
4. Sem `CORS_ALLOWED_ORIGINS`, o navegador da Vercel é recusado. Isso é intencional.

## 5. Atualizar

Faça push na branch ligada ao Render e à Vercel. Os dois redesenham. O Flyway aplica só migrations novas. Não edite uma migration já aplicada.

## 6. Limites do plano gratuito

- A API gratuita suspende por inatividade. A primeira requisição pode levar cerca de um minuto.
- O Postgres gratuito pode expirar ou ser apagado. Não há backup clínico garantido.
- Disco do Web Service é efêmero. Este backend não depende dele.
- Não use esta hospedagem para dados médicos reais.
