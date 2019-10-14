package springAnno.schema;

import graphql.annotations.AnnotationsSchemaCreator;
import graphql.schema.GraphQLSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springAnno.interfaces.DirectiveDeclaration;
import springAnno.interfaces.MutationRoot;
import springAnno.interfaces.QueryRoot;
import springAnno.interfaces.SubscriptionRoot;

import java.util.List;
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

    @Autowired
    private List<DirectiveDeclaration> directiveDeclarations;

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
        builder.directives(directiveDeclarations.stream().map(DirectiveDeclaration::getClass)
                .collect(Collectors.toSet()));
        return builder.build();
    }
}
