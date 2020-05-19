package main;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Console extends DefaultStyledDocument{

	private static final long serialVersionUID = 1L;
	private StyleContext style;
	private AttributeSet attr,attrBlack;

	private ArrayList<coloreado> a;
	String actualString,oldString;
	public Console() {
		style = StyleContext.getDefaultStyleContext();
        attr = style.addAttribute(style.getEmptySet(),StyleConstants.Foreground,Color.red);
        attrBlack = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
	}

	@Override
	public void insertString(int arg0, String arg1, AttributeSet arg2) throws BadLocationException {
		super.insertString(arg0, arg1, arg2);
		algo();
	}

	@Override
	public void remove(int arg0, int arg1) throws BadLocationException {
		super.remove(arg0, arg1);
		algo();
	}

	private synchronized void algo() throws BadLocationException{
		MutableAttributeSet asnew = null;
		a = new ArrayList<>();
		colorea();
		asnew = new SimpleAttributeSet(attrBlack.copyAttributes());
    	StyleConstants.setBold(asnew, false);
        setCharacterAttributes(0,getText(0, getLength()).length(),asnew,true);

		for (int i = 0; i < a.size(); i++) {
			asnew = new SimpleAttributeSet(attr.copyAttributes());
        	StyleConstants.setBold(asnew, true);
            setCharacterAttributes(a.get(i).position, a.get(i).palabra.length(),asnew,true);
		}
	}

	private void colorea() throws BadLocationException{
		String t = getText(0, getLength()), P = "";
		actualString = t;
		t += " ";
		int delimitador =  0;

		for (int i = 0; i < t.length(); i++) {
			char car = t.charAt(i);
			if(car == '"'){
				i++;
				car = t.charAt(i);
				P += car;
				while(car != '"'){
					P += car;
					i++;
					car = t.charAt(i);
				}

				i++;
				car = t.charAt(i);
				P += car;
				delimitador = i;
				if(P.length() > 0){
					a.add(new coloreado(delimitador-P.length(), P));
					P = "";
				}
			}
		}
		oldString = actualString;
	}
	class coloreado{
		int position;
		String palabra;
		coloreado(int po, String pa){
			position = po;
			palabra = pa;
		}
	}
}