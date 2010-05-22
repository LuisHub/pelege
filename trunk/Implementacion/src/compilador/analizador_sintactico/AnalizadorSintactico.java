package compilador.analizador_sintactico;

import vm.instruccion.*;

import java.util.StringTokenizer;
import java.util.Vector;

import compilador.analizador_lexico.AnalizadorLexico;
import compilador.tabla.*;
import compilador.tokens.*;

public class AnalizadorSintactico {
	// esto es una prueba para subir contenido al repositorio willy

	private final int valorIndefinido=Integer.MIN_VALUE;
	public TablaSimbolos _ts;
	public AnalizadorLexico _lexico;
	public Vector<Instruccion> _instrucciones;
	public Token _tokenActual;
	public int _nivel;
	public int _dir;
	public int _tamDatosLocales;
	public int _numNiveles;
	public int _etq;
	
	
	private final int longInicio=4;
	private final int longPrologo=13;
	private final int longEpilogo=13;
	private final int longInicioPaso=3;
	private final int longFinPaso=1;
	private final int longApilaRet = 5;
	private final int longPasoParametro= 1;
	private final int longDireccionParFormal = 2;
	
	
	public Vector<String> _pend;

	public AnalizadorSintactico(String e) {

	
		_ts = new TablaSimbolos();
		_lexico= new AnalizadorLexico(e);
		_lexico.iniciaLexico();			
		_instrucciones = new Vector<Instruccion>();
		_tokenActual=_lexico.sigToken();
		_pend = new Vector<String>();
		_numNiveles = 0;
	}

	public boolean init() {
		try {
			programa();
		} catch (Error e) {
			System.out.println(e.getMsg());
			return false;
		}
		return true;
	}

	public void programa() throws Error {
		
		_etq=0;
		_nivel=0;
		inicio(valorIndefinido,valorIndefinido);
		_etq +=  longInicio;
		_ts.creaTS();
		_dir=0;
		_instrucciones.add(new InstruccionIRA(valorIndefinido));
		_etq++;
		Declaraciones();
		
		emparejaToken(TipoToken.PROGRAM);
		_numNiveles++;
		parchea(0, _numNiveles + 1);
		parchea(2, _numNiveles + _tamDatosLocales);
		parchea(4,_etq);
		Sentencias();
		_instrucciones.add(new InstruccionSTOP());
		if (_tokenActual.getTipo()==TipoToken.EOF)
			{
			emparejaToken(TipoToken.EOF);
			System.out.println("Se ha realizado el analisis sintáctico con éxito.");
			}
	}

	// para las declaraciones
	public void Declaraciones() throws Error {
		
		if (_tokenActual.getTipo() != TipoToken.PROGRAM) {

			if (_tokenActual.getTipo() == TipoToken.TYPE) {
				DecsTipo();
			} else if ((_tokenActual.getTipo() == TipoToken.INTEGER)
					|| (_tokenActual.getTipo() == TipoToken.NATURAL)
					|| (_tokenActual.getTipo() == TipoToken.FLOAT)
					|| (_tokenActual.getTipo() == TipoToken.CHARACTER)
					|| (_tokenActual.getTipo() == TipoToken.ID)
					|| (_tokenActual.getTipo() == TipoToken.BOOLEAN)
					|| (_tokenActual.getTipo() == TipoToken.PUNTERO)
					|| (_tokenActual.getTipo() == TipoToken.ARRAY)
					|| (_tokenActual.getTipo() == TipoToken.RECORD)) {
			DecsVar();
			_tamDatosLocales = _dir;
			} else if (_tokenActual.getTipo() == TipoToken.PROCEDURE) {
				DecsProc();
			}
			
			Declaraciones();
		}
		//TODO mirar si quedan atribitos en pendientes y si quedan error
	}
	


///////////////////////////////////////////////////////////////
	
	
	
	public void DecsTipo() throws Error{
					
			emparejaToken(TipoToken.TYPE);
			if (_tokenActual.getTipo()==TipoToken.ID){
				Resp dec = DecTipo();				
				_ts.anadeId(dec.getId(), dec.getProps());
				if (dec.getProps().getClase()==EClase.TIPO)
					_pend.remove(dec.getId());
			}
			
}

	
	
public Resp DecTipo()throws Error{									
	String id = _tokenActual.getLexema();										
	emparejaToken(TipoToken.ID);		
	emparejaToken(TipoToken.IGUAL);								
	Tipo tipo = Tipo1();
	if (!errorEnDeclaracion(id, _nivel, tipo)){
		Propiedades props = new Propiedades(EClase.TIPO, tipo, _nivel);
		emparejaToken( TipoToken.SEPARADOR);
		return new Resp(id, props);	
	}
	else				
		throw new Error("DecTipo: Identificador duplicado o referencia a un identificador que no es de clase TIPO. " + id + " " + _tokenActual.toString() + ")");				
}
	
public Tipo Tipo1() throws Error{			
	Tipo tipo = null;
	if (_tokenActual.getTipo()==TipoToken.INTEGER){				
		tipo = new Tipo(ETipo.INTEGER, 1);
		emparejaToken( TipoToken.INTEGER);
	}
	else if (_tokenActual.getTipo()==TipoToken.BOOLEAN){
		tipo = new Tipo(ETipo.BOOLEAN, 1);
		emparejaToken( TipoToken.BOOLEAN);
	}
	else if (_tokenActual.getTipo()==TipoToken.NATURAL){
		tipo = new Tipo(ETipo.NATURAL, 1);
		emparejaToken( TipoToken.NATURAL);
	}
	else if (_tokenActual.getTipo()==TipoToken.FLOAT){
		tipo = new Tipo(ETipo.FLOAT, 1);
		emparejaToken( TipoToken.FLOAT);
	}
	else if (_tokenActual.getTipo()==TipoToken.CHARACTER){
		tipo = new Tipo(ETipo.CHAR, 1);
		emparejaToken( TipoToken.CHARACTER);
	}
	else if (_tokenActual.getTipo()==TipoToken.BOOLEAN){
		tipo = new Tipo(ETipo.BOOLEAN, 1);
		emparejaToken( TipoToken.BOOLEAN);
	}
	else if (_tokenActual.getTipo()==TipoToken.BOOLEAN){
		tipo = new Tipo(ETipo.BOOLEAN, 1);
		emparejaToken( TipoToken.BOOLEAN);
	}
	else if (_tokenActual.getTipo()==TipoToken.PUNTERO){
		//TODO esto lo tendre que hacer con mas cabeza tendre que poner lo de pendiente aqui
	//	emparejaToken( TipoToken.PUNTERO);
	//	tipo = new Tipo(ETipo.BOOLEAN, 1);
	//	emparejaToken( TipoToken.BOOLEAN);
	}
	else if (_tokenActual.getTipo()==TipoToken.ID){
		String id = _tokenActual.getLexema();
		if (_ts.existeId(id) && _ts.getClase(id)!=EClase.TIPO)
			//TODO && _ts.getClase(id)!=EClase.TIPO) no tiene que existir otro tipo =			
			throw new Error("Tipo: " + id + " no es de clase TIPO ." + _tokenActual.toString() + ")");							
		else{
			
			emparejaToken( TipoToken.ID);
			tipo = new Tipo(ETipo.REF, id, _ts.getTipo(id).getTam());
			//TODO Solo en la declaracion de punteros noÇ?
			//	if (!_ts.existeId(id))
			//	_pend.add(id);
		}
			
								
	}
	else if (_tokenActual.getTipo()==TipoToken.ARRAY){
		emparejaToken( TipoToken.ARRAY);
		emparejaToken( TipoToken.CORAP);
		int n = Integer.parseInt(_tokenActual.getLexema());
		emparejaToken( TipoToken.NATURAL);
		emparejaToken( TipoToken.CORCLA);
		emparejaToken( TipoToken.OF);
		Tipo tipo_aux = Tipo1();
		if (!referenciaErronea(tipo_aux)){
			tipo = new Tipo(ETipo.ARRAY, n, tipo_aux, tipo_aux.getTam() * n);
		}
		else
			throw new Error("Tipo: referencia errónea ya que " + tipo_aux.getId() + "no está definido. "+ _tokenActual.toString() + ")");					
								
	}
	else if (_tokenActual.getTipo()==TipoToken.RECORD){
		emparejaToken( TipoToken.RECORD);
		emparejaToken( TipoToken.LLAVEAP);	
		Resp campos = Campos();
		tipo = new Tipo(ETipo.RECORD, campos.getCampos(), campos.getTam());			
		emparejaToken( TipoToken.LLAVECLA);					
	}		
	else
		throw new Error("Tipo: Se esperaba tokenINTEGER o tokenBOOLEAN o tokenID o tokenARRAY o tokenRECORD y no " + _tokenActual.toString() + ")");	
	return tipo;
}	
	
	
	
public Resp Campos() throws Error{
	
	Resp campo = Campo();
	Campo campo_aux = new Campo(campo.getId(), campo.getTipo(), 0);
	Vector<Campo> vector_aux = new Vector<Campo>();
	vector_aux.add(campo_aux);
	Resp campos_aux = new Resp(vector_aux, campo_aux.getTipo().getTam());
	return RCampos(campos_aux);
}

public Resp Campo() throws Error{
	Tipo tipo = Tipo1();
	String id = _tokenActual.getLexema();
	emparejaToken( TipoToken.ID);
		
	emparejaToken( TipoToken.SEPARADOR);
	return new Resp(id, tipo);		
}


public Resp RCampos(Resp campos) throws Error{
	if (_tokenActual.getTipo()!=TipoToken.LLAVECLA){
		Resp campo = Campo();
		if (!existeCampo(campo.getId(), campos.getCampos())){
			Campo campo_aux = new Campo(campo.getId(), campo.getTipo(), campos.getTam());
			campos.getCampos().add(campo_aux);
			campos.setTam(campos.getTam() + campo.getTipo().getTam());
			return RCampos(campos);
		}
		else
			throw new Error("RCampos: Campo " + campo.getId() + " duplicado. " + _tokenActual.toString() + ")");
	}
	else
		return campos;
}
	
	
public boolean existeCampo(String id, Vector<Campo> campos){
	boolean encontrado=false;
	int i=0;
	while (encontrado==false && i<campos.size()){
		Campo campo= (Campo) campos.elementAt(i);
		if (campo.getId().equals(id)){
			encontrado=true;
		}
		else
			i++;				
	}
	return encontrado;
}	
	
	
	
	
public boolean referenciaErronea(Tipo tipo){
	return tipo.getTipo() == ETipo.REF && !_ts.existeId(tipo.getId());
}	


public boolean errorEnDeclaracion(String id, int nivel, Tipo tipo){
	return (_ts.existeId(id) && _ts.getNivel(id)==nivel || referenciaErronea(tipo));
}	
	
	
public void DecsVar() throws Error{
			Resp dec = DecVar();				
			_ts.anadeId(dec.getId(), dec.getProps());
			
}
		




//_tokenActual -> TokenId
public Resp DecVar()throws Error{	
	Tipo tipo = Tipo1();
	String id = _tokenActual.getLexema();		
	emparejaToken(TipoToken.ID);
	
	
	if (!errorEnDeclaracion(id, _nivel, tipo)){	
		Propiedades props = new Propiedades(EClase.VAR, _dir, tipo, _nivel);															
		_dir = _dir + tipo.getTam();
		emparejaToken( TipoToken.SEPARADOR);
		return new Resp(id, props);		
	}
	else				
		throw new Error("DecVar: Identificador duplicado o referencia a un identificador que no es de clase TIPO. " + id + " " + _tokenActual.toString() + ")");
}


public void DecsProc() throws Error{
	
		Resp decProc = DecProc();
		if (!errorEnDeclaracion(decProc.getId(), _nivel, decProc.getProps().getTipo())){
				_ts.anadeId(decProc.getId(), decProc.getProps());				
			
		}
		else
			throw new Error("DecsProc: Identificador ya definido. " + _tokenActual.toString());
	
}



public Resp DecProc() throws Error{
	int inicio;
	emparejaToken( TipoToken.PROCEDURE);
	String id = _tokenActual.getLexema();
	emparejaToken( TipoToken.ID);
	int _tamDatosLocalesAnt = _tamDatosLocales; 
	TablaSimbolos _tsPadre = _ts;
	_ts = new TablaSimbolos();
	_ts.creaTS(_tsPadre);
	_nivel++;
	if (_numNiveles < _nivel)
		_numNiveles = _nivel;
	_dir=0;
	Vector<Parametro> params = FParams();
	
		
	Propiedades props = new Propiedades(EClase.PROC, new Tipo(ETipo.PROC, params), _nivel);
//	juntar propiedades
	if (!errorEnDeclaracion(id, _nivel, props.getTipo())){
		_ts.anadeId(id, props);
		_ts.getRegistroTabla(id).setNivel(_nivel);
		inicio = Bloque(id);			
		//parchear la dirección del procedimiento en la tabla asociada al procedimiento
		props.setInicio(inicio);			
		_nivel--;
		_ts = _tsPadre;
		_tamDatosLocales = _tamDatosLocalesAnt;
		
		return new Resp(id, props);
	}
	else
		throw new Error("DecProc: Identificador ya definido. " + _tokenActual.toString());
	
}

public Vector<Parametro> FParams() throws Error{
	
		emparejaToken( TipoToken.PARAP);
		if (_tokenActual.getTipo()!=TipoToken.PARCLA)
		{
		Vector<Parametro> params = LFParams();
		emparejaToken( TipoToken.PARCLA);
		return params;
		}
		else{ 
			emparejaToken( TipoToken.PARCLA);
			return new Vector<Parametro>();
		}
		
	
	
}
	
public Vector<Parametro> LFParams() throws Error{
	Resp fParam = FParam();
	Vector<Parametro> params = new Vector<Parametro>();
	params.add(fParam.getParam());
	_ts.anadeId(fParam.getId(), fParam.getProps());
	return RLFParams(params);		
}

public Vector<Parametro> RLFParams(Vector<Parametro> params1) throws Error{
	if (_tokenActual.getTipo()==TipoToken.COMA){
		emparejaToken( TipoToken.COMA);
		Resp fParam = FParam();
		params1.add(fParam.getParam());
		if (!(_ts.existeId(fParam.getId()) && _ts.getNivel(fParam.getId()) == _nivel)){
			_ts.anadeId(fParam.getId(), fParam.getProps());
			return RLFParams(params1);
		}
		else
			throw new Error("DecsProc: Identificador ya definido. " + _tokenActual.toString());			
	}
	else
		return params1;
}

public Resp FParam() throws Error{
	if (_tokenActual.getTipo()==TipoToken.VAR){
		emparejaToken( TipoToken.VAR);
		Tipo tipo = Tipo1();
		String id = _tokenActual.getLexema();
		emparejaToken( TipoToken.ID);
		Propiedades props = new Propiedades(EClase.PVAR, _dir, tipo, _nivel);
		Parametro param = new Parametro(EModo.VARIABLE, tipo, _dir);
		_dir++;
		return new Resp(id, props, param);			
	}
	else 
	{
		
		Tipo tipo = Tipo1();
		String id = _tokenActual.getLexema();
		emparejaToken( TipoToken.ID);
		Propiedades props = new Propiedades(EClase.VAR, _dir,  tipo, _nivel);
		Parametro param = new Parametro(EModo.VALOR, tipo, _dir);
		_dir = _dir + tipo.getTam();
		return new Resp(id, props, param);
	}
}


public int Bloque(String id) throws Error{
	int inicio=0;
	emparejaToken( TipoToken.LLAVEAP);	
	Declaraciones();
	emparejaToken(TipoToken.PROGRAM);
	
	inicio=_etq;
	//parchear la dirección del procedimiento en la tabla asociada al procedimiento para posibles llamadas recursivas
	_ts.getRegistroTabla(id).setInicio(inicio);
	
	prologo(_nivel,_tamDatosLocales);
	_etq += longPrologo;
	
	Sentencias();
	emparejaToken( TipoToken.LLAVECLA);	
	//aqui tengo que parchear seguro con el inicio
	//parcheo +11

	epilogo(_nivel);
	_instrucciones.add(new InstruccionIRIND());
	_etq = _etq + longEpilogo + 1;
	
	return inicio;

}



public void Sentencias() throws Error{		
			
	ListaSents();
}






public void ListaSents() throws Error{		
	if (_tokenActual.getTipo()==TipoToken.ID ||
			_tokenActual.getTipo()==TipoToken.READ ||
			_tokenActual.getTipo()==TipoToken.WRITE ||
			_tokenActual.getTipo()==TipoToken.FOR||
			_tokenActual.getTipo()==TipoToken.IF ||
			_tokenActual.getTipo()==TipoToken.WHILE){

			Instr();
			ListaSents();
		}		
}


//TODO	
public void inicio(int numNiveles, int tamDatosLocales){
		
		_instrucciones.add(new InstruccionAPILA(numNiveles+1));
		_instrucciones.add(new InstruccionDESAPILADIR(1));
		_instrucciones.add(new InstruccionAPILA(1+numNiveles+tamDatosLocales));
		_instrucciones.add(new InstruccionDESAPILADIR(0));
}

public void prologo(int nivel, int tamDatosLocales){
		_instrucciones.add(new InstruccionAPILADIR(0));
		_instrucciones.add(new InstruccionAPILA(2));
		_instrucciones.add(new InstruccionSUMA());
		_instrucciones.add(new InstruccionAPILADIR(1+nivel));
		_instrucciones.add(new InstruccionDESAPILAIND());
		_instrucciones.add(new InstruccionAPILADIR(0));
		_instrucciones.add(new InstruccionAPILA(3));
		_instrucciones.add(new InstruccionSUMA());
		_instrucciones.add(new InstruccionDESAPILADIR(1+nivel));
		_instrucciones.add(new InstruccionAPILADIR(0));
		_instrucciones.add(new InstruccionAPILA(tamDatosLocales+2));
		_instrucciones.add(new InstruccionSUMA());
		_instrucciones.add(new InstruccionDESAPILADIR(0));
}

public void epilogo(int nivel){
	_instrucciones.add(new InstruccionAPILADIR(1+nivel));
	_instrucciones.add(new InstruccionAPILA(2));
	_instrucciones.add(new InstruccionRESTA());
	_instrucciones.add(new InstruccionAPILAIND());
	_instrucciones.add(new InstruccionAPILADIR(1+nivel));
	_instrucciones.add(new InstruccionAPILA(3));
	_instrucciones.add(new InstruccionRESTA());
	_instrucciones.add(new InstruccionCOPIA());
	_instrucciones.add(new InstruccionDESAPILADIR(0));
	_instrucciones.add(new InstruccionAPILA(2));
	_instrucciones.add(new InstruccionSUMA());
	_instrucciones.add(new InstruccionAPILAIND());
	_instrucciones.add(new InstruccionDESAPILADIR(1+nivel));
}

public void inicioPaso(){
	_instrucciones.add(new InstruccionAPILADIR(0));
	_instrucciones.add(new InstruccionAPILA(3));
	_instrucciones.add(new InstruccionSUMA());
}

public void apilaRet(int ret){
	_instrucciones.add(new InstruccionAPILADIR(0));
	_instrucciones.add(new InstruccionAPILA(1));
	_instrucciones.add(new InstruccionSUMA());
	_instrucciones.add(new InstruccionAPILA(ret));
	_instrucciones.add(new InstruccionDESAPILAIND());
}

public void accesoVar(RegistroTabla infoID){
	_instrucciones.add(new InstruccionAPILADIR(1+infoID.getNivel()));
	_instrucciones.add(new InstruccionAPILA(infoID.getDir()));
	_instrucciones.add(new InstruccionSUMA());
	if (infoID.getClase() == EClase.PVAR){
		_instrucciones.add(new InstruccionAPILAIND());
	}
	
}

public int longAccesoVar(RegistroTabla infoID){
	if (infoID.getClase() == EClase.PVAR){
		return 4;
	}
	else{
		return 3;
	}
}

public void direccionParFormal(Parametro pFormal){
	_instrucciones.add(new InstruccionAPILA(pFormal.getDir()));
	_instrucciones.add(new InstruccionSUMA());
}

public void finPaso(){
	_instrucciones.add(new InstruccionDESAPILA());		
}

public void pasoParametro(EModo modoReal,Parametro pFormal){
	if (pFormal.getModo() == EModo.VALOR && modoReal == EModo.VARIABLE){
		_instrucciones.add(new InstruccionMUEVE(pFormal.getTipo().getTam()));
	}
	else{
		_instrucciones.add(new InstruccionDESAPILAIND());
	}
}


public void parchea(int etqInstruccion, int operando){
	InstruccionConOperando i = (InstruccionConOperando) _instrucciones.get(etqInstruccion);
	i.setOperando(operando);				
}


public Tipo Mem(Token tokenid) throws Error{
	String id;
	if (tokenid!=null)
	 id= tokenid.getLexema();
	else {id=this._tokenActual.getLexema();
		emparejaToken(TipoToken.ID);
	}
	Tipo tipo_aux = tipoDeID(id);
	accesoVar(_ts.getRegistroTabla(id));
	_etq+=longAccesoVar(_ts.getRegistroTabla(id));
	return RMem(tipo_aux);		
}

public Tipo RMem(Tipo tipo1) throws Error{
	if (_tokenActual.getTipo()==TipoToken.PUNTO){
		emparejaToken(TipoToken.PUNTO);
		String id = _tokenActual.getLexema();
		emparejaToken(TipoToken.ID);
		Tipo tipo2 = tipoDeCampo(id, tipo1);
		_instrucciones.add(new InstruccionAPILA(tipo1.getCampo(id).getDesp()));
		_instrucciones.add(new InstruccionSUMA());
		_etq = _etq + 2;
		return RMem(tipo2);
	}
	else if (_tokenActual.getTipo()==TipoToken.CORAP){
		emparejaToken(TipoToken.CORAP);
		boolean parh = false;
		Tipo tipo2 = Exp(parh).getTipo();
		emparejaToken(TipoToken.CORCLA);
		Tipo tipo3 = tipoDeTBase(tipo1, tipo2);
		_instrucciones.add(new InstruccionAPILA(tipo1.getTbase().getTam()));
		_instrucciones.add(new InstruccionMULT());
		_instrucciones.add(new InstruccionSUMA());
		_etq = _etq + 3;
		return RMem(tipo3);
	}
	else
		return tipo1;
}


public Tipo tipoDeID(String id) throws Error{
	if (_ts.existeId(id))
		if (_ts.getClase(id) == EClase.VAR || _ts.getClase(id) == EClase.PVAR)
			return referencia(_ts.getTipo(id));
		else
			throw new Error("tipoDeID: Identificador " + id + " no es de tipo VAR. " + _tokenActual.toString());
	else
		throw new Error("tipoDeID: Identificador " + id + " no definido. " + _tokenActual.toString());		
}

public Tipo tipoDeTBase(Tipo tipo1, Tipo tipo2) throws Error{
	if (tipo1.getTipo() == ETipo.ARRAY && tipo2.getTipo() == ETipo.INTEGER)
		return referencia(tipo1.getTbase());
	else
		throw new Error("tipoDeTBase: Identificadores para array no del tipo ARRAY e INTEGER. " + _tokenActual.toString());
}

public Tipo tipoDeCampo(String id, Tipo tipo) throws Error{
	if (tipo.getTipo() == ETipo.RECORD)
		if (existeCampo(id, tipo.getCampos()))
			return referencia(tipo.getCampo(id).getTipo());
		else
			throw new Error("tipoDeCampo: Identificador " + id + " no es un campo del registro. " + _tokenActual.toString());
	else
		throw new Error("tipoDeCampo: Identificador no es de tipo REG. " + _tokenActual.toString());
}


public Tipo referencia(Tipo tipo){
	if (tipo.getTipo()==ETipo.REF)
		if (_ts.existeId(tipo.getId()))
			return referencia(_ts.getTipo(tipo.getId()));
		else
			return new Tipo(ETipo.ERR);
	else
		return tipo;
}


///////////////////////////////////////////////////////////////	
/*
public void RDecs() throws Error {
		if (_tokenActual.getTipo() == TipoToken.ID) {
			Token _tokenTmp = _tokenActual;
			DecResp dr = Dec();
			if (!_ts.existeId(dr.getNombre())) {
				_ts.anadeId(dr.getNombre(), dr.getTipo());
				RDecs();
			} else
				throw new Error("Identificador duplicado. "
						+ _tokenTmp.toString());
		}
	}

	// _tokenActual -> TokenId
	public DecResp Dec() throws Error {
		String lex = _tokenActual.getLexema();
		TipoInstruccion tipo;
		emparejaToken(TipoToken.ID);
		emparejaToken(TipoToken.DOSPUNTOS);
		tipo = Tipo1();
		emparejaToken(TipoToken.SEPARADOR);
		return new DecResp(lex, tipo);
	}
/*
	public TipoInstruccion Tipo() throws Error {
		TipoInstruccion tipo = null;
		if (_tokenActual.getTipo() == TipoToken.INTEGER) {
			tipo = TipoInstruccion.INTEGER;
			emparejaToken(TipoToken.INTEGER);
		} else if (_tokenActual.getTipo() == TipoToken.BOOLEAN) {
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.BOOLEAN);
		} else if (_tokenActual.getTipo() == TipoToken.NATURAL) {
			tipo = TipoInstruccion.NATURAL;
			emparejaToken(TipoToken.NATURAL);
		} else if (_tokenActual.getTipo() == TipoToken.FLOAT) {
			tipo = TipoInstruccion.FLOAT;
			emparejaToken(TipoToken.FLOAT);
		} else if (_tokenActual.getTipo() == TipoToken.CHARACTER) {
			tipo = TipoInstruccion.CHARACTER;
			emparejaToken(TipoToken.CHARACTER);
		} else
			throw new Error(
					"Tipo: Se esperaba una palabra reservada INTEGER o FLOAT o BOOLEAN o CHAR y no "
							+ _tokenActual.toString() + ")");

		return tipo;
	}

	public void Sents() throws Error {
		Instr();
		RSents();
	}

	public void RSents() throws Error {
		if (_tokenActual.getTipo() == TipoToken.ID
				|| _tokenActual.getTipo() == TipoToken.READ
				|| _tokenActual.getTipo() == TipoToken.WRITE) {
			Instr();
			RSents();
		}
	}
*/


	public void Instr() throws Error {
		
		
		
		if (_tokenActual.getTipo()==TipoToken.ID)
			InstrId();
			
		else if (_tokenActual.getTipo()==TipoToken.READ)		
			InstrLect();
		else if (_tokenActual.getTipo()==TipoToken.WRITE)		
			InstrEsc();
		/*
		else if (_tokenActual.getTipo()==TipoToken.IF)		
			InstrIf();
		else if (_tokenActual.getTipo()==TipoToken.WHILE)		
			InstrWhile();
		else if (_tokenActual.getTipo()==TipoToken.FOR)		
			InstrFor();
		else if (_tokenActual.getTipo()==TipoToken.NEW)		
			InstrNew();
		else if (_tokenActual.getTipo()==TipoToken.DISPOSE)		
			InstrDis();
			*/
		else
			throw new Error(
					"Instr: Se esperaba tokenID, tokenREAD o tokenWRITE. "
							+ _tokenActual.toString());
	}

	public void InstrIf() throws Error{		
		emparejaToken(TipoToken.IF);
		boolean parh=false;
		Tipo tipo = Exp(parh).getTipo();
		if (tipo.getTipo() == ETipo.BOOLEAN){
			emparejaToken(TipoToken.THEN);
			int etq1 = _etq;	
			_instrucciones.add(new InstruccionIRF(valorIndefinido));
			_etq++;
			Instr();
			int etq2 = _etq;
			_instrucciones.add(new InstruccionIRA(valorIndefinido));
			_etq++;
			parchea(etq1, _etq);
			PElse();
			parchea(etq2, _etq);
		}
		else
			throw new Error("InstrIf: El tipo de la expresión no es BOOLEAN.");	
	}
	
	public void PElse() throws Error{
		if (_tokenActual.getTipo()==TipoToken.ELSE){
			emparejaToken(TipoToken.ELSE);
			Instr();	
		}
	}
	
	public void InstrWhile() throws Error{		
		emparejaToken(TipoToken.WHILE);
		int etq1 = _etq;
		boolean parh=false;
		Tipo tipo = Exp(parh).getTipo();
		if (tipo.getTipo() == ETipo.BOOLEAN){
			//emparejaToken(EToken.DO);			
			_instrucciones.add(new InstruccionIRF(valorIndefinido));
			int etq2 = _etq;
			_etq++;
			Instr();			
			_instrucciones.add(new InstruccionIRA(etq1));
			_etq++;
			parchea(etq2, _etq);
		}
		else
			throw new Error("InstrWhile: El tipo de la expresión no es BOOLEAN.");	
	}

	public void InstrId() throws Error {
		Token tokenid= new Token(_tokenActual.getNumLinea(),_tokenActual.getNumColumna(),_tokenActual.getLexema(),_tokenActual.getTipo());
		emparejaToken(TipoToken.ID);
		if (_tokenActual.getTipo()==TipoToken.ASIG ||
				_tokenActual.getTipo()==TipoToken.PUNTO ||
				_tokenActual.getTipo()==TipoToken.CORAP)
				InstrAsig(tokenid);
			else
				InstrCall(tokenid);
	
}
	
	public void InstrCall(Token tokenid) throws Error{
		int etq2;
		String id = tokenid.getLexema();
		emparejaToken(TipoToken.ID);
		if (_ts.existeId(id) && _ts.getClase(id) == EClase.PROC){
			apilaRet(valorIndefinido);
			etq2 = _etq + 3;		
			_etq+= longApilaRet;
			RParams(_ts.getTipo(id).getParametros());
			_instrucciones.add(new InstruccionIRA(_ts.getInicio(id)));
			_etq++;
			parchea(etq2,_etq);
			emparejaToken(TipoToken.SEPARADOR);
		}
		else throw new Error("InstrCall: Identificador '" + id + "' no definido o no de clase 'proc'.");	

	}
	
	public void RParams(Vector <Parametro> params) throws Error{
		if (_tokenActual.getTipo()==TipoToken.PARAP){
			emparejaToken( TipoToken.PARAP);
			inicioPaso();
			_etq += longInicioPaso;
			int nparams = LRParams(params);
			if (params.size() == nparams){
				finPaso();
				_etq += longFinPaso;
				emparejaToken( TipoToken.PARCLA);
			}
			else throw new Error("RParams: Número de parámetros reales(" + String.valueOf(nparams) + ") no corresponde con número de parámetros formales (" + String.valueOf(params.size()) + ")");
		}
		else
			if (params.size() != 0) throw new Error("RParams: Número de parámetros reales no corresponde con número de parámetros formales (" + String.valueOf(params.size()) + ")");
	}
	
	public int LRParams(Vector <Parametro> params) throws Error{
		_instrucciones.add(new InstruccionCOPIA());
		_etq++;
		boolean parh = true;
		Resp resp = Exp(parh);
		pasoParametro(resp.getModo(),params.get(0));
		_etq = _etq + longPasoParametro;
		//TODO compatibilidad
		/*
		if (params.size() == 0 || 
			params.get(0).getModo() == EModo.VARIABLE && resp.getModo() == EModo.VALOR 
			|| !compatibles(params.get(0).getTipo(), resp.getTipo()))
			throw new Error("LRParams: ERROR");
		else
			return RLRParams(params, 1);
			*/
		return 0;
		}

	
	public int RLRParams(Vector <Parametro> params, int nparams) throws Error{
		if (_tokenActual.getTipo() == TipoToken.COMA){
			emparejaToken( TipoToken.COMA);
			_instrucciones.add(new InstruccionCOPIA());
			direccionParFormal(params.get(nparams));
			_etq = _etq + longDireccionParFormal + 1;
			boolean parh = true;
			Resp resp = Exp(parh);
			pasoParametro(resp.getModo(), params.get(nparams));
			_etq += longPasoParametro;
			
			//TODO compatibilidad
			/*
			if (nparams > params.size() ||
			params.get(nparams).getModo() == EModo.VARIABLE && resp.getModo() == EModo.VALOR 
			|| !compatibles(params.get(nparams).getTipo(), resp.getTipo()))
				throw new Error("RLRParams: ERROR");
			else	
			return RLRParams(params, nparams+1);
		*/
			return 0;
		}
		else{
			return nparams;
		}
		
	}


	// _tokenpasado -> tokenId
	public void InstrAsig(Token tokenid) throws Error {
		
		Tipo tipo1 = Mem(tokenid);
		emparejaToken(TipoToken.ASIG);
		boolean parh=false;
		Tipo tipo2 = Exp(parh).getTipo();
		emparejaToken(TipoToken.SEPARADOR);
		
		//fallaasig() es lo que tenia antes
		//TODO tengo que hacerme la compatibilidad
		/*
		if (compatibles(tipo1,tipo2)){
			if (compatibles(tipo1, new Tipo(ETipo.INTEGER)) || 
				compatibles(tipo1, new Tipo(ETipo.BOOLEAN)))
				_instrucciones.add(new InstruccionDESAPILAIND());				
			else
				_instrucciones.add(new InstruccionMUEVE(tipo1.getTam()));
			_etq++;
			emparejaToken(EToken.SEPARADOR);
		}
		else
			throw new Error("InstrAsig: Tipos no compatibles " + _tokenActual.toString());				
	*/
		
		

	}

	// _tokenActual -> TokenREAD
	public void InstrLect() throws Error {
		
		
		emparejaToken(TipoToken.READ);
		emparejaToken(TipoToken.PARAP);
		Tipo tipo = Mem(null);
		_instrucciones.add(new InstruccionIN());
		_etq++;
		if (tipo.getTipo()!=ETipo.ERR){
			_instrucciones.add(new InstruccionDESAPILAIND());
			_etq++;
			emparejaToken(TipoToken.PARCLA);
			emparejaToken(TipoToken.SEPARADOR);
		}			
		else
			throw new Error("InstrLect: Tipo de Mem = ERR " + _tokenActual.toString());
	
		
/*		
		if (_tokenActual.getTipo() == TipoToken.ID) {
			String lex = _tokenActual.getLexema();
			if (!_ts.existeId(lex))
				throw new Error("InstrLect: Identificador no decladaro. "
						+ _tokenActual.toString());
			emparejaToken(TipoToken.ID);
			emparejaToken(TipoToken.PARCLA);
			emparejaToken(TipoToken.SEPARADOR);
			_instrucciones.add(new InstruccionIN());
			_instrucciones.add(new InstruccionDESAPILADIR(_ts.dameDir(lex)));
		} else
			throw new Error("InstrLect: Se esperaba tokenID y no "
					+ _tokenActual.toString());
*/
	}

	// _tokenActual -> TokenWRITE
	public void InstrEsc() throws Error {
		emparejaToken(TipoToken.WRITE);
		emparejaToken(TipoToken.PARAP);
		Token tokenTmp = _tokenActual;
		boolean parh = false;
		Tipo tipo = Exp(parh).getTipo();
		if (tipo.getTipo() != ETipo.ERR) {
			emparejaToken(TipoToken.PARCLA);
			emparejaToken(TipoToken.SEPARADOR);
			_instrucciones.add(new InstruccionOUT());
		} else
			throw new Error(
					"InstrEsc: El tipo de la expresión entre paréntesis es ERROR. "
							+ tokenTmp.toString());
	}
	
	
	public Resp Exp(boolean parhin) throws Error{
		Resp tipoModo;
		Resp expSimple = ExpSimple(parhin);
		tipoModo = RExp(expSimple.getTipo(), expSimple.getModo()); 
		return tipoModo;
	}



	public Resp RExp(Tipo tipo1, EModo modo1) throws Error {
		Resp tipo;
		Resp tipo2;
		if (_tokenActual.getTipo() == TipoToken.MENOR
				|| _tokenActual.getTipo() == TipoToken.MAYOR
				|| _tokenActual.getTipo() == TipoToken.IGUAL
				|| _tokenActual.getTipo() == TipoToken.DISTINTO
				|| _tokenActual.getTipo() == TipoToken.MENORIGUAL
				|| _tokenActual.getTipo() == TipoToken.MAYORIGUAL) {

			//TODO resp lo tengo que asociar con algo
			Resp resp = OpComp();
			tipo2 = ExpSimple(false);

			tipo= new Resp(tipoDeExpComp(tipo1, tipo2.getTipo()), EModo.VALOR);;

			_instrucciones.add(resp.getCodigo());
		} else {
			tipo = new Resp(tipo1, modo1);
		}
		return tipo;
	}

	public Resp ExpSimple(boolean parhin) throws Error{
		Resp tipoModo;
		Resp term = Term(parhin);
		tipoModo = RExpSimple(term.getTipo(), term.getModo());//equivale a RExpSimple(tipo1);
		return tipoModo;
	}
	


	public Resp RExpSimple(Tipo tipo1, EModo modo1) throws Error {
		Resp tipo;
		Resp tipo2;
		if (_tokenActual.getTipo() == TipoToken.SUMA
				|| _tokenActual.getTipo() == TipoToken.RESTA
				|| _tokenActual.getTipo() == TipoToken.OR) {

			boolean parche=false;
			//TODO resp lo tengo que asociar con algo
			Resp resp = OpAd();
			tipo2 = Term(parche);
			//TODO ver el tipo final que sale
			//tipo2 = tipoDeExpBin(resp.getTipo(), tipo1, tipo2);

			_instrucciones.add(resp.getCodigo());

			tipo = RExpSimple(tipo2.getTipo(),  tipo2.getModo());

		} else {
			tipo = new Resp(tipo1, modo1);
		}
		return tipo;
	}
	


	public Resp Term(boolean parhin) throws Error {
		//TODO tengo que hacer el desplazamiento
		Resp resp;
		resp = despl(parhin);
		resp = RTerm(resp.getTipo(),resp.getModo());
		return resp;
	}

	public Resp RTerm(Tipo tipo1, EModo modo1) throws Error {
		Resp tipo;
		Resp tipo2;
		if (_tokenActual.getTipo() == TipoToken.MULT
				|| _tokenActual.getTipo() == TipoToken.DIV
				|| _tokenActual.getTipo() == TipoToken.AND) {

			Resp resp = OpMul();
			boolean parhc=false;
			tipo2 = despl(parhc);
			Tipo tipofinal=	tipoDeExpBin(resp.getTipo(),tipo1, tipo2.getTipo());
			tipo = new Resp (tipofinal, EModo.VALOR);

			_instrucciones.add(resp.getCodigo());
			_etq++;

			tipo =RTerm(tipo.getTipo(), tipo.getModo());
		} else {
			tipo = new Resp(tipo1, modo1);
		}
		return tipo;
	}

	// voy a poner el modulo y el desplazamiento en la misma produccion

	public Resp despl(boolean parchin) throws Error {
		
				
		Resp tipofinal;
		Resp tipo2;
		// tipo2= this.OpcionA();
		// tipo= Rdespl(tipo2);
		
		tipo2 = Fact(parchin);
		tipofinal = Rdespl(tipo2.getTipo(),tipo2.getModo());
		return tipofinal;

	}

	public Resp Rdespl(Tipo tipo1, EModo modo1) throws Error {

		Resp respfinal;
		Resp tipo2;
		Tipo tipo;

		if (_tokenActual.getTipo() == TipoToken.DESPDER
				|| _tokenActual.getTipo() == TipoToken.DESPIZQ
				|| _tokenActual.getTipo() == TipoToken.PORCEN) {

			Resp aux = opdesp();
			// tipo2 = OpcionA();
			boolean parch=false;
			tipo2 = Fact(parch);
			tipo = tipoDeDespl(tipo1, tipo2.getTipo(), aux.getTipo());

			// _instrucciones.add(aux.getCodigo());


			respfinal = Rdespl(tipo,modo1);

			// creo que la recursion a derechas es solo poner instrucion detras
			// de esto
			_instrucciones.add(aux.getCodigo());
		} else {
			respfinal = new Resp(tipo1, modo1);
		}
		return respfinal;
	}



	private Resp opdesp() throws Error {


		Instruccion codigo = null;
		Tipo tipo = null;
		if (_tokenActual.getTipo() == TipoToken.DESPIZQ) {
			codigo = new InstruccionDESPI();
			tipo =new Tipo(ETipo.DESP);
			emparejaToken(TipoToken.DESPIZQ);
		} else if (_tokenActual.getTipo() == TipoToken.DESPDER) {
			codigo = new InstruccionDESPD();
			tipo =new Tipo(ETipo.DESP);
			emparejaToken(TipoToken.DESPDER);
		}

		return new Resp(codigo, tipo);
	}
	
	

	public Resp Fact(boolean parhin) throws Error {
		Resp tipoModo;
		Resp fact;
		boolean parh;
		if (_tokenActual.getTipo() == TipoToken.NOT
				|| _tokenActual.getTipo() == TipoToken.CASTINGNAT
				|| _tokenActual.getTipo() == TipoToken.RESTA
				|| _tokenActual.getTipo() == TipoToken.CASTINGCHAR
				|| _tokenActual.getTipo() == TipoToken.CASTINGFLOAT
				|| _tokenActual.getTipo() == TipoToken.CASTINGINT) {
			
			Resp opUnario = OpUnario();
			parh=false;
			fact=Fact(parh);
			tipoModo = new Resp(tipoDeFact(opUnario.getTipo(),fact.getTipo()), EModo.VALOR);						
			_instrucciones.add(opUnario.getCodigo());
			_etq++;	
			//TODO esto lo tengo que ver bien xq no se bien como va
			/*
			
			OpResp resp = OpUnario();
			tipo1 = Fact();
			tipo = tipoDeFact(resp.getTipo(), tipo1);
			_instrucciones.add(resp.getCodigo());
			*/
			
		} else {
			tipoModo = Atomo(parhin);
		}
		return tipoModo;
	}
	
	

	
	

	public Resp Atomo(boolean parhin) throws Error {
		Resp tipo = null;

		if (_tokenActual.getTipo() == TipoToken.INTEGER) {
			_instrucciones.add(new InstruccionAPILA(Double
					.parseDouble(_tokenActual.getLexema())));
			
			tipo = new Resp(new Tipo(ETipo.INTEGER), EModo.VALOR);
			_etq++;
			emparejaToken(TipoToken.INTEGER);
		} else if (_tokenActual.getTipo() == TipoToken.NATURAL) {
			_instrucciones.add(new InstruccionAPILA(Double
					.parseDouble(_tokenActual.getLexema())));
			tipo = new Resp(new Tipo(ETipo.NATURAL), EModo.VALOR);
			_etq++;
			emparejaToken(TipoToken.NATURAL);
		} else if (_tokenActual.getTipo() == TipoToken.FLOAT) {
			StringTokenizer to = new StringTokenizer(_tokenActual.getLexema(),
					"Ee", true);

			String a1 = to.nextToken();
			if (!to.hasMoreElements())
				_instrucciones.add(new InstruccionAPILA(Double
						.parseDouble(_tokenActual.getLexema())));
			
			else {
				to.nextToken();
				String a2 = to.nextToken();
				int numEntero = Integer.parseInt(a2);
				String aux2 = "1";
				for (int i = 0; i < numEntero; i++)
					aux2 = aux2 + "0";

				int numEntero2 = Integer.parseInt(aux2);
				float numEnter3 = Float.valueOf(a1).floatValue();
				numEnter3 = numEnter3 * numEntero2;

				_instrucciones.add(new InstruccionAPILA(numEnter3));

			}
			tipo = new Resp(new Tipo(ETipo.FLOAT), EModo.VALOR);
			emparejaToken(TipoToken.FLOAT);
			_etq++;
		} else if (_tokenActual.getTipo() == TipoToken.CHARACTER) {
			_instrucciones.add(new InstruccionAPILA((double) _tokenActual
					.getLexema().charAt(0)));
			_etq++;
			tipo = tipo = new Resp(new Tipo(ETipo.CHAR), EModo.VALOR);
			emparejaToken(TipoToken.CHARACTER);
		} 
		
		else if (_tokenActual.getTipo() == TipoToken.ID) {
			/*
			_instrucciones.add(new InstruccionAPILADIR(_ts.dameDir(_tokenActual
					.getLexema())));
			tipo = _ts.tipoDe(_tokenActual.getLexema());
			emparejaToken(TipoToken.ID);
			*/
			
			tipo = new Resp(Mem(null), EModo.VARIABLE);
			//TODO coppatibilidad
			/*
			if ((compatibles(tipo.getTipo(), new Tipo(ETipo.INTEGER)) || 
				compatibles(tipo.getTipo(), new Tipo(ETipo.BOOLEAN)))&& !parhin){
				_instrucciones.add(new InstruccionAPILAIND());
				_etq++;
				
			}	
			*/
			
			
			
		} else if (_tokenActual.getTipo() == TipoToken.TRUE) {
			_instrucciones.add(new InstruccionAPILA(1));
			tipo = new Resp(new Tipo(ETipo.BOOLEAN), EModo.VALOR);
			emparejaToken(TipoToken.TRUE);
			_etq++;
		} else if (_tokenActual.getTipo() == TipoToken.FALSE) {
			_instrucciones.add(new InstruccionAPILA(0));
			tipo = new Resp(new Tipo(ETipo.BOOLEAN), EModo.VALOR);
			emparejaToken(TipoToken.FALSE);
			_etq++;
		} else if (_tokenActual.getTipo() == TipoToken.PARAP) {
			emparejaToken(TipoToken.PARAP);
			tipo = Exp(parhin);
			emparejaToken(TipoToken.PARCLA);
			
		} else if (_tokenActual.getTipo() == TipoToken.VALORABSO) {
			emparejaToken(TipoToken.VALORABSO);
			tipo = Exp(parhin);
			if ((tipo.getTipo().getTipo() != ETipo.FLOAT)
					|| (tipo.getTipo().getTipo() != ETipo.INTEGER)
					|| (tipo.getTipo().getTipo() != ETipo.NATURAL))
			//	tipo = TipoInstruccion.ERROR;
				tipo=new Resp(new Tipo(ETipo.ERR), EModo.VALOR);
			emparejaToken(TipoToken.VALORABSO);
		} 
		
		
		else{
			tipo=new Resp(new Tipo(ETipo.ERR), EModo.VALOR);
		}		

		
		

		return tipo;
	}

	public Resp OpAd() throws Error {
		Instruccion codigo = null;
		Tipo tipo = null;
		if (_tokenActual.getTipo() == TipoToken.SUMA) {
			codigo = new InstruccionSUMA();
			tipo =new Tipo(ETipo.NUMERICA);
			emparejaToken(TipoToken.SUMA);
		} else if (_tokenActual.getTipo() == TipoToken.RESTA) {
			codigo = new InstruccionRESTA();
			tipo =new Tipo(ETipo.NUMERICA);
			emparejaToken(TipoToken.RESTA);
		} else if (_tokenActual.getTipo() == TipoToken.OR) {
			codigo = new InstruccionOR();
			tipo =new Tipo(ETipo.BOOLEAN);
			emparejaToken(TipoToken.OR);
		} else {
			throw new Error(
					"OpAd: Se esperaba tokenSUMA, tokenRESTA o tokenOR y no "
							+ _tokenActual.toString());
		}
		return new Resp(codigo, tipo);
		

	}
//TODO como esta
	public Resp OpMul() throws Error {
		Instruccion codigo = null;
		Tipo tipo = null;
		if (_tokenActual.getTipo() == TipoToken.MULT) {
			codigo = new InstruccionMULT();
			tipo =new Tipo(ETipo.NUMERICA);
			emparejaToken(TipoToken.MULT);
		} else if (_tokenActual.getTipo() == TipoToken.DIV) {
			codigo = new InstruccionDIV();
			tipo =new Tipo(ETipo.NUMERICA);
			emparejaToken(TipoToken.DIV);
		} else if (_tokenActual.getTipo() == TipoToken.AND) {
			codigo = new InstruccionAND();
			tipo =new Tipo(ETipo.BOOLEAN);
			emparejaToken(TipoToken.AND);
		} else if (_tokenActual.getTipo()==TipoToken.PORCEN){				
			codigo = new InstruccionMOD();
			tipo = new Tipo(ETipo.NUMERICA);
			emparejaToken(TipoToken.PORCEN);
		}else
			throw new Error(
					"OpMul: Se esperaba tokenMULT, tokenDIV, tokenAND o tokenPORCEN y no "
							+ _tokenActual.toString());

		return new Resp(codigo, tipo);

	}

	public Resp OpComp() throws Error {
		Instruccion codigo = null;
		Tipo tipo = null;
		if (_tokenActual.getTipo() == TipoToken.MAYOR) {
			codigo = new InstruccionGT();
			tipo = new Tipo(ETipo.BOOLEAN);
			emparejaToken(TipoToken.MAYOR);
		} else if (_tokenActual.getTipo() == TipoToken.MENOR) {
			codigo = new InstruccionLT();
			tipo = new Tipo(ETipo.BOOLEAN);
			emparejaToken(TipoToken.MENOR);
		} else if (_tokenActual.getTipo() == TipoToken.IGUAL) {
			codigo = new InstruccionEQ();
			tipo = new Tipo(ETipo.BOOLEAN);
			emparejaToken(TipoToken.IGUAL);
		} else if (_tokenActual.getTipo() == TipoToken.DISTINTO) {
			codigo = new InstruccionNEQ();
			tipo = new Tipo(ETipo.BOOLEAN);
			emparejaToken(TipoToken.DISTINTO);
		} else if (_tokenActual.getTipo() == TipoToken.MAYORIGUAL) {
			codigo = new InstruccionGE();
			tipo = new Tipo(ETipo.BOOLEAN);
			emparejaToken(TipoToken.MAYORIGUAL);
		} else if (_tokenActual.getTipo() == TipoToken.MENORIGUAL) {
			codigo = new InstruccionLE();
			tipo = new Tipo(ETipo.BOOLEAN);
			emparejaToken(TipoToken.MENORIGUAL);
		} else
			throw new Error(
					"OpComp: Se esperaba tokenMATOR, tokenMENOR, tokenIGUAL, tokenDISTINTO, tokenMAYORIGUAL o tokenMENORIGUAL y no "
							+ _tokenActual.toString());

		return new Resp(codigo, tipo);

	}

	public Resp OpUnario() throws Error {
		
		Instruccion codigo = null;
		Tipo tipo = null;
		if (_tokenActual.getTipo() == TipoToken.RESTA) {
			codigo = new InstruccionNEG();
			tipo = new Tipo(ETipo.NATURAL);
			emparejaToken(TipoToken.RESTA);
		} else if (_tokenActual.getTipo() == TipoToken.NOT) {
			codigo = new InstruccionNOT();
			tipo = new Tipo(ETipo.BOOLEAN);
			emparejaToken(TipoToken.NOT);
		} else if (_tokenActual.getTipo() == TipoToken.CASTINGCHAR) {
			codigo = new InstruccionCASTCHAR();
			tipo = new Tipo(ETipo.CHAR);
			emparejaToken(TipoToken.CASTINGCHAR);
		} else if (_tokenActual.getTipo() == TipoToken.CASTINGNAT) {
			codigo = new InstruccionCASTNAT();
			tipo = new Tipo(ETipo.NATURAL);
			emparejaToken(TipoToken.CASTINGNAT);
		} else if (_tokenActual.getTipo() == TipoToken.CASTINGFLOAT) {
			codigo = new InstruccionCASTFLOAT();
			tipo = new Tipo(ETipo.FLOAT);
			emparejaToken(TipoToken.CASTINGFLOAT);
		} else if (_tokenActual.getTipo() == TipoToken.CASTINGINT) {
			codigo = new InstruccionCASTINT();
			tipo = new Tipo(ETipo.INTEGER);
			emparejaToken(TipoToken.CASTINGINT);
		} else
			throw new Error(
					"OpUnario: Se esperaba tokenRESTA, tokenNOT o un tokenCasting y no "
							+ _tokenActual.toString());
		

		return new Resp(codigo, tipo);
	}

	public void emparejaToken(TipoToken tt) throws Error {
		if (_tokenActual.getTipo() == tt)
			_tokenActual = _lexico.sigToken();
		else
			throw new Error("comparaToken: Se esperaba token" + tt + " y no "
					+ _tokenActual.toString());
	}

	//

	public Tipo tipoDeFact(Tipo tOperador,
			Tipo tOperando)
	// Comprueba que el tipo de tOperador y de tOperando sean compatibles. En
	// caso de serlo devuelve dicho tipo y en caso contrario devuelve err.
	{

		if ((tOperador.getTipo() == ETipo.INTEGER
				|| tOperador.getTipo() == ETipo.NATURAL
				|| tOperador.getTipo() == ETipo.NUMERICA
				|| tOperador.getTipo() == ETipo.CHAR || tOperador.getTipo() == ETipo.FLOAT)
				&& (tOperando.getTipo() == ETipo.BOOLEAN))
			return new Tipo(ETipo.ERR);
		else if ((tOperador.getTipo() == ETipo.INTEGER
				|| tOperador.getTipo() == ETipo.NATURAL
				|| tOperador.getTipo() == ETipo.NATURAL || tOperador.getTipo() == ETipo.NUMERICA)
				&& tOperando.getTipo() == ETipo.CHAR)// casting
			return new Tipo(ETipo.ERR);
		/*
		 * else if(tOperador== || && (tOperando.tipo == boolean ||
		 * tOperando.tipo == boolean))//valor abs return <tipo:’err’>
		 */
		else if (tOperador.getTipo() == ETipo.CHAR
				&& (tOperando.getTipo() == ETipo.INTEGER || tOperando.getTipo() == ETipo.FLOAT))
			return new Tipo(ETipo.ERR);
		else if (tOperador.getTipo() == ETipo.NUMERICA
				&& tOperando.getTipo() == ETipo.NATURAL)
			return new Tipo(ETipo.ERR);
		else if (tOperador.getTipo() == ETipo.NUMERICA)
			return tOperando;
		else
			return tOperador;
	}

	public Tipo tipoDeExpBin(Tipo tipoInstruccion,
			Tipo tOperando1, Tipo tOperando2)
	// Comprueba que el tipo de tOperador, de tOperando1 y tOperando2 sean
	// compatibles. En caso de serlo devuelve dicho tipo y en caso contrario
	// devuelve err.
	{
		if ((tOperando1.getTipo() == ETipo.ERR)
				||(tOperando2.getTipo() == ETipo.ERR)
				|| (tOperando1.getTipo() == ETipo.CHAR)
				|| (tOperando2.getTipo() == ETipo.CHAR))
			return new Tipo(ETipo.ERR);
		else {
			if (tipoInstruccion.getTipo() == ETipo.NUMERICA) {
				if ((tOperando1.getTipo() != ETipo.BOOLEAN)	&& (tOperando2.getTipo() != ETipo.BOOLEAN)) {
					if ((tOperando1.getTipo() == ETipo.FLOAT)
							|| (tOperando2.getTipo() == ETipo.FLOAT))
						return new Tipo(ETipo.FLOAT);
					else if ((tOperando1.getTipo() == ETipo.INTEGER)
							|| (tOperando2.getTipo() == ETipo.INTEGER))
						return new Tipo(ETipo.INTEGER);
					else
						return new Tipo(ETipo.NATURAL);
				} else			return new Tipo(ETipo.ERR);

			} else {
				if ((tOperando1 == tOperando2)
						&& (tOperando1.getTipo() == ETipo.BOOLEAN))
					return new Tipo(ETipo.BOOLEAN);
				else
					return new Tipo(ETipo.ERR);

			}

		}

	}

	public Tipo tipoDeExpComp(Tipo tOperador1,
			Tipo tOperador2) {
		if ((tOperador1.getTipo() == ETipo.ERR)
				|| (tOperador2.getTipo() == ETipo.ERR))
			return new Tipo(ETipo.ERR);
		else {
			if ((tOperador1.getTipo() == ETipo.BOOLEAN)
					|| (tOperador2.getTipo() == ETipo.BOOLEAN)) {
				if (tOperador1 == tOperador2)
					return new Tipo(ETipo.BOOLEAN);
				else
					return new Tipo(ETipo.ERR);
			} else {
				if ((tOperador1.getTipo() == ETipo.CHAR)
						|| (tOperador2.getTipo() == ETipo.CHAR)) {
					if (tOperador1 == tOperador2)
						return new Tipo(ETipo.BOOLEAN);
					else
						return new Tipo(ETipo.ERR);
				} else
					return new Tipo(ETipo.BOOLEAN);
			}
		}

	}
//TODO no se si me va a hacer falta
	public boolean fallaasig(TipoInstruccion a, TipoInstruccion b) {
		boolean falla = false;
		if ((a == TipoInstruccion.CHARACTER)
				&& (b != TipoInstruccion.CHARACTER))
			falla = true;

		if ((a == TipoInstruccion.BOOLEAN) && (b != TipoInstruccion.BOOLEAN))
			falla = true;

		if ((a == TipoInstruccion.NATURAL) && (b != TipoInstruccion.NATURAL))
			falla = true;

		if ((a == TipoInstruccion.INTEGER)
				&& ((b != TipoInstruccion.NATURAL) && (b != TipoInstruccion.INTEGER)))
			falla = true;

		if ((a == TipoInstruccion.FLOAT)
				&& ((b != TipoInstruccion.NATURAL)
						&& (b != TipoInstruccion.INTEGER) && (b != TipoInstruccion.FLOAT)))
			falla = true;

		return falla;
	}

	public Tipo tipoDeDespl(Tipo tOperador1,
			Tipo tOperador2, Tipo tOperacion) {
		if ((tOperador1.getTipo() == ETipo.ERR)
				|| (tOperador2.getTipo() == ETipo.ERR)
				|| (tOperador1.getTipo() == ETipo.CHAR)
				|| (tOperador2.getTipo() == ETipo.CHAR))

			return new Tipo(ETipo.ERR);
		else {
			if (tOperacion.getTipo() == ETipo.DESP) {
				if ((tOperador1 == tOperador2)
						&& (tOperador1.getTipo() == ETipo.NATURAL))
					return new Tipo(ETipo.NATURAL);
				else
					return new Tipo(ETipo.ERR);

			} else {

				if (((tOperador1.getTipo() == ETipo.NATURAL) && (tOperador2.getTipo() == ETipo.NATURAL))
						|| ((tOperador1.getTipo() == ETipo.NATURAL) && (tOperador2.getTipo() == ETipo.INTEGER))) {
					return tOperador1;
				} else
					return new Tipo(ETipo.ERR);
			}
		}
	}

}
