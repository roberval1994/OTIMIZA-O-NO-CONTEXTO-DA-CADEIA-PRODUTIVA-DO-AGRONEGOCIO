package br.com.uern.projeto.leituraArquivos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Classe para a leitura de arquivos, 
 * Esta classe é responsável pela leitura de todos os arquivos nas instâncias
 * Possui 3 métodos pois são cada instância possui 3 arquivos individuais.
 * @author Roberval
 *
 */
public class Arquivos {
	
	/**
	 * Método para ler a matriz de custos da instância
	 * Os parametros do método são o nome que contem a 
	 * localização do arquivo e o número de cidades.
	 * Os parametros são os memos para os demais métodos
	 * @param nome
	 * @param nCidades
	 * @return
	 */
	public static double[][] lerCustos(String nome, int nCidades) {
		//Esse vetor armazena os custos
		double[][] custos = new double[nCidades][nCidades];
		try {
			@SuppressWarnings("resource")
			Scanner s = new Scanner(new File(nome));
			for (int i = 0; i < nCidades; i++) {
				for (int j = 0; j < nCidades; j++) {
					//Leitura de cada elemento
					custos[i][j] = s.nextInt();
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//Erro de leitura do arquivo chama essa exceção
			System.err.println("Erro: " + e);
		}

		return custos;
	}
	
	/**
	 * Método para a leitura das penalidades da instância
	 * @param nome
	 * @param nCidades
	 * @return
	 */
	public static double[] lerPenalidades(String nome, int nCidades) {
		//Vetor de armazenar as penalidades
		double[] penalidades = new double[nCidades];
		try {
			@SuppressWarnings("resource")
			Scanner s = new Scanner(new File(nome));
			for (int i = 0; i < nCidades; i++) {
				penalidades[i] = s.nextInt();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Erro: " + e);
		}

		return penalidades;
	}
	
	/**
	 * Método para a leitura dos prêmios da instância
	 * @param nome
	 * @param nCidades
	 * @return
	 */
	public static double[] lerPremios(String nome, int nCidades) {
		//Vetor para armazenar os prêmios
		double[] premios = new double[nCidades];
		try {
			@SuppressWarnings("resource")
			Scanner s = new Scanner(new File(nome));
			for (int i = 0; i < nCidades; i++) {
				premios[i] = s.nextInt();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Erro: " + e);
			e.getMessage();
		}

		return premios;
	}
}
