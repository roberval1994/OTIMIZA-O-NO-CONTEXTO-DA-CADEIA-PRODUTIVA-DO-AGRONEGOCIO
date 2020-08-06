package br.com.uern.projeto;

import br.com.uern.projeto.algoritmo.GRASP;
import br.com.uern.projeto.algoritmo.GRASPv2;
import br.com.uern.projeto.leituraArquivos.Arquivos;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int nCidades = 17;
		int[] solucao = new int[nCidades];
		for (int i = 0; i < solucao.length; i++) {
			solucao[i] = i;
		}
		// Leitura de arquivos via leitura de caminho ABSLUTO, para utilizar, cole
		// o caminho até a instância
		double[][] custos = Arquivos
				// .lerCustos("C:\\Users\\roberval\\OneDrive\\Mestrado\\Dissertação\\Instancias\\PCV_CP27d.txt",
				// nCidades);
				.lerCustos("D:\\Users\\eclipse-workspace\\Projeto IC\\src\\br\\com\\uern\\projeto\\instancias\\d17.txt",
						nCidades);
		for (int i = 0; i < custos.length; i++) {
			for (int j = 0; j < custos.length; j++) {
				if (i != j && custos[i][j] == 0) {
					custos[i][j] = 99999;
				}
			}
		}

		double[] penalidades = Arquivos.// lerPenalidades(
		// "C:\\Users\\roberval\\OneDrive\\Mestrado\\Dissertação\\Instancias\\PCV_CP27p.txt",
		// nCidades);
				lerPenalidades(
						"D:\\Users\\eclipse-workspace\\Projeto IC\\src\\br\\com\\uern\\projeto\\instancias\\p17.txt",
						nCidades);
		double[] premios = Arquivos.// lerPremios(
		// "C:\\Users\\roberval\\OneDrive\\Mestrado\\Dissertação\\Instancias\\PCV_CP27pr.txt",
		// nCidades);
				lerPremios("D:\\Users\\eclipse-workspace\\Projeto IC\\src\\br\\com\\uern\\projeto\\instancias\\w17A.txt",
						nCidades);

		int k = 0;
		/*
		System.out.println("GRASPv01");
		
		while (k < 1) {
			int tempo = (int) System.currentTimeMillis();
			 System.out.println("Solucao " + (k + 1) + ":");
			GRASP.solucaoGrasp(solucao, custos, penalidades, premios);
			k++;
			 System.out.println("Tempo de execucao: " + ((int) System.currentTimeMillis()
			 - tempo) + " Milissegundos");
		}
		System.out.println("------------------------------------------------------------------");
		System.out.println();
*/
		System.out.println("GRASPv02");
		k = 0;
		while (k < 50) {
			int tempo = (int) System.currentTimeMillis();
			 System.out.println("     ->Solucao " + (k + 1) + ":");
			GRASPv2.solucaoGrasp(solucao, custos, penalidades, premios);
			k++;
			 System.out.println("Tempo de execucao: " + ((int) System.currentTimeMillis()
			 - tempo) + " Milissegundos");
			 System.out.println();
		}

	}

}
