package compilador.analizador_sintactico;

/* Clase que representa un error sintáctico.
 */
public class Error extends Throwable {

	private static final long serialVersionUID = -5221865301528156757L;
	private String msg;
	
	public String getMsg() {
		return msg;
	}

	public Error(String s){
		msg = s;
	}
	
}
