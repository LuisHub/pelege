package main;

import jargs.gnu.*;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.UnknownOptionException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Vector;

import compilador.analizador_sintactico.*;

import vm.instruccion.Instruccion;
import vm.maquinavirtual.MaquinaVirtual;

public class Main {

	public static void main(String[] args) {

		//TESTING
		//*********************************************************************************
		/*AnalizadorSintactico analizador= new AnalizadorSintactico("entrada.txt");
		analizador.init();
		
		for (Instruccion i : analizador._instrucciones)
			System.out.println(i.toString());
		
		SerializeInstrucciones("instrucciones.dat", analizador._instrucciones);

		MaquinaVirtual vm = new MaquinaVirtual(true);
		vm.UnserializeInstrucciones("instrucciones.dat");
		vm.run();*/
		//*********************************************************************************
		
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option c = parser.addBooleanOption('c', "compilador");
		CmdLineParser.Option mv = parser.addBooleanOption('m', "maquinavirtual");		
		CmdLineParser.Option entrada = parser.addStringOption('i', "input");
		CmdLineParser.Option salida = parser.addStringOption('o', "output");
		CmdLineParser.Option t = parser.addBooleanOption('t', "traza");			
		String entradaValue = "";
		String salidaValue = "";
		boolean modoCompilador = false, modoMaquinaVirtual = false;
		boolean modoTraza = false;

		try {
			parser.parse(args);
		} catch (IllegalOptionValueException e) {
			e.printStackTrace();
		} catch (UnknownOptionException e) {
			e.printStackTrace();
		}
			
		try {
			modoCompilador = (Boolean) parser.getOptionValue(c);
		} catch (Exception e) {}
		try {
			modoMaquinaVirtual = (Boolean) parser.getOptionValue(mv);
		} catch (Exception e) {}
											
		if (!modoCompilador && !modoMaquinaVirtual)
			printUsage();
		
		if (modoCompilador){				
			entradaValue = (String) parser.getOptionValue(entrada);
			salidaValue = (String) parser.getOptionValue(salida);
			
			if (entradaValue==null || salidaValue==null)
				printUsage();
			
			Date d = new Date();
			Long x = d.getTime();
			
			AnalizadorSintactico analizador= new AnalizadorSintactico(entradaValue);
			if (analizador.init()){
				SerializeInstrucciones(salidaValue, analizador._instrucciones);
			
				d = new Date();
				Long y = d.getTime();
			
				System.out.println(salidaValue + " generado con éxito tras " + (y-x) + "ms.");
			}
		}
		else if (modoMaquinaVirtual){
			entradaValue = (String) parser.getOptionValue(entrada);
			
			if (entradaValue==null)
				printUsage();
			
			try {
				modoTraza = (Boolean) parser.getOptionValue(t);
			} catch (Exception e) {}
			
			MaquinaVirtual vm = new MaquinaVirtual(modoTraza);
			vm.UnserializeInstrucciones(entradaValue);
			vm.run();
		}												
	}
	
	public static void SerializeInstrucciones(String path, Vector<Instruccion> instrucciones){
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(path);
		    ObjectOutputStream oos = new ObjectOutputStream(fout);			
			oos.writeObject(instrucciones);	    
		    oos.close();			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void printUsage() {
		System.out.println("Uso: -c  --compilador");
		System.out.println("     -m  --maquinavirtual");		
		System.out.println("(compilador):");
		System.out.println("-i --input  fichero : fichero de entrada.");
		System.out.println("-o --output fichero : fichero de salida.");
		System.out.println("(maquina virtual):");
		System.out.println("-i fichero : fichero de entrada.");
		System.out.println("-t --traza : modo traza");		
		System.exit(2);
	}	

}
