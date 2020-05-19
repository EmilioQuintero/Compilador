package main;

import java.util.ArrayList;

import javax.swing.JTextPane;

import main.Lexer;
import main.Parser2;
import main.Semantic;
import codigoIntermedio.generadorCuadruplos;
import main.Identificador;
import main.Token;

public class Analizador {
	
	private ArrayList<Token> arr = new ArrayList<>();
	private ArrayList<Identificador> symTab;
	private String output = "",message = "",file="";
	//private boolean algo = true;
	private Lexer lex = new Lexer();
	private Parser2 p;
	private Semantic sem;
	public boolean show;
	private JTextPane container;
	public Object cuadrupleList,stringList;
	
	public ArrayList<Token> retArr(){
		return arr;
	}
	public ArrayList<Identificador> retArrS(){
		/*try {
			return p.r();
		} catch (NullPointerException e) {
			return new ArrayList<Identificador>(0);
		}*/
		return symTab;
		
	}
	public void setView(JTextPane view){
		container = view;
	}
	public String retMessage(){ return message; }
	public String retFile(){ return file; }
	public String compilation(String input){
		output = "";
		show = false;
		if( lex.lexico(input.toCharArray())){
			output += "\tNo hay errores lexicos\n";
			arr = lex.retComp();
			p = new Parser2(arr);
			output += p.sintacticAnalize();
		}else 
			output = lex.retMsg();
		
		if( output.equals("\tNo hay errores lexicos\n") ){
			output += "\tSin errores de analisis sintactico\n";
			sem = new Semantic(p.r());
			output += sem.semanticAnalize();
		}
		if( output.endsWith("Sin errores de analisis sintactico\n")){
			output += "\tNo hay errores semanticos\n"+
					"\n\tPrograma compilado exitosamente";
			show = true;
			generadorCuadruplos cuad = new generadorCuadruplos(p.r(),container);
			cuad.genarateCuadruples();
			message = cuad.retOutput();
			symTab = cuad.retTable();
			cuadrupleList = cuad.retObjs();
			stringList = cuad.retStr();
		}
		return output;
	}
	
}

