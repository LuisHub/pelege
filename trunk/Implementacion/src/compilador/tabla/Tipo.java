package compilador.tabla;

import java.util.Vector;

import vm.instruccion.Instruccion;

public class Tipo {

	private ETipo tipo;
	private int tam;
	private String id;
	private int num_elems;
	private Tipo tbase;
	private Vector<Campo> campos;
	private Vector<Parametro> parametros;

	


	public Tipo(ETipo tipo){
		this.tipo = tipo;
	}
	
	public Tipo(ETipo tipo, int tam){
		this.tipo = tipo;
		this.tam = tam;	
	}
	
	public Tipo(ETipo tipo, Vector<Parametro> parametros){
		this.tipo = tipo;
		this.parametros = parametros;		
	}
	
	public Tipo(ETipo tipo, String id, int tam){
		this.tipo = tipo;
		this.id = id;
		this.tam = tam;	
	}
	
	public Tipo(ETipo tipo, Vector<Campo> campos, int tam){
		this.tipo = tipo;
		this.campos = campos;
		this.tam = tam;	
	}
	
	public Tipo(ETipo tipo, int num_elems, Tipo tbase, int tam){
		this.tipo = tipo;
		this.num_elems = num_elems;
		this.tbase = tbase;
		this.tam = tam;	
	}
	
	public Tipo(ETipo tipo, Tipo tbase, int tam){
		this.tipo = tipo;
		this.tbase = tbase;
		this.tam = tam;	
	}
	
	public ETipo getTipo() {
		return tipo;
	}

	public void setTipo(ETipo tipo) {
		this.tipo = tipo;
	}

	public int getTam() {
		return tam;
	}

	public void setTam(int tam) {
		this.tam = tam;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNum_elems() {
		return num_elems;
	}

	public void setNum_elems(int num_elems) {
		this.num_elems = num_elems;
	}

	public Tipo getTbase() {
		return tbase;
	}

	public void setTbase(Tipo tbase) {
		this.tbase = tbase;
	}

	public Vector<Campo> getCampos() {
		return campos;
	}

	public void setCampos(Vector<Campo> campos) {
		this.campos = campos;
	}
	
	public boolean existeCampo(String id){
		boolean encontrado=false;
		int i=0;
		while (encontrado==false && i<campos.size()){
			Campo campo= (Campo) campos.elementAt(i);
			if (campo.getId().equals(id)){
				encontrado=true;
			}
			else
				i++;				
		}
		return encontrado;
	}
	
	public Campo getCampo(String id){
		Campo campo = null;
		boolean encontrado=false;
		int i=0;
		while (encontrado==false && i<campos.size()){
			Campo aux= (Campo) campos.elementAt(i);
			if (aux.getId().equals(id)){
				campo=aux;
				encontrado=true;
			}
			else
				i++;		
		}	
		return campo;
	}

	public Vector<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(Vector<Parametro> parametros) {
		this.parametros = parametros;
	}
	
	
	
}
