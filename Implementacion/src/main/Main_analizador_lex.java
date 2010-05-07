package main;

import compilador.tokens.TipoToken;
import compilador.tokens.Token;
import compilador.analizador_lexico.AnalizadorLexico;

public class Main_analizador_lex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AnalizadorLexico a=new AnalizadorLexico("pruebas/i1.txt");
		
		a.iniciaLexico();
		
		Token auxiliar;
		
		auxiliar=a.sigToken();
		
		
		
		while (auxiliar.getTipo()!=TipoToken.EOF)
		{
			System.out.print(auxiliar.getTipo()+"    ");
			System.out.print(auxiliar.getLexema()+"   ");
			System.out.print("linea: "+auxiliar.getNumLinea()+"    ");
			System.out.println("columna: "+auxiliar.getNumColumna());
			
			auxiliar=a.sigToken();
			
		}

	}

}
