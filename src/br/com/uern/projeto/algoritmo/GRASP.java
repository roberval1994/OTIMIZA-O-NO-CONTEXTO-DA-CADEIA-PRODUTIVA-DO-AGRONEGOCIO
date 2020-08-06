package br.com.uern.projeto.algoritmo;

import java.util.ArrayList;

/**
 * Metaheuristica GRASP Essa metaheuristica funciona em duas etapas
 * indepentendes em cada interação, os resultados de cada etapa não influencia
 * nos resultados de outra etapa, ao fim da execução, conforme o crité- rio de
 * parada pre-estabelecido, o melhor resultado de todas as interações será o
 * exibido.
 * 
 * @author Roberval
 *
 */
public class GRASP {
	// Custo mínimo global
	static int custoMinimo = 0;
	// Melhor rota global
	static int[] melhorRota;
	// Alfa, está definico como constante mas pode ser criado como variável de modo
	// que o grasp passa a ser reativo.
	public static final double ALFA = 0.50;

	/**
	 * Função de avaliação básica Essa função serve para avaliar soluções
	 * incompletas Ela avalia as soluções de caixeiro viajante normal.
	 * 
	 * @param solucao
	 * @param custos
	 * @return
	 */
	static int avaliaSolucao(int[] solucao, double[][] custos) {
		int custoSolucao = 0;

		for (int i = 0; i < solucao.length - 1; i++) {
			custoSolucao += custos[solucao[i]][solucao[i + 1]];
		}
		custoSolucao += custos[solucao[solucao.length - 1]][solucao[0]];

		return custoSolucao;
	}

	/**
	 * Função de avaliação PCVCP Esas função serve para avaliar a solução com base
	 * no problema do caixeiro viajante com coleta de prêmios Essa função também
	 * utiliza as penalidades para avaliar a solução.
	 * 
	 * @param solucao
	 * @param custos
	 * @param penalidades
	 * @return
	 */
	static int avaliaSolucao(int[] solucao, double[][] custos, double[] penalidades) {
		int custoSolucao = 0;
		int[] nSolucao = new int[penalidades.length - solucao.length];

		for (int i = 0; i < solucao.length - 1; i++) {
			custoSolucao += custos[solucao[i]][solucao[i + 1]];
		}

		custoSolucao += custos[solucao[solucao.length - 1]][solucao[0]];
		int aux = 0;
		int cont = 0;
		for (int i = 0; i < penalidades.length; i++) {
			for (int j = 0; j < solucao.length; j++) {
				if (i == solucao[j]) {
					aux = 1;
				}
			}
			if (aux == 0) {
				nSolucao[cont] = i;
				cont++;
			} else {
				aux = 0;
			}
		}

		for (int i : nSolucao) {
			custoSolucao += penalidades[i];
		}

		return custoSolucao;
	}

	/**
	 * Função para geração de número aleatório. Recebe dois números que representam
	 * a faixa de valores Será utilizada para a construção da solução inicial.
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	static int numeroAleatorio(int min, int max) {
		int randomNum = min + (int) (Math.random() * (max - min));

		return randomNum;
	}

	/**
	 * Função para realizar uma busca local Essa função serve para melhorar os
	 * resultados obtidos na etapa da criação da solução inicial, foi feito uma
	 * busca 2opt por ser se fácil implementação de de resultar em resultados
	 * eficientes.
	 * 
	 * @param solucao
	 * @param custos
	 * @return
	 */
	static int[] buscaLocal(int[] solucao, double[][] custos) {
		int auxTroca;
		int custoSolucao = 0;

		int[] auxSolucao = solucao.clone();

		melhorRota = auxSolucao.clone();

		custoSolucao = avaliaSolucao(auxSolucao, custos);

		custoMinimo = custoSolucao;

		for (int i = 0; i < auxSolucao.length; i++) {
			for (int j = i + 1; j < auxSolucao.length; j++) {
				// Troca simples de valores
				auxTroca = auxSolucao[i];
				auxSolucao[i] = auxSolucao[j];
				auxSolucao[j] = auxTroca;

				custoSolucao = 0;

				// Avalia custo da nova solução obtida
				custoSolucao = avaliaSolucao(auxSolucao, custos);

				if (custoSolucao < custoMinimo) {
					custoMinimo = custoSolucao;
					melhorRota = auxSolucao.clone();
				}
				// desfaz a troca para a próxima interação
				auxSolucao[j] = auxSolucao[i];
				auxSolucao[i] = auxTroca;

			}
		}
		return auxSolucao;
	}

	/**
	 * Função para a construção da solução inicial A solução inicial é feita de
	 * forma gulosa e aleatória O parametro alfa estabelecido como constante no
	 * inicio do código define o nível de aleatóriedade, caso o alfa seja maior ou
	 * igual que 1 então o algoritmo será 100% aleatório e se ele for melhor ou
	 * igual a 0 ele será 100% guloso.
	 * 
	 * @param custos
	 * @return
	 */
	@SuppressWarnings("unused")
	public static int[] ConstrucaoSolucaoInicial(double[][] custos, double[] premios) {
		ArrayList<Integer> sequencia = new ArrayList<Integer>();

		double premioMinimo = 0;
		for (int i = 0; i < premios.length; i++) {
			premioMinimo += premios[i];
		}
		premioMinimo = premioMinimo * 0.75;

		ArrayList<Integer> listaCadidatos = new ArrayList<Integer>();
		listaCadidatos.add(0);
		// Ordena vetor (Para uma solucao gulosa)
		double auxm = 99999;
		int auxc = 0;
		int aux = -1;

		while (listaCadidatos.size() < custos.length) {
			for (int i = 0; i < custos.length; i++) {

				if (custos[auxc][i] < auxm && i != auxc && !listaCadidatos.contains(i)) {

					auxm = custos[auxc][i];
					aux = i;
				}

			}
			listaCadidatos.add(aux);
			auxm = 99999;
			auxc = aux;
			aux = -1;
		}

		int tamanhoListaRestritaDeCandidatos;
		if (ALFA <= 0) {
			tamanhoListaRestritaDeCandidatos = 1;
		} else {
			if (ALFA >= 1) {
				tamanhoListaRestritaDeCandidatos = custos.length;
			} else {
				tamanhoListaRestritaDeCandidatos = (int) (1 + ALFA * custos.length);
			}
		}

		double premioTotal = 0;
		while (listaCadidatos.size() > 0) {

			int listaRestritaDeCandidatos[] = new int[tamanhoListaRestritaDeCandidatos];
			int i = numeroAleatorio(0, tamanhoListaRestritaDeCandidatos - 1);

			for (int j = 0; j < tamanhoListaRestritaDeCandidatos; j++) {
				listaRestritaDeCandidatos[j] = listaCadidatos.get(j);

				if (tamanhoListaRestritaDeCandidatos >= listaCadidatos.size()) {
					tamanhoListaRestritaDeCandidatos--;
				}
			}

			premioTotal += premios[listaRestritaDeCandidatos[i]];
			sequencia.add(listaRestritaDeCandidatos[i]);

			listaCadidatos.remove(i);
		}
		int[] sequenciaFinal = new int[sequencia.size()];
		int cont = 0;
		for (int i : sequencia) {
			sequenciaFinal[cont] = i;
			cont++;
		}

		// System.out.println(sequencia.toString());
		return sequenciaFinal;
	}

	/**
	 * Etapa Heuristica para remoção de cidades Essa função funciona como uma nova
	 * etapa do GRASP Com uma solução para o caixeiro viajante formada, essa função
	 * tem por objetivo remover cidades da ro- ta para melhorar o valor da função
	 * objetivo.
	 * 
	 * @param solucao
	 * @param custos
	 * @param penalidades
	 * @param premios
	 * @return
	 */
	public static int[] remocaoCidades(int[] solucao, double[][] custos, double[] penalidades, double[] premios) {
		ArrayList<Integer> solAux = new ArrayList<Integer>();
		for (int i : solucao) {
			solAux.add(i);
		}

		double somatorioPremios = 0;

		for (double i : premios) {

			somatorioPremios += i;

		}

		double premioMinimo = somatorioPremios * 0.75;

		int custoSolucao = avaliaSolucao(solucao, custos, penalidades);
		int cont = 0;

		double custoMudancaPremio = somatorioPremios;
		// Variável para calcular o impacto de qualquer mudança no
		// código com base em uma função de avaliação local

		while (cont < (int) solucao.length / 2) {
			int aux = numeroAleatorio(0, solAux.size());

			double custoDescolamento = 0;

			if (aux > 0) {
				if (aux < solAux.size() - 1) {
					custoDescolamento = custos[solAux.get(aux)][solAux.get(aux + 1)]
							+ custos[solAux.get(aux)][solAux.get(aux - 1)];
				} else {
					custoDescolamento = custos[solAux.get(aux)][solAux.get(0)]
							+ custos[solAux.get(aux)][solAux.get((aux - 1))];
				}
			} else {
				custoDescolamento = custos[solAux.get(aux)][solAux.get(aux + 1)]
						+ custos[solAux.get(aux)][solAux.get(solAux.size() - 1)];
			}

			int custoNovaSolucao = custoSolucao + (int) penalidades[aux] - (int) custoDescolamento;
			custoMudancaPremio -= premios[solAux.get(aux)];

			if (custoMudancaPremio < premioMinimo) {

				cont++;

			} else {
				if (custoNovaSolucao > custoSolucao) {

					cont++;
				} else {
					custoSolucao = custoNovaSolucao;
					solAux.remove(aux);
				}
			}

		}

		int[] solucaoDepoisDaRemocao = new int[solAux.size()];

		int h = 0;
		for (int i : solAux) {
			solucaoDepoisDaRemocao[h] = i;
			h++;
		}

		return solucaoDepoisDaRemocao;
	}

	/**
	 * Função com o objetivo de Atribuir todos os vértices que não foram
	 * selecionados em algum dos vértices selecionados para que sejam criados
	 * clusters e assim todos possam contribuir com seus produtos
	 * 
	 * @param solucao
	 * @param custos
	 * @return
	 */
	static double[][] atribuiNaoSelecionados(int[] solucao, double[][] custos) {
		double[][] atribuicaoDosMaisProximos = new double[custos.length - solucao.length][3];
		ArrayList<Integer> solucaoAux = new ArrayList<Integer>();
		for (int nb : solucao) {
			solucaoAux.add(nb);
		}

		ArrayList<Integer> naoSelecionados = new ArrayList<Integer>();
		for (int i = 0; i < custos.length; i++) {
			naoSelecionados.add(i);
		}

		naoSelecionados.removeAll(solucaoAux);

		// Algoritmo para calcular a menor distâncias dos vertices não selecionados para
		// os vertices que fazem parte da solução final
		int cont = 0;
		for (int nb : naoSelecionados) {
			double menor = 999999;
			int escolha = 9999;
			for (int i = 0; i < solucao.length; i++) {
				if (custos[nb][solucao[i]] < menor) {
					menor = custos[nb][solucao[i]];
					escolha = solucao[i];
				}
			}

			atribuicaoDosMaisProximos[cont][0] = nb;
			atribuicaoDosMaisProximos[cont][1] = escolha;
			atribuicaoDosMaisProximos[cont][2] = menor;

			cont++;
		}

		return atribuicaoDosMaisProximos;

	}

	/**
	 * Algoritmo GRASP O parametro de parada usado foi o número de interações mas
	 * pode ser alterado para um determinado tempo decorrido ou em caso de testes
	 * onde o ótimo global é conhecido, ele para somente quando esse valor for
	 * alcançado porém não há garantias de que ele vai vir.
	 * 
	 * @param solucao
	 * @param custos
	 * @param alfa
	 */
	public static void solucaoGrasp(int[] solucao, double[][] custos, double[] penalidades, double[] premios) {
		int tempo = (int) System.currentTimeMillis();
		int[] solucaoFinal = solucao.clone();

		int[] solucaoPossivel = solucao.clone();
		int tempoPossivelSolucao = 0;
		int tempoSolucaoFinal = 9999999;
		int i = 0;

		while (i < 500000) {

			solucaoPossivel = ConstrucaoSolucaoInicial(custos, premios);

			solucaoPossivel = buscaLocal(solucaoPossivel, custos).clone();

			tempoPossivelSolucao = avaliaSolucao(solucaoPossivel, custos);

			if (tempoPossivelSolucao < tempoSolucaoFinal) {
				tempoSolucaoFinal = tempoPossivelSolucao;
				solucaoFinal = solucaoPossivel.clone();

			}

			i++;
		}
		i = 0;

		tempoSolucaoFinal = avaliaSolucao(solucaoFinal, custos, penalidades);
		int[] auxSoluc = solucaoFinal.clone();
		while (i < 100000) {
			solucaoPossivel = remocaoCidades(auxSoluc, custos, penalidades, premios).clone();

			tempoPossivelSolucao = avaliaSolucao(solucaoPossivel, custos, penalidades);

			if (tempoPossivelSolucao < tempoSolucaoFinal) {
				tempoSolucaoFinal = tempoPossivelSolucao;
				solucaoFinal = solucaoPossivel.clone();

			}

			i++;
		}

		System.out.println("Solucao final: ");
		for (int j = 0; j < solucaoFinal.length; j++) {
			System.out.print(solucaoFinal[j] + 1 + " ");
		}
		System.out.println("\nFuncao Objetivo: " + tempoSolucaoFinal);

		int premioTotalColetado = 0;
		for (int h : solucaoFinal) {
			premioTotalColetado += premios[h];
		}
		//O +1 foi adicionado para tornar a solução mais legível
		System.out.println("Premio total coletado: " + premioTotalColetado);

		System.out.println(
				tempoSolucaoFinal + " " + premioTotalColetado + " " + ((int) System.currentTimeMillis() - tempo));
		//Impressão da solução sem a adição do +1
		for (int gh : solucaoFinal) {
			System.out.println(gh);
		}

		double[][] custonovasolucao = atribuiNaoSelecionados(solucaoFinal, custos).clone();
		for (int k = 0; k < custos.length - solucaoFinal.length; k++) {
			System.out.println("--------------------");
			System.out.println(custonovasolucao[k][0]);
			System.out.println(custonovasolucao[k][1]);
			System.out.println(custonovasolucao[k][2]);
			System.out.println("--------------------");
		}

	}

}
