package vm.instruccion;

public class InstruccionMUEVE extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8942009055392278070L;
	private int operando;
	
	public InstruccionMUEVE(int operando){
		this.operando = operando;
	}
	
	public void ejecuta(){		
		vm.operando1 = vm.pila.pop();
		vm.operando2 = vm.pila.pop();
		for (int i=0;i<operando;i++)
			vm.memoria[(int) (vm.operando2+i)] = vm.memoria[(int) (vm.operando1+i)];
	}
	
	public String toString(){
		return "MUEVE(" + String.valueOf(operando) + ")";
	}
	
}
