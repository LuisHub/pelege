package vm.instruccion;

public class InstruccionAPILA extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6467508721563613953L;
	private double operando;
	
	public InstruccionAPILA(double d) {
		this.operando = d;
	}

	@Override
	public void ejecuta(){
		vm.pila.push(operando);
	}
	
	@Override
	public String toString(){
		return "APILA("+operando+")";
	}
	
}
