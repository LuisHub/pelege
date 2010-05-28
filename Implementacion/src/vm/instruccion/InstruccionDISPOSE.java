package vm.instruccion;

public class InstruccionDISPOSE extends Instruccion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5037207498394203382L;
	private int tam;
	private double dirComienzo;
	
	public InstruccionDISPOSE(int tam){
		this.tam=tam;
		this.dirComienzo=dirComienzo;
	}
	@Override
	public void ejecuta() {
		/*DEL(t): Desapila una dirección de comienzo d de la 
		cima de la pila, y libera en el  heap t celdas 
		consecutivas a partir de d.*/
		dirComienzo=vm.pila.pop(); //ya esta en la cima gracias al accesovar
		vm.deleteVM( tam,  dirComienzo);
		//actualizar los otros punteros
		int[] reg;
		reg=vm.getRegistroPuntero();
		for(int i=0;i<reg.length;i++){
			if(reg[i]!=0)
				vm.memoria[i]=reg[i];
		}
	}

	public String toString(){
		return ("DISPOSE "+tam);
	}
}

