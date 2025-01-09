# %%

# Load input data
with open('../resources/day12.txt', 'r') as file:
    input = file.read().strip()


# %%

def solve(input):
    input = input.split('\n')
    plots = {}
    for y, line in enumerate(input):
        for x,letter in enumerate(line):
            plots[(x,y)] = letter

    remaining = plots.copy()

    def flood(loc, letter):
        if loc not in remaining or plots[loc] != letter:
            return (0,0,0)

        del remaining[loc]
        x,y = loc
        perimeter = 0
        # X is the current location
        # H A B
        # G X C
        # F E D
        A = plots.get((x,y-1), ".")
        B = plots.get((x+1,y-1), ".")
        C = plots.get((x+1,y), ".")
        D = plots.get((x+1,y+1), ".")
        E = plots.get((x,y+1), ".")
        F = plots.get((x-1,y+1), ".")
        G = plots.get((x-1,y), ".")
        H = plots.get((x-1,y-1), ".")

        if A != letter:
            perimeter += 1
        if C != letter:
            perimeter += 1
        if E != letter:
            perimeter += 1
        if G != letter:
            perimeter += 1

        # Part 2
        # Counting inner and outer corners <=> Counting number of sides!
        # Outer corners
        tl = A != letter and G != letter
        tr = A != letter and C != letter
        bl = E != letter and G != letter
        br = E != letter and C != letter

        # Inner corners
        inner_tl = H == letter and G != letter
        inner_tr = B == letter and C != letter
        inner_bl = F == letter and G != letter
        inner_br = D == letter and C != letter

        # Sometimes inner and outer corners coincide and we shouldn't count them twice
        sides = (tl or inner_tl) + (tr or inner_tr) + (bl or inner_bl) + (br or inner_br)

        a1,p1,s1 = flood((x-1,y), letter)
        a2,p2,s2 = flood((x+1,y), letter)
        a3,p3,s3 = flood((x,y-1), letter)
        a4,p4,s4 = flood((x,y+1), letter)
        return (a1+a2+a3+a4+1, p1+p2+p3+p4+perimeter, s1+s2+s3+s4+sides)
    

    price = 0
    bulk_price = 0
    while len(remaining) > 0:
        loc, letter = next(iter(remaining.items()))
        area, perimeter, sides = flood(loc, letter)
        # print("Letter", letter, "area", area, "perimeter", perimeter, "sides", sides)
        price += area * perimeter
        bulk_price += area * sides
        

    print("Day 12 part 1: ", price)
    print("Day 12 part 2: ", bulk_price)

# Example
solve("""RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE""")

# Example for part 2, should be 368
solve("""AAAAAA
AAABBA
AAABBA
ABBAAA
ABBAAA
AAAAAA""")

# %%
solve(input)



# %%
