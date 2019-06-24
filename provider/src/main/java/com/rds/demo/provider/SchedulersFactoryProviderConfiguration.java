package com.rds.demo.provider;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(
        name = "Schedulers Factory Provider COnfiguration"
)
public @interface SchedulersFactoryProviderConfiguration {

    @AttributeDefinition(
            name = ".blocking",
            type = AttributeType.STRING,
            description = "Blocking scheduler",
            required = false,
            options = {
                    @Option(label = "io", value = "IO"),
                    @Option(label = "computation", value = "COMPUTATION")
            }
    )
    String _blocking() default "IO";

    @AttributeDefinition(
            name = ".pooled",
            type = AttributeType.STRING,
            description = "Pooled scheduler",
            required = false,
            options = {
                    @Option(label = "io", value = "IO"),
                    @Option(label = "computation", value = "COMPUTATION")
            }
    )
    String _pooled() default "COMPUTATION";

}
