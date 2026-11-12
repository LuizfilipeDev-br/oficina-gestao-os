import { useState, type FormEvent } from 'react';
import { api } from './services/api';
import { Cpu, Lock, LogOut } from 'lucide-react';
import { PortalCliente } from './components/PortalCliente';
import { PainelTecnico } from './components/PainelTecnico';

export function App() {
  const [token, setToken] = useState<string | null>(localStorage.getItem('oficina_token'));
  const [userName, setUserName] = useState<string>(localStorage.getItem('oficina_user') || '');
  const [modoPublico, setModoPublico] = useState<boolean>(!localStorage.getItem('oficina_token'));
  const [email, setEmail] = useState('admin@oficina.com');
  const [senha, setSenha] = useState('admin123');
  const [erroLogin, setErroLogin] = useState('');

  async function fazerLogin(e: FormEvent) {
    e.preventDefault();
    setErroLogin('');
    try {
      const res = await api.post('/auth/login', { email, senha });
      localStorage.setItem('oficina_token', res.data.token);
      localStorage.setItem('oficina_user', res.data.nome);
      setToken(res.data.token);
      setUserName(res.data.nome);
      setModoPublico(false);
    } catch {
      setErroLogin('E-mail ou senha inválidos.');
    }
  }

  function sair() {
    localStorage.removeItem('oficina_token');
    localStorage.removeItem('oficina_user');
    setToken(null);
    setModoPublico(true);
  }

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100">
      <header className="bg-slate-900 border-b border-slate-800 px-6 py-4 flex justify-between items-center">
        <div className="flex items-center gap-2 font-bold text-xl text-cyan-400">
          <Cpu className="w-7 h-7" /> TechFix Hardware Lab
        </div>
        {token ? (
          <div className="flex items-center gap-3 text-sm">
            <span className="text-slate-400">Olá, <strong className="text-white">{userName}</strong></span>
            <button onClick={sair} className="bg-red-950/60 text-red-300 px-3 py-1.5 rounded-lg flex items-center gap-1 border border-red-800/50">
              <LogOut className="w-4 h-4" /> Sair
            </button>
          </div>
        ) : (
          <button onClick={() => setModoPublico(!modoPublico)} className="flex items-center gap-2 bg-slate-800 hover:bg-slate-700 px-4 py-2 rounded-lg text-sm font-medium border border-slate-700">
            <Lock className="w-4 h-4 text-cyan-400" />
            {modoPublico ? 'Área do Técnico (Login)' : 'Portal do Cliente'}
          </button>
        )}
      </header>

      <main className="max-w-5xl mx-auto p-6 mt-4">
        {token ? (
          <PainelTecnico onSair={sair} />
        ) : modoPublico ? (
          <PortalCliente />
        ) : (
          <div className="max-w-md mx-auto bg-slate-900 border border-slate-800 rounded-2xl p-8 shadow-xl mt-8">
            <h2 className="text-2xl font-bold mb-1">Acesso Restrito</h2>
            <p className="text-slate-400 text-sm mb-6">Entre com suas credenciais de técnico.</p>
            {erroLogin && <div className="bg-red-950/70 border border-red-700 text-red-200 p-3 rounded-lg text-xs mb-4">{erroLogin}</div>}
            <form onSubmit={fazerLogin} className="space-y-4">
              <input type="email" required value={email} onChange={(e) => setEmail(e.target.value)} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-4 py-2.5 text-sm" />
              <input type="password" required value={senha} onChange={(e) => setSenha(e.target.value)} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-4 py-2.5 text-sm" />
              <button type="submit" className="w-full bg-cyan-500 hover:bg-cyan-400 text-slate-950 font-bold py-3 rounded-lg">Entrar no Painel</button>
            </form>
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
