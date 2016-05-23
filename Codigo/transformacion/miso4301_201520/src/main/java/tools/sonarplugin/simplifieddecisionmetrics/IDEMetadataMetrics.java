package tools.sonarplugin.simplifieddecisionmetrics;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

public class IDEMetadataMetrics implements Metrics {

	public static final Metric NUMBER_OF_PACKAGES = new Metric.Builder(
			"number_of_packages", // metric
			// identifier
			"Number of packages", // metric name
			Metric.ValueType.INT)
			// metric data type
			.setDescription("The name of packages used on the project")
			.setQualitative(false).setDomain(CoreMetrics.DOMAIN_GENERAL)
			.create();

	public List<Metric> getMetrics() {
		return Arrays.asList(NUMBER_OF_PACKAGES);
	}

}
