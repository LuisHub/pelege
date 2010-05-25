package vm.instruccion;

public class InstruccionOUT extends InstruccionConOperando{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 7195416841813116170L;

	@Override
	public void ejecuta(){
		System.out.println("OUT->"+vm.pila.pop());
	}
	
	@Override
	public String toString(){
		return "OUT";
	}
	
}
