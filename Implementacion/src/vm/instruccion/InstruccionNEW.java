package vm.instruccion;

public class InstruccionNEW extends Instruccion {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8608533947859137958L;
	//NEW(t):Reserva espacio en el heap para  t celdas 
	//consecutivas y apila en la cima de la pila la direcci�n de comienzo.
	private int tam;
	private int direccionID;
	
	public InstruccionNEW (int tam){
		this.tam=tam;
		this.direccionID=direccionID;
	}
	
	public void ejecuta() {
		//apila la direccion del ID.
		//vm.pila.push((double) direccionID); no lo hago xq ya esta
		
		int direccionReserva;
		direccionReserva=vm.newVM(tam); //deja en la cima, la direccion del bloque reservado
		
		vm.pila.push((double) direccionReserva);
		
		//desapila-ind : guarda en la variable, su direccion que fue devuelta por el new
		vm.operando1 = vm.pila.pop();
		vm.operando2 = vm.pila.pop();
		vm.memoria[(int) vm.operando2]=vm.operando1;
		//limpiar posicion apuntada
		vm.memoria[(int)vm.operando1]=Double.NEGATIVE_INFINITY;
		
		double dirReserva=vm.operando1; //para saber donde se guarda, para actualizar cuando se mueva el heap
		vm.ponRegistroPuntero(dirReserva, vm.operando2);
	}

	public String toString(){
		return ("NEW "+tam);
	}
}
