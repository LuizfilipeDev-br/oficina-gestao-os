import { useState, useEffect, type FormEvent } from 'react';
import { api } from '../services/api';
import { Wrench, Users, Package, LayoutDashboard, Plus, AlertTriangle, Printer } from 'lucide-react';

export function PainelTecnico({ onSair }: { onSair: () => void }) {
  const [abaAtiva, setAbaAtiva] = useState<'dashboard' | 'os' | 'clientes' | 'estoque'>('dashboard');
  const [dashboard, setDashboard] = useState<any>(null);
  const [ordens, setOrdens] = useState<any[]>([]);
  const [clientes, setClientes] = useState<any[]>([]);
  const [pecas, setPecas] = useState<any[]>([]);
  const [osImpressao, setOsImpressao] = useState<any>(null);

  const [novoCliente, setNovoCliente] = useState({ nome: '', cpf: '', telefone: '', email: '' });
  const [novoEquip, setNovoEquip] = useState({ clienteId: '', tipo: 'Notebook', marca: '', modelo: '', numeroSerie: '', especificacoesHardware: '' });
  const [novaPeca, setNovaPeca] = useState({ codigoSku: '', nome: '', categoria: 'Armazenamento', quantidadeEstoque: 5, estoqueMinimo: 2, precoCusto: '', precoVenda: '' });
  const [novaOs, setNovaOs] = useState({ equipamentoId: '', defeitoRelatado: '' });
  const [pecaParaOs, setPecaParaOs] = useState<Record<number, { pecaId: string; qtd: number }>>({});
  const [statusParaOs, setStatusParaOs] = useState<Record<number, { status: string; maoDeObra: string; diag: string }>>({});

  useEffect(() => { carregarTudo(); }, []);

  async function carregarTudo() {
    try {
      const [dashRes, osRes, cliRes, pecaRes] = await Promise.all([
        api.get('/dashboard'), api.get('/ordens-servico'), api.get('/clientes'), api.get('/pecas'),
      ]);
      setDashboard(dashRes.data);
      setOrdens(osRes.data);
      setClientes(cliRes.data);
      setPecas(pecaRes.data);
    } catch (err: any) {
      if (err.response?.status === 401 || err.response?.status === 403) onSair();
    }
  }

  async function cadastrarCliente(e: FormEvent) {
    e.preventDefault();
    try {
      await api.post('/clientes', novoCliente);
      setNovoCliente({ nome: '', cpf: '', telefone: '', email: '' });
      carregarTudo();
      alert('Cliente cadastrado!');
    } catch (err: any) { alert(err.response?.data?.erro || 'Erro ao cadastrar.'); }
  }

  async function cadastrarEquipamento(e: FormEvent) {
    e.preventDefault();
    try {
      await api.post('/equipamentos', { ...novoEquip, clienteId: Number(novoEquip.clienteId) });
      setNovoEquip({ clienteId: '', tipo: 'Notebook', marca: '', modelo: '', numeroSerie: '', especificacoesHardware: '' });
      alert('Equipamento vinculado!');
    } catch (err: any) { alert(err.response?.data?.erro || 'Erro ao cadastrar equipamento.'); }
  }

  async function cadastrarPeca(e: FormEvent) {
    e.preventDefault();
    try {
      await api.post('/pecas', {
        ...novaPeca,
        quantidadeEstoque: Number(novaPeca.quantidadeEstoque),
        estoqueMinimo: Number(novaPeca.estoqueMinimo),
        precoCusto: Number(novaPeca.precoCusto),
        precoVenda: Number(novaPeca.precoVenda),
      });
      setNovaPeca({ codigoSku: '', nome: '', categoria: 'Armazenamento', quantidadeEstoque: 5, estoqueMinimo: 2, precoCusto: '', precoVenda: '' });
      carregarTudo();
      alert('Peça cadastrada!');
    } catch (err: any) { alert(err.response?.data?.erro || 'Erro ao cadastrar peça.'); }
  }

  async function abrirNovaOs(e: FormEvent) {
    e.preventDefault();
    try {
      await api.post('/ordens-servico', { equipamentoId: Number(novaOs.equipamentoId), defeitoRelatado: novaOs.defeitoRelatado });
      setNovaOs({ equipamentoId: '', defeitoRelatado: '' });
      carregarTudo();
      alert('OS aberta!');
    } catch (err: any) { alert(err.response?.data?.erro || 'Erro ao abrir OS.'); }
  }

  async function vincularPecaOs(osId: number) {
    const item = pecaParaOs[osId];
    if (!item?.pecaId) return alert('Selecione uma peça.');
    try {
      await api.post(`/ordens-servico/${osId}/pecas`, { pecaId: Number(item.pecaId), quantidade: Number(item.qtd || 1) });
      carregarTudo();
      alert('Peça vinculada e baixada do estoque!');
    } catch (err: any) { alert(err.response?.data?.erro || 'Erro ao adicionar peça.'); }
  }

  async function atualizarStatusOs(osId: number) {
    const dados = statusParaOs[osId];
    if (!dados?.status) return alert('Selecione o status.');
    try {
      await api.patch(`/ordens-servico/${osId}/status`, {
        novoStatus: dados.status,
        valorMaoDeObra: dados.maoDeObra ? Number(dados.maoDeObra) : null,
        diagnosticoTecnico: dados.diag || null,
      });
      carregarTudo();
      alert('Status atualizado!');
    } catch (err: any) { alert(err.response?.data?.erro || 'Erro ao atualizar status.'); }
  }

  if (osImpressao) {
    return (
      <div className="p-8 max-w-2xl mx-auto bg-white text-black rounded-xl">
        <div className="border-b-2 border-black pb-4 mb-6 flex justify-between">
          <div><h1 className="text-2xl font-bold">TECHFIX - ORDEM DE SERVIÇO</h1><p className="text-sm">Protocolo: {osImpressao.protocolo}</p></div>
          <span className="font-bold">{osImpressao.status}</span>
        </div>
        <p><strong>Cliente:</strong> {osImpressao.clienteNome}</p>
        <p><strong>Equipamento:</strong> {osImpressao.equipamentoDescricao}</p>
        <p><strong>Defeito:</strong> {osImpressao.defeitoRelatado}</p>
        <p className="mt-4 font-bold text-lg">Total: R$ {Number(osImpressao.valorTotal).toFixed(2)}</p>
        <div className="mt-6 print:hidden flex gap-3">
          <button onClick={() => window.print()} className="bg-blue-600 text-white px-4 py-2 rounded">Imprimir / Salvar PDF</button>
          <button onClick={() => setOsImpressao(null)} className="bg-gray-300 px-4 py-2 rounded">Voltar</button>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap gap-2 border-b border-slate-800 pb-4">
        <button onClick={() => setAbaAtiva('dashboard')} className={`px-4 py-2 rounded-lg text-sm flex items-center gap-1.5 ${abaAtiva === 'dashboard' ? 'bg-cyan-500 text-slate-950 font-bold' : 'bg-slate-900'}`}>
          <LayoutDashboard className="w-4 h-4" /> Dashboard
        </button>
        <button onClick={() => setAbaAtiva('os')} className={`px-4 py-2 rounded-lg text-sm flex items-center gap-1.5 ${abaAtiva === 'os' ? 'bg-cyan-500 text-slate-950 font-bold' : 'bg-slate-900'}`}>
          <Wrench className="w-4 h-4" /> Ordens de Serviço
        </button>
        <button onClick={() => setAbaAtiva('clientes')} className={`px-4 py-2 rounded-lg text-sm flex items-center gap-1.5 ${abaAtiva === 'clientes' ? 'bg-cyan-500 text-slate-950 font-bold' : 'bg-slate-900'}`}>
          <Users className="w-4 h-4" /> Clientes & Máquinas
        </button>
        <button onClick={() => setAbaAtiva('estoque')} className={`px-4 py-2 rounded-lg text-sm flex items-center gap-1.5 ${abaAtiva === 'estoque' ? 'bg-cyan-500 text-slate-950 font-bold' : 'bg-slate-900'}`}>
          <Package className="w-4 h-4" /> Estoque
        </button>
      </div>

      {abaAtiva === 'dashboard' && dashboard && (
        <div className="space-y-6">
          <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-4">
            <div className="bg-slate-900 border border-slate-800 p-5 rounded-xl"><p className="text-xs text-slate-400">Faturamento</p><p className="text-2xl font-bold text-cyan-400">R$ {Number(dashboard.faturamentoTotalFinalizado).toFixed(2)}</p></div>
            <div className="bg-slate-900 border border-slate-800 p-5 rounded-xl"><p className="text-xs text-slate-400">Lucro Líquido</p><p className="text-2xl font-bold text-emerald-400">R$ {Number(dashboard.lucroLiquidoReal).toFixed(2)}</p></div>
            <div className="bg-slate-900 border border-slate-800 p-5 rounded-xl"><p className="text-xs text-slate-400">OS em Bancada</p><p className="text-2xl font-bold text-amber-400">{dashboard.osAbertasOuEmAndamento}</p></div>
            <div className="bg-slate-900 border border-slate-800 p-5 rounded-xl"><p className="text-xs text-slate-400">OS Concluídas</p><p className="text-2xl font-bold">{dashboard.osFinalizadas}</p></div>
          </div>
          <div className="bg-slate-900 border border-slate-800 p-5 rounded-xl">
            <h2 className="font-bold flex items-center gap-2 text-amber-400 mb-3"><AlertTriangle className="w-5 h-5" /> Alertas de Estoque ({dashboard.alertasEstoqueBaixo?.length || 0})</h2>
            {dashboard.alertasEstoqueBaixo?.map((p: any) => (
              <p key={p.id} className="text-sm text-amber-200">• {p.nome} (SKU: {p.codigoSku}) — Restam {p.quantidadeEstoque} un</p>
            ))}
          </div>
        </div>
      )}

      {abaAtiva === 'os' && (
        <div className="space-y-6">
          <form onSubmit={abrirNovaOs} className="bg-slate-900 border border-slate-800 p-4 rounded-xl flex flex-wrap gap-3">
            <input type="number" required placeholder="ID Equipamento (ex: 1)" value={novaOs.equipamentoId} onChange={(e) => setNovaOs({ ...novaOs, equipamentoId: e.target.value })} className="bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm w-44" />
            <input type="text" required placeholder="Defeito relatado..." value={novaOs.defeitoRelatado} onChange={(e) => setNovaOs({ ...novaOs, defeitoRelatado: e.target.value })} className="flex-1 bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <button type="submit" className="bg-cyan-500 text-slate-950 font-bold px-4 py-2 rounded-lg text-sm flex items-center gap-1"><Plus className="w-4 h-4" /> Abrir OS</button>
          </form>

          {ordens.map((os) => (
            <div key={os.id} className="bg-slate-900 border border-slate-800 p-5 rounded-xl space-y-3">
              <div className="flex justify-between items-start">
                                <div>
                  <span className="font-mono text-xs bg-slate-800 px-2 py-1 rounded text-cyan-400">{os.protocolo}</span>
                  <h3 className="font-bold text-lg mt-1">{os.clienteNome} — {os.equipamentoDescricao}</h3>
                  <p className="text-sm text-slate-400">Defeito: {os.defeitoRelatado}</p>
                </div>
                <div className="text-right">
                  <span className="text-xs font-bold px-2.5 py-1 rounded-full bg-slate-800">{os.status}</span>
                  <p className="font-bold text-cyan-400 mt-2">R$ {Number(os.valorTotal).toFixed(2)}</p>
                  <button onClick={() => setOsImpressao(os)} className="mt-1 text-xs text-slate-400 hover:text-white flex items-center gap-1 ml-auto">
                    <Printer className="w-3.5 h-3.5" /> Imprimir OS
                  </button>
                </div>
              </div>

              <div className="grid md:grid-cols-2 gap-3 pt-3 border-t border-slate-800">
                <div className="flex gap-2">
                  <select
                    value={pecaParaOs[os.id]?.pecaId || ''}
                    onChange={(e) => setPecaParaOs({ ...pecaParaOs, [os.id]: { pecaId: e.target.value, qtd: pecaParaOs[os.id]?.qtd || 1 } })}
                    className="flex-1 bg-slate-950 border border-slate-700 rounded px-2 py-1.5 text-xs"
                  >
                    <option value="">Selecionar peça...</option>
                    {pecas.map((p) => (
                      <option key={p.id} value={p.id}>{p.nome} ({p.quantidadeEstoque} un)</option>
                    ))}
                  </select>
                  <input
                    type="number"
                    min={1}
                    value={pecaParaOs[os.id]?.qtd || 1}
                    onChange={(e) => setPecaParaOs({ ...pecaParaOs, [os.id]: { pecaId: pecaParaOs[os.id]?.pecaId || '', qtd: Number(e.target.value) } })}
                    className="w-16 bg-slate-950 border border-slate-700 rounded px-2 py-1.5 text-xs"
                  />
                  <button onClick={() => vincularPecaOs(os.id)} className="bg-slate-800 hover:bg-slate-700 px-3 py-1.5 rounded text-xs font-semibold">
                    + Peça
                  </button>
                </div>

                <div className="flex gap-2">
                  <select
                    value={statusParaOs[os.id]?.status || ''}
                    onChange={(e) => setStatusParaOs({ ...statusParaOs, [os.id]: { ...statusParaOs[os.id], status: e.target.value } })}
                    className="bg-slate-950 border border-slate-700 rounded px-2 py-1.5 text-xs"
                  >
                    <option value="">Alterar status...</option>
                    <option value="EM_ANALISE">Em Análise</option>
                    <option value="AGUARDANDO_APROVACAO">Aguardando Aprovação</option>
                    <option value="EM_REPARO">Em Reparo</option>
                    <option value="FINALIZADA">Finalizada</option>
                    <option value="ENTREGUE">Entregue</option>
                  </select>
                  <input
                    type="number"
                    placeholder="Mão de obra R$"
                    value={statusParaOs[os.id]?.maoDeObra || ''}
                    onChange={(e) => setStatusParaOs({ ...statusParaOs, [os.id]: { ...statusParaOs[os.id], maoDeObra: e.target.value } })}
                    className="w-28 bg-slate-950 border border-slate-700 rounded px-2 py-1.5 text-xs"
                  />
                  <button onClick={() => atualizarStatusOs(os.id)} className="bg-cyan-500 text-slate-950 font-bold px-3 py-1.5 rounded text-xs">
                    Salvar
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {abaAtiva === 'clientes' && (
        <div className="grid md:grid-cols-2 gap-6">
          <form onSubmit={cadastrarCliente} className="bg-slate-900 border border-slate-800 p-5 rounded-xl space-y-3">
            <h2 className="font-bold text-cyan-400">Novo Cliente</h2>
            <input type="text" required placeholder="Nome completo" value={novoCliente.nome} onChange={(e) => setNovoCliente({ ...novoCliente, nome: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <input type="text" required placeholder="CPF" value={novoCliente.cpf} onChange={(e) => setNovoCliente({ ...novoCliente, cpf: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <input type="text" required placeholder="Telefone" value={novoCliente.telefone} onChange={(e) => setNovoCliente({ ...novoCliente, telefone: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <input type="email" required placeholder="E-mail" value={novoCliente.email} onChange={(e) => setNovoCliente({ ...novoCliente, email: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <button type="submit" className="w-full bg-cyan-500 text-slate-950 font-bold py-2 rounded-lg text-sm">Cadastrar Cliente</button>
          </form>

          <form onSubmit={cadastrarEquipamento} className="bg-slate-900 border border-slate-800 p-5 rounded-xl space-y-3">
            <h2 className="font-bold text-cyan-400">Vincular Equipamento</h2>
            <select required value={novoEquip.clienteId} onChange={(e) => setNovoEquip({ ...novoEquip, clienteId: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm">
              <option value="">Selecione o cliente...</option>
              {clientes.map((c) => (
                <option key={c.id} value={c.id}>{c.nome} ({c.cpf})</option>
              ))}
            </select>
            <input type="text" required placeholder="Marca (ex: Dell, Asus)" value={novoEquip.marca} onChange={(e) => setNovoEquip({ ...novoEquip, marca: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <input type="text" required placeholder="Modelo" value={novoEquip.modelo} onChange={(e) => setNovoEquip({ ...novoEquip, modelo: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <input type="text" placeholder="Número de Série" value={novoEquip.numeroSerie} onChange={(e) => setNovoEquip({ ...novoEquip, numeroSerie: e.target.value })} className="w-full bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <button type="submit" className="w-full bg-cyan-500 text-slate-950 font-bold py-2 rounded-lg text-sm">Salvar Equipamento</button>
          </form>
        </div>
      )}

      {abaAtiva === 'estoque' && (
        <div className="space-y-6">
          <form onSubmit={cadastrarPeca} className="bg-slate-900 border border-slate-800 p-5 rounded-xl grid sm:grid-cols-3 gap-3">
            <input type="text" required placeholder="Código SKU" value={novaPeca.codigoSku} onChange={(e) => setNovaPeca({ ...novaPeca, codigoSku: e.target.value })} className="bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <input type="text" required placeholder="Nome da peça" value={novaPeca.nome} onChange={(e) => setNovaPeca({ ...novaPeca, nome: e.target.value })} className="bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <input type="number" required placeholder="Qtd Estoque" value={novaPeca.quantidadeEstoque} onChange={(e) => setNovaPeca({ ...novaPeca, quantidadeEstoque: Number(e.target.value) })} className="bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <input type="number" required placeholder="Preço Custo" value={novaPeca.precoCusto} onChange={(e) => setNovaPeca({ ...novaPeca, precoCusto: e.target.value })} className="bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <input type="number" required placeholder="Preço Venda" value={novaPeca.precoVenda} onChange={(e) => setNovaPeca({ ...novaPeca, precoVenda: e.target.value })} className="bg-slate-950 border border-slate-700 rounded-lg px-3 py-2 text-sm" />
            <button type="submit" className="bg-cyan-500 text-slate-950 font-bold rounded-lg text-sm">Cadastrar Peça</button>
          </form>

          <div className="bg-slate-900 border border-slate-800 rounded-xl overflow-hidden">
            <table className="w-full text-sm text-left">
              <thead className="bg-slate-950 text-slate-400">
                <tr>
                  <th className="p-3">SKU</th>
                  <th className="p-3">Peça</th>
                  <th className="p-3">Estoque</th>
                  <th className="p-3">Venda</th>
                </tr>
              </thead>
              <tbody>
                {pecas.map((p) => (
                  <tr key={p.id} className="border-t border-slate-800">
                    <td className="p-3 font-mono">{p.codigoSku}</td>
                    <td className="p-3">{p.nome}</td>
                    <td className="p-3">{p.quantidadeEstoque} un</td>
                    <td className="p-3 text-cyan-400">R$ {Number(p.precoVenda).toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}

