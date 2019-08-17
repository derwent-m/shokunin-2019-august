# Shokunin Challenge, August 2019 : 10x Developer

## My Solution

Can encode inequalities as a matrix, E.G.

```md
  2x + 3y > 1
  - y < 2 - 3x
```

Can be written as

```md
  -2 -3 -1  // -2x -3y < -1
   3 -1  2  // 3x - y < 2
```

If there are 5 people, then they can be ranked 1 to 5 where 1 is the best, and 5 is the worst.

We can start by writing the boundary constraints. No one's rank is less than one or greater than 5

```md
 Je Ev Jo Sa Ma
 -- -- -- -- --
 -1  0  0  0  0 |  0              // Je > 0 => -Je < 0
  0 -1  0  0  0 |  0              // Ev > 0 => -Ev < 0
  0  0 -1  0  0 |  0              // Jo > 0 => -Jo < 0
  0  0  0 -1  0 |  0              // Sa > 0 => -Sa < 0
  0  0  0  0 -1 |  0              // Ma > 0 => -Ma < 0
  1  0  0  0  0 |  6              // Je < 6
  0  1  0  0  0 |  6              // Ev < 6
  0  0  1  0  0 |  6              // Jo < 6
  0  0  0  1  0 |  6              // Sa < 6
  0  0  0  0  1 |  6              // Ma < 6
```

The first 4 constraints set by the problem in matrix form:

```md
 Je Ev Jo Sa Ma
 -- -- -- -- --
 -1  0  0  0  0 | -1              // Je > 1 => -Je < -1
  0  1  0  0  0 |  5              // Ev < 5
  0  0 -1  0  0 | -1              // Jo > 1 => -Jo < -1
  0  0  1  0  0 |  5              // Jo < 5
  0  1  0 -1  0 |  0              // Sa > Ev => Ev - Sa < 0
```

The fifth is an OR constraint. Only one of these is true

```md
 Je Ev Jo Sa Ma
 -- -- -- -- --
  0  0 -1  0  1 | -1              // Ma < Jo - 1 => Ma - Jo < -1
  0  0  1  0 -1 | -1              // Ma > Jo + 1 => Jo - Ma < -1
```

The sixth is an OR constraint. Only one of these is true

```md
 Je Ev Jo Sa Ma
 -- -- -- -- --
  0 -1  1  0  0 |  1              // Jo < Ev - 1 => Jo - Ev < -1
  0  1 -1  0  0 |  1              // Jo > Ev + 1 => Ev - Jo < -1
```
