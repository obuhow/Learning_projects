package rogue.model.domain.entities.characters.communicators;

import rogue.model.domain.entities.Level;
import rogue.model.domain.entities.ObjectT;
import rogue.model.domain.entities.Room;
import rogue.model.domain.enums.Directions;
import rogue.model.domain.enums.AttractivenessType;
import rogue.model.domain.enums.CommunicatorType;

public class Networker extends Communicator {
    private boolean invisible;
    private int teleportCounter;

    public Networker() {
        super(new ObjectT(), 75.0, 75, 10, AttractivenessType.LOW, false, Directions.STOP);
        invisible = false;
        teleportCounter = 0;
    }

    public Networker(Room room) {
        super(room.getRandomFreeCellInRoom(), 75.0, 75, 25, AttractivenessType.HIGH, false, Directions.STOP);
        invisible = false;
        teleportCounter = 0;
    }

    public Networker(ObjectT coords, double actPowers, int relationQuality, int impulsePower, AttractivenessType attractiveness,
            boolean isChasing, Directions dir) {
        super(coords, actPowers, relationQuality, impulsePower, attractiveness, isChasing, dir);
        invisible = false;
        teleportCounter = 0;
    }

    public boolean isInvisible() {
        return invisible;
    }

    @Override
    public CommunicatorType getType() {
        return CommunicatorType.NETWORKER;
    }

    @Override
    protected void movePattern(Level level) { 
        teleportCounter++;
        if (teleportCounter >= 3) {
            teleportCounter = 0;
            int roomIdx = level.getRoomIndexByCoord(this);
            if (roomIdx != -1) {
                ObjectT p = level.getRoom(roomIdx).getRandomFreeCellInRoom();
                if (p != null) {
                    setCoordinates(p.getCoordinates());
                }
            }
            invisible = !invisible; 
        }
    }
}
