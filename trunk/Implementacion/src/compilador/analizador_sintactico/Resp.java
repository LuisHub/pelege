package compilador.analizador_sintactico;

import java.util.Vector;

import compilador.tabla.Campo;
import compilador.tabla.EModo;
import compilador.tabla.Parametro;
import compilador.tabla.Propiedades;
import compilador.tabla.Tipo;

import vm.instruccion.Instruccion;


/* Clase que representa la respuesta del métodos que devuelven varios parámetros. Es necesaria 
 * ya que no existe en Java el paso de parámetros por referencia.
 */
public class Resp {
	private Instruccion codigo;
	private Tipo tipo;
	private String id;
	private Propiedades props;
	private Parametro param;
	private Vector<Campo> campos;
	private int tam;
	private EModo modo;
	
	//usado por Atomo(), Fact(), Term(), ExpSimpl() y Exp()
	public Resp(Tipo tipo, EModo modo){
		this.modo = modo;
		this.tipo = tipo;
	}
	
	//usado por Campos()
	public Resp(Vector<Campo> campos, int tam){
		this.campos = campos;
		this.tam = tam;
	}	
	
	//usado por FParam()
	public Resp(String id, Propiedades props, Parametro param) {		
		this.id = id;
		this.props = props;
		this.param = param;
	}
	
	//usado por OpAd(), OpComp(), OpMul(), y OpUnario()
	public Resp(Instruccion c, Tipo t){
		codigo =c ;
		tipo = t;
	}
	
	//usado por Dec()
	public Resp(String id, Propiedades props){
		this.id = id;
		this.props = props;
	}
	
	//usado por Campo()
	public Resp(String id, Tipo tipo){
		this.id = id;
		this.tipo = tipo;
	}

	public Instruccion getCodigo() {
		return codigo;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public String getId() {
		return id;
	}

	public Propiedades getProps() {
		return props;
	}

	public Parametro getParam() {
		return param;
	}

	public Vector<Campo> getCampos() {
		return campos;
	}

	public int getTam() {
		return tam;
	}

	public void setTam(int tam) {
		this.tam = tam;
	}

	public EModo getModo() {
		return modo;
	}
	

	
}
