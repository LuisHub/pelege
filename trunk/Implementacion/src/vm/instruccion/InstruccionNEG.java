package vm.instruccion;

public class InstruccionNEG extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 130987178391812359L;

	@Override
	public void ejecuta(){		
		vm.operando1 = vm.pila.pop();
		vm.operando1 = -vm.operando1;
		vm.pila.push(vm.operando1);
	}
	
	@Override
	public String toString(){
		return "NEG";
	}
	
}
