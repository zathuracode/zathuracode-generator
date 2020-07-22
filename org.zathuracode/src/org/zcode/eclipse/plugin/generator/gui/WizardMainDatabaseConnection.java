package org.zcode.eclipse.plugin.generator.gui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.zcode.eclipse.plugin.generator.ZathuraGeneratorActivator;
import org.zcode.eclipse.plugin.generator.utilities.ConnectionModel;
import org.zcode.eclipse.plugin.generator.utilities.ConnectionsUtils;
import org.zcode.swt.utilities.ResourceManager;


/**
 * Zathuracode Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 * @see Wizard
 */
public class WizardMainDatabaseConnection extends Wizard {

	/** The wizard database connection. */
	private WizardPageDatabaseConnection wizardDatabaseConnection;

	/**
	 * The Constructor.
	 */
	public WizardMainDatabaseConnection() {
		super();
		
		setWindowTitle(Messages.title);
		setDefaultPageImageDescriptor(ResourceManager.getPluginImageDescriptor(ZathuraGeneratorActivator.getDefault(), "icons/balvardi-Robotic7070.jpg")); //$NON-NLS-1$
		wizardDatabaseConnection = new WizardPageDatabaseConnection();
	}

	/*
	 * 
	 * private Text txtConnectionURL; private Text txtUserName; private Text
	 * txtPassword; private Combo cmbDriverTemplate; private List listJARs;
	 * private Text txtDriverClassName; private Button btnTestDriver; private
	 * boolean testConnection=false; private Text txtDriverName;
	 */
	/**
	 * The Constructor.
	 *
	 * @param connectionName the connection name
	 */
	public WizardMainDatabaseConnection(String connectionName) {
		super();
		setWindowTitle(Messages.title);
		setDefaultPageImageDescriptor(ResourceManager.getPluginImageDescriptor(ZathuraGeneratorActivator.getDefault(), "icons/balvardi-Robotic7070.jpg")); //$NON-NLS-1$
		ConnectionModel connectionModel = ConnectionsUtils.getTheZathuraConnectionModel(connectionName);

		wizardDatabaseConnection = new WizardPageDatabaseConnection();
		wizardDatabaseConnection.setDriverTemplate(connectionModel.getDriverTemplate());
		wizardDatabaseConnection.setName(connectionModel.getName());
		wizardDatabaseConnection.setUrl(connectionModel.getUrl());
		wizardDatabaseConnection.setUser(connectionModel.getUser());
		wizardDatabaseConnection.setPassword(connectionModel.getPassword());
		wizardDatabaseConnection.setDriverClassName(connectionModel.getDriverClassName());
		//wizardDatabaseConnection.setJarPath(connectionModel.getJarPath());
		
		//Maven
		wizardDatabaseConnection.setConnectionArtifactId(connectionModel.getConnectionArtifactId());
		wizardDatabaseConnection.setConnectionGroupId(connectionModel.getConnectionGroupId());
		wizardDatabaseConnection.setConnectionVersion(connectionModel.getConnectionVersion());
		
		
		wizardDatabaseConnection.setTitle(Messages.WizardMainDatabaseConnection_4);
		wizardDatabaseConnection.setDescription(Messages.WizardMainDatabaseConnection_5);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {

		addPage(wizardDatabaseConnection);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		try {
			ConnectionModel connectionModel = new ConnectionModel(wizardDatabaseConnection.getCmbDriverTemplate().getText(), wizardDatabaseConnection
					.getTxtDriverName().getText(), wizardDatabaseConnection.getTxtConnectionURL().getText(), wizardDatabaseConnection.getTxtUserName()
					.getText(), wizardDatabaseConnection.getTxtPassword().getText(), wizardDatabaseConnection.getTxtDriverClassName().getText());
			
			//Pone la configuracion de Maven
			connectionModel.setConnectionArtifactId(wizardDatabaseConnection.getConnectionArtifactId());
			connectionModel.setConnectionGroupId(wizardDatabaseConnection.getConnectionGroupId());
			connectionModel.setConnectionVersion(wizardDatabaseConnection.getConnectionVersion());

			//Graba la configuracion de la nueva conexion si es exitosa
			ConnectionsUtils.saveConnectionModel(connectionModel);
			WizardPageSelectDBConnection.loadConnections();
		} catch (Exception e) {
			MessageDialog.openError(getShell(), Messages.WizardMainDatabaseConnection_6, e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Gets the wizard database connection.
	 *
	 * @return the wizard database connection
	 */
	public WizardPageDatabaseConnection getWizardDatabaseConnection() {
		return wizardDatabaseConnection;
	}

	/**
	 * Sets the wizard database connection.
	 *
	 * @param wizardDatabaseConnection the wizard database connection
	 */
	public void setWizardDatabaseConnection(WizardPageDatabaseConnection wizardDatabaseConnection) {
		this.wizardDatabaseConnection = wizardDatabaseConnection;
	}

}
