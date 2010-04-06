package vm.instruccion;

public class InstruccionMULT extends Instruccion{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1006799156833965050L;

	@Override
	public void ejecuta(){
		vm.operando2 = vm.pila.pop();
		vm.operando1 = vm.pila.pop();
		vm.pila.push(vm.operando1 * vm.operando2);
	}
	
	@Override
	public String toString(){
		return "MULT";
	}
	
}
