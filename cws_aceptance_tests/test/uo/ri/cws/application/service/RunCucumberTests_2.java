package uo.ri.cws.application.service;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.junit.platform.engine.Constants;

@Suite

// common basic tests
@SelectClasspathResource("uo/ri/cws/application/service/mechanic")
@SelectClasspathResource("uo/ri/cws/application/service/invoice")
@SelectClasspathResource("uo/ri/cws/application/service/workorder")

// UO % 3 == 2 tests
@SelectClasspathResource("uo/ri/cws/application/service/payroll")
@SelectClasspathResource("uo/ri/cws/application/service/professionalgroup")
@SelectClasspathResource("uo/ri/cws/application/service/mechanicext")

@ConfigurationParameter(
	key = Constants.PLUGIN_PROPERTY_NAME, 
	value = "pretty, html:target/cucumber-results.html"
)
@ConfigurationParameter(
	key = Constants.SNIPPET_TYPE_PROPERTY_NAME, 
	value = "camelcase"
)
@ConfigurationParameter(
	key = Constants.GLUE_PROPERTY_NAME, 
	value = "uo.ri.cws.application.service"
)
public class RunCucumberTests_2 {}
