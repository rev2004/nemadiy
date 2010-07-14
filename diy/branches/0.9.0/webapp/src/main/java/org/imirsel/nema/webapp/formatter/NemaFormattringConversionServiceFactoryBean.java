/**
 * 
 */
package org.imirsel.nema.webapp.formatter;

import org.springframework.format.FormatterRegistry;
import org.imirsel.nema.model.MirexTask;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

/**
 * @author gzhu1
 *
 */
public class NemaFormattringConversionServiceFactoryBean extends
FormattingConversionServiceFactoryBean {
	@Override
	protected void installFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldType(MirexTask.class,new MirexTaskFormatter());
		super.installFormatters(registry);
	}
}
