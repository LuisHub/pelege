package vm.instruccion;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InstruccionIN extends Instruccion{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3337187440125428960L;

	@Override
	public void ejecuta(){
		double entrada = 0;
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader (isr);		
		try {
			System.out.print("IN<-:");
			entrada = Double.parseDouble(br.readLine());
		} catch (java.io.IOException e) {
		}
		vm.pila.push(entrada);
	}
	
	@Override
	public String toString(){
		return "IN";
	}
	
}
