package vm.instruccion;

public class InstruccionRESTA extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5046268607861001658L;

	@Override
	public void ejecuta(){
		vm.operando2 = vm.pila.pop();
		vm.operando1 = vm.pila.pop();
		vm.pila.push(vm.operando1 - vm.operando2);	
	}
	
	@Override
	public String toString(){
		return "RESTA";
	}
	
}
