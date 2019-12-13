package presentation;

import domain.PlaylistTrack;
import domain.Track;
import dto.PlaylistRequest;
import dto.PlaylistResponse;
import dto.TrackRequest;
import dto.TrackResponse;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import exceptions.EntityNotFoundException;
import exceptions.InternalServerException;
import service.interfaces.PlaylistControllerInterface;
import service.interfaces.PlaylistTrackControllerInterface;
import service.interfaces.TracksControllerInterface;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/playlists")
public class PlaylistEndpoint {

    @Inject
    PlaylistControllerInterface playlistController;

    @Inject
    private TracksControllerInterface tracksController;

    @Inject
    private PlaylistTrackControllerInterface playlistTrackController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists(@QueryParam("token") String token) {
        try {
            PlaylistResponse playlistResponse = playlistController.getAllPlaylists(token);
            return Response.status(200).entity(playlistResponse).build();
        } catch (InternalServerException e) {
            return Response.status(500).build();
        } catch (EntityNotFoundException e) {
            return Response.status(400).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{playlistId}")
    public Response deletePlaylist(@PathParam("playlistId") Integer playlistId, @QueryParam("token") String token) {
        try {
            playlistController.deletePlaylist(playlistId);
            PlaylistResponse playlists = playlistController.getAllPlaylists(token);
            return Response.status(200).entity(playlists).build();
        } catch (DatabaseQueryException | EntityNotFoundException | DatabaseConnectionException e) {
            return Response.status(400).build();
        } catch (InternalServerException e) {
            return Response.status(500).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlaylist(PlaylistRequest playlistRequest, @QueryParam("token") String token) {
        try{
            playlistController.addPlaylist(playlistRequest, token);
            PlaylistResponse playlists = playlistController.getAllPlaylists(token);
            return Response.status(200).entity(playlists).build();
        } catch (DatabaseQueryException | EntityNotFoundException | DatabaseConnectionException e) {
            return Response.status(400).build();
        } catch (InternalServerException e) {
            return Response.status(500).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePlaylist(PlaylistRequest playlistRequest, @QueryParam("token") String token) {
        try{
            playlistController.updatePlaylist(playlistRequest);
            PlaylistResponse playlists = playlistController.getAllPlaylists(token);
            return Response.status(200).entity(playlists).build();
        } catch (DatabaseQueryException | EntityNotFoundException | DatabaseConnectionException e) {
            return Response.status(400).build();
        } catch (InternalServerException e) {
            return Response.status(500).build();
        }
    }


    @GET
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksInPlaylist(@PathParam("id") Integer playlistId) {
        try {
            TrackResponse trackResponse = tracksController.getTracksInPlaylist(playlistId);
            return Response.status(200).entity(trackResponse).build();
        } catch (DatabaseQueryException | DatabaseConnectionException e) {
            return Response.status(400).build();
        }
    }

    @DELETE
    @Path("/{id}/tracks/{trackId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTrackFromPlaylist(@PathParam("id") Integer playlistId, @PathParam("trackId") String trackId) {
        try {
            playlistTrackController.removeTrackFromPlaylist(playlistId, trackId);
            TrackResponse trackResponse = tracksController.getTracksInPlaylist(playlistId);
            return Response.status(200).entity(trackResponse).build();
        } catch (DatabaseQueryException | DatabaseConnectionException e) {
            return Response.status(400).build();
        }
    }

    @POST
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@PathParam("id") Integer playlistId, TrackRequest track) {
        try {
            PlaylistTrack playlistTrack = new PlaylistTrack(track.getId(), playlistId, track.isOfflineAvailable(), track.getPlayCount());
            playlistTrackController.addTrackToPlaylist(playlistTrack);
            TrackResponse trackResponse = tracksController.getTracksInPlaylist(playlistId);
            return Response.status(200).entity(trackResponse).build();
        } catch (DatabaseQueryException | DatabaseConnectionException e) {
            return Response.status(400).build();
        }
    }


    public void setPlaylistController(PlaylistControllerInterface playlistController) {
        this.playlistController = playlistController;
    }

    public void setPlaylistTrackController(PlaylistTrackControllerInterface playlistTrackController) {
        this.playlistTrackController = playlistTrackController;
    }

    public void setTracksController(TracksControllerInterface tracksController) {
        this.tracksController = tracksController;
    }
}
