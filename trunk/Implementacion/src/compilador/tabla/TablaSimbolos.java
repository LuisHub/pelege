package compilador.tabla;

import java.lang.reflect.Array;
import java.util.Set;
import java.util.TreeMap;

public class TablaSimbolos {
	
	private TreeMap<String, RegistroTabla> tabla;
	private TablaSimbolos tsPadre = null;
	
	public TablaSimbolos(){
		creaTS();
	}
		
	public void creaTS(){
		tabla= new TreeMap<String, RegistroTabla>();
	}
	
	public void creaTS(TablaSimbolos tsPadre){
		tabla= new TreeMap<String, RegistroTabla>();
		this.tsPadre = tsPadre;
	}
	
	public void anadeId(String id , Propiedades props){			
			tabla.put(id, new RegistroTabla(props.getClase(), props.getDir(), props.getTipo(), props.getNivel(), props.getInicio()));			
	}
	
	public boolean existeId(String id){			
		return tabla.containsKey(id) || tsPadre!=null && tsPadre.existeId(id);	
	}
	
	
	public RegistroTabla getRegistroTabla(String id){
		if (!tabla.containsKey(id) && tsPadre!=null)
			return tsPadre.tabla.get(id);
		else
			return tabla.get(id);
	}
	
	public EClase getClase(String id){
		if (!tabla.containsKey(id) && tsPadre!=null)
				return tsPadre.getClase(id);
		else
			return tabla.get(id).getClase();
	}
	
	public int getDir(String id){
		if (!tabla.containsKey(id) && tsPadre!=null)
			return tsPadre.getDir(id);
		else
			return tabla.get(id).getDir();	
	}
	
	public Tipo getTipo(String id){
		if (!tabla.containsKey(id) && tsPadre!=null)
			return tsPadre.getTipo(id);
		else
			return tabla.get(id).getTipo();	
	}
	
	public int getNivel(String id){
		if (!tabla.containsKey(id) && tsPadre!=null)
			return tsPadre.getNivel(id);
		else
			return tabla.get(id).getNivel();	
	}
	
	public int getInicio(String id){
		if (!tabla.containsKey(id) && tsPadre!=null)
			return tsPadre.getInicio(id);
		else
			return tabla.get(id).getInicio();	
	}
	
	public String toString(){		
		String ret = "";
		Object[] id = tabla.keySet().toArray();				
		int i = 0;
		for (RegistroTabla r : tabla.values()){
			ret = ret + "<id:" + id[i] + r.toString() + "\n";
			i++;
		}
		return ret + "-------------------------\n\n";
	}
	
	
}
