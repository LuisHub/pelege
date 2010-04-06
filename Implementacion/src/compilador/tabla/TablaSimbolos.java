package compilador.tabla;

import vm.instruccion.TipoInstruccion;

import java.util.Vector;

import compilador.tokens.TipoToken;

public class TablaSimbolos {
	
	private Vector<EntradaTabla> tabla;
	
	public TablaSimbolos(){
		creaTS();
	}
	
	
	public void creaTS(){
		tabla= new Vector<EntradaTabla>();
	}
	public int anadeId(String n , TipoInstruccion t){			
			tabla.add(new EntradaTabla(n, t, tabla.size()));
			return tabla.size();
	}
	
	public boolean existeId(String nombre){
			boolean encontrado=false;
			int i=0;
			while (encontrado==false && i<tabla.size()){
				EntradaTabla aux= tabla.elementAt(i);
				if (aux.getNombre().equals(nombre)){
					encontrado=true;
				}
				else{
					i++;
				}
			}
			return encontrado;
	}
	
	public TipoInstruccion tipoDe(String nombre){
			TipoInstruccion tipo = null;
			boolean encontrado=false;
			int i=0;
			while (encontrado==false && i<tabla.size()){
				EntradaTabla aux= (EntradaTabla) tabla.elementAt(i);
				if (aux.getNombre().equals(nombre)){
					tipo = aux.getTipo();
					encontrado=true;
				}
				else{
					i++;
				}
			}
			if (encontrado==false){
				tipo= TipoInstruccion.ERROR;
			}	
			return tipo;
	}
	
	public boolean tiposCompat(TipoToken id_tipo, TipoInstruccion tipoExp)
	//Comprueba que la asignación se haga entre variables y expresiones de tipos compatibles.
	{if (id_tipo == TipoToken.FLOAT  && ((tipoExp.toString()==TipoToken.CHARACTER.toString()) || ( tipoExp.toString()==TipoToken.BOOLEAN.toString())))
		return true;
	else if (id_tipo == TipoToken.INTEGER && ((tipoExp.toString()==TipoToken.CHARACTER.toString()) || (tipoExp.toString()==TipoToken.BOOLEAN.toString()) || (tipoExp.toString()==TipoToken.FLOAT.toString())))
		return true;
	else if (id_tipo == TipoToken.NATURAL && (tipoExp.toString()!=TipoToken.NATURAL.toString()))
		return true;
	else if (id_tipo == TipoToken.CHARACTER && (tipoExp.toString()!=TipoToken.CHARACTER.toString()))
		return true;
	else if (id_tipo == TipoToken.BOOLEAN && (tipoExp.toString()!=TipoToken.BOOLEAN.toString()))
		return true;
	else
		return false;
	}

	
	
	public int dameDir(String nombre){
		int dir = 0;
		boolean encontrado=false;
		int i=0;
		while (encontrado==false && i<tabla.size()){
			EntradaTabla aux= tabla.elementAt(i);
			if (aux.getNombre().equals(nombre)){
				dir=aux.getDir();
				encontrado=true;
			}
			else{
				i++;
			}
		}
		if (encontrado==false){
			dir=0;
		}	
		return dir;
	}
}
