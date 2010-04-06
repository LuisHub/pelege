package vm.instruccion;

public class InstruccionDESPI extends Instruccion {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8451621340402772841L;

	@Override
	public void ejecuta() {
		vm.operando2 = vm.pila.pop();
		vm.operando1 = vm.pila.pop();
		String binario = Integer.toString((int) vm.operando1,2);
		for (int i=0; i<vm.operando2;i++)
			binario=binario+("0");
		vm.pila.push((double)Integer.parseInt(binario,2));
	}

	@Override
	public String toString() {
		return "DESPI";
	}

	
}
