package com.bbva.cmek.lib.rme1.impl;

import com.bbva.cmek.lib.rme1.CMEKRME1;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.library.AbstractLibrary;

/**
 * This class automatically defines the libraries and utilities that it will use.
 */
public abstract class CMEKRME1Abstract extends AbstractLibrary implements CMEKRME1 {

	protected ApplicationConfigurationService applicationConfigurationService;


	/**
	* @param applicationConfigurationService the this.applicationConfigurationService to set
	*/
	public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}

}