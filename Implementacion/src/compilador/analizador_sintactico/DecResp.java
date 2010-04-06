package compilador.analizador_sintactico;

import vm.instruccion.TipoInstruccion;


/* Clase que representa la respuesta del metodo Dec(). Es necesaria ya que Dec() devuelve dos parametros 
 * y no existe en Java el paso de parametros por referencia. 
 */
public class DecResp {
	private String nombre;
	private TipoInstruccion tipo;
	
	public DecResp(String n, TipoInstruccion t){
		nombre = n ;
		tipo = t;
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public TipoInstruccion getTipo(){
		return tipo;
	}
	
}
