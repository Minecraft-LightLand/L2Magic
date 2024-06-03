# L2Magic

# Concepts

## Direction and Normal

# Root Types and Parameters

## Default Parameters
For all cases:
- PosX: current cursor position
- PosY: current cursor position
- PosZ: current cursor position
- CasterX: current caster position
- CasterY: current caster position
- CasterZ: current caster position

With Scheduler:
- Time: current scheduler time

## Spell Action
Parameters:
- TickUsing: ticks player is using this spell
- Power: The power factor of this spell
- CastX: Position where this spell is casted
- CastY: Position where this spell is casted
- CastZ: Position where this spell is casted

## Projectile 
Parameters:
- TickCount: Ticks this projectile has existed
- ProjectileX: Position of this projectile (when root block is initiated)
- ProjectileY: Position of this projectile (when root block is initiated)
- ProjectileZ: Position of this projectile (when root block is initiated)

# Block Types

## Action Blocks (15)

<details>
<summary>All logic action block types</summary>

### if

Executes either one of the 2 action blocks based on a condition

- param `predicate` (bool expression): condition
- param `action` (action block, default empty): action block to execute when predicate results in true
- param `fallback` (action block, default empty): action block to execute when predicate results in false

### list

Executes multiple action blocks at the same time, with the same context

- param `children` (list of action blocks): action blocks to execute

### delay

Delays execution of an action block

- param `tick` (int expression): tick to delay
- param `child` (action block): action block to be executed later

### move

Move the position and orientation of execution context

- param `modifiers` (list of modifier): a list of modifiers to move
- param `child` (action block): subsequent action block using new position and orientation

### processor

Selects entity using entity selectors, and process them using entity processors

- param `selector` (entity selector): selector to determine a list of entities to process
- param `processors` (list of entity processor): a list of processors to run over for the entities selected
- param `target` (enum):
  - `ENEMY`: all entities that are not allied with spell caster
  - `ENEMY_NO_FAMILY` all entities that are not allied with spell caster, except the ones that are the same type and
    don't hate spell caster
  - `ALLY`: all entities that are allied with spell caster
  - `ALLY_OR_FAMILY` all entities that are allied with spell caster, or the ones that are the same type and don't hate
    spell caster

Note:

- Using `ENEMY_NO_FAMILY` can help to prevent friendly fire without needing to setup team system
- Using `ALLY_OR_FAMILY` can help to give positive effects to other players without needing to setup team system

</details>

<hr>

<details>
<summary>All iterator action block types</summary>

All iterators have an optional param called `index`. It for specifying the name of iterating index as variable.
It starts from 0, and ends at `step-1`. You can use it to do calculation for subsequent blocks.

### iterate
Execute an action block for multiple times
- param `step` (int expression): step to iterate
- param `child` (action block): action block to execute repeatedly
- param `index` (optional string): variable name for index. If you don't need index as variable, just don't add this to the block to improve efficiency.

Note that most iterators are actually simplification of an `iterate` block with another commonly used block. 

### iterate_delayed
Execute an action block for multiple times, with delays between them.
- param `step` (int expression): step to iterate
- param `delay` (int expression): delay in ticks between steps
- param `child` (action block): action block to execute repeatedly
- param `index` (optional string): variable name for index. If you don't need index as variable, just don't add this to the block to improve efficiency.

Note that first step (`index` = 0) will be executed immediately.

### iterate_linear
Execute an action block for multuple times over a sequence of points on a straight line
- param `step` (int expression): step to iterate
- param `child` (action block): action block to execute repeatedly
- param `index` (optional string): variable name for index. If you don't need index as variable, just don't add this to the block to improve efficiency.

Other optional parameters:
- param `aloneDir` (double expression, default 0): step size along `dir`
- param `offset` (static vector, default (0,0,0)): alternative direction to move toward
- param `aloneOffset` (double expression, default 0): step size along alternative direction
- param `startFromOrigin` (static bool, default true): whether to start from `pos` or `pos+dir*alongDir+offset*alongOffset`.
Note that in either case, `index` will start from 0.

### iterate_arc
Execute an action block for multuple times over a sequence of points on an arc
- param `count` (int expression): total number of times to iterate
- param `child` (action block): action block to execute repeatedly
- param `index` (optional string): variable name for index. If you don't need index as variable, just don't add this to the block to improve efficiency.
- param `radius` (float expression): radius of the arc
- param `minAngle` (float expression, default -180): starting angle in degree
- param `maxAngle` (float expression, default 180): ending angle in degree
- param `maxInclusive` (static bool, default false):
  - If true, `index = 0` will be at `minAngle`, and `index = count - 1` will be at `maxAngle`
  - If false, `index = 0` will be at `minAngle`, and `index = count - 1` will ba 1 step away from `maxAngle`

If angle between `minAngle` and `maxAngle` is 360 degree, using `maxInclusive = false` yields uniform distribution over a circle.
However, if the angle in between is smaller, it's recommended to set `maxInclusive` to true.

Note that if you set `maxInclusive = true` while having `count = 1`, game will crash

### random_pos_fan
Select several random points on a fan with specified angle range and radius range. The chance of selecting at any point is the same regardless of the distance to origin.

Can also be used to select points on a ring (maxAngle-minAngle=360), on a pie (minRadius = 0), or on a circle (intersection of previous 2 special cases).

- param `count` (int expression): total number of times to iterate
- param `child` (action block): action block to execute repeatedly
- param `index` (optional string): variable name for index. If you don't need index as variable, just don't add this to the block to improve efficiency.
- param `minRadiuss` (float expression): minimum radius to select a point
- param `maxRadiuss` (float expression): maximum radius to select a point
- param `minAngle` (float expression, default -180): starting angle in degree
- param `maxAngle` (float expression, default 180): ending angle in degree

Extra variables: Normally iterators gives one variable defined by `index`, but this block gives 2 extra variables if you hve `index` defined:
- `<index>_radius`: the actual radius selected
- `<index>_angle`: the actual angle selected
If you use `index="i"`, then `"i_radius"` and `"i_angle"` will also be added to variable list.

</details>
<hr>
<details>
<summary>All particle action block types</summary>

### particle
### block_particle
### item_particle
### dust_particle
### transition_particle

</details>

<hr>

## Modifiers (10)

<details>
<summary>All modifier types</summary>

### forward
Move `pos` in the direction of `dir` by `distance`
- param `distance` (float expression)


### rotate
Rotate `dir` with normal vector of `normal`
- param `degree` (float expression): degree to rotate for Y-Rot
- param `vertical` (float expression, default 0): degree to rotate for X-Rot (positive means closer to normal)

### offset
Move `pos` with absolute offset
- param `x` (float expression, default 0)
- param `y` (float expression, default 0)
- param `z` (float expression, default 0)

### direction
Set `dir` to an absolute value. Will be normalized if it's not unit vector
- param `x` (float expression, default 0)
- param `y` (float expression, default 0)
- param `z` (float expression, default 0)

### random_offset
Move `pos` with a random offset
- param `shape` (enum), specifies the shape of the random distribution.
- param `x` (float expression, default 0): scales x-axis of the random vector
- param `y` (float expression, default 0): scales y-axis of the random vector
- param `z` (float expression, default 0): scales z-axis of the random vector

Shapes:
- `RECT`: uniform distribution of a `[-1,1]^3` unit cube. Same as using `offset` with `rand(-x,x)`, `rand(-y,y)`, `rand(-z-z)`
- `SPHERE`: uniform distribution on surface of a unit sphere. 
- `GAUSSIAN`: uniform Gaussian distribution.

### set_normal
Set `normal` to an absolute value. Will be normalized if it's not unit vector
- param `x` (float expression, default 0)
- param `y` (float expression, default 0)
- param `z` (float expression, default 0)
- 
### direction_to_normal
Let `dir` = `normal`

### normal_to_direction
Let `normal` = `dir`

### move_to_caster
Set `pos` to current caster position

### align_with_caster
Set `dir` to current caster facing

</details>

<hr>

## EntitySelectors (8)
<details>
<summary>All selector types</summary>

### self
Selects the caster itself

### move
Moves the position and orientation of the selector using modifiers, then invoke subsequent selectors
- param `modifiers`: a list of modifiers to move
- param `child`: subsequent selectors using new position and orientation

### box
Selects with a single bounding box of shape `[-size/2, size/2]x[0, y]x[-size/2, size/2]`.
- param `size` (float expression): width of the box
- param `y` (float expression): height of the box
- param `center` (static bool, default false):
  - If true, use `pos` as the center of the box
  - If false, use `pos` as the center bottom of the box

### compound
Merges results of multiple selectors
- param `function` (enum): `UNION` for merging
- param `selectors`: a list of selectors to merge

### line
A series of box selectors in a line
- param `step` (int expression): number of steps to move forward
- param `size` (float expression): size of the bounding box, also the step size to move forward

Note that there will be `step+1` boxes, from `pos` to `pos+dir*size*step`

### arc
A series of box selectors in an arc
- param `step` (int expression): number of steps to divide the arc
- param `radius` (float expression): radius of the arc
- param `size` (float expression): size of the bounding box
- param `minAngle` (float expression, default -180): starting angle in degree
- param `maxAngle` (float expression, default 180): ending angle in degree

Note that there will be `step+1` boxes, from `minAngle` to `maxAngle`

### cylinder
Approximated Cylinder selector with 5 boxes, from bottom center of the cylinder
- param `r` (float expression): radius of the cylinder
- param `y` (float expression): height of the cylinder

### ball
Approximated Ball selector with 7 boxes, from center of the ball
- param `r` (float expression): radius of the ball

</details>

<hr>

## EntityProcessors (5)
<details>
<summary>All processor types</summary>

### damage
- param `damage_type`: damage type id, vanilla or modded
- param `damage` (float expression) damage to deal
- param `indirect` (static bool, default false) make the damage indirect
- param `positioned` (static bool, default true) supply damage origin. 
Vanilla use it to calculate shield blocking, default damange knockback, etc

### knockback
Knock back target, facing from damage position to target.
Triggers knockback events.
Exact effect affected by knockback resistance.
- param `knockback` (float expression), knockback strength
- param `angle` (float expression, default 0), angle to shift the knockback direction in Y-Rot
- param `tilt` (float expression, default 0), angle to shift the knockback direction in X-Rot

Note that y-axis component in knockback vector will be discarded. Use `push` if you want y-axis movement.

### push
Push target, facing depending on parameter. Unaffected by knockback resistance.
- param `speed` (float expression), push strength
- param `angle` (float expression, default 0), angle to shift the push direction in Y-Rot
- param `tilt` (float expression, default 0), angle to shift the push direction in X-Rot
- param `vector`: enum that determines push direction:
  - `UNIFORM`: push direction aligns with `dir`
  - `TO_CENTER`: push direction is a factor from `pos` to entity center
  - `TO_BOTTOM`: push direction is a factor from `pos` to entity position

### effect
- param `effect`: mob effect ID
- param `duration` (int expression): duration of effect in ticks
- param `amplifier` (int expression, default 0): level of effect, Lv.I is 0
- param `ambient` (static bool, default false): if the effect is ambient (constantly refreshed)
- param `visible` (static bool, default true): if the effect shows effect particles

### property
- param `duration` (int expression)
- param `property`: enum that determines which property to set
  - `IGNITE` set entity to be ignited for x ticks
  - `FREEZE` set entity to be frozen for x ticks already 
(note that entity unfroze at double rate, so it's x/2 ticks to continue freezing)


</details>