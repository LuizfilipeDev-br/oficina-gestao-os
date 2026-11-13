# 🖥️ TechFix Hardware Lab — Sistema Full Stack de Gestão para Oficina de Computadores

Sistema completo desenvolvido para gerenciar o fluxo operacional, estoque e financeiro de uma assistência técnica de computadores, aplicando arquitetura moderna de Engenharia de Software e regras reais de manutenção de hardware.

## 🌐 Demonstração Online (Acesse ao Vivo)

- **Sistema Web (Front-end):** oficina-gestao-os.vercel.app
- **Documentação Interativa da API (Swagger):** https://oficina-gestao-os.onrender.com/swagger-ui/index.html

### 🔑 Credenciais de Teste (Já preenchidas na tela de Login)
- **E-mail:** `admin@oficina.com`
- **Senha:** `admin123`

> **⚠️ Aviso sobre o primeiro acesso:** O servidor Back-end está hospedado em instância gratuita no Render e entra em hibernação após 15 minutos de inatividade. Ao clicar em **"Entrar no Painel"** pela primeira vez, aguarde cerca de **30 a 40 segundos** para o servidor inicializar.

---

## 🛠️ Stack Tecnológica

- **Back-end:** Java 21, Spring Boot, Spring Security + JWT (JSON Web Token), Spring Data JPA, Bean Validation, Flyway Migrations e Swagger (OpenAPI).
- **Banco de Dados:** PostgreSQL 16 (Docker Compose em desenvolvimento e banco gerenciado em produção).
- **Front-end:** React, TypeScript, Vite, TailwindCSS, Axios e Lucide Icons.
- **Qualidade e DevOps:** Testes unitários com JUnit 5 e Mockito, containerização com Docker e deploy contínuo (CI/CD) via Render e Vercel.

---

## ✨ Funcionalidades e Regras de Negócio

- **Gestão de Clientes e Equipamentos:** Cadastro de clientes vinculado ao histórico técnico de máquinas (marca, modelo, número de série e especificações de hardware).
- **Fluxo Auditável de Ordem de Serviço (OS):** Geração automática de protocolo único (`OS-2026-XXXXXX`), linha do tempo de alterações de status e geração de comprovante/garantia em PDF.
- **Controle Inteligente de Estoque:** Alerta automático de estoque mínimo no Dashboard e **baixa atômica** ao vincular componentes a uma OS, bloqueando a operação em caso de saldo insuficiente.
- **Cálculo Financeiro e Lucro Líquido Real:** Congelamento do preço de custo e venda no momento do uso da peça para cálculo exato de faturamento e lucro líquido.
- **Portal Público do Cliente:** Consulta sem necessidade de login utilizando apenas **Protocolo + CPF**, permitindo acompanhar o diagnóstico e aprovar orçamentos online.
- **Autenticação e Segurança JWT:** Proteção de rotas administrativas e controle de acesso baseado em tokens.

---

## ⚙️ Como Executar Localmente

### 1. Subir o Banco de Dados (PostgreSQL via Docker)
```bash
docker compose up -d
```

### 2. Iniciar o Back-end (Spring Boot)
```bash
cd backend
./mvnw spring-boot:run
```

### 3. Iniciar o Front-end (React + Vite)
```bash
cd frontend
npm install
npm run dev
```
