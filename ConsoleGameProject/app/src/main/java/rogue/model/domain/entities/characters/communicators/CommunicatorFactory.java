package rogue.model.domain.entities.characters.communicators;

import java.util.ArrayList;
import java.util.Random;

import rogue.model.domain.entities.ObjectT;
import rogue.model.domain.entities.Room;
import rogue.model.domain.enums.Directions;
import rogue.model.domain.enums.AttractivenessType;
import rogue.model.domain.enums.CommunicatorType;

public class CommunicatorFactory {
    public static Communicator getCommunicator(CommunicatorType type) {
        Communicator communicator = null;

        switch (type) {
            case PHLEGMATIC:
                communicator = new Communicator();
                break;
            case STUDENT:
                communicator = new Student();
                break;
            case NETWORKER:
                communicator = new Networker();
                break;
            case TRACTOR:
                communicator = new Tractor();
                break;
            case GUIDE:
                communicator = new Guide();
            default:
                break;
        }
        return communicator;
    }

    public static Communicator getCommunicator(CommunicatorType type, Room room) {
        Communicator communicator = null;

        switch (type) {
            case PHLEGMATIC:
                communicator = new Communicator(room);
                break;
            case STUDENT:
                communicator = new Student(room);
                break;
            case NETWORKER:
                communicator = new Networker(room);
                break;
            case TRACTOR:
                communicator = new Tractor(room);
                break;
            case GUIDE:
                communicator = new Guide(room);
            default:
                break;
        }
        return communicator;
    }

    public static Communicator getCommunicator(CommunicatorType type, ObjectT coords, double actPowers, int relationQuality, int impulsePower, AttractivenessType attractiveness,
            boolean isChasing, Directions dir) {
        Communicator communicator = null;

        switch (type) {
            case PHLEGMATIC:
                communicator = new Communicator(coords, actPowers, relationQuality, impulsePower, attractiveness, isChasing, dir);
                break;
            case STUDENT:
                communicator = new Student(coords, actPowers, relationQuality, impulsePower, attractiveness, isChasing, dir);
                break;
            case NETWORKER:
                communicator = new Networker(coords, actPowers, relationQuality, impulsePower, attractiveness, isChasing, dir);
                break;
            case TRACTOR:
                communicator = new Tractor(coords, actPowers, relationQuality, impulsePower, attractiveness, isChasing, dir);
                break;
            case GUIDE:
                communicator = new Guide(coords, actPowers, relationQuality, impulsePower, attractiveness, isChasing, dir);
            default:
                break;
        }
        return communicator;
    }

    public static ArrayList<Communicator> getCommunicatorsArray(Room room, int levelNum) {
        ArrayList<Communicator> communicatorArray = null;
        int numberOfCommunicators = countNumberOfCommunicators(levelNum);
        if (numberOfCommunicators != 0) {
            communicatorArray = new ArrayList<>(numberOfCommunicators);
            for (int i = 0; i < numberOfCommunicators; i++) {
                communicatorArray.add(getCommunicator(CommunicatorType.random(), room));
                communicatorArray.get(i).setPower(levelNum);
            }
        }
        return communicatorArray;
    }

    // монстров в комнате: до 2 уровня 0-1, до 5 уровня - 0-2, до 10 уровня 1-3, до 21 уровня 2-3; 
    private static int countNumberOfCommunicators(int levelNum) {
        int minCommunicators = levelNum <= 2 ? 0 : 
                            levelNum <= 10 ? 1 : 2;
        int maxCommunicators = levelNum <= 2 ? 2 : 
                            levelNum <= 5 ? 3 : 4;
        return new Random().nextInt(minCommunicators, maxCommunicators);
    }
}
