package vm.instruccion;

import java.io.Serializable;

import vm.maquinavirtual.MaquinaVirtual;


public abstract class Instruccion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2330341964109791801L;
	protected MaquinaVirtual vm;
		
	public void setVm(MaquinaVirtual vm) {
		this.vm = vm;
	}

	public abstract void ejecuta();
	
}
