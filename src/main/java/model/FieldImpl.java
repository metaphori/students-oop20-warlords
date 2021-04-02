package model;

import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

public final class FieldImpl implements Field {

    private final List<Lane> lanes;
    private final int cellsNumber;
    private final int laneNumber;

    private static final String MESSAGE_OUT_OF_FIELD = "The entered lane is out of the limits";

    public FieldImpl(final int cellsNumber, final int laneNumber) {
        this.cellsNumber = cellsNumber;
        this.laneNumber = laneNumber;
        this.lanes = Stream.generate(() -> cellsNumber)
                .limit(laneNumber)
                .map(LaneImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public void addUnit(final int laneIndex, final Unit unit) {
        if (laneIndex < 0 || laneIndex >= this.lanes.size()) {
            throw new IndexOutOfBoundsException(MESSAGE_OUT_OF_FIELD);
        }
        this.lanes.get(laneIndex).addUnit(unit);
    }

    @Override
    public List<Lane> getLanes() {
        return Collections.unmodifiableList(this.lanes);
    }


    @Override
    public Optional<Integer> getScore(final PlayerType player) {
        return this.lanes.stream()
                .map(l -> l.getScore(player))
                .reduce((s1, s2) -> s1 + s2);
    }

    @Override
    public Map<Unit, Pair<Integer, Integer>> getUnits() {
        final Map<Unit, Pair<Integer, Integer>> units = new HashMap<>();
        this.lanes.forEach(l -> l.getUnits().entrySet()
                .forEach(e -> units.put(e.getKey(), Pair.of(e.getValue(), lanes.indexOf(l)))));
        return Collections.unmodifiableMap(units);
    }

    @Override
    public void update() {
        this.lanes.forEach(l -> l.update());
    }

    @Override
    public int getLaneNumber() {
        return this.laneNumber;
    }

    @Override
    public int getCellsNumber() {
        return this.cellsNumber;
    }


}
