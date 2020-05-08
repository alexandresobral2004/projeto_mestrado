import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;



/**
 * TODO Put here a description of what this class does.
 *
 * @author alexandre.
 *         Created 18 de mar de 2019.
 */
public final class WriteConsole {
	
	
	private static  WriteConsole instancia;
	 BufferedWriter bw;
	 FileWriter fw ;
	 String [] lista = new String[500];// armazena os resultados pra n repetir
	 int cont = 0;
	

	//singleton
	/*public static synchronized WriteConsole getInstance() {
		if(instancia == null) {
			instancia = new WriteConsole();
		}
		return instancia;
	}*/
	
	
	

	
	/*public void salva() {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream("saida.txt");
			System.setOut(new PrintStream(fileOutputStream, true));
			//System.out.println(texto);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}*/

	
	

	//grava os dados na String
	public boolean gravaString(String texto) {
		
		if(texto != null) {
			
		
		//verifica se n existe igual
		boolean result = buscaString(texto);
		
		if(texto != null && result == true) {
			lista[this.cont++] = texto;
			return true;
		}
		return false;
		}
		return false;
	
	}
	
	//consulta se ja existe uma string igual
	public boolean buscaString(String texto) {
		
		if( texto != null) {
		String temp;
		
			for(int i=1;i < 100;i++) {
				lista[0] = "inicio";
				temp = (lista[i] == null)? "inicio":lista[i];
				String myString1 = new String(temp);
				String myString2 = new String(texto);
				
				
				if(myString1.equals(myString2)) {
					System.out.println("Repetido");
					return false;
					
				}
				
			}
		}
		
		
		return true;
		
	}


	public void salva2(String texto) throws IOException {
		boolean teste = gravaString(texto);
		
		if(teste == true) {
			
		
		 try {
	            // Conteudo
	           

	            // Cria arquivo
	        
	            // Se o arquivo nao existir, ele gera
			 	File file = new File("teste.txt");
	            if (!file.exists()) {
	                file.createNewFile();
	            }

	            // Prepara para escrever no arquivo
	            fw = new FileWriter(file.getAbsoluteFile(),true);
	            bw = new BufferedWriter(fw);
	            if( texto != null) {
	            	   bw.write("\n"+texto);
	            }
	         
	            
	            
	            // PrintWriter pw = new PrintWriter(bw);
	           // Scanner ler = new Scanner(System.in);
	            
		 } finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}
	            
	            // Escreve e fecha arquivo
	            
	            
	           // pw.println(texto);
	            //pw.println();
	            //pw.close();
	           
	            
	            
	            // Le o arquivo
	       /*     FileReader ler = new FileReader("teste.txt");
	            BufferedReader reader = new BufferedReader(ler);  
	            String linha;
	            while( (linha = reader.readLine()) != null ){
	                System.out.println(linha);
	            }*/

		 }
		}
		
	}
	
	
	
	

}
