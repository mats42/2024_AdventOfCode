# %%
import matplotlib.pyplot as plt

# Load input data
with open('../resources/day15.txt', 'r') as file:
    input = file.read().strip()


# %%
def move(pos, delta):
    return (pos[0] + delta[0], pos[1] + delta[1])

def solve1(input, w=101, h=103, seconds=100):
    map,moves = input.split('\n\n')
    map = map.split('\n')
    w = len(map[0])
    h = len(map)
    moves = moves.replace('\n', '')
    walls = set()
    boxes = set()
    robot = None

    # Find all objects
    for y, line in enumerate(map):
        for x,letter in enumerate(line):
            if letter == '#':
                walls.add((x,y))
            elif letter == 'O':
                boxes.add((x,y))
            elif letter == '@':
                robot = (x,y)

    def push_box(pos, delta):
        next_box = move(pos, delta)
        if next_box in walls:
            return False
        if next_box not in boxes:
            boxes.remove(pos)
            boxes.add(next_box)
            return True
        if next_box in boxes and push_box(next_box, delta):
            boxes.remove(pos)
            boxes.add(next_box)
            return True
        return False
                            

    for m in moves:
        delta = (0,0)
        if m == '>':
            delta = (1,0)
        elif m == '<':
            delta = (-1,0)
        elif m == '^':
            delta = (0,-1)
        elif m == 'v':
            delta = (0,1)

        print("\nMove: ", m)
        next = move(robot, delta)
        print("Next: ", next)
        if next in walls:
            print("*Wall")
            continue
        if next in boxes:
            if push_box(next, delta):
                robot = next
                print("*Pushed box")
            else:
                print("*Stuck")
                continue
        else:
            robot = next
            print("*Moved")

    pixels = [[[220,220,220] for _ in range(w)] for _ in range(h)]
    for (x,y) in walls:
        pixels[y][x] = [200,0,0]
    for (x,y) in boxes:
        pixels[y][x] = [100,100,200]
    (x,y) = robot
    pixels[y][x] = [255,255,50]
    plt.imshow(pixels)
    plt.axis('off')
    plt.show()

    sum = 0
    for box in boxes:
        x,y = box
        gps = 100*y+x
        sum += gps

    print("Day 15 part 1: ", sum)        

# %%
solve1("""##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
""")

# %%
solve1(input)

# %%
