package presentation;

import domain.Track;
import dto.TrackResponse;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import service.TracksController;
import service.interfaces.TracksControllerInterface;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tracks")
public class TracksEndpoint {

    @Inject
    private TracksControllerInterface tracksController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks(@QueryParam("forPlaylist") @DefaultValue("0") Integer playListId) {
        List<Track> tracks;
        try {
            if (playListId.equals(0)) {
                tracks = tracksController.getTracks();
            } else {
                tracks = tracksController.getTracksNotInPlaylist(playListId);
            }
        }
        catch (DatabaseQueryException | DatabaseConnectionException e) {
            return Response.status(400).build();
        }

        TrackResponse trackResponse =  new TrackResponse(tracks);
        return Response.status(200).entity(trackResponse).build();
    }

    public void setTracksController(TracksControllerInterface tracksController) {
        this.tracksController = tracksController;
    }
}
