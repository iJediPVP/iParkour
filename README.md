# iParkour
* This plugin allows player to set up custom parkour courses that includes:
  * Start/Finish Points
  * Checkpoints
  * Tracks player course time records.
* This plugin only allows for ONE Start and Finish point in each world. This means that server owners can only have ONE course per world.

**Commands**
* /iparkour
  * Displays a list of iParkour commands.
  * Usage: **/iparkour**
* /iparkour edit
  * Display a menu inventory that allows players to remove points that have been set.
  * Usage: **/iparkour edit**
* /iparkour checkpoint
  * Teleports the user to the last checkpoint that they reached.
  * Usage: **/iparkour checkpoint**

**Permissions**
* iParkour.*
  * Give access to all iParkour commands.
* iParkour.iparkour:
  * Give access to the /iparkour command.
* iParkour.iparkour.edit:
  * Give access to the /iparkour edit command as well as allow players to set parkour points.
* iParkour.iparkour.checkpoint:
  * Give access to the /iparkour checkpoint command.

**How to set points (This requires the permission: iParkour.iparkour.edit)**
* To set a Start/Finish point, simply place down a sign at the location you want the point to be and on the first line type:
  * Start: **/pkstart**
  * Finish: **/pkfinish**
* To set a Checkpoint:
  * Place down a sign at the location of where you want the point to be and put the following on the first line:
    * Checkpoint: **/pkcheckpoint**
  * On the second line of the sign, type an integer (whole number). Typing anything besides an integer on the second line will give you an error.