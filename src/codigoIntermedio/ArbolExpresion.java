package codigoIntermedio;

import java.util.ArrayList;

import Entidades.Cuadruplos;
import main.Identificador;
import main.Token;

public class ArbolExpresion {
	
	private Node<Token> root,current;
	private boolean flag = false;
	private int counter = 0;
	public String result = "", temp = "";
	private Identificador token;
	private ArrayList<Identificador> symbols;
	private ArrayList<Cuadruplos> cuadruplos = new ArrayList<>();

	public ArbolExpresion(){}

	public ArbolExpresion(Identificador t,ArrayList<Identificador> symb){
		token = t; symbols = symb;
	}
	public ArrayList<Cuadruplos> retList(){
		return cuadruplos;
	}
	public void add(Token t){
		current = insert(current,t);
		flag = false;
	}

	public Node<Token> insert(Node<Token> current,Token t){
		Node<Token> newNode, aux;
		if( current == null){
			newNode = new Node<Token>(t);
			if( root != null){
				newNode.left = root;
				root.parent = newNode;
			}
			root = newNode;

			return newNode;
		}
		newNode = new Node<Token>(t);
		int currentPriority = priority(current), 
				newPriority = priority(newNode);
		if( currentPriority >= newPriority){
			aux = insert(current.parent,t);
			if( flag ){
				current.parent = aux;
				aux.left = current;
			}

			current = aux;
			flag=false; 
		}else{
			newNode.parent = current;
			current.right = newNode;
			current = newNode;
			flag = true;
		}

		return current;
	}

	private int priority(Node<Token> n){
		int val = -1;
		Token t = (Token) n.data;
		switch (t.getTipo()) {
		case Token.DIG: 
			case Token.ID: val = 3; break;
		case Token.AOP:
			if( t.getToken().matches("(\\*|/)") ){
				val = 2;
			}else{
				val = 1;
			}

			break;
		}
		return val;
	}
	private boolean assignment(){
		if( root.left == null && root.right == null )
			return true;
		return false;
	}
	public String generateCuadruple(Node<Token> node) {
	     if (node != null) {
	    	 String value1,value2,vv1=null,vv2=null;
	    	 value1 = generateCuadruple(node.left);
	         value2 = generateCuadruple(node.right);
	         if( node.data.getTipo() == Token.AOP){
	        	 int distance = 8;
	        	 counter++;
	        	 if( !value1.matches("(T\\d+|\\d+)")){
	        		 vv1 = retValue(value1);
	        	 }
	        	 if( !value2.matches("(T\\d+|\\d+)")){
	        		 vv2 = retValue(value2);
	        	 }

	        	 if( node.parent == null){
	        		 result += String.format("%5s %-"+(distance+2)+"s %-"+(distance+2)
	        				 +"s %-"+(distance+4)+"s %-"+(distance+0)+"s %n","",node.data.getToken(),value1,value2,token.getNombre());
	        		 cuadruplos.add(
	        				 new Cuadruplos(token.getNombre(), node.data.getToken(), value1, value2, vv1, vv2)	
	        				 );
	        	 }else{
	        	 	result += String.format("%5s %-"+(distance+2)+"s %-"+(distance+2)+"s %-"
	        		 +(distance+4)+"s %-"+(1+0)+"s %n","",node.data.getToken(),value1,value2,"Vt"+counter);
	        	 	cuadruplos.add(
	        				 new Cuadruplos("f"+counter, node.data.getToken(), value1, value2, vv1, vv2)	
	        				 );
	        	 }
	        	 return "Vt"+(counter);
	         }else{
	        	 if( assignment() )
	        		 result += String.format("%8s %s %s %n %n", "T1",":=",node.data.getToken());
	        	 return node.data.getToken();
	         }


	     }
	     return "";
	 }

	 public String solve(){
	 	String resultValue = "";
		 switch(token.getTipo()){
		 	case "int":
				resultValue = String.valueOf((int)solveGen(root));
				break;
		 	case "double":
				resultValue = String.valueOf(solveGen(root));
				break;
		 	case "float":
				resultValue = String.valueOf(solveGen(root))+"f";
				break;
		 }
		 	generateCuadruple(root);
			result += String.format("%s %s %s %n",token.getNombre(),":=",resultValue);
		 return resultValue;
	 }
	 
	 public double solveGen(Node<Token> node) {
	     if (node != null) {
	    	 double value1,value2;
	    	 value1 = solveGen(node.left);
	         value2 = solveGen(node.right);
	         if( node.data.getTipo() == Token.AOP){
	         	char c = node.data.getToken().charAt(0);
	         	double res = 0;
	        	 switch(c){
	        	 	case '*': res = value1 * value2; break;
	        	 	case '/': res = value1 / value2; break;
	        	 	case '+': res = value1 + value2; break;
	        	 	case '-': res = value1 - value2; break;
	        	 }

	        	 return res;
	         }else if( node.data.getTipo() == Token.ID)
	        	 return Double.parseDouble(retValue(node.data.getToken()));
	         else
	        	 return Double.parseDouble(node.data.getToken());

	     }
	     return 0;
	 }
	
	 private String retValue(String id){
		 for (Identificador identifier : symbols) {
			if( identifier.getNombre().equals(id) )
				return identifier.getValor();
		}
		 return null;
	 }
	class Node<Token>{
		Token data;
		Node<Token> parent,right,left;

		public Node(Token val){ data = val; }
	}
}