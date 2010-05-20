package compilador.tabla;

public class RegistroTabla {
		
		private EClase clase;		
		private int dir;
		private Tipo tipo;
		private int nivel;
		private int inicio;
		
		public RegistroTabla(EClase clase, int dir, Tipo tipo, int nivel, int inicio){			
			this.clase=clase;
			this.dir=dir;
			this.tipo=tipo;
			this.nivel=nivel;
			this.inicio=inicio;
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
		
		public int getInicio() {
			return this.inicio;
		}
		
	
		public void setInicio(int ini) {
			this.inicio = ini;
		}
		
		public int getNivel() {
			return nivel;
		}

		public void setNivel(int nivel) {
			this.nivel = nivel;
		}
		
		public String toString(){
			return ", clase:" + clase.toString() + ", dir:" + String.valueOf(dir) + ", nivel:" + String.valueOf(nivel) + ", inicio:" + String.valueOf(inicio) + ">";
		}
		
}
