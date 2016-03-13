package tools.sonarplugin.simplifieddecisionmetrics;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

public class IDEMetadataSensor implements Sensor {

	private ModuleFileSystem fileSystem;

	public IDEMetadataSensor(ModuleFileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	public boolean shouldExecuteOnProject(Project projectInfo) {
		return true;
	}

	public void analyse(Project projectInfo, SensorContext sensorContext) {
/*
		try {
			final KDMSourceDiscoverer disco = new KDMSourceDiscoverer();
			final Resource modelKDM = disco.getKDMModelFromDirectory(fileSystem
					.baseDir());
			System.out.println(modelKDM.getAllContents().next().toString());

			new SubKDM2SDMTransformation().execute();

			EmfModel model = EpsilonStandaloneExecuter.createEmfModel(
					"sourcemodel",
					"/simplifieddecisionmetrics/kdmResultadoSub.xmi",
					"/simplifieddecisionmetrics/SubKDM.ecore", true, false);

			Collection<EObject> resource = model
					.getAllOfKind("SimplifiedDecisionMetrics");
			System.out.println(resource.size());
			Iterator<EObject> iter = resource.iterator();
			while (iter.hasNext()) {
				EObject object = iter.next();
				EStructuralFeature feature = object.eClass()
						.getEStructuralFeature("measurements");
				System.out.println("Feature: " + feature);
				if (feature != null) {
					EcoreEList<EObject> measurements = (EcoreEList<EObject>) object
							.eGet(feature);
					System.out.println("measurements: " + measurements.size());
					for (EObject measurement : measurements) {
						feature = measurement.eClass().getEStructuralFeature(
								"value");
						String variableName = measurement.eClass().getName();
						Object variableNameObject = measurement.eGet(feature);
						// String variableValue;
						// if (variableNameObject != null) {
						// variableValue = variableNameObject.toString();
						// } else {
						// variableValue = "";
						// }
						// variableName = variableName + ':' +
						// variableValue;
						System.out.println(variableName);
						Measure measure = new Measure(
								IDEMetadataMetrics.NUMBER_OF_PACKAGES,
								Double.parseDouble(variableNameObject
										.toString())); // for
						// boolean
						// values,
						// 1
						// is
						// true,
						// 0
						// is
						// false
						sensorContext.saveMeasure(measure);
					}
				}
			}
			// Measure measure = new Measure(
			// IDEMetadataMetrics.NUMBER_OF_PACKAGES,
			// projectInfo.getLanguageKey()); // for
			// // boolean
			// // values,
			// // 1
			// // is
			// // true,
			// // 0
			// // is
			// // false
			// sensorContext.saveMeasure(measure);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		

	}
}
