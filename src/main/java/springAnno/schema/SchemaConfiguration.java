package springAnno.schema;

import graphql.annotations.AnnotationsSchemaCreator;
import graphql.annotations.annotationTypes.directives.definition.GraphQLDirectiveDefinition;
import graphql.schema.GraphQLSchema;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springAnno.interfaces.AdditionalGraphQLType;
import springAnno.interfaces.MutationRoot;
import springAnno.interfaces.QueryRoot;
import springAnno.interfaces.SubscriptionRoot;

import java.util.Set;
import java.util.stream.Collectors;

import static graphql.annotations.AnnotationsSchemaCreator.newAnnotationsSchema;

@Configuration
public class SchemaConfiguration {
    @Autowired
    private QueryRoot queryRoot;

    @Autowired(required = false)
    private MutationRoot mutationRoot;

    @Autowired(required = false)
    private SubscriptionRoot subscriptionRoot;

    @Autowired(required = false)
    private Set<AdditionalGraphQLType> additionalGraphQLTypes;

    @Value("${directives.package}")
    private String directivesPackage;

    @Bean
    GraphQLSchema schema() {
        AnnotationsSchemaCreator.Builder builder = newAnnotationsSchema();
        builder.query(queryRoot.getClass());
        if (mutationRoot != null) {
            builder.mutation(mutationRoot.getClass());
        }
        if (subscriptionRoot != null) {
            builder.subscription(subscriptionRoot.getClass());
        }

        Reflections reflections = new Reflections(directivesPackage);

        Set<Class<?>> directiveDeclarations = reflections.getTypesAnnotatedWith(GraphQLDirectiveDefinition.class);

        builder.directives(directiveDeclarations);

        if (additionalGraphQLTypes!=null) {
            builder.additionalTypes(additionalGraphQLTypes.stream().map(AdditionalGraphQLType::getClass).collect(Collectors.toSet()));
        }

        return builder.build();
    }
}
