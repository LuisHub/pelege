package vm.instruccion;

public class InstruccionAPILAIND extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8942009055392278070L;

	public void ejecuta(){		
		vm.operando1 = vm.pila.pop();		
		vm.pila.push(vm.memoria[vm.operando1]);		
	}
	
	public String toString(){
		return "APILAIND";
	}
	
}
