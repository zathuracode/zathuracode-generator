package org.zcode.eclipse.plugin.generator.utilities;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.generator.exceptions.GeneratorNotFoundException;
import org.zcode.metadata.exceptions.MetaDataReaderNotFoundException;


// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 * @see IRunnableWithProgress
 */
public class RunningGeneration implements IRunnableWithProgress {
	
	private static final Logger log = LoggerFactory.getLogger(RunningGeneration.class);
	
	/** The shell. */
	private org.eclipse.swt.widgets.Shell shell;

	/**
	 * RunningGeneration constructor.
	 *
	 * @param shell the shell
	 */
	public RunningGeneration(Shell shell) {
		this.shell=shell;
	}

	/**
	 * Runs the long running operation.
	 *
	 * @param monitor the progress monitor
	 * @throws InvocationTargetException the invocation target exception
	 * @throws InterruptedException the interrupted exception
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		
		try {
			
			monitor.setTaskName("Generating Artifacts");
			monitor.beginTask("Generation in progress...", IProgressMonitor.UNKNOWN);
			
			EclipseGeneratorUtil.generate();

			monitor.subTask("Refresh " + EclipseGeneratorUtil.projectName + " project...");
			EclipseGeneratorUtil.project.refreshLocal(IResource.DEPTH_INFINITE, monitor);			
			
		} catch (MetaDataReaderNotFoundException e) {
			monitor.setCanceled(true);
			log.error("Error  in run MetaDataReaderNotFoundException",e);
			ZathuraGeneratorLog.logError(e);
			//MessageDialog.openError(getShell(), "Error","The generation was canceled MetaDataReaderNotFoundException"+e.getMessage());
			throw new InterruptedException("The generation was cancelled:"+e.toString());
		} catch (GeneratorNotFoundException e) {
			monitor.setCanceled(true);
			log.error("Error  in run GeneratorNotFoundException",e);
			ZathuraGeneratorLog.logError(e);
			//MessageDialog.openError(getShell(), "Error","The generation was canceled GeneratorNotFoundException"+e.getMessage());
			throw new InterruptedException("The generation was cancelled:"+e.toString());
		} catch (CoreException e) {
			monitor.setCanceled(true);
			log.error("Error  in run CoreException",e);
			ZathuraGeneratorLog.logError(e);
			//MessageDialog.openError(getShell(), "Error","The generation was canceled CoreException"+e.getMessage());
			throw new InterruptedException("The generation was cancelled:"+e.toString());
		}catch (Exception e) {
			monitor.setCanceled(true);
			log.error("Error  in run Exception",e);
			ZathuraGeneratorLog.logError(e);
			//MessageDialog.openError(getShell(), "Error","The generation was canceled Exception"+e.getMessage());
			throw new InterruptedException("The generation was cancelled:"+e.toString());
		}
		
		
		
		
		try {
			monitor.subTask("Building " + EclipseGeneratorUtil.projectName + " project...");
			EclipseGeneratorUtil.project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
		} catch (CoreException e) {
			log.error("Error  in Building Exception The generation was successful but the compilation has errors",e);
			ZathuraGeneratorLog.logError(e);
			MessageDialog.openWarning(getShell(), "Warning Message","The generation was successful but the compilation has errors");
			e.printStackTrace();
		}catch (Exception e) {
			monitor.setCanceled(true);
			log.error("Error  in Building Exception The generation was successful but the compilation has errors",e);
			ZathuraGeneratorLog.logError(e);
		}
		
		monitor.done();
		if (monitor.isCanceled()) {
			throw new InterruptedException("The generation was cancelled");
		}
	}
	
	/**
	 * Gets the shell.
	 *
	 * @return the shell
	 */
	public org.eclipse.swt.widgets.Shell getShell() {
		return shell;
	}

	/**
	 * Sets the shell.
	 *
	 * @param shell the shell
	 */
	public void setShell(org.eclipse.swt.widgets.Shell shell) {
		this.shell = shell;
	}
}
