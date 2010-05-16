package vm.instruccion;

public class InstruccionIRIND extends Instruccion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5848925950722660179L;

	@Override
	public void ejecuta() {
		// TODO Auto-generated method stub
		vm.operando1 = vm.pila.pop();
		vm.setSig_pc((int) vm.operando1);
	}

	public String toString(){
		return "IRIND";
	}
}
