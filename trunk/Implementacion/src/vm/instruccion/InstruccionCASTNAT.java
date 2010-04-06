package vm.instruccion;

public class InstruccionCASTNAT extends Instruccion{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7619814046149259770L;

	@Override
	public void ejecuta() {
		
		vm.operando1 = vm.pila.pop();
		vm.operando1=(int)vm.operando1;//sólo admite naturales o representaciones de char, luego el signo ya viene quitado
		vm.pila.push(vm.operando1);
	}

	@Override
	public String toString() {
		return "CASTNAT";
	}
}
