package br.com.uern.projeto.leituraArquivos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Classe para a leitura de arquivos, 
 * Esta classe � respons�vel pela leitura de todos os arquivos nas inst�ncias
 * Possui 3 m�todos pois s�o cada inst�ncia possui 3 arquivos individuais.
 * @author Roberval
 *
 */
public class Arquivos {
	
	/**
	 * M�todo para ler a matriz de custos da inst�ncia
	 * Os parametros do m�todo s�o o nome que contem a 
	 * localiza��o do arquivo e o n�mero de cidades.
	 * Os parametros s�o os memos para os demais m�todos
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
			//Erro de leitura do arquivo chama essa exce��o
			System.err.println("Erro: " + e);
		}

		return custos;
	}
	
	/**
	 * M�todo para a leitura das penalidades da inst�ncia
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
	 * M�todo para a leitura dos pr�mios da inst�ncia
	 * @param nome
	 * @param nCidades
	 * @return
	 */
	public static double[] lerPremios(String nome, int nCidades) {
		//Vetor para armazenar os pr�mios
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
