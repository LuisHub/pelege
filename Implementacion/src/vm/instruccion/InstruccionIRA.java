package vm.instruccion;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InstruccionIRA extends InstruccionConOperando{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3337187440125428960L;
	private int operando;
	
	public void setOperando(int operando) {
		this.operando = operando;
	}

	public InstruccionIRA(int operando) {
		this.operando = operando;
	}

	public void ejecuta(){		
		vm.setSig_pc(operando);
	}
	
	public String toString(){
		return "IRA("+operando+")";
	}
	
}
