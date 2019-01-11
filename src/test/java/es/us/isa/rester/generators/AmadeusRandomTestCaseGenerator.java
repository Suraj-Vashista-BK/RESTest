package es.us.isa.rester.generators;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import es.us.isa.rester.configuration.TestConfigurationIO;
import es.us.isa.rester.configuration.pojos.TestConfigurationObject;
import es.us.isa.rester.specification.OpenAPISpecification;
import es.us.isa.rester.testcases.TestCase;
import es.us.isa.rester.testcases.writters.RESTAssuredWritter;
import es.us.isa.rester.util.TestConfigurationFilter;

public class AmadeusRandomTestCaseGenerator {

	@Test
	public void amadeusSearchHotels() {
		

		// Load specification
		String OAISpecPath = "src/main/resources/Amadeus/spec.json";
		OpenAPISpecification spec = new OpenAPISpecification(OAISpecPath);
		
		// Load configuration
		TestConfigurationObject conf = TestConfigurationIO.loadConfiguration("src/main/resources/Amadeus/confTest.json");
		
		// Set number of test cases to be generated on each path, on each operation
		int numTestCases = 10;
		
		// Create generator and filter
		AbstractTestCaseGenerator generator = new RandomTestCaseGenerator(spec, conf, numTestCases);
		
		List<TestConfigurationFilter> filters = new ArrayList<>();
		TestConfigurationFilter filter = new TestConfigurationFilter();
		filter.setPath("/hotels/search-airport");
		filter.addGetMethod();
		filters.add(filter);
		
		Collection<TestCase> testCases = generator.generate(filters);
		
		assertEquals("Incorrect number of test cases", numTestCases, testCases.size());
		
		// Write RESTAssured test cases
		RESTAssuredWritter writer = new RESTAssuredWritter();
		writer.setOAIValidation(true);
		writer.setLogging(true);
		String basePath = spec.getSpecification().getSchemes().get(0).name() + "://" + spec.getSpecification().getHost() + spec.getSpecification().getBasePath();
		writer.write(OAISpecPath, "src/generation/java", "AmadeusHotelSearch", null, basePath.toLowerCase(), testCases);

	}

}