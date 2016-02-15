package tools.sonarplugin.simplifieddecisionmetrics;

import org.sonar.api.web.Description;
import org.sonar.api.web.RubyRailsWidget;
import org.sonar.api.web.UserRole;
import org.sonar.api.web.AbstractRubyTemplate;
import org.sonar.api.web.WidgetCategory;

@UserRole(UserRole.USER)
@Description("Permite revisar la estructura del proyecto en base a metricas para tomar decisiones sobre modernización")
@WidgetCategory("Decisions")
public class IDEMetadataDashboardWidget extends AbstractRubyTemplate implements
		RubyRailsWidget {

	public String getId() {
		return "simplifieddecisionmetrics";
	}

	public String getTitle() {
		return "Simplified Decision Metrics";
	}

	@Override
	protected String getTemplatePath() {
		// uncomment next line for change reloading during development
		// return
		// "c:/projects/xxxxx/src/main/resources/xxxxx/sonar/idemetadata/idemetadata_widget.html.erb";
		return "/simplifieddecisionmetrics/sdm_widget.html.erb";
	}

}
