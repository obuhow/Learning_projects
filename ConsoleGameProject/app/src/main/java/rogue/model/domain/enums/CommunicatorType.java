package rogue.model.domain.enums;

import java.util.Random;

public enum CommunicatorType {
    PHLEGMATIC,
    STUDENT,
    NETWORKER,
    TRACTOR,
    GUIDE,
    COMMUNICATOR_TYPE_NUM;

    public static CommunicatorType random() {
        CommunicatorType communicatorTypes[] = CommunicatorType.values();
        return communicatorTypes[new Random().nextInt(COMMUNICATOR_TYPE_NUM.ordinal())];
    }

}