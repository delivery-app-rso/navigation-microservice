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
public class NavigationResource {//should be ok

        private Logger log = Logger.getLogger(NavigationResource.class.getName());

        @Inject
        private NavigationBean navigationBean;

        @Context
        protected UriInfo uriInfo;

        @Operation(description = "Get all destinations.", summary = "Get all destinations")
        @APIResponses({
                        @APIResponse(responseCode = "200", description = "List of addresses", content = @Content(schema = @Schema(implementation = Navigation.class, type = SchemaType.ARRAY)), headers = {
                                        @Header(name = "X-Total-Count", description = "Number of objects in list") }) })
        @GET
        public Response getAddresses() {

                List<Navigation> navigationMetadata = navigationBean.getAddressFilter(uriInfo);

                return Response.status(Response.Status.OK).entity(navigationMetadata).build();
        }

        @Operation(description = "Get address.", summary = "Get address")
        @APIResponses({
                        @APIResponse(responseCode = "200", description = "Address data", content = @Content(schema = @Schema(implementation = Navigation.class))) })
        @GET
        @Path("/{addressId}")
        public Response getItem(
                        @Parameter(description = "user ID.", required = true) @PathParam("addressId") Integer addressId) {

                Navigation item = navigationBean.getAddresses(addressId);

                if (item == null) {
                        return Response.status(Response.Status.NOT_FOUND).build();
                }

                return Response.status(Response.Status.OK).entity(item).build();
        }

        @Operation(description = "Add address.", summary = "Add address")
        @APIResponses({
                        @APIResponse(responseCode = "201", description = "Address successfully added."),
                        @APIResponse(responseCode = "405", description = "Validation error .")
        })
        @POST
        public Response createItem(
                        @RequestBody(description = "DTO object with item data.", required = true, content = @Content(schema = @Schema(implementation = Navigation.class))) Navigation item) {

                if ((item.getSender() == null || item.getReceiver() == null) || item.getSentOn() == null || item.getDeliveredOn() == null) {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                } else {
                        item = navigationBean.createItem(item);
                }

                return Response.status(Response.Status.CONFLICT).entity(item).build();

        }

        @Operation(description = "Update address.", summary = "Update address")
        @APIResponses({
                        @APIResponse(responseCode = "200", description = "Address successfully updated.")
        })
        @PUT
        @Path("{addressId}")
        public Response putUser(
                        @Parameter(description = "Address ID.", required = true) @PathParam("addressId") Integer addressId,
                        @RequestBody(description = "DTO object with item.", required = true, content = @Content(schema = @Schema(implementation = Navigation.class))) Navigation item) {

                item = navigationBean.putItem(addressId, item);

                if (item == null) {
                        return Response.status(Response.Status.NOT_FOUND).build();
                }

                return Response.status(Response.Status.NOT_MODIFIED).build();

        }

        @Operation(description = "Delete address.", summary = "Delete address")
        @APIResponses({
                        @APIResponse(responseCode = "200", description = "Address successfully deleted."),
                        @APIResponse(responseCode = "404", description = "Not found.")
        })
        @DELETE
        @Path("{addressId}")
        public Response deleteUser(
                        @Parameter(description = "Address ID.", required = true) @PathParam("addressId") Integer addressId) {

                boolean deleted = navigationBean.deleteItem(addressId);

                if (deleted) {
                        return Response.status(Response.Status.NO_CONTENT).build();
                } else {
                        return Response.status(Response.Status.NOT_FOUND).build();
                }
        }

}
