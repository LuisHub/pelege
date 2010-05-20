package compilador.tabla;

public class Parametro {
	private EModo modo;
	private Tipo tipo;
	private int dir;
	
	public Parametro(EModo modo, Tipo tipo, int dir) {
		super();
		this.modo = modo;
		this.tipo = tipo;
		this.dir = dir;
	}
	public EModo getModo() {
		return modo;
	}
	public void setModo(EModo modo) {
		this.modo = modo;
	}
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	public int getDir() {
		return dir;
	}
	public void setDir(int dir) {
		this.dir = dir;
	}
	
	
	
	
}
