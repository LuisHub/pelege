package vm.instruccion;

public class InstruccionVABS extends Instruccion{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -759360278782108065L;

	@Override
	public void ejecuta(){
		vm.operando1 = vm.pila.pop();
		vm.pila.push(Math.abs(vm.operando1));
	}
	
	@Override
	public String toString(){
		return "VABS";
	}
}
