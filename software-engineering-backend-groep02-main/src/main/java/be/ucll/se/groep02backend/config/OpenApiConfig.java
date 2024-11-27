package be.ucll.se.groep02backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(contact = @Contact(name = "Software engineering Groep02", url = "https://github.com/orgs/UcllSoftwareEngineering2324/teams/groep02/repositories"), description = "OpenApi documentation for Spring Security", title = "OpenApi specification - Group02", version = "1.0"), servers = {
        @Server(description = "Local ENV", url = "http://localhost:8080"),
        @Server(description = "Azure Acceptance ENV", url = "https://groep02-backend-acceptance.azurewebsites.net/"),
        @Server(description = "Azure Production ENV", url = "https://groep02-backend.azurewebsites.net/")
}, security = {
        @SecurityRequirement(name = "bearerAuth")
})
@SecurityScheme(name = "bearerAuth", description = "JWT auth description", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {
}
