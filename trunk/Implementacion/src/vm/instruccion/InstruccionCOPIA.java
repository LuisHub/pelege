package vm.instruccion;

public class InstruccionCOPIA extends Instruccion {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4742336501896451235L;

	@Override
	public void ejecuta() {
		// TODO Auto-generated method stub
		vm.pila.push(vm.pila.peek());
		vm.pc++;
		//revisar el contador++
	}

	public String toString(){
		return "COPIA";
	}
}
