package vm.instruccion;


public class InstruccionIRF extends InstruccionConOperando{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3337187440125428960L;
	private int operando;
	
	public void setOperando(int operando) {
		this.operando = operando;
	}
	
	public InstruccionIRF(int operando) {
		this.operando = operando;
	}

	public void ejecuta(){
		vm.operando1 = vm.pila.pop();
		if (vm.operando1 == 0)
			vm.setSig_pc(operando);
	}
	
	public String toString(){
		return "IRF("+operando+")";
	}
	
}
