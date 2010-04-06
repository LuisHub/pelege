package compilador.tabla;



public class TablaReservadas {

	private String[] lista;
	
	public TablaReservadas(){
			lista = new String[13];
	}
	
	public void anadePalabra(String pal, int i){
			lista[i]=pal;
			
	}
	
	public boolean esReservada(String pal){
			int encontrada=0;
			int i=0;
			while (encontrada==0 && i<lista.length){
					if (pal.equals(lista[i])==true){
						encontrada=1;
					}
					i++;
			}
			if (encontrada==1) return true;
			else return false;
	}
	
	public int dameIndice(String pal){
		boolean encontrada=false;
		int i=0;
		while (encontrada==false){
			if (lista[i].equals(pal)){
				encontrada=true;
			}
			else{
				i++;
			}
		}
		return i;
	}
}
