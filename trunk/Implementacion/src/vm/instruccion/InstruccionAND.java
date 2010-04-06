package vm.instruccion;

import vm.util.Util;

public class InstruccionAND extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4563798038559359457L;

	@Override
	public void ejecuta(){
		vm.operando2 = vm.pila.pop();
		vm.operando1 = vm.pila.pop();
		if (Util.NumToBoolean(vm.operando1) && Util.NumToBoolean(vm.operando2))
			vm.pila.push(1.0);
		else
			vm.pila.push(0.0);
	}
	
	@Override
	public String toString(){
		return "AND";
	}
}
