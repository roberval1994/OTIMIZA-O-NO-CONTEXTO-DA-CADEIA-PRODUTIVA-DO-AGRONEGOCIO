package br.com.uern.projeto.leituraArquivos;

public class AuxMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int nCidades = 11;
		
		double[][] custos = Arquivos
				// .lerCustos("C:\\Users\\roberval\\OneDrive\\Mestrado\\Dissertação\\Instancias\\PCV_CP27d.txt",
				// nCidades);
				.lerCustos("D:\\Users\\eclipse-workspace\\Projeto IC\\src\\br\\com\\uern\\projeto\\instancias\\d10.txt",
						nCidades);
		for (int i = 0; i < custos.length; i++) {
			for (int j = 0; j < custos.length; j++) {
				if (i != j && custos[i][j] == 0) {
					custos[i][j] = 99999;
				}
			}
		}
		
		
		for(int i=0; i<nCidades; i++) {
			for(int j=0; j<nCidades; j++) {
				System.out.print(custos[i][j]+" ");
			}System.out.println();
		}
	}

}
