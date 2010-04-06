package compilador.tabla;

import vm.instruccion.TipoInstruccion;

public class EntradaTabla {

		private String nombre;
		private TipoInstruccion tipo;
		private int dir;
		
		public EntradaTabla(String s, TipoInstruccion t, int d){
			nombre=s;
			tipo=t;
			dir=d;
				
		}
		
		public String getNombre(){
				return nombre;
		}
		
		public TipoInstruccion getTipo(){
			return tipo;
		}
		
		public int getDir(){
			return dir;
		}
		
		public void setNombre(String s){
			nombre=s;
		}
		
		public void setTipo(TipoInstruccion t){
			tipo=t;
		}

		public void setDir(int d){
			dir=d;
		}
}
