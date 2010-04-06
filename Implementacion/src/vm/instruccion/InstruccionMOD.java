package vm.instruccion;

public class InstruccionMOD extends Instruccion{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5567436769331498697L;

	@Override
	public void ejecuta() {
		
		vm.operando2 = vm.pila.pop();
		vm.operando1 = vm.pila.pop();
		vm.pila.push(vm.operando1 % vm.operando2);
	}

	@Override
	public String toString() {
		return "MODULO";
	}
}
