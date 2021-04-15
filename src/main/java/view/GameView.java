package view;

import java.util.EnumMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import controllers.Controller;
import model.PlayerType;

/** 
 * This interface models the GameView.
 *
 */
public interface GameView {

    /**
     * Method to Update the selected lane.
     * @param playerType PlayerType
     * @param index int
     * @param next int
     * */
    void updateSelectLane(PlayerType playerType, int index, int next);

    /**
     * Method to Update the selected unit.
     * @param playerType PlayerType
     * @param index int
     * @param next int
     * */
    void updateSelectUnit(PlayerType playerType, int index, int next);

    /**
     * Method to Update the Timer.
     * @param mins int
     * @param seconds int
     * */
    void updateTimer(int mins, int seconds);

    /**
     * Method to Update the player timer.
     * @param seconds int
     * @param playerType PlayerType
     * */
    void updatePlayerTimer(int seconds, PlayerType playerType);

    /**
     * Method to set the Observer.
     * @param observer Controller
     * */
    void setObserver(Controller observer);

    /**
     * Method to draw a unit in the grid.
     * @param units EnumMap<UnitViewType, List<Pair<Integer, Integer>>>
     * */
    void show(EnumMap<UnitViewType, List<Pair<Integer, Integer>>> units);

}
