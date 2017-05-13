package projects.austin.gymrat.model.Workout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Austin on 2017-05-02.
 * A particular exercise has:
 * - A name
 * - (TODO: Name variants)
 * - Description
 * - ExerciseType
 *
 */

public class Exercise {
    private String name;
    private String description;
    private ExerciseType exerciseType;

    public Exercise(String name, String description, ExerciseType exerciseType) {
        this.name = name;
        this.description = description;
        this.exerciseType = exerciseType;
    }

    /**
     * Factory design pattern, required to scrub input to create a new exercise
     * @param exerciseAsJSONString the Exercise to create as a JSON : String (DatabaseIO)
     * @return a new Exercise object or null if error occurred parsing
     */
    public static Exercise newInstance(String exerciseAsJSONString){
        try {
            JSONObject exerciseJSON = new JSONObject(exerciseAsJSONString);
            String name = exerciseJSON.getString("Name");
            String description = exerciseJSON.getString("Description");
            ExerciseType exerciseType = ExerciseType.getTypeFromString(exerciseJSON.getString("ExerciseType"));
            return new Exercise(name, description, exerciseType);
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Type: ").append(exerciseType.toString()).append("\n");
        return sb.toString();
    }

    public JSONObject toJSONObject(){
        JSONObject myJSONObject = new JSONObject();
        try {
            myJSONObject.put("Name", name);
            myJSONObject.put("Description", description);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return myJSONObject;
    }
}
