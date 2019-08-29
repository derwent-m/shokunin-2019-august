# Shokunin Challenge, August 2019 : 10x Developer

## The Challenge

### Background

Jessie, Evan, John, Sarah and Matt are all engineers in the same delivery team (note: any resemblance to actual TWers, living or dead, is purely coincidental)... and each of them has a different level of coding skill to the other.  This means it possible to order them from best to... "least best".  Importantly, the best of them is the mythical 10x Developer!!!

*But which one is it?!?*

*Here's what we know:*

- Jessie is not the best developer
- Evan is not the worst developer
- John is not the best developer or the worst developer
- Sarah is a better developer than Evan
- Matt is not directly below or above John as a developer
- John is not directly below or above Evan as a developer

### Challenge

You need to write a solution to compute these answers:

- Who is the 10x developer on the team?
- What is the correct ordering of the members of the team according to their coding skills?

### Submission

What to submit?
A link to your Github solution repo in a response to this email

When to submit?
By 23:59:59 on Wednesday August 28th AEST (that's 2 weeks away, peoples)

### Criteria for Awesomeness

- Solving the problem as outlined in "Challenge"
- Clean Code.  Note: the right choice of language and/or library could make your solution much more elegant.
- Evidence of TDD
- A go script and README (for people submitting solutions to be run locally)
- (Bonus) Parse the English-facts of the problem as appearing in the section "Here's what we know".  If you choose not to do this bonus requirement, you can express the facts in whatever form make it easiest for your program to handle.

## My Solution

Can encode inequalities as a matrix, E.G.

```md
  2x + 3y > 1
  - y < 2 - 3x
```

Can be written as

```md
   x  y
  -- --
  -2 -3 | -1  // -2x -3y < -1
   3 -1 |  2  // 3x - y < 2
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
[ 1  2  3  4  5 ]
[-1  0  0  0  0 ]
 Je Ev Jo Sa Ma
 -- -- -- -- --
 -1  0  0  0  0 | -1              // Je > 1 => -Je < -1
  0  1  0  0  0 |  5              // Ev < 5
  0  0 -1  0  0 | -1              // Jo > 1 => -Jo < -1
  0  0  1  0  0 |  5              // Jo < 5
  0 -1  0  1  0 |  0              // Sa < Ev => Sa - Ev < 0
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
  0 -1  1  0  0 | -1              // Jo < Ev - 1 => Jo - Ev < -1
  0  1 -1  0  0 | -1              // Jo > Ev + 1 => Ev - Jo < -1
```

## How to run

```bash
sbt compile
sbt run
```

## Solution

<details>
  <summary>Spoiler warning</summary>
  <ol>
    <li>Sarah</li>
    <li>John</li>
    <li>Jessie</li>
    <li>Evan</li>
    <li>Matt</li>
  </ol>
</details>
