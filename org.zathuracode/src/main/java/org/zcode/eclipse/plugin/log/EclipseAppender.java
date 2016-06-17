package org.zcode.eclipse.plugin.log;

import java.io.IOException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * Este appender sirve para imprimir los log de ZathuraCode en la consola de eclipse
 * 
 * @author dgomez
 *
 */
public class EclipseAppender extends AppenderSkeleton {
	
	private static String CONSOLE_NAME="zathuraCodeConsole";
	
	private static MessageConsole myConsole=null;
	
	private static MessageConsoleStream out=null;

	public EclipseAppender() {
		
		if(myConsole==null || out==null){
			myConsole = findConsole(CONSOLE_NAME);
			out = myConsole.newMessageStream();
		    out.println("=============== Wellcome to Zathuracode is Cool ===============");
		}else{
			System.out.println("Zathuracode EclipseAppender: The Eclipse Console was not finded");
		}
		
	}
	
	@Override
	protected void append(LoggingEvent loggingEvent) {
		out.print(getLayout().format(loggingEvent));
	}
	
	/*
	 * Busca la consola de eclipse para poder escribir los log
	 */
	private MessageConsole findConsole(String name) {
	      ConsolePlugin plugin = ConsolePlugin.getDefault();
	      IConsoleManager conMan = plugin.getConsoleManager();
	      IConsole[] existing = conMan.getConsoles();
	      for (int i = 0; i < existing.length; i++)
	         if (name.equals(existing[i].getName()))
	            return (MessageConsole) existing[i];
	   
	      MessageConsole myConsole = new MessageConsole(name, null);
	      conMan.addConsoles(new IConsole[]{myConsole});
	      return myConsole;
	   }

	@Override
	public void close() {
		try {
			if(out!=null){
				out.close();
				myConsole=null;
				out=null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean requiresLayout() {
		return true;
	}	
}
