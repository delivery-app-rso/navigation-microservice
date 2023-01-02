package si.fri.rso.navigationmicroservice.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.navigationmicroservice.lib.Navigation;
import si.fri.rso.navigationmicroservice.lib.NavigationDto;
import si.fri.rso.navigationmicroservice.services.beans.NavigationBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/navigation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NavigationResource {

        private Logger log = Logger.getLogger(NavigationResource.class.getName());

        @Inject
        private NavigationBean navigationBean;

        @Context
        protected UriInfo uriInfo;

        @Operation(description = "Get all navigations.", summary = "Get all navigations")
        @APIResponses({
                        @APIResponse(responseCode = "200", description = "List of navigations", content = @Content(schema = @Schema(implementation = Navigation.class, type = SchemaType.ARRAY)), headers = {
                                        @Header(name = "X-Total-Count", description = "Number of objects in list") }) })
        @GET
        public Response getNavigations() {

                List<Navigation> navigationMetadata = navigationBean.getNavigationFilter(uriInfo);

                return Response.status(Response.Status.OK).entity(navigationMetadata).build();
        }

        @Operation(description = "Get navigation by delivery id.", summary = "Get navigation")
        @APIResponses({
                        @APIResponse(responseCode = "200", description = "Address data", content = @Content(schema = @Schema(implementation = Navigation.class))) })
        @GET
        @Path("/{deliveryId}")
        public Response getNavigation(
                        @Parameter(description = "user ID.", required = true) @PathParam("navigationId") Integer navigationId) {

                Navigation item = navigationBean.getNavigation(navigationId);

                if (item == null) {
                        return Response.status(Response.Status.NOT_FOUND).build();
                }

                return Response.status(Response.Status.OK).entity(item).build();
        }

        @Operation(description = "Add navigation.", summary = "Add navigation")
        @APIResponses({
                        @APIResponse(responseCode = "201", description = "Navigation successfully added."),
                        @APIResponse(responseCode = "405", description = "Validation error .")
        })
        @POST
        public Response createNavigation(
                        @RequestBody(description = "DTO object with navigation data.", required = true, content = @Content(schema = @Schema(implementation = NavigationDto.class))) NavigationDto navigationDto) {
                System.out.println("we here boss");
                if ((navigationDto.getDeliveryId() == null || navigationDto.getDestination() == null)
                                || navigationDto.getOrigin() == null) {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                }

                Navigation navigation = navigationBean.createNavigation(navigationDto);

                return Response.status(Response.Status.OK).entity(navigation).build();

        }
}
