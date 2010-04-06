package vm.instruccion;

public class InstruccionCASTFLOAT extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8674673266424075682L;

	@Override
	public void ejecuta() {
		
		vm.operando1 = vm.pila.pop();
		vm.operando1=(float)vm.operando1;
		vm.pila.push(vm.operando1);
	}

	@Override
	public String toString() {
		return "CASTFLOAT";
	}

}
