package vm.instruccion;

public class InstruccionSUMA extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8386673534952813158L;

	@Override
	public void ejecuta(){
		vm.operando2 = vm.pila.pop();
		vm.operando1 = vm.pila.pop();
		vm.pila.push(vm.operando1 + vm.operando2);	
	}
	
	@Override
	public String toString(){
		return "SUMA";
	}
	
}
