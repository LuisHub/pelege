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
	private int pc;
	private Instruccion instruccionActual;
	public double[] memoria;
	public double operando1;
	public double operando2;
	public Stack<Double> pila;
	public boolean modoTraza;
	public InputStreamReader isr;
	public BufferedReader br;
	
	public MaquinaVirtual(boolean t){
		modoTraza = t;
		if (modoTraza){
			isr = new InputStreamReader(System.in);
			br = new BufferedReader (isr);
		}
		instrucciones = new Vector<Instruccion>();
		memoria = new double[32];
		for (int i=0; i<32; i++)
			memoria[i] = 0;
		pila = new Stack<Double>();
		pc = 0;		
	}
	
	public void run(){
		instruccionActual = instrucciones.get(pc);
		while ((instruccionActual != null) && !(instruccionActual instanceof InstruccionSTOP)){						
			instruccionActual.ejecuta();
			if (modoTraza){
				System.out.println("PC->" + pc);				
				System.out.println(instruccionActual.toString());
				System.out.println("PILA");
				System.out.println(pila.toString());
				System.out.println("MEMORIA");
				imprimeMemoria();				
				try {
					br.readLine();
				} catch (IOException e) {}
			}			
			pc++;			
			try{
				instruccionActual = instrucciones.get(pc);
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
		for (int i=0;i<32;i++){
			if ((i+1)%8 == 0)
				System.out.println("["+memoria[i]+"]");
			else
				System.out.print("["+memoria[i]+"]");		
		}
	}
	
}
