package compilador.analizador_lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import compilador.tabla.TablaReservadas;
import compilador.tokens.TipoToken;
import compilador.tokens.Token;
 




public class AnalizadorLexico {
	
	private char actual;
	private String buffer;
	private int estado;
	private TablaReservadas reservadas;
	private int numLinea;
	private int numCol;
	private BufferedReader br;
	private FileReader fr;
	private File archivo;
	private boolean fin;	
	private String archivoEntrada;	
	
	public AnalizadorLexico(String e){
			buffer="";
			estado=0;
			numLinea=0;
			numCol=0;
			fin=false;
			reservadas=new TablaReservadas();			
			archivoEntrada=e;			
	}
	
	public void iniciaLexico(){
			crearTablaReservadas();
			iniciaLector();
			actual=siguienteCaracter();
			
	}


	public Token sigToken(){
		Token tok = null;
		estado=0;
		buffer="";
		boolean creado=false;
		while (tok==null){
		switch (estado) {
			case 0:
				if (esLetra(actual)){
					estado=1;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (esDigito(actual)){
					
					if (actual!=0) 
						{estado=26;
						buffer+=actual;
						actual=siguienteCaracter();
						}else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				}
				else if (actual=='&'){
					estado=2;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual==':'){
					estado=3;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='+'){
					estado=4;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='*'){
					estado=5;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if(actual=='/'){
					estado=11;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='='){
					estado=6;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='>'){
					estado=12;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='<'){
					estado=14;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual==';'){
					estado=7;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='%'){
					estado=8;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='-'){
					estado=9;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='('){
					estado=17;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual==')'){
					estado=18;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='\''){
					estado=23;
					//buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='|'){
					estado=29;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='#'){
					estado=22;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual==' ' || actual=='\r' || actual=='\t' || actual=='\n'){
					if (actual==' '){
							numCol++;
					}
					actual=siguienteCaracter();
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
		
			case 1:
				if (esLetra(actual) || esDigito(actual)){
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (esSeparador(actual)){
					if (reservadas.esReservada(buffer)){
						tok= CreaTokenReservada();
						numCol+=buffer.length();
						if (actual==' '){
							numCol++;
						}
					}
					else{
						tok= new Token(numLinea,numCol,buffer,TipoToken.ID);
						numCol+=buffer.length();
						if (actual==' '){
							numCol++;
						}
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
		//CAMBIO
			case 2:
				if (esSeparador(actual) || esLetra(actual)|| esDigito(actual)){
					tok= new Token(numLinea,numCol,TipoToken.PROGRAM);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;	
			
			case 3:
				if (actual=='='){
					estado=10;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (esSeparador(actual) || esLetra(actual)){
						tok=new Token(numLinea,numCol,TipoToken.DOSPUNTOS);
						numCol+=buffer.length();
						if (actual==' '){
							numCol++;
						}
						creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 4:
				if (esSeparador(actual) || esLetra(actual)|| esDigito(actual)){
					tok= new Token(numLinea,numCol,TipoToken.SUMA);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 5:
				if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,TipoToken.MULT);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 6:
				if (actual=='/')
				{
					estado=19;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,TipoToken.IGUAL);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 7:
				if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,TipoToken.SEPARADOR);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 8:
				if (actual==' ' || actual=='\r'||this.esDigito(actual)||this.esLetra(actual)||this.esSeparador(actual)){
					tok= new Token(numLinea,numCol,TipoToken.PORCEN);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 9:
				if (esSeparador(actual) || esLetra(actual) || esDigito(actual)){
					tok= new Token(numLinea,numCol,TipoToken.RESTA);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 10:
				if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){				
					tok= new Token(numLinea,numCol,TipoToken.ASIG);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 11:
				if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,TipoToken.DIV);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 12:
				if (actual=='='){
					estado=13;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if(actual=='>'){
					estado=21;
					buffer+=actual;
					actual=siguienteCaracter();	
				}
				else if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,TipoToken.MAYOR);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}

				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 13:
				if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,TipoToken.MAYORIGUAL);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 14:
				if (actual=='='){
					estado=15;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if(actual=='<'){
					estado=16;
					buffer+=actual;
					actual=siguienteCaracter();	
				}				
				else if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,TipoToken.MENOR);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}

				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 15:
				if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,TipoToken.MENORIGUAL);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 16:
				if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,TipoToken.DESPIZQ);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			case 17:
				
				
			try {
				br.mark(100);
				String auxiliar=""+actual;
				auxiliar = auxiliar+(char)br.read();
				auxiliar = auxiliar+(char)br.read();
				auxiliar = auxiliar+(char)br.read();
				
				
				if (auxiliar.equals("int)"))
				{
					
					tok=new Token(numLinea,numCol,TipoToken.CASTINGINT);
					actual=this.siguienteCaracter();
					numCol=numCol+3;
					
					
				}else
					
					if (auxiliar.equals("nat)"))
					{
						
						tok=new Token(numLinea,numCol,TipoToken.CASTINGNAT);
						actual=this.siguienteCaracter();
						numCol=numCol+3;
						
						
					}else	
					{auxiliar = auxiliar+(char)br.read();
					
						if (auxiliar.equals("char)"))
							
						{
						
							tok=new Token(numLinea,numCol,TipoToken.CASTINGCHAR);
							actual=this.siguienteCaracter();
							
							numCol=numCol+4;
							
						}
						else 
							{ 
							auxiliar = auxiliar+(char)br.read();
						
							if (auxiliar.equals("float)"))
								{
								
						//
								tok=new Token(numLinea,numCol,TipoToken.CASTINGFLOAT);
								actual=this.siguienteCaracter();
								numCol=numCol+5;
								}else
										{br.reset();
										
										if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
											if (buffer.equals("(")){
													tok=new Token(numLinea,numCol,TipoToken.PARAP);
													numCol+=buffer.length();
													if (actual==' '){
														numCol++;
													}
													creado=true;
											}									
										}
										else tok= new Token(numLinea,numCol,TipoToken.ERROR);
									
										}
						
							} 
					}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
				
				
				
				break;
			case 18:
				if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					if (buffer.equals(")")){
						tok= new Token(numLinea,numCol,TipoToken.PARCLA);
						numCol+=buffer.length();
						if (actual==' '){
							numCol++;
						}
						creado=true;
					}	
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;	
			case 19:
				if (actual=='='){
					estado=20;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
				
			case 20:
				if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,TipoToken.DISTINTO);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
			
			case 21:
				if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,TipoToken.DESPDER);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
				//TODO el comentario va a ser chungo luego
			case 22:
				if (!(actual=='\r') && !(actual=='\t') && !(actual=='\n')){
					actual=siguienteCaracter();
					//tok= new Token(numLinea,numCol,TipoToken.ERROR);
					}else {
						buffer="";
						estado=0;
						//tok= new Token(numLinea,numCol,TipoToken.COMENTARIO);
					}
				break;

			case 23:
					estado=24;
					buffer+=actual;
					actual=siguienteCaracter();
				break;
				
			case 24:
				if (actual=='\''){
					estado=25;
					//buffer+=actual;
					actual=siguienteCaracter();
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
				
			case 25:
				if (esSeparador(actual) ||esDigito(actual) || esLetra(actual)){
					tok= new Token(numLinea,numCol,buffer,TipoToken.CHARACTER);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
				
			case 26:
				if (esDigito(actual)){
					estado=26;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (actual=='.'){
					estado=27;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if ((actual=='E')||(actual=='e')){
					estado=31;
					buffer+=actual;
					actual=siguienteCaracter();
				}
				else if (esSeparador(actual)){
					tok= new Token(numLinea,numCol,buffer,TipoToken.NATURAL);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
				
			case 27:
				if (esDigito(actual)){
					if (actual==0)
					{
					estado=29;
					buffer+=actual;
					actual=siguienteCaracter();
					}
					else
					{estado=28;
					buffer+=actual;
					actual=siguienteCaracter();
					}
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
				
			case 28:
				if (esDigito(actual)){
					
					if (actual==0)
					{
						estado=30;
						buffer+=actual;
						actual=siguienteCaracter();	
					}
					else
					{	
					estado=28;
					buffer+=actual;
					actual=siguienteCaracter();
					}
				}
				else if (esSeparador(actual)){
					tok= new Token(numLinea,numCol,buffer,TipoToken.FLOAT);
					numCol+=buffer.length();
					if (actual==' '){
						numCol++;
					}
					creado=true;
				}else if((actual=='e')||(actual=='E'))
						{
						estado=31;
						buffer+=actual;
						actual=siguienteCaracter();
						}
						else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
				
			case 29:
					if (esDigito(actual)){
					
						if (actual==0)
						{
						estado=30;
						buffer+=actual;
						actual=siguienteCaracter();	
						}
						else
						{	
							estado=28;
							buffer+=actual;
							actual=siguienteCaracter();
						}
					}
					else if (esSeparador(actual)){
						tok= new Token(numLinea,numCol,buffer,TipoToken.FLOAT);
						numCol+=buffer.length();
						if (actual==' '){
								numCol++;
						}
					creado=true;
					}else tok= new Token(numLinea,numCol,TipoToken.ERROR);
				break;
				
			case 30:
				if (esDigito(actual)){
				
					if (actual==0)
					{
					estado=30;
					buffer+=actual;
					actual=siguienteCaracter();	
					}
					else
					{	
						estado=28;
						buffer+=actual;
						actual=siguienteCaracter();
					}
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
			break;
			
			case 31:
				if (esDigito(actual)){
				
					if (actual==0)
					{
					tok= new Token(numLinea,numCol,TipoToken.ERROR);	
					}
					else
					{	
						estado=32;
						buffer+=actual;
						actual=siguienteCaracter();
					}
				}
				else tok= new Token(numLinea,numCol,TipoToken.ERROR);
			break;
			
			case 32:
				if (esDigito(actual)){
					estado=32;
					buffer+=actual;
					actual=siguienteCaracter();	
					}
				else if (esSeparador(actual)){
					tok= new Token(numLinea,numCol,buffer,TipoToken.FLOAT);
					numCol+=buffer.length();
					if (actual==' '){
							numCol++;
					}
				creado=true;
				}else tok= new Token(numLinea,numCol,TipoToken.ERROR);
			break;
				
				
			}
		}
		if (fin==true){
				tok= new Token(numLinea, numCol,TipoToken.EOF);
		}	
		return tok;
	}
	
	private Token CreaTokenReservada() {
		// TODO Auto-generated method stub
		
		switch (reservadas.dameIndice(buffer)) {
		case 0:
			return new Token(numLinea,numCol,TipoToken.FLOAT);
				
		case 1:
			return new Token(numLinea,numCol,TipoToken.CHARACTER);
			
		case 2:
			return new Token(numLinea,numCol,TipoToken.NOT);
		
		case 3:
			return new Token(numLinea,numCol,TipoToken.NATURAL);
			
		case 4:
			return new Token(numLinea,numCol,TipoToken.INTEGER);
		
		case 5:
			return new Token(numLinea,numCol,TipoToken.BOOLEAN);
		
		case 6:
			return new Token(numLinea,numCol,TipoToken.READ);
		
		case 7:
			return new Token(numLinea,numCol,TipoToken.WRITE);
		
		case 8:
			return new Token(numLinea,numCol,TipoToken.TRUE);
			
		case 9:
			return new Token(numLinea,numCol,TipoToken.FALSE);
		
		case 10:
			return new Token(numLinea,numCol,TipoToken.AND);
		
		case 11:
			return new Token(numLinea,numCol,TipoToken.OR);			
				

		}
		return null;
	}


	public boolean esLetra(char c){
		return ((c>='a' && c<='z') || (c>='A' && c<='Z')||(c=='·')||(c=='È')||(c=='Ì')||(c=='Û')||(c=='˙'));
	}
	
	public boolean esDigito(char c){
		return (c>='0' && c<='9');		
	}
	
	public boolean esSeparador(char c){
		return (c==' ' ||
				c==':' ||
				c==';' ||
				c=='=' ||
				c=='<' ||
				c=='>' ||
				c=='(' ||
				c==')' ||
				c=='+' ||
				c=='-' ||
				c=='*' ||
				c=='/' ||
				c=='%' ||
				c=='\'' ||
				c=='|' ||
				c=='&' ||
				c=='#' ||
				c=='\t' ||
				c=='\r' );
	}
	
	
	
	private void crearTablaReservadas() {
		// TODO Auto-generated method stub
		reservadas.anadePalabra("float",0);
		reservadas.anadePalabra("char",1);
		reservadas.anadePalabra("not",2);
		reservadas.anadePalabra("natural",3);
		reservadas.anadePalabra("integer",4);
		reservadas.anadePalabra("boolean",5);
		reservadas.anadePalabra("in",6);
		reservadas.anadePalabra("out",7);
		reservadas.anadePalabra("true",8);
		reservadas.anadePalabra("false",9);
		reservadas.anadePalabra("and",10);		
		reservadas.anadePalabra("or",11);
		
		
	}
	
	private void iniciaLector() {
		// TODO Auto-generated method stub
		archivo= new File(archivoEntrada);
		try {
			fr= new FileReader(archivo);
			br= new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private char siguienteCaracter() {
		// TODO Auto-generated method stub
		try {
			br.mark(100);
	
			if (br.readLine()==null) fin=true;
			else br.reset();
			char c= (char) br.read();
			//para que no distinga entre may√∫sculas y min√∫sculas
			//TODO habra que quitar para distinguir entre mayusculas y minusculas
			//c = Character.toLowerCase(c);
			if (c=='\n'){
				numCol=0;
				numLinea++;
			}
			else if (c=='\032'){
				fin=true;
			}
			else if (c==' ' && buffer.length()==0){
				numCol++;
			}
			return c;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return '0';
	}
	
}
