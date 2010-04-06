package vm.instruccion;

public class InstruccionNOT extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 130987178391812359L;

	@Override
	public void ejecuta(){		
		vm.operando1 = vm.pila.pop();
		if (vm.operando1 == 0)
			vm.pila.push(1.0);
		else
			vm.pila.push(0.0);
	}
	
	@Override
	public String toString(){
		return "NOT";
	}
	
}
