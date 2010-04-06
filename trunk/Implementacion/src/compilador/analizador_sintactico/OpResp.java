package compilador.analizador_sintactico;

import vm.instruccion.Instruccion;
import vm.instruccion.TipoInstruccion;

/* Clase que representa la respuesta del método OpAd(), OpComp(), OpMul(), y OpUnario(). Es necesaria 
 * ya que estos métodos devuelven dos parámetros y no existe en Java el paso de parámetros por referencia.
 */
public class OpResp {
	private Instruccion codigo;
	private TipoInstruccion tipo;
	
	public OpResp(Instruccion c, TipoInstruccion t){
		codigo =c ;
		tipo = t;
	}
	
	public Instruccion getCodigo(){
		return codigo;
	}
	
	public TipoInstruccion getTipo(){
		return tipo;
	}
	
}
