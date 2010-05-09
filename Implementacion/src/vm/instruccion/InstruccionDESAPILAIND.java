package vm.instruccion;

public class InstruccionDESAPILAIND extends Instruccion{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8942009055392278070L;

	/*
	 * [subcima, cima] ---->
	 * desapila el valor 'cima' en la direccionn de memoria 'subcima'
	 */
	public void ejecuta(){		
		vm.operando1 = vm.pila.pop();
		vm.operando2 = vm.pila.pop();
		vm.memoria[vm.operando2]=vm.operando1;		
	}
	
	public String toString(){
		return "DESAPILAIND";
	}
	
}
