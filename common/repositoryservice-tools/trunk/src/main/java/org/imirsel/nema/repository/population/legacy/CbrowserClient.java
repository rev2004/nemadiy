/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repository.population.legacy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.imirsel.nema.repository.DatabaseConnector;
import org.imirsel.nema.repository.RepositoryProperties;

/**
 *
 * @author kriswest
 */
public class CbrowserClient {

    public static final String GET_ALL_TRACK_METADATA = "SELECT " +
            "cbrowser.tracks.track_title, cbrowser.artists.artist_name, cbrowser.albums.album_title, cbrowser.genres.genre_label " +
            "FROM cbrowser.tracks, cbrowser.artists, cbrowser.albums, cbrowser.genres " +
            "WHERE cbrowser.tracks.track_internal_id=? " +
            "AND cbrowser.tracks.track_artist=cbrowser.artists.artist_id " +
            "AND cbrowser.tracks.track_album=cbrowser.albums.album_id " +
            "AND cbrowser.tracks.track_genre=cbrowser.genres.genre_id";
    PreparedStatement getTrackMeta;

    public static final String TITLE = "track_title";
    public static final String ARTIST = "artist_name";
    public static final String ALBUM = "album_title";
    public static final String GENRE = "genre_label";


    DatabaseConnector dbCon;

    public CbrowserClient() throws SQLException{
        dbCon = new DatabaseConnector(
                "cbrowser",
                RepositoryProperties.getProperty(RepositoryProperties.DB_LOCATOR),
                RepositoryProperties.getProperty(RepositoryProperties.DB_USER),
                RepositoryProperties.getProperty(RepositoryProperties.DB_PASS)
            );
        dbCon.connect();
        getTrackMeta = dbCon.con.prepareStatement(GET_ALL_TRACK_METADATA);
    }

    public Map<String,String> getTrackMetadata(String track_id) throws SQLException{
        getTrackMeta.setString(1, track_id);
        ResultSet rs = getTrackMeta.executeQuery();
        if(rs.next()){
            HashMap<String,String> out = new HashMap<String,String>();
            out.put(TITLE, rs.getString(TITLE));
            out.put(ARTIST, rs.getString(ARTIST));
            out.put(ALBUM, rs.getString(ALBUM));
            out.put(GENRE, rs.getString(GENRE));
            return out;
        }else{
            System.out.println("Failed to retrieve data from cbrowser for " + track_id);
            return null;
        }
    }

}
