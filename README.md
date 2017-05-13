<div id="table-of-contents">
<h2>Table of Contents</h2>
<div id="text-table-of-contents">
<ul>
<li><a href="#sec-1">1. GymRat</a>
<ul>
<li><a href="#sec-1-1">1.1. Commits</a>
<ul>
<li><a href="#sec-1-1-1">1.1.1. May 12th 2017</a></li>
<li><a href="#sec-1-1-2">1.1.2. May 8th 2017</a></li>
<li><a href="#sec-1-1-3">1.1.3. May 2nd 2017</a></li>
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

### May 12th 2017<a id="sec-1-1-1" name="sec-1-1-1"></a>

Replaced clunky addSet feature by adding a WorkoutInstanceManager
-   **WorkoutInstanceManager:** Essentially a cached version of the current WorkoutInstance. Allows modification for database IO, and retaining context on fragment navigation

Use recyclerView for display of editTexts - with InputFilters to only allow #s

### May 8th 2017<a id="sec-1-1-2" name="sec-1-1-2"></a>

Added workoutLogging functionality
-   **WorkoutInstance:** Extends the workout type, but has a date associated with the workout
-   **WorkoutInstanceExercise:** Extends Exercise, has a list of reps associated with it
-   **DatabaseIO:** Allows storage of workout and workoutLogs as .json file on local file. Methods locally have a toJSONObject method

### May 2nd 2017<a id="sec-1-1-3" name="sec-1-1-3"></a>

Initial commit
-   **Workout:** Consists a name, an arbitrary length list of exercises, and an arbitrary length list of associated tags
-   **Exercise:** Consists of an ExerciseType, name and description
-   **ExerciseType:** One of: MACHINE, BARBELL, DUMBELL, FREWEIGHT, CARDIO<sub>MACHINE</sub>, CARDIO<sub>FREE</sub> or UNDEFINED (&asymp; ERROR)

Contains the necessary fragments for basic navigation