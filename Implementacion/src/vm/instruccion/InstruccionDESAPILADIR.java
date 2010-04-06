package vm.instruccion;

public class InstruccionDESAPILADIR extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2133587229031995319L;
	private int operando;
	
	public InstruccionDESAPILADIR(int operando) {
		this.operando = operando;
	}

	@Override
	public void ejecuta(){
		vm.memoria[operando] = vm.pila.pop();
	}
	
	@Override
	public String toString(){
		return "DESAPILADIR("+operando+")";
	}
}
