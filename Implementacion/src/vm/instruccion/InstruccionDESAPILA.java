package vm.instruccion;

public class InstruccionDESAPILA extends Instruccion {

	/**
	 * 
	 */
	private static final long serialVersionUID = -743861462445791409L;

	@Override
	public void ejecuta() {
		// TODO Auto-generated method stub
		vm.pila.pop();
		vm.setPc(vm.getPc() + 1);
		//revisar el contador++
	}

	public String toString(){
		return "DESAPILA";
	}
}
