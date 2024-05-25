# L2Magic

# Concepts

## Direction and Normal

# Data types

## ConfiguredEngine
<details>
<summary>All engine types</summary>

### if
### list
### delay
### move
### processor

### iterate
### iterate_delayed
### iterate_linear
### iterate_arc
### random_pos_fan

### particle
### block_particle
### item_particle
### dust_particle
### transition_particle
</details>

## Modifiers

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

## EntitySelectors
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

## EntityProcessors
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