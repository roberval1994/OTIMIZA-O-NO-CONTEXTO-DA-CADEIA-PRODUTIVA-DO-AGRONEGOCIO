package br.com.uern.projeto.algoritmo;

import java.util.ArrayList;

/**
 * Metaheuristica GRASP Essa metaheuristica funciona em duas etapas
 * indepentendes em cada intera��o, os resultados de cada etapa n�o influencia
 * nos resultados de outra etapa, ao fim da execu��o, conforme o crit�- rio de
 * parada pre-estabelecido, o melhor resultado de todas as intera��es ser� o
 * exibido.
 * 
 * @author Roberval
 *
 */
public class GRASP {
	// Custo m�nimo global
	static int custoMinimo = 0;
	// Melhor rota global
	static int[] melhorRota;
	// Alfa, est� definico como constante mas pode ser criado como vari�vel de modo
	// que o grasp passa a ser reativo.
	public static final double ALFA = 0.50;

	/**
	 * Fun��o de avalia��o b�sica Essa fun��o serve para avaliar solu��es
	 * incompletas Ela avalia as solu��es de caixeiro viajante normal.
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
	 * Fun��o de avalia��o PCVCP Esas fun��o serve para avaliar a solu��o com base
	 * no problema do caixeiro viajante com coleta de pr�mios Essa fun��o tamb�m
	 * utiliza as penalidades para avaliar a solu��o.
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
	 * Fun��o para gera��o de n�mero aleat�rio. Recebe dois n�meros que representam
	 * a faixa de valores Ser� utilizada para a constru��o da solu��o inicial.
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
	 * Fun��o para realizar uma busca local Essa fun��o serve para melhorar os
	 * resultados obtidos na etapa da cria��o da solu��o inicial, foi feito uma
	 * busca 2opt por ser se f�cil implementa��o de de resultar em resultados
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

				// Avalia custo da nova solu��o obtida
				custoSolucao = avaliaSolucao(auxSolucao, custos);

				if (custoSolucao < custoMinimo) {
					custoMinimo = custoSolucao;
					melhorRota = auxSolucao.clone();
				}
				// desfaz a troca para a pr�xima intera��o
				auxSolucao[j] = auxSolucao[i];
				auxSolucao[i] = auxTroca;

			}
		}
		return auxSolucao;
	}

	/**
	 * Fun��o para a constru��o da solu��o inicial A solu��o inicial � feita de
	 * forma gulosa e aleat�ria O parametro alfa estabelecido como constante no
	 * inicio do c�digo define o n�vel de aleat�riedade, caso o alfa seja maior ou
	 * igual que 1 ent�o o algoritmo ser� 100% aleat�rio e se ele for melhor ou
	 * igual a 0 ele ser� 100% guloso.
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
	 * Etapa Heuristica para remo��o de cidades Essa fun��o funciona como uma nova
	 * etapa do GRASP Com uma solu��o para o caixeiro viajante formada, essa fun��o
	 * tem por objetivo remover cidades da ro- ta para melhorar o valor da fun��o
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
		// Vari�vel para calcular o impacto de qualquer mudan�a no
		// c�digo com base em uma fun��o de avalia��o local

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
	 * Fun��o com o objetivo de Atribuir todos os v�rtices que n�o foram
	 * selecionados em algum dos v�rtices selecionados para que sejam criados
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

		// Algoritmo para calcular a menor dist�ncias dos vertices n�o selecionados para
		// os vertices que fazem parte da solu��o final
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
	 * Algoritmo GRASP O parametro de parada usado foi o n�mero de intera��es mas
	 * pode ser alterado para um determinado tempo decorrido ou em caso de testes
	 * onde o �timo global � conhecido, ele para somente quando esse valor for
	 * alcan�ado por�m n�o h� garantias de que ele vai vir.
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
		//O +1 foi adicionado para tornar a solu��o mais leg�vel
		System.out.println("Premio total coletado: " + premioTotalColetado);

		System.out.println(
				tempoSolucaoFinal + " " + premioTotalColetado + " " + ((int) System.currentTimeMillis() - tempo));
		//Impress�o da solu��o sem a adi��o do +1
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
