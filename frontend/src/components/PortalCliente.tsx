import { useState, type FormEvent } from 'react';
import { api } from '../services/api';
import { Search, CheckCircle2 } from 'lucide-react';

export function PortalCliente() {
  const [protocoloBusca, setProtocoloBusca] = useState('');
  const [cpfBusca, setCpfBusca] = useState('');
  const [osPublica, setOsPublica] = useState<any>(null);
  const [msgPublica, setMsgPublica] = useState('');

  async function consultarOsPublica(e: FormEvent) {
    e.preventDefault();
    setMsgPublica('');
    setOsPublica(null);
    try {
      const res = await api.get('/ordens-servico/publico/consulta', {
        params: { protocolo: protocoloBusca.trim(), cpf: cpfBusca.trim() }
      });
      setOsPublica(res.data);
    } catch (err: any) {
      setMsgPublica(err.response?.data?.erro || 'OS não encontrada. Verifique o protocolo e o CPF.');
    }
  }

  async function aprovarOrcamentoCliente() {
    try {
      const res = await api.post('/ordens-servico/publico/aprovar', null, {
        params: { protocolo: protocoloBusca.trim(), cpf: cpfBusca.trim() }
      });
      setOsPublica(res.data);
      setMsgPublica('Orçamento aprovado com sucesso! Nossa equipe iniciará o reparo.');
    } catch (err: any) {
      setMsgPublica(err.response?.data?.erro || 'Erro ao aprovar orçamento.');
    }
  }

  return (
    <div className="bg-slate-900 border border-slate-800 rounded-2xl p-8 shadow-xl">
      <h1 className="text-2xl font-bold mb-2">Acompanhe o Reparo do seu Equipamento</h1>
      <p className="text-slate-400 text-sm mb-6">Informe o protocolo da sua Ordem de Serviço e o CPF cadastrado.</p>

      <form onSubmit={consultarOsPublica} className="grid md:grid-cols-3 gap-4 mb-6">
        <input type="text" placeholder="Protocolo (ex: OS-2026-A1B2C3)" required
          value={protocoloBusca} onChange={(e) => setProtocoloBusca(e.target.value)}
          className="bg-slate-950 border border-slate-700 rounded-lg px-4 py-3 text-sm" />
        <input type="text" placeholder="CPF (11 números)" required maxLength={11}
          value={cpfBusca} onChange={(e) => setCpfBusca(e.target.value)}
          className="bg-slate-950 border border-slate-700 rounded-lg px-4 py-3 text-sm" />
        <button type="submit" className="bg-cyan-500 hover:bg-cyan-400 text-slate-950 font-bold rounded-lg px-6 py-3 flex items-center justify-center gap-2">
          <Search className="w-4 h-4" /> Consultar OS
        </button>
      </form>

      {msgPublica && <div className="p-4 rounded-lg bg-cyan-950/60 border border-cyan-700 text-cyan-200 text-sm mb-6">{msgPublica}</div>}

      {osPublica && (
        <div className="border-t border-slate-800 pt-6 space-y-6">
          <div className="flex flex-wrap justify-between items-start gap-4">
            <div>
              <span className="text-xs uppercase tracking-wider text-cyan-400 font-bold">{osPublica.protocolo}</span>
              <h2 className="text-xl font-bold">{osPublica.equipamentoDescricao}</h2>
              <p className="text-sm text-slate-400">Cliente: {osPublica.clienteNome}</p>
            </div>
            <span className="px-4 py-1.5 rounded-full text-xs font-bold bg-cyan-500/20 text-cyan-300 border border-cyan-500/40">
              {osPublica.status}
            </span>
          </div>

          <div className="grid md:grid-cols-2 gap-4 bg-slate-950 p-4 rounded-xl border border-slate-800 text-sm">
            <div><p className="text-slate-400">Defeito Relatado:</p><p className="font-medium mt-1">{osPublica.defeitoRelatado}</p></div>
            <div><p className="text-slate-400">Diagnóstico Técnico:</p><p className="font-medium mt-1">{osPublica.diagnosticoTecnico || 'Aguardando avaliação'}</p></div>
          </div>

          <div className="bg-slate-950 p-4 rounded-xl border border-slate-800 flex flex-wrap justify-between items-center gap-4">
            <div className="text-sm">
              <p className="text-slate-400">Mão de Obra: <strong className="text-white">R$ {Number(osPublica.valorMaoDeObra).toFixed(2)}</strong> | Peças: <strong className="text-white">R$ {Number(osPublica.valorTotalPecas).toFixed(2)}</strong></p>
              <p className="text-lg font-bold text-emerald-400 mt-1">Total: R$ {Number(osPublica.valorTotal).toFixed(2)}</p>
            </div>
            {osPublica.status === 'AGUARDANDO_APROVACAO' && (
              <button onClick={aprovarOrcamentoCliente} className="bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-bold px-6 py-3 rounded-lg flex items-center gap-2">
                <CheckCircle2 className="w-5 h-5" /> Aprovar Orçamento
              </button>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
