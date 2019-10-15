package springAnno.schema;

import graphql.annotations.AnnotationsSchemaCreator;
import graphql.annotations.annotationTypes.directives.definition.GraphQLDirectiveDefinition;
import graphql.schema.GraphQLSchema;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springAnno.interfaces.MutationRoot;
import springAnno.interfaces.QueryRoot;
import springAnno.interfaces.SubscriptionRoot;

import java.util.Set;

import static graphql.annotations.AnnotationsSchemaCreator.newAnnotationsSchema;

@Configuration
public class SchemaConfiguration {
    @Autowired
    private QueryRoot queryRoot;

    @Autowired(required = false)
    private MutationRoot mutationRoot;

    @Autowired(required = false)
    private SubscriptionRoot subscriptionRoot;

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

        Reflections reflections = new Reflections();

        Set<Class<?>> directiveDeclarations = reflections.getTypesAnnotatedWith(GraphQLDirectiveDefinition.class);

        builder.directives(directiveDeclarations);
        return builder.build();
    }
}
