<div id="table-of-contents">
<h2>Table of Contents</h2>
<div id="text-table-of-contents">
<ul>
<li><a href="#sec-1">1. GymRat</a>
<ul>
<li><a href="#sec-1-1">1.1. Commits</a>
<ul>
<li><a href="#sec-1-1-1">1.1.1. June 7th 2017</a></li>
<li><a href="#sec-1-1-2">1.1.2. May 27th 2017</a></li>
<li><a href="#sec-1-1-3">1.1.3. May 12th 2017</a></li>
<li><a href="#sec-1-1-4">1.1.4. May 8th 2017</a></li>
<li><a href="#sec-1-1-5">1.1.5. May 2nd 2017</a></li>
</ul>
</li>
</ul>
</li>
</ul>
</div>
</div>

# GymRat<a id="sec-1" name="sec-1"></a>

A simple workout logging application

## Commits<a id="sec-1-1" name="sec-1-1"></a>

### June 7th 2017<a id="sec-1-1-1" name="sec-1-1-1"></a>

-   Added an exercise manager, and IO (only input is done, need to complete save to disk feature)
-   Reconfigured Workout and WorkoutInstance structure:
    -   A Workout consists of: A name, and a list of exercises (string). **THAT IS ALL**
        
            {
                "Name": "Some Name",
                "Tags": ["Some value", "Some other value"],
                "Exercises": ["String key for exercise1", "String key for exercise2"]
            }
    
    -   A WorkoutInstance consists of: A name, a list of reps, and rest
        
            {
                  "Name":"Default_Chest2",
                  "ExerciseList":[
                    {
                      "Name":"Bench Press",
                      "Reps":[
                        12,
                        10,
                        8,
                        8,
                        5
                      ],
                      "Rest": 60
                    }
                  ],
                  "Tags": ["Chest"]
            }
    -   Exercise information must be pulled from exerciseManager, using the name as a key
        
            {
                "Name": "Run",
                "Description": "Lift legs off the ground in alternating sequence. ",
                "Type": "CARDIO_FREE"
            }

### May 27th 2017<a id="sec-1-1-2" name="sec-1-1-2"></a>

-   Refactored project as GymRat (changed graphics accordingly)
-   Added NewWorkoutFragment, allowing user to create and add new workout
    -   Requires a workout name, and at least 1 exercise
    -   Exercise creation done by AlertDialog with immediate show override to maintain window if form not complete
    -   Exercise reps addition by (finally) a reasonably elegant solution, using ViewHolder tag and getLayoutPosition
-   Added long-click menu for workout selection, replacing spinner with ListView
-   Timer toggle allows user to switch in between viewing exercises, and a rest timer with reset button fragment.

### May 12th 2017<a id="sec-1-1-3" name="sec-1-1-3"></a>

Replaced clunky addSet feature by adding a WorkoutInstanceManager
-   **WorkoutInstanceManager:** Essentially a cached version of the current WorkoutInstance. Allows modification for database IO, and retaining context on fragment navigation

Use recyclerView for display of editTexts - with InputFilters to only allow #s

### May 8th 2017<a id="sec-1-1-4" name="sec-1-1-4"></a>

Added workoutLogging functionality
-   **WorkoutInstance:** Extends the workout type, but has a date associated with the workout
-   **WorkoutInstanceExercise:** Extends Exercise, has a list of reps associated with it
-   **DatabaseIO:** Allows storage of workout and workoutLogs as .json file on local file. Methods locally have a toJSONObject method

### May 2nd 2017<a id="sec-1-1-5" name="sec-1-1-5"></a>

Initial commit
-   **Workout:** Consists a name, an arbitrary length list of exercises, and an arbitrary length list of associated tags
-   **Exercise:** Consists of an ExerciseType, name and description
-   **ExerciseType:** One of: MACHINE, BARBELL, DUMBELL, FREWEIGHT, CARDIO<sub>MACHINE</sub>, CARDIO<sub>FREE</sub> or UNDEFINED (&asymp; ERROR)

Contains the necessary fragments for basic navigation