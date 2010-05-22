package main;

import compilador.analizador_sintactico.AnalizadorSintactico;



public class Main_Sintactico {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		AnalizadorSintactico a=new AnalizadorSintactico("pruebas/i13.txt");
		
		a.init();
		
		for (int i=0;i<a.get_instrucciones().size();i++)
		{
			System.out.println(a.get_instrucciones().get(i).toString());
		}

	}

}
