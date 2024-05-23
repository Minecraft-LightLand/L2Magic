# L2Magic

## Concepts

### Direction and Normal

## Data types

### ConfiguredEngine
<details>
<summary>All engine types</summary>

#### if
#### list
#### delay
#### move
#### processor

#### iterate
#### iterate_delayed
#### iterate_linear
#### iterate_arc
#### random_pos_fan

#### particle
#### block_particle
#### item_particle
#### dust_particle
#### transition_particle
</details>

### Modifiers

<details>
<summary>All modifier types</summary>

#### forward
Move `pos` in the direction of `dir` by `distance`
- param `distance` (float expression)


#### rotate
Rotate `dir` with normal vector of `normal`
- param `degree` (float expression): degree to rotate for Y-Rot
- param `vertical` (float expression, default 0): degree to rotate for X-Rot (positive means closer to normal)

#### offset
Move `pos` with absolute offset
- param `x` (float expression, default 0)
- param `y` (float expression, default 0)
- param `z` (float expression, default 0)

#### direction
Set `dir` to an absolute value. Will be normalized if it's not unit vector
- param `x` (float expression, default 0)
- param `y` (float expression, default 0)
- param `z` (float expression, default 0)

#### random_offset
Move `pos` with a random offset
- param `shape` (enum), specifies the shape of the random distribution.
- param `x` (float expression, default 0): scales x-axis of the random vector
- param `y` (float expression, default 0): scales y-axis of the random vector
- param `z` (float expression, default 0): scales z-axis of the random vector

Shapes:
- `RECT`: uniform distribution of a `[-1,1]^3` unit cube. Same as using `offset` with `rand(-x,x)`, `rand(-y,y)`, `rand(-z-z)`
- `SPHERE`: uniform distribution on surface of a unit sphere. 
- `GAUSSIAN`: uniform Gaussian distribution.

#### set_normal
Set `normal` to an absolute value. Will be normalized if it's not unit vector
- param `x` (float expression, default 0)
- param `y` (float expression, default 0)
- param `z` (float expression, default 0)
- 
#### direction_to_normal
Let `dir` = `normal`

#### normal_to_direction
Let `normal` = `dir`

#### move_to_caster
Set `pos` to current caster position

#### align_with_caster
Set `dir` to current caster facing

</details>

### EntitySelectors
<details>
<summary>All selector types</summary>

#### self
Selects the caster itself

#### move
Moves the position and orientation of the selector using modifiers, then invoke subsequent selectors
- param `modifiers`: a list of modifiers to move
- param `child`: subsequent selectors using new position and orientation

#### box
Selects with a single bounding box of shape `[-size/2, size/2]x[0, y]x[-size/2, size/2]`.
- param `size` (float expression): width of the box
- param `y` (float expression): height of the box

Note that the position is used as the bottom center of the box, not the middle center.

#### compound
Merges results of multiple selectors
- param `function` (enum): `UNION` for merging
- param `selectors`: a list of selectors to merge

#### line
A series of box selectors in a line
- param `step` (int expression): number of steps to move forward
- param `size` (float expression): size of the bounding box, also the step size to move forward

Note that there will be `step+1` boxes, from `pos` to `pos+dir*size*step`

#### arc
A series of box selectors in an arc
- param `step` (int expression): number of steps to move forward
- param `size` (float expression): size of the bounding box, also the step size to move forward

Note that there will be `step+1` boxes, from `pos` to `pos+dir*size*step`

#### cylinder
Approximated Cylinder selector with 5 boxes

#### ball
Approximated Ball selector with 7 boxes
 
</details>

### EntityProcessors
<details>
<summary>All processor types</summary>

#### damage
#### knockback
#### push
#### effect
#### property

</details>