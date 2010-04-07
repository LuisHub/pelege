package compilador.analizador_sintactico;

import vm.instruccion.*;

import java.util.StringTokenizer;
import java.util.Vector;

import compilador.analizador_lexico.AnalizadorLexico;
import compilador.tabla.*;
import compilador.tokens.*;

public class AnalizadorSintactico {
	//esto es una prueba para subir contenido al repositorio willy

	public TablaSimbolos _ts;	
	public AnalizadorLexico _lexico;	
	public Vector<Instruccion> _instrucciones;
	public Token _tokenActual;
	
	public AnalizadorSintactico(String e){
		_ts = new TablaSimbolos();
		_lexico= new AnalizadorLexico(e);
		_lexico.iniciaLexico();			
		_instrucciones = new Vector<Instruccion>();
		_tokenActual=_lexico.sigToken();		
	}
	
	public boolean init(){
		try{
			Bloque();
		}
		catch(Error e){
			System.out.println(e.getMsg());
			return false;
		}
		return true;
	}	
	
	public void Bloque() throws Error{
		Decs();
		emparejaToken(TipoToken.PROGRAM);
		Sents();
		if (_tokenActual.getTipo()==TipoToken.EOF)
			{System.out.println("Se ha realizado el analisis sintáctico con éxito.");
			 for(int i=0;i<this._instrucciones.size();i++)
			 {
				 System.out.println("  "+this._instrucciones.elementAt(i));
			 }
			}
	}
//para las declaraciones
	public void Decs()throws Error{			
		DecResp dr = Dec();
		
		
		if (!_ts.existeId(dr.getNombre())){
			_ts.anadeId(dr.getNombre(), dr.getTipo());													
			RDecs();			
		}
	}
	
	public void RDecs() throws Error{				
		if (_tokenActual.getTipo()==TipoToken.ID){				
			Token _tokenTmp = _tokenActual;
			DecResp dr = Dec();			
			if (!_ts.existeId(dr.getNombre())){
				_ts.anadeId(dr.getNombre(), dr.getTipo());															
				RDecs();
			}
			else throw new Error("Identificador duplicado. " + _tokenTmp.toString());
		}
	}
			
	//_tokenActual -> TokenId
	public DecResp Dec()throws Error{				
		String lex = _tokenActual.getLexema();
		TipoInstruccion tipo;									
		emparejaToken(TipoToken.ID);		
		emparejaToken(TipoToken.DOSPUNTOS);								
		tipo = Tipo();
		emparejaToken( TipoToken.SEPARADOR);
		return new DecResp(lex,tipo);				
	}
	
	
	public TipoInstruccion Tipo()throws Error{
		TipoInstruccion tipo = null;		
		if (_tokenActual.getTipo()==TipoToken.INTEGER){				
			tipo = TipoInstruccion.INTEGER; 
			emparejaToken( TipoToken.INTEGER);
		}
		else if (_tokenActual.getTipo()==TipoToken.BOOLEAN){
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken( TipoToken.BOOLEAN);
		}
		else if (_tokenActual.getTipo()==TipoToken.NATURAL){
			tipo = TipoInstruccion.NATURAL;
			emparejaToken( TipoToken.NATURAL);
		}
		else if (_tokenActual.getTipo()==TipoToken.FLOAT){				
			tipo = TipoInstruccion.FLOAT;
			emparejaToken( TipoToken.FLOAT);
		}
		else if (_tokenActual.getTipo()==TipoToken.CHARACTER){				
			tipo = TipoInstruccion.CHARACTER;
			emparejaToken( TipoToken.CHARACTER);
		}
		else throw new Error("Tipo: Se esperaba una palabra reservada INTEGER o FLOAT o BOOLEAN o CHAR y no " + _tokenActual.toString() + ")");	
		
		return tipo;
	}
	

	
	
	 
	public void Sents() throws Error{		
		Instr();
		RSents();		
	}
	
	public void RSents()  throws Error{
		if (_tokenActual.getTipo()==TipoToken.ID ||
			_tokenActual.getTipo()==TipoToken.READ ||
			_tokenActual.getTipo()==TipoToken.WRITE){		
			Instr();
			RSents();
		}
	}
	
	public void Instr() throws Error{				
		if (_tokenActual.getTipo()==TipoToken.ID)		
			InstrAsig();		
		else if (_tokenActual.getTipo()==TipoToken.READ)		
			InstrLect();
		else if (_tokenActual.getTipo()==TipoToken.WRITE)		
			InstrEsc();
		else
			throw new Error("Instr: Se esperaba tokenID, tokenREAD o tokenWRITE. " + _tokenActual.toString());
	}
	
	
///perfect	
	
	
	//_tokenActual -> TokenId
	public void InstrAsig() throws Error{				
		Token _tokenTmp = _tokenActual;		
		emparejaToken(TipoToken.ID);
		emparejaToken(TipoToken.ASIG);
		TipoInstruccion tipoExp = Exp();
		
		
		if (tipoExp == TipoInstruccion.ERROR)
			throw new Error("InstrAsig: tipoExp == ERROR. " + _tokenActual.toString());
		else if (!_ts.existeId(_tokenTmp.getLexema()))			
			throw new Error("InstrAsig: Identificador no declarado. "+ _tokenTmp.toString());
		
		else if (fallaasig(_ts.tipoDe(_tokenTmp.getLexema()), tipoExp))
				throw new Error("InstrAsig: ts.tipoDe(nombre)!= tipoExp. "+ _tokenTmp.toString());
		else{			
			_instrucciones.add(new InstruccionDESAPILADIR(_ts.dameDir(_tokenTmp.getLexema())));
			emparejaToken(TipoToken.SEPARADOR);
			//System.out.println("la linea anterior a este comando funciona "+_tokenActual.getLexema());//TODO importante tenemos que ver si se puede asociar el id a el resultado
		}
	}

	//_tokenActual -> TokenREAD
	public void InstrLect() throws Error{		
		emparejaToken(TipoToken.READ);
		emparejaToken(TipoToken.PARAP);		
		if (_tokenActual.getTipo()==TipoToken.ID){					
			String lex = _tokenActual.getLexema();
			if (!_ts.existeId(lex))
				throw new Error("InstrLect: Identificador no decladaro. " + _tokenActual.toString());
			emparejaToken( TipoToken.ID);
			emparejaToken( TipoToken.PARCLA);
			emparejaToken(TipoToken.SEPARADOR);
			_instrucciones.add(new InstruccionIN());
			_instrucciones.add(new InstruccionDESAPILADIR(_ts.dameDir(lex)));
		}
		else
			throw new Error("InstrLect: Se esperaba tokenID y no " + _tokenActual.toString());
	}

	//_tokenActual -> TokenWRITE
	public void InstrEsc() throws Error{		
		emparejaToken(TipoToken.WRITE);
		emparejaToken(TipoToken.PARAP);
		Token tokenTmp = _tokenActual;
		TipoInstruccion tipo = Exp();
		if (tipo != TipoInstruccion.ERROR){
			emparejaToken(TipoToken.PARCLA);
			emparejaToken(TipoToken.SEPARADOR);	
			_instrucciones.add(new InstruccionOUT());
		}
		else
			throw new Error("InstrEsc: El tipo de la expresión entre paréntesis es ERROR. " + tokenTmp.toString());	
	}	
	


	
	public TipoInstruccion Exp() throws Error{
		TipoInstruccion tipo;
		TipoInstruccion tipo2;
		tipo2= ExpSimple();
		tipo= RExp(tipo2);
		return tipo;
	}
		
	public TipoInstruccion RExp(TipoInstruccion tipo1) throws Error{
		TipoInstruccion tipo;
		TipoInstruccion tipo2;		 	
		if (_tokenActual.getTipo()==TipoToken.MENOR  || 
			_tokenActual.getTipo()==TipoToken.MAYOR  || 
			_tokenActual.getTipo()==TipoToken.IGUAL  || 
			_tokenActual.getTipo()==TipoToken.DISTINTO || 
			_tokenActual.getTipo()==TipoToken.MENORIGUAL || 
			_tokenActual.getTipo()==TipoToken.MAYORIGUAL){
			
			OpResp resp = OpComp();
			tipo2 = ExpSimple();
			
		
			tipo = tipoDeExpComp(tipo1,tipo2);
			
			_instrucciones.add(resp.getCodigo());					
		}
		else{
			tipo = tipo1;
		}
		return tipo;
	}
	
	public TipoInstruccion ExpSimple() throws Error{
		TipoInstruccion tipo;
		TipoInstruccion tipo1;
		tipo1 = Term();
		tipo = RExpSimple(tipo1);
		return tipo;
	}
	
	
	public TipoInstruccion RExpSimple(TipoInstruccion tipo1) throws Error{
		TipoInstruccion tipo;
		TipoInstruccion tipo2;
		if (_tokenActual.getTipo()==TipoToken.SUMA  || 
			_tokenActual.getTipo()==TipoToken.RESTA  || 
			_tokenActual.getTipo()==TipoToken.OR){				
			
			OpResp resp= OpAd();
			tipo2 = Term();
			
			tipo2= tipoDeExpBin(resp.getTipo(), tipo1, tipo2);
			
			_instrucciones.add(resp.getCodigo());		
			
			tipo = RExpSimple(tipo2);
			
		}
		else{
			tipo=tipo1;
		}
		return tipo;
	}
	
	
	public TipoInstruccion Term() throws Error{
		TipoInstruccion tipo;
		tipo= despl();
		tipo= RTerm(tipo);
		return tipo;
	}
	
	public TipoInstruccion RTerm(TipoInstruccion tipo1) throws Error{
		TipoInstruccion tipo;
		TipoInstruccion tipo2;		
		if (_tokenActual.getTipo()==TipoToken.MULT  || 
			_tokenActual.getTipo()==TipoToken.DIV  || 
			_tokenActual.getTipo()==TipoToken.AND){
			
			OpResp resp= OpMul();
			tipo2 = despl();
			tipo = tipoDeExpBin(resp.getTipo(), tipo1, tipo2);
						
			_instrucciones.add(resp.getCodigo());		
			
			tipo = RTerm(tipo);
		}
		else{
			tipo = tipo1;
		}
		return tipo;
	}
	
//voy a poner el modulo y el desplazamiento en la misma produccion
	
	public TipoInstruccion despl() throws Error{
		TipoInstruccion tipo;
		TipoInstruccion tipo2;
		//tipo2= this.OpcionA();
		//tipo= Rdespl(tipo2);
		tipo2= this.Fact();
		tipo= Rdespl(tipo2);
		return tipo;
		
	}
	
	
	public TipoInstruccion Rdespl(TipoInstruccion tipo1) throws Error{
		TipoInstruccion tipo;
		TipoInstruccion tipo2;
		if (_tokenActual.getTipo()==TipoToken.DESPDER  || 
			_tokenActual.getTipo()==TipoToken.DESPIZQ 
			||_tokenActual.getTipo()==TipoToken.PORCEN){				
			
			OpResp aux=opdesp();
			//tipo2 = OpcionA();
			tipo2=this.Fact();
			tipo=tipoDeDespl(tipo1,tipo2,aux.getTipo());
			_instrucciones.add(aux.getCodigo());
			
			tipo = Rdespl(tipo);
		}
		else{
			tipo=tipo1;
		}
		return tipo;
	}
	
	
	private OpResp opdesp() throws Error
	{
		
		Instruccion codigo = null;
		TipoInstruccion tipo = null;
		if (_tokenActual.getTipo()==TipoToken.DESPIZQ){					
			codigo = new InstruccionDESPI();
			tipo = TipoInstruccion.DESP;
			emparejaToken(TipoToken.DESPIZQ);
		}	
		else if (_tokenActual.getTipo()==TipoToken.DESPDER){					
			codigo = new InstruccionDESPD();
			tipo = TipoInstruccion.DESP;
			emparejaToken(TipoToken.DESPDER);
		}
		else if (_tokenActual.getTipo()==TipoToken.PORCEN){	//TODO ponerlo en opmul no aqui				
			codigo = new InstruccionMOD();
			tipo = TipoInstruccion.NUMERICA;
			emparejaToken(TipoToken.PORCEN);
		}
		
		return new OpResp(codigo,tipo);
	}
	
	


////////////////////////////////////////////////////////////////	
	
	/*
	public TipoInstruccion OpcionA() throws Error{
		
		if (_tokenActual.getTipo()==TipoToken.PARAP)
		{
		emparejaToken(TipoToken.PARAP);	
		return OpcionB();
		}else {
			TipoInstruccion tipo;
			TipoInstruccion tipo2;
			tipo2= Fact();
			tipo= Rdespl(tipo2);
			return tipo;
				}
	}
	
	private TipoInstruccion OpcionB() throws Error {
		//  Auto-generated method stub
		TipoInstruccion tipo=null;
		
		// tengo que poner reestriccionde de tipos y traduccion
		
		if (_tokenActual.getTipo()==TipoToken.INTEGER)
		{
		this.emparejaToken(TipoToken.INTEGER);
		
		if (_tokenActual.getTipo()==TipoToken.PARCLA)
				{
			this.emparejaToken(TipoToken.PARCLA);
			tipo=Fact();
			
				}else {throw new Error("no encontrado el parentesis de clausura del cansting integer, y encontrado en su lugar  " + _tokenActual.toString());}
			
			
		}
		else if (_tokenActual.getTipo()==TipoToken.FLOAT)
		{
		
			this.emparejaToken(TipoToken.FLOAT);
			
			if (_tokenActual.getTipo()==TipoToken.PARCLA)
					{
				this.emparejaToken(TipoToken.PARCLA);
				tipo=Fact();
				
					}else {throw new Error("no encontrado el parentesis de clausura del cansting float, y encontrado en su lugar  " + _tokenActual.toString());}
				
		}
		else if(_tokenActual.getTipo()==TipoToken.CHAR)
		{
			
			this.emparejaToken(TipoToken.CHAR);
			
			if (_tokenActual.getTipo()==TipoToken.PARCLA)
					{
				this.emparejaToken(TipoToken.PARCLA);
				tipo=Fact();
				
					}else {throw new Error("no encontrado el parentesis de clausura del cansting char, y encontrado en su lugar  " + _tokenActual.toString());}
				
		}else {
			
			//para que este bien tendra que ser una  expresion
			
			if (_tokenActual.getTipo()==TipoToken.PARAP){
				emparejaToken(TipoToken.PARAP);
				tipo = Exp();
				
				
				if (_tokenActual.getTipo()==TipoToken.PARCLA)
					this.emparejaToken(TipoToken.PARCLA);
						else {throw new Error("no encontrado el parentesis de clausura en la expresion en su lugr a encontrado " + _tokenActual.toString());}

			}
			
			}
			
		
		
		
		return tipo;
	}
*/
	public TipoInstruccion Fact() throws Error{
		TipoInstruccion tipo;
		TipoInstruccion tipo1;		
		if (_tokenActual.getTipo()==TipoToken.NOT ||_tokenActual.getTipo()==TipoToken.CASTINGNAT|| _tokenActual.getTipo()==TipoToken.RESTA||_tokenActual.getTipo()==TipoToken.CASTINGCHAR||_tokenActual.getTipo()==TipoToken.CASTINGFLOAT||_tokenActual.getTipo()==TipoToken.CASTINGINT)
		{			
			OpResp resp = OpUnario();
			tipo1=Fact();
			tipo=tipoDeFact(resp.getTipo(),tipo1);						
				_instrucciones.add(resp.getCodigo());		
		}
		else{
			tipo = Atomo();
		}
		return tipo;
	}
	
	public TipoInstruccion Atomo() throws Error{
		TipoInstruccion tipo=null;
	
	
		if (_tokenActual.getTipo()==TipoToken.INTEGER){				
			_instrucciones.add(new InstruccionAPILA(Double.parseDouble(_tokenActual.getLexema())));
			tipo = TipoInstruccion.INTEGER;
			emparejaToken(TipoToken.INTEGER);
		}
		else if (_tokenActual.getTipo()==TipoToken.NATURAL){				
			_instrucciones.add(new InstruccionAPILA(Double.parseDouble(_tokenActual.getLexema())));
			tipo = TipoInstruccion.NATURAL;
			emparejaToken(TipoToken.NATURAL);
		}
		else if (_tokenActual.getTipo()==TipoToken.FLOAT){		
			StringTokenizer to=new StringTokenizer(_tokenActual.getLexema(),"Ee",true);
			
			String a1=to.nextToken();
			if (!to.hasMoreElements())
			_instrucciones.add(new InstruccionAPILA(Double.parseDouble(_tokenActual.getLexema())));
			else {
				to.nextToken();
				String a2=to.nextToken();
				int numEntero = Integer.parseInt(a2);
				String aux2="1";
				for(int i=0; i<numEntero;i++)
				 aux2=aux2+"0";
				
				int numEntero2 = Integer.parseInt(aux2);
				float numEnter3 = Float.valueOf(a1).floatValue();
				numEnter3=numEnter3*numEntero2;
				
				_instrucciones.add(new InstruccionAPILA(numEnter3));
				
				}
			
			tipo = TipoInstruccion.FLOAT;
			emparejaToken(TipoToken.FLOAT);
		}
		else if (_tokenActual.getTipo()==TipoToken.CHARACTER){				
			_instrucciones.add(new InstruccionAPILA((double)_tokenActual.getLexema().charAt(0)));
			tipo = TipoInstruccion.CHARACTER;
			emparejaToken(TipoToken.CHARACTER);
		}
		else if (_tokenActual.getTipo()==TipoToken.ID){				
			_instrucciones.add(new InstruccionAPILADIR(_ts.dameDir(_tokenActual.getLexema())));
			tipo = _ts.tipoDe(_tokenActual.getLexema());
			emparejaToken(TipoToken.ID);
		}
		else if (_tokenActual.getTipo()==TipoToken.TRUE){					
			_instrucciones.add(new InstruccionAPILA(1));
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.TRUE);
		}
		else if (_tokenActual.getTipo()==TipoToken.FALSE){					
			_instrucciones.add(new InstruccionAPILA(0));
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.FALSE);
		}
		else if (_tokenActual.getTipo()==TipoToken.PARAP){
			emparejaToken(TipoToken.PARAP);
			tipo = Exp();
			emparejaToken(TipoToken.PARCLA);
		}
		else if (_tokenActual.getTipo()==TipoToken.VALORABSO){
			emparejaToken(TipoToken.VALORABSO);
			tipo = Exp();
			if ((tipo!=TipoInstruccion.FLOAT)||(tipo!=TipoInstruccion.INTEGER)||(tipo!=TipoInstruccion.NATURAL))
				tipo = TipoInstruccion.ERROR;
			emparejaToken(TipoToken.VALORABSO);
		}
		else{
			tipo = TipoInstruccion.ERROR;
		}
		return tipo;
	}
	
	public OpResp OpAd()  throws Error{
		Instruccion codigo = null;
		TipoInstruccion tipo = null;
		if (_tokenActual.getTipo()==TipoToken.SUMA){					
			codigo = new InstruccionSUMA();
			tipo = TipoInstruccion.NUMERICA;
			emparejaToken(TipoToken.SUMA);
		}	
		else if (_tokenActual.getTipo()==TipoToken.RESTA){					
			codigo = new InstruccionRESTA();
			tipo = TipoInstruccion.NUMERICA;
			emparejaToken(TipoToken.RESTA);
		}
		else if (_tokenActual.getTipo()==TipoToken.OR){					
			codigo = new InstruccionOR();
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.OR);
		}
		else{
			throw new Error("OpAd: Se esperaba tokenSUMA, tokenRESTA o tokenOR y no " + _tokenActual.toString());
		}
		OpResp resp= new OpResp(codigo,tipo);
		return resp;
		
	}
	
	public OpResp OpMul() throws Error{
		Instruccion codigo = null;
		TipoInstruccion tipo = null;		
		if (_tokenActual.getTipo()==TipoToken.MULT){					
			codigo = new InstruccionMULT();
			tipo = TipoInstruccion.NUMERICA;
			emparejaToken(TipoToken.MULT);
		}
		else if (_tokenActual.getTipo()==TipoToken.DIV){					
			codigo = new InstruccionDIV();
			tipo = TipoInstruccion.NUMERICA;
			emparejaToken(TipoToken.DIV);
		}
		else if (_tokenActual.getTipo()==TipoToken.AND){					
			codigo = new InstruccionAND();
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.AND);
		}
		else			
			throw new Error("OpMul: Se esperaba tokenMULT, tokenDIV o tokenAND y no " + _tokenActual.toString());
		
		return new OpResp(codigo,tipo);
		
	}
	
	public OpResp OpComp() throws Error{
		Instruccion codigo = null;
		TipoInstruccion tipo = null;		
		if (_tokenActual.getTipo()==TipoToken.MAYOR){					
			codigo = new InstruccionGT();
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.MAYOR);
		}		
		else if (_tokenActual.getTipo()==TipoToken.MENOR){					
			codigo = new InstruccionLT();
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.MENOR);
		}		
		else if (_tokenActual.getTipo()==TipoToken.IGUAL){					
			codigo = new InstruccionEQ();
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.IGUAL);
		}
		else if (_tokenActual.getTipo()==TipoToken.DISTINTO){					
			codigo = new InstruccionNEQ();
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.DISTINTO);
		}
		else if (_tokenActual.getTipo()==TipoToken.MAYORIGUAL){					
			codigo = new InstruccionGE();
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.MAYORIGUAL);
		}
		else if (_tokenActual.getTipo()==TipoToken.MENORIGUAL){					
			codigo = new InstruccionLE();
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.MENORIGUAL);
		}
		else	throw new Error("OpComp: Se esperaba tokenMATOR, tokenMENOR, tokenIGUAL, tokenDISTINTO, tokenMAYORIGUAL o tokenMENORIGUAL y no " + _tokenActual.toString());
				
		return new OpResp(codigo,tipo);
		
	}
	
	public OpResp OpUnario() throws Error{
		Instruccion codigo = null;
		TipoInstruccion tipo = null;
		if (_tokenActual.getTipo()==TipoToken.RESTA){					
			codigo = new InstruccionNEG();
			tipo = TipoInstruccion.NUMERICA;
			emparejaToken(TipoToken.RESTA);
		}
		else if (_tokenActual.getTipo()==TipoToken.NOT){
			codigo = new InstruccionNOT();
			tipo = TipoInstruccion.BOOLEAN;
			emparejaToken(TipoToken.NOT);
		}
		else if (_tokenActual.getTipo()==TipoToken.CASTINGCHAR){
			codigo = new InstruccionCASTCHAR();
			tipo = TipoInstruccion.CHARACTER;
			emparejaToken(TipoToken.CASTINGCHAR);
		}
		else if (_tokenActual.getTipo()==TipoToken.CASTINGNAT){
			codigo = new InstruccionCASTNAT();
			tipo = TipoInstruccion.NATURAL;
			emparejaToken(TipoToken.CASTINGNAT);
		}
		else if (_tokenActual.getTipo()==TipoToken.CASTINGFLOAT){
			codigo = new InstruccionCASTFLOAT();
			tipo = TipoInstruccion.FLOAT;
			emparejaToken(TipoToken.CASTINGFLOAT);
		}
		else if (_tokenActual.getTipo()==TipoToken.CASTINGINT){
			codigo = new InstruccionCASTINT();
			tipo = TipoInstruccion.INTEGER;
			emparejaToken(TipoToken.CASTINGINT);
		}
		else
			throw new Error("OpUnario: Se esperaba tokenRESTA, tokenNOT o un tokenCasting y no " + _tokenActual.toString());
		
		return new OpResp(codigo,tipo);
	}
	
	
	public void emparejaToken(TipoToken tt) throws Error{
		if (_tokenActual.getTipo()==tt)
			_tokenActual = _lexico.sigToken();
		else			
			throw new Error("comparaToken: Se esperaba token" + tt + " y no " + _tokenActual.toString());	
	}
	
	
	//
	

	
	
	public TipoInstruccion tipoDeFact(TipoInstruccion tOperador, TipoInstruccion tOperando)
	//Comprueba que el tipo de tOperador y de tOperando sean compatibles. En caso de serlo devuelve dicho tipo y en caso contrario devuelve err.
	{ 
		
		if((tOperador == TipoInstruccion.INTEGER|| tOperador == TipoInstruccion.NATURAL || tOperador == TipoInstruccion.NUMERICA || tOperador == TipoInstruccion.CHARACTER || tOperador == TipoInstruccion.FLOAT ) && (tOperando == TipoInstruccion.BOOLEAN))
		return TipoInstruccion.ERROR;
	else if ((tOperador == TipoInstruccion.INTEGER|| tOperador == TipoInstruccion.NATURAL || tOperador == TipoInstruccion.NATURAL|| tOperador == TipoInstruccion.NUMERICA) && tOperando == TipoInstruccion.CHARACTER)//casting
		return TipoInstruccion.ERROR;
	/*else if(tOperador== || && (tOperando.tipo == boolean || tOperando.tipo == boolean))//valor abs
		return <tipo:’err’>*/
	else if (tOperador== TipoInstruccion.CHARACTER && (tOperando == TipoInstruccion.INTEGER || tOperando== TipoInstruccion.FLOAT))
		return TipoInstruccion.ERROR;
	else  if (tOperador==TipoInstruccion.NUMERICA && tOperando == TipoInstruccion.NATURAL) return TipoInstruccion.ERROR;
	else if (tOperador==TipoInstruccion.NUMERICA) return tOperando;
	else return  tOperador;
	}

	public TipoInstruccion tipoDeExpBin(TipoInstruccion tOperador, TipoInstruccion tOperando1, TipoInstruccion tOperando2)
	//Comprueba que el tipo de tOperador, de tOperando1 y tOperando2 sean compatibles. En caso de serlo devuelve dicho tipo y en caso contrario devuelve err.
	{ 
if ((tOperando1==TipoInstruccion.ERROR)||(tOperando2==TipoInstruccion.ERROR)||(tOperando1==TipoInstruccion.CHARACTER)||(tOperando2==TipoInstruccion.CHARACTER))
			return TipoInstruccion.ERROR;
		else
		{	
			if(tOperador== TipoInstruccion.NUMERICA)
			{
				if((tOperando1!=TipoInstruccion.BOOLEAN)&&(tOperando2!=TipoInstruccion.BOOLEAN))
				{	
					if((tOperando1==TipoInstruccion.FLOAT)||(tOperando2==TipoInstruccion.FLOAT))
						return TipoInstruccion.FLOAT;
					else if((tOperando1==TipoInstruccion.INTEGER)||(tOperando2==TipoInstruccion.INTEGER))
						return TipoInstruccion.INTEGER;
					else return TipoInstruccion.NATURAL;
				} else return TipoInstruccion.ERROR;

			}else {
				if((tOperando1== tOperando2) &&(tOperando1== TipoInstruccion.BOOLEAN))
					return TipoInstruccion.BOOLEAN;
				else return TipoInstruccion.ERROR;

			}



		}
		
	}	


	public TipoInstruccion tipoDeExpComp(TipoInstruccion tOperador1, TipoInstruccion tOperador2)
	{	
		if ((tOperador1==TipoInstruccion.ERROR)||(tOperador2==TipoInstruccion.ERROR))
			return TipoInstruccion.ERROR;
		else
		{
			if ((tOperador1==TipoInstruccion.BOOLEAN)||(tOperador2==TipoInstruccion.BOOLEAN))
			{
				if (tOperador1==tOperador2)
					return TipoInstruccion.BOOLEAN;
				else return TipoInstruccion.ERROR;
			}
			else
			{
				if ((tOperador1==TipoInstruccion.CHARACTER)||(tOperador2==TipoInstruccion.CHARACTER))
				{
					if (tOperador1==tOperador2)
						return TipoInstruccion.BOOLEAN;
					else return TipoInstruccion.ERROR;
				}else return TipoInstruccion.BOOLEAN;
			}
		}

	}
	
	
	public boolean fallaasig(TipoInstruccion a,TipoInstruccion b)
	{
		boolean  falla=false;
		if((a==TipoInstruccion.CHARACTER)&&(b!=TipoInstruccion.CHARACTER))
			falla=true;

		if((a==TipoInstruccion.BOOLEAN)&&(b!=TipoInstruccion.BOOLEAN))
			falla=true;


		if((a==TipoInstruccion.NATURAL)&&(b!=TipoInstruccion.NATURAL))
			falla=true;

		if((a==TipoInstruccion.INTEGER)&&((b!=TipoInstruccion.NATURAL)&&(b!=TipoInstruccion.INTEGER)))
			falla=true;

		if((a==TipoInstruccion.FLOAT)&&((b!=TipoInstruccion.NATURAL)&&(b!=TipoInstruccion.INTEGER)&&(b!=TipoInstruccion.FLOAT)))
			falla=true;

		return falla;
	}
	
	public TipoInstruccion tipoDeDespl(TipoInstruccion tOperador1,TipoInstruccion tOperador2,TipoInstruccion tOperacion)
	{
		if ((tOperador1==TipoInstruccion.ERROR)||(tOperador2==TipoInstruccion.ERROR)||(tOperador1==TipoInstruccion.CHARACTER)||(tOperador2==TipoInstruccion.CHARACTER))

			return TipoInstruccion.ERROR;
		else
		{	
			if(tOperacion==TipoInstruccion.DESP)
			{
				if ((tOperador1==tOperador2)&&(tOperador1==TipoInstruccion.NATURAL))
					return TipoInstruccion.NATURAL;
				else return TipoInstruccion.ERROR;

			}else {

				if(((tOperador1==TipoInstruccion.NATURAL)&&(tOperador2==TipoInstruccion.NATURAL))||((tOperador1==TipoInstruccion.NATURAL)&&(tOperador2==TipoInstruccion.INTEGER)))
				{
					return tOperador1;	
				}else return TipoInstruccion.ERROR;
			}
		}
	}	
	
	
}
