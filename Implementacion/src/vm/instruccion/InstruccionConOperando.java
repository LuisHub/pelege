package vm.instruccion;

public abstract class InstruccionConOperando extends Instruccion{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3337187440125428960L;
	public void setOperando(int operando) {
	}

	public abstract void ejecuta();	
	
}
