package com.planejadorviagem.adapter.out.integration;

import com.planejadorviagem.application.port.out.LlmPort;
import com.planejadorviagem.domain.model.GeneratedPlan;

public final class InMemoryLlmAdapter implements LlmPort {

    @Override
    public GeneratedPlan generate(String prompt) {
        return new GeneratedPlan("""
                Roteiro de Viagem
                
                Dia 1: Chegada e reconhecimento da cidade.
                - Check-in no hotel
                - Passeio pelo centro histórico
                - Jantar em restaurante típico
                
                Dia 2: Visita aos pontos turísticos principais.
                - Museu e monumentos pela manhã
                - Almoço em café local
                - Parque ou mirante à tarde
                
                Dia 3: Atividades culturais e gastronomia local.
                - Aula de culinária regional
                - Feira de artesanato
                - Apresentação cultural à noite
                
                Dia 4: Dia livre para exploração.
                - Opção de bate-volta para cidade vizinha
                - Compras e lembranças
                - Spa ou descanso
                
                Dia 5: Retorno.
                - Café da manhã especial
                - Check-out e traslado para o aeroporto
                
                Dicas de segurança:
                - Mantenha documentos no cofre do hotel
                - Evite andar com objetos de valor à mostra
                - Use transporte credenciado
                
                Orçamento estimado por dia: R$ 200 alimentação + R$ 300 hospedagem
                """);
    }
}
