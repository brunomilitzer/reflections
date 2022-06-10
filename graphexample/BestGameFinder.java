package graphexample;

import graphexample.annotations.Annotations;
import graphexample.databases.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import graphexample.annotations.Annotations.Operation;
import graphexample.annotations.Annotations.DependsOn;
import graphexample.annotations.Annotations.FinalResult;

public class BestGameFinder {

    private Database database = new Database();

    @Operation("All-Games")
    public Set<String> getAllGames() {
        return this.database.readAllGames();
    }

    @Operation("Game-To-Price")
    public Map<String, Float> getGameToPrice( @DependsOn("All-Games") Set<String> allGames ) {
        return this.database.readGameToPrice( allGames );
    }

    @Operation("Game-To-Rating")
    public Map<String, Float> getGameToRating( @DependsOn("All-Games") Set<String> allGames ) {
        return database.readGameToRatings( allGames );
    }

    @Operation("Score-To-Games")
    public SortedMap<Double, String> scoreGames( @DependsOn("Game-To-Price") Map<String, Float> gameToPrice,
                                                 @DependsOn("Game-To-Rating") Map<String, Float> gameToRating ) {
        SortedMap<Double, String> gameToScore = new TreeMap<>( Collections.reverseOrder() );

        for ( String gameName : gameToPrice.keySet() ) {
            double score = (double) gameToRating.get( gameName ) / gameToPrice.get( gameName );
            gameToScore.put( score, gameName );
        }

        return gameToScore;
    }

    @FinalResult
    public List<String> getTopGames( @DependsOn("Score-To-Games") SortedMap<Double, String> gameToScore ) {
        return new ArrayList<>( gameToScore.values() );
    }

}
