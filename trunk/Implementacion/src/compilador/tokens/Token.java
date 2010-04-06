package compilador.tokens;

/* Clase que representa un token que sera¡ generado por AnalizadorLexico.
 */
public class Token {
	
	protected int numLinea;
	protected int numColumna;
	protected TipoToken tipo;
	private String lexema;
	
	
	public Token(int l, int c, TipoToken t){
		numLinea=l;
		numColumna=c;
		tipo=t;
		lexema="";
	}
	
	public Token(int l, int c, String lex, TipoToken t){
		numLinea=l;
		numColumna=c;
		tipo=t;
		lexema=lex;
	}
	
	public int getNumLinea(){
		return numLinea;
	}
	public void setNumLinea(int i){
		numLinea=i;
	}
	
	public int getNumColumna(){
		return numColumna;
	}
	public void setNumColumna(int i){
		numColumna=i;
	}
	
	
	public TipoToken getTipo(){
		return tipo;
	}
	public void setTipo(TipoToken t){
		tipo=t;
	}
	
	public String getLexema(){
		return this.lexema;
	}
	public void setLex(String l){
		this.lexema=l;
	}	
	
	@Override
	public String toString(){
		return "token" + tipo + " '" + lexema + "' (linea: " + (numLinea+1) + " columna: " + numColumna + ")";
	}
}
