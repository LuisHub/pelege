package compilador.tabla;

public class Propiedades {

	private EClase clase;		
	private int dir;
	private Tipo tipo;
	private int nivel;
	private int inicio;
	
	public Propiedades(EClase clase, Tipo tipo, int nivel) {
		this.clase = clase;
		this.dir = Integer.MIN_VALUE;
		this.tipo = tipo;
		this.nivel = nivel;
		this.inicio = Integer.MIN_VALUE;
	}
	
	public Propiedades(EClase clase, int dir, Tipo tipo, int nivel) {
		this.clase = clase;
		this.dir = dir;
		this.tipo = tipo;
		this.nivel = nivel;
		this.inicio = Integer.MIN_VALUE;
	}
	
	public Propiedades(EClase clase, int dir, Tipo tipo, int nivel, int inicio) {
		this.clase = clase;
		this.dir = dir;
		this.tipo = tipo;
		this.nivel = nivel;
		this.inicio = inicio;
	}

	public EClase getClase() {
		return clase;
	}

	public void setClase(EClase clase) {
		this.clase = clase;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getInicio() {
		return inicio;
	}

	public void setInicio(int inicio) {
		this.inicio = inicio;
	}
	
	
	
	
	
}
