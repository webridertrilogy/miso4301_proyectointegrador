package tools.sonarplugin.simplifieddecisionmetrics;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.Extension;
import org.sonar.api.SonarPlugin;

public class IDEMetadataPlugin extends SonarPlugin {
	public List<Class<? extends Extension>> getExtensions() {
		return Arrays.asList(IDEMetadataMetrics.class, IDEMetadataSensor.class,
				IDEMetadataDashboardWidget.class);
	}
}