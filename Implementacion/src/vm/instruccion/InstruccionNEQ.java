package vm.instruccion;

public class InstruccionNEQ extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3517642192071204499L;

	@Override
	public void ejecuta(){
		vm.operando2 = vm.pila.pop();
		vm.operando1 = vm.pila.pop();
		if (vm.operando1 != vm.operando2)
			vm.pila.push(1.0);
		else
			vm.pila.push(0.0);
	}
	
	@Override
	public String toString(){
		return "NEQ";
	}
	
}
