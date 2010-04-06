package vm.instruccion;

public class InstruccionCASTCHAR extends Instruccion{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6162707119674733955L;

	@Override
	public void ejecuta() {
		
		vm.operando1 = vm.pila.pop();
		vm.operando1=(char)vm.operando1;
		vm.pila.push(vm.operando1);
	}

	@Override
	public String toString() {
		return "CASTCHAR";
	}
}
