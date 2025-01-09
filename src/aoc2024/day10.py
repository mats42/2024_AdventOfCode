# %%
# Load input data
with open('../resources/day10.txt', 'r') as file:
    input = file.read().strip()


# %%
def solve1(input):
    input = input.split('\n')
    xs = len(input[0])
    ys = len(input)
    trailheads = []
    for y in range(ys):
        for x in range(xs):
            h = int(input[y][x])
            if h == 0:
                trailheads.append((x, y))

    def search(head, previous_h):
        x, y = head
        if x < 0 or x >= xs or y < 0 or y >= ys:
            return set()
        h = int(input[y][x])
        if h != previous_h + 1:
            return set()
        if h == 9:
            return {head}        
        return set.union(search((x-1, y), h), search((x+1, y), h), search((x, y-1), h), search((x, y+1), h))
    
    sum = 0
    for head in trailheads:
        peaks = search(head, -1)
        score = len(peaks)
        sum += score
        
    print("Day 10 part 1: ", sum)



# %%
def solve2(input):
    input = input.split('\n')
    xs = len(input[0])
    ys = len(input)
    trailheads = []
    for y in range(ys):
        for x in range(xs):
            h = int(input[y][x])
            if h == 0:
                trailheads.append((x, y))

    def search(head, previous_h):
        x, y = head
        if x < 0 or x >= xs or y < 0 or y >= ys:
            return 0
        h = int(input[y][x])
        if h != previous_h + 1:
            return 0
        if h == 9:
            return 1
        
        return search((x-1, y), h) + search((x+1, y), h) + search((x, y-1), h) + search((x, y+1), h)
    
    sum = 0
    for head in trailheads:
        rating = search(head, -1)
        sum += rating
        
    print("Day 10 part 2: ", sum)


# %%
# Example input
solve2("""89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732""")

# %%
solve1(input)
solve2(input)


