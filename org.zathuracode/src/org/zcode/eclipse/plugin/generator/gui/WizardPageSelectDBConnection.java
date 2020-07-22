package org.zcode.eclipse.plugin.generator.gui;

import java.sql.SQLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.zcode.eclipse.plugin.generator.utilities.ConnectionModel;
import org.zcode.eclipse.plugin.generator.utilities.ConnectionsUtils;
import org.zcode.eclipse.plugin.generator.utilities.EclipseGeneratorUtil;
import org.zcode.reverse.utilities.ZathuraReverseEngineeringUtil;

//import com.vortexbird.amazilia.plugin.sp.gui.WizardPageSelectStoreProcedure;


// TODO: Auto-generated Javadoc
/**
 * Zathuracode Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 * @see Wizard
 */
public class WizardPageSelectDBConnection extends WizardPage {
	
	
	
	/** The wizard database connection. */
	public WizardPageDatabaseConnection wizardDatabaseConnection;
	
	/** The list connections. */
	private static List listConnections;
	
	/** The link edit connection. */
	private Link linkEditConnection;
	
	/** The link remove connection. */
	private Link linkRemoveConnection;
	
	
	/**
	 * The Constructor.
	 */
	public WizardPageSelectDBConnection() {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.WizardPageSelectDBConnection_1);
		setDescription(Messages.WizardPageSelectDBConnection_2);
		setPageComplete(false);

	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NULL);	
		setControl(container);
		
		Label lblEspecifyTheLocation = new Label(container, SWT.NONE);
		lblEspecifyTheLocation.setBounds(10, 0, 534, 15);
		lblEspecifyTheLocation.setText(Messages.WizardPageSelectDBConnection_3);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 21, 554, 149);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		listConnections = new List(scrolledComposite, SWT.BORDER);
		listConnections.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				
				try {
					if(listConnections.getSelection().length==0){
						return;
					}
					linkEditConnection.setEnabled(true);
					linkRemoveConnection.setEnabled(true);
					validatePageComplete();
					
						
					String connectionSelected=(listConnections.getSelection()[0]);
					
					ConnectionModel connectionModel=ConnectionsUtils.getTheZathuraConnectionModel(connectionSelected);
					
					EclipseGeneratorUtil.connectionDriverName=		connectionModel.getName();
					EclipseGeneratorUtil.connectionDriverClass=		connectionModel.getDriverClassName();
					EclipseGeneratorUtil.connectionUrl=				connectionModel.getUrl();
					EclipseGeneratorUtil.connectionUsername=		connectionModel.getUser();
					EclipseGeneratorUtil.connectionPassword=		connectionModel.getPassword();
					
					EclipseGeneratorUtil.connectionDriverTemplate=	connectionModel.getDriverTemplate();
					
					//Maven
					EclipseGeneratorUtil.connectionArtifactId=		connectionModel.getConnectionArtifactId();
					EclipseGeneratorUtil.connectionGroupId=			connectionModel.getConnectionGroupId();
					EclipseGeneratorUtil.connectionVersion=			connectionModel.getConnectionVersion();
					
					
				
					
					//EclipseGeneratorUtil.loadJarSystem(EclipseGeneratorUtil.connectionDriverJarPath,connectionModel.getDriverClassName());
					//EclipseGeneratorUtil.loadJarSystem(EclipseGeneratorUtil.connectionDriverJarPath,connectionModel.getDriverClassName());
					
				
					
					
					ZathuraReverseEngineeringUtil.testDriver(EclipseGeneratorUtil.connectionUrl, 
															 EclipseGeneratorUtil.connectionDriverClass,
															 EclipseGeneratorUtil.connectionUsername,
															 EclipseGeneratorUtil.connectionPassword);
					
					MessageDialog.openInformation(getShell(), Messages.WizardPageSelectDBConnection_4, Messages.WizardPageSelectDBConnection_5);
					
					//TODO Cambiar esta locura por la fabrica
					WizardPageSelectTables.db=EclipseGeneratorUtil.connectionDriverTemplate;
					
					if(getNextPage() instanceof WizardPageSelectTables){
						WizardPageSelectTables wizardSelectTables=(WizardPageSelectTables)getNextPage();						
						wizardSelectTables.resetForm();					

					}
					
					
					validatePageComplete();
					
				} catch (ClassNotFoundException e1) {					
					setPageComplete(false);
					MessageDialog.openError(getShell(), Messages.WizardPageSelectDBConnection_6, e1.getMessage());
				} catch (SQLException e1) {
					setPageComplete(false);
					MessageDialog.openError(getShell(), Messages.WizardPageSelectDBConnection_7, e1.getMessage());
				} catch (Exception e1) {
					setPageComplete(false);
					MessageDialog.openError(getShell(), Messages.WizardPageSelectDBConnection_8, e1.getMessage());
				}
			}
		});
		
		scrolledComposite.setContent(listConnections);
		scrolledComposite.setMinSize(listConnections.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setBounds(10, 208, 554, 30);
		lblDescription.setText(Messages.WizardPageSelectDBConnection_9);
		
		Link linkCreateNewConnection = new Link(container, SWT.NONE);
		linkCreateNewConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 WizardMainDatabaseConnection wizardMainSelectDBConnection=new WizardMainDatabaseConnection();
				 WizardDialog dlg = new WizardDialog(getShell(),wizardMainSelectDBConnection);
				 dlg.open();
				
			}
		});
		
		linkCreateNewConnection.setBounds(52, 176, 124, 15);
		linkCreateNewConnection.setText(Messages.WizardPageSelectDBConnection_10);
		
		linkEditConnection = new Link(container, SWT.NONE);
		linkEditConnection.setEnabled(false);
		linkEditConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					
					String conectionSelected[]=listConnections.getSelection()!=null?listConnections.getSelection():null;
					if(conectionSelected==null || conectionSelected.length==0){
						throw new Exception(Messages.WizardPageSelectDBConnection_11);
					}
					String connectionName=listConnections.getSelection()[0];
					WizardMainDatabaseConnection wizardMainSelectDBConnection=new WizardMainDatabaseConnection(connectionName);
					WizardDialog dlg = new WizardDialog(getShell(),wizardMainSelectDBConnection);					
					dlg.open();
					
					
					
				} catch (Exception e2) {
					MessageDialog.openError(getShell(), Messages.WizardPageSelectDBConnection_12, e2.getMessage());
				}			
			}
		});
		linkEditConnection.setBounds(228, 176, 124, 15);
		linkEditConnection.setText(Messages.WizardPageSelectDBConnection_13);
		
		linkRemoveConnection = new Link(container, SWT.NONE);
		linkRemoveConnection.setEnabled(false);
		linkRemoveConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					String conectionSelected[]=listConnections.getSelection()!=null?listConnections.getSelection():null;
					if(conectionSelected==null || conectionSelected.length==0){
						throw new Exception(Messages.WizardPageSelectDBConnection_14);
					}
					String connectionName=listConnections.getSelection()[0];
					ConnectionsUtils.removeConnectionModel(connectionName);
					loadConnections();
				} catch (Exception e2) {
					MessageDialog.openError(getShell(), Messages.WizardPageSelectDBConnection_15, e2.getMessage());
				}	
			}
		});
		linkRemoveConnection.setBounds(404, 177, 124, 13);
		linkRemoveConnection.setText(Messages.WizardPageSelectDBConnection_16);
		
		loadConnections();

	}
	
	/**
	 * Load connections.
	 */
	public static void loadConnections(){
		java.util.List<String> theConnections=ConnectionsUtils.getConnectionNames();
		if(theConnections!=null && listConnections!=null){
			listConnections.removeAll();
			for (String connectionName : theConnections) {
				listConnections.add(connectionName);
			}
		}
		
		
	}
	
	/**
	 * Validate page complete.
	 */
	private void validatePageComplete(){			
		try {		
			setErrorMessage(null);
			setPageComplete(true);			
		} catch (Exception e) {
			setPageComplete(false);
			setErrorMessage(e.getMessage());
		}	
	}
}
