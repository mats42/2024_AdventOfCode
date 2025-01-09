# %%
import re
import numpy as np

# Load input data
with open('../resources/day13.txt', 'r') as file:
    input = file.read().strip()


# %%
def solve(input, part2 = False):
    input = input.split('\n\n')
    pattern = r"Button A: X\+(\d+), Y\+(\d+)\nButton B: X\+(\d+), Y\+(\d+)\nPrize: X=(\d+), Y=(\d+)"
    total = 0
    for s in input:
        print(s)
        groups = re.search(pattern, s).groups()
        ax, ay, bx, by, px, py = map(int, groups)

        if part2:
            px += 10000000000000
            py += 10000000000000

        # Use linear algebra to solve the system of equations
        # a*ax + b*bx = px
        # a*ay + b*by = py
        A = np.array([[ax, bx], [ay, by]])
        C = np.array([px, py])
        solution = np.linalg.solve(A, C)
        a,b = solution
        if (np.round(solution, decimals=3) == np.round(solution)).all():
            tokens = 3 * round(a) + 1 * round(b)
            total += tokens
            print("Tokens:", tokens, "\n")


    print()
    if part2:
        print("Day 13, part2: ", total)
    else:
        print("Day 13, part1: ", total)     


# %%
print("Example (should result in 480)")
solve("""Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279""", True)

# %%
solve(input, False)
solve(input, True)

# %%
