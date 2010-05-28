package vm.maquinavirtual;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.Stack;
import java.util.Vector;

import vm.instruccion.*;


public class MaquinaVirtual {

	private Vector<Instruccion> instrucciones;
	private int pc, sig_pc;;
	private Instruccion instruccionActual;
	public double[] memoria;
	public double operando1;
	public double operando2;
	public Stack<Double> pila;
	public boolean modoTraza;
	public InputStreamReader isr;
	public BufferedReader br;
	//--
	private int pHeap;
	private int[]regPunteros;//guarda la relacion entre un puntero y su apuntador
	private int tamMemoria=64;
	
	public MaquinaVirtual(boolean t){
		modoTraza = t;
		if (modoTraza){
			isr = new InputStreamReader(System.in);
			br = new BufferedReader (isr);
		}
		instrucciones = new Vector<Instruccion>();
		//memoria = new double[32];
		memoria = new double[tamMemoria];
		for (int i=0; i<tamMemoria; i++)
			memoria[i] = Double.NEGATIVE_INFINITY;
		pila = new Stack<Double>();
		setPc(0);
		setSig_pc(1);
		pHeap=memoria.length-1;
		regPunteros=new int[tamMemoria];
	}
	
	public void run(){
		instruccionActual = instrucciones.get(getPc());
		while ((instruccionActual != null) && !(instruccionActual instanceof InstruccionSTOP)){						
			instruccionActual.ejecuta();
			if (modoTraza){
				System.out.println("PC->" + getPc());				
				System.out.println(instruccionActual.toString());
				System.out.println("PILA");
				System.out.println(pila.toString());
				System.out.println("MEMORIA");
				imprimeMemoria();				
				try {
					br.readLine();
				} catch (IOException e) {}
			}			
			setPc(getSig_pc());
			setSig_pc(getSig_pc() + 1);			
			try{
				instruccionActual = instrucciones.get(getPc());
			}
			catch(Exception e){
				instruccionActual = null;
			}			
		}
		System.out.println("STOP.");
	}
	
	//revisar
	public void UnserializeInstrucciones(String path){
	    FileInputStream fin;
		try {
			fin = new FileInputStream(path);
		    ObjectInputStream ois = new ObjectInputStream(fin);
		    instrucciones = (Vector<Instruccion>) ois.readObject();
		    ois.close();			
		} catch (FileNotFoundException e) {
			System.out.println("Archivo " +path +" no encontrado.");
			System.exit(2);
		} catch (StreamCorruptedException e){
			System.out.println("Archivo " +path +" invalido.");
			System.exit(2);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		for (Instruccion i : instrucciones){
			i.setVm(this);
		}			
	}
	
	public void imprimeMemoria(){		
		for (int i=0;i<tamMemoria;i++){
			if ((i+1)%8 == 0)
				System.out.println("["+memoria[i]+"]");
			else
				System.out.print("["+memoria[i]+"]");		
		}
	}

	public void setSig_pc(int sig_pc) {
		this.sig_pc = sig_pc;
	}

	public int getSig_pc() {
		return sig_pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getPc() {
		return pc;
	}

	//reserva en el heap, devuelve la direccion del bloque reservado
	public int newVM(int tam) {
		//int dir=pHeap;
		pHeap = pHeap -tam; //para que ahora apunte al siguiente libre
		return pHeap+1;
	}
	public void deleteVM(int tam, double dirComienzo) {
		
		int aux=(int)dirComienzo-1;
		int aux2=(int)dirComienzo+tam-1;
		if(aux!=pHeap){			//si elimina algo de en medio, desplaza el heap
			int bloque=aux-pHeap;
			for (int i=0;i<bloque;i++)
				memoria[(int) aux2-i ] =memoria[aux-i];
		}

		pHeap = pHeap +tam; //para que ahora apunte al siguiente libre
		//actualizar los otros punteros
		for(int i=0;i<tamMemoria;i++){
			if ((0<regPunteros[i])&&(regPunteros[i]<dirComienzo))
				regPunteros[i]=regPunteros[i]+tam;
		}
		
	}

	public void ponRegistroPuntero(double dirReserva, double dirVariable) {
		regPunteros[(int)dirVariable]=(int)dirReserva;
		//la variable que apunta a dirReserva esta en dirVariable
	}
	public int[] getRegistroPuntero(){
		return regPunteros;
	}
	
}
