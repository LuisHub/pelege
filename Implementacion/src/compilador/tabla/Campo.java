package compilador.tabla;

public class Campo {

	private String id;
	private Tipo tipo;
	private int desp;
	
	public Campo(String id, Tipo tipo, int desp) {
		this.id = id;
		this.tipo = tipo;
		this.desp = desp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public int getDesp() {
		return desp;
	}

	public void setDesp(int desp) {
		this.desp = desp;
	}
	
}
