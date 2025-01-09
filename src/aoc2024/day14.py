# %%
import re, time
import matplotlib.pyplot as plt

# Load input data
with open('../resources/day14.txt', 'r') as file:
    input = file.read().strip()


# %%

def solve1(input, w=101, h=103, seconds=100):
    input = input.split('\n')
    robots = []
    end_pos = []
    pattern = r"p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)"

    for line in input:
        groups = re.search(pattern, line).groups()
        px,py,vx,vy = map(int, groups)
        robots.append((px,py,vx,vy))

    for (px,py,vx,vy) in robots:
        new_px = (px + vx * seconds) % w
        new_py = (py + vy * seconds) % h
        end_pos.append((new_px, new_py))

    q0_count = sum(1 for p in end_pos if p[0] < (w//2) and p[1] < (h//2))
    q1_count = sum(1 for p in end_pos if p[0] > (w//2) and p[1] < (h//2))
    q2_count = sum(1 for p in end_pos if p[0] < (w//2) and p[1] > (h//2))
    q3_count = sum(1 for p in end_pos if p[0] > (w//2) and p[1] > (h//2))

    print("Day 14 part 1: ", q0_count * q1_count * q2_count * q3_count)
    

def solve2(input, w=101, h=103, t_range=range(100,200)):
    input = input.split('\n')
    robots = []
    end_pos = []
    pattern = r"p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)"

    for i, line in enumerate(input):
        groups = re.search(pattern, line).groups()
        px,py,vx,vy = map(int, groups)
        robots.append((px,py,vx,vy))

    for t in t_range:
        end_pos = []
        max_density = 0
        for (px,py,vx,vy) in robots:
            new_px = (px + vx * t) % w
            new_py = (py + vy * t) % h
            end_pos.append((new_px, new_py))
        pixels = [[0 for _ in range(w)] for _ in range(h)]
        for (x,y) in end_pos:
            pixels[y][x] += 1
            if pixels[y][x] > max_density:
                max_density = pixels[y][x]

        # Wild guess (after some wild trials and errors):
        # Lets assume that when the easter egg appear, no robots overlap
        # ie max density is 1
        if max_density == 1:
            plt.figure(figsize=(2, 2))
            plt.imshow(pixels)
            plt.axis('off')
            plt.show()
            print("Day 14 part 2: ", t)



print("Example (should be 12):")
solve1("""p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3""", 11, 7)


# %%
solve1(input)


# %%
solve2(input, t_range=range(1,10000))
# %%
