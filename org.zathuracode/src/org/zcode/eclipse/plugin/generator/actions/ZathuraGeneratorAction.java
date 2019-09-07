package org.zcode.eclipse.plugin.generator.actions;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.zcode.eclipse.plugin.generator.gui.WizardMainZathura;
import org.zcode.eclipse.plugin.generator.utilities.ConfigEclipsePluginPath;


// TODO: Auto-generated Javadoc
/**
 * Zathura Generator
 * 
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 * @see IWorkbenchWindowActionDelegate
 */
public class ZathuraGeneratorAction implements IWorkbenchWindowActionDelegate {
	
	/** The window. */
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public ZathuraGeneratorAction() {
		ConfigEclipsePluginPath.getInstance();
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 *
	 * @param action the action
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		
		
		
		//Valida que este instalado un JDK
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if(compiler==null){
			 MessageDialog.openWarning(window.getShell(),"Zathuracode Warning", "Javac not found. The eclipse was executed with a JRE. "
			 		+ "Please configure a JDK with JAVA_HOME for execute eclipse. Zathuracode not work without jdk because it is necessary to compile java code. Information available in http://docs.oracle.com/cd/E19182-01/820-7851/inst_cli_jdk_javahome_t/index.html");
			 return;
		}

		WizardMainZathura wizardMain = new WizardMainZathura();
		WizardDialog dialog = new WizardDialog(window.getShell(), wizardMain);
		dialog.create();
		dialog.open();
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 *
	 * @param action the action
	 * @param selection the selection
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 *
	 * @param window the window
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;

	}
}