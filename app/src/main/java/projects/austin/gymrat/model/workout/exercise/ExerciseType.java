package projects.austin.gymrat.model.workout.exercise;

/**
 * Created by Austin on 2017-05-02.
 */

public enum ExerciseType {
    MACHINE("MACHINE"), BARBELL("BARBELL"), DUMBBELL("DUMBELL"), FREEWEIGHT("FREEWEIGHT"),
    CARDIO_MACHINE("CARDIO_MACHINE"), CARDIO_FREE("CARDIO_FREE"), UNDEFINED("UNDEFINED");
    String value;

    ExerciseType(String value){
        this.value = value;
    }

    public static ExerciseType getTypeFromString(String stringType){
        switch(stringType.trim().toLowerCase()){
            case "machine":
                return MACHINE;
            case "barbell":
                return BARBELL;
            case "dumbbell":
                return DUMBBELL;
            case "freeweight":
                return FREEWEIGHT;
            case "cardio_machine":
                return CARDIO_MACHINE;
            case "cardio_free":
                return CARDIO_FREE;
            default:
                return UNDEFINED;
        }
    }

    @Override
    public String toString(){
        return this.value;
    }
}
