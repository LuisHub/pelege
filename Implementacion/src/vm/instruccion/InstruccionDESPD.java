package vm.instruccion;

public class InstruccionDESPD extends Instruccion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7512193229442215084L;

	@Override
	public void ejecuta() {
		vm.operando2 = vm.pila.pop();
		vm.operando1 = vm.pila.pop();
		String binario = Integer.toString((int) vm.operando1,2);
		binario=binario.substring(0, (int) (binario.length()-vm.operando1));
		vm.pila.push((double)Integer.parseInt(binario,2));
	}

	@Override
	public String toString() {
		return "DESPD";
	}

}