package vm.instruccion;

public class InstruccionAPILADIR extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8243517995643915957L;
	private int operando;
	
	public InstruccionAPILADIR(int operando) {
		this.operando = operando;
	}

	@Override
	public void ejecuta(){
		vm.pila.push(vm.memoria[operando]);
	}
	
	@Override
	public String toString(){
		return "APILADIR("+operando+")";
	}
}
