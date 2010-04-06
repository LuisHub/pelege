package vm.instruccion;

public class InstruccionCASTINT extends Instruccion {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 424176851695125643L;

	@Override
	public void ejecuta() {
		
		/*�	(int) a devuelve el propio a si es entero, Si a es real el resultado es la parte entera de a. 
		Si a es natural el resultado es a convertido a entero (con signo positivo). Si a es un car�cter el resultado 
		es el c�digo de dicho car�cter convertido a entero.*/
		vm.operando1 = vm.pila.pop();
		vm.operando1=(int)vm.operando1; //los char estar�n convertidos a ascci
		vm.pila.push(vm.operando1);
	}

	@Override
	public String toString() {
		return "CASTINT";
	}

}
