# %%
import matplotlib.pyplot as plt

# Load input data
with open('../resources/day15.txt', 'r') as file:
    input = file.read().strip()


# %%
def solve2(input, w=101, h=103, seconds=100):
    map,moves = input.split('\n\n')
    map = map.split('\n')
    w = 2*len(map[0])
    h = len(map)
    moves = moves.replace('\n', '')
    walls = set()
    boxes = set() # Stores left position of box
    robot = None

    # Find all objects
    for y, line in enumerate(map):
        for x,letter in enumerate(line):
            x  *= 2
            if letter == '#':
                walls.add((x,y))
                walls.add((x+1,y))
            elif letter == 'O':
                boxes.add((x,y))
            elif letter == '@':
                robot = (x,y)


    def push_x(p, dx):
        print("Push x: ", p, dx)
        next = p[0] + dx, p[1]
        box_to_push = None
        if next in walls:
            return False
        if dx > 0 and next in boxes:
            box_to_push = next
            moved = push_x((next[0]+1, next[1]), dx)
        elif (dx < 0 and (next[0]-1, next[1]) in boxes):
            box_to_push = (next[0]-1, next[1])
            moved = push_x((next[0]-1, next[1]), dx)
        else:
            return True # Empty space

        if moved:
            boxes.remove(box_to_push)
            boxes.add((box_to_push[0]+dx, next[1]))
            return True
        else:
            return False


    def push_y(ps, dy):
        print("Push y: ", ps, dy)
        if not ps:
            return True
        next_ps = set()
        for p in ps:
            next_ps.add((p[0], p[1] + dy))

        boxes_to_push = set()
        positions_to_clear = set()
        for p in next_ps:
            if p in walls:
                return False
            elif p in boxes:
                boxes_to_push.add(p)
                positions_to_clear.add(p)
                positions_to_clear.add((p[0]+1, p[1]))
            elif (p[0]-1, p[1]) in boxes:
                boxes_to_push.add((p[0]-1, p[1]))
                positions_to_clear.add((p[0]-1, p[1]))
                positions_to_clear.add(p)
            else:
                continue

        moved = push_y(positions_to_clear, dy)
        if moved:
            for b in boxes_to_push:
                boxes.remove(b)
                boxes.add((b[0], b[1]+dy))
            return True
                    

    for m in moves:
        dx = 0
        dy = 0
        if m == '>':
            dx, dy = 1, 0
        elif m == '<':
            dx, dy = -1, 0
        elif m == '^':
            dx, dy = 0, -1
        elif m == 'v':
            dx, dy = 0, 1

        print("\nMove: ", m)
        moved = False
        if dy == 0:
            moved = push_x(robot, dx)

        elif dx == 0:
            moved = push_y({robot}, dy)

        if moved:
            robot = robot[0] + dx, robot[1] + dy
            print("  Robot moved to ", robot)
        elif not moved:
            print("  Stuck, skipping move")
            continue

    # Print final map (indent once to print every step)
    pixels = [[[220,220,220] for _ in range(w)] for _ in range(h)]
    for (x,y) in walls:
        pixels[y][x] = [200,0,0]
    for (x,y) in boxes:
        pixels[y][x] = [100,100,200]
        pixels[y][x+1] = [100,100,200]
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

    print("Day 15 part 2: ", sum)        

# %%
print("Example small:")
solve2("""#######
#...#.#
#.....#
#..OO@#
#..O..#
#.....#
#######

<vv<<^^<<^^""")

# %%
print("Example large: (Should be 9021)")
solve2("""##########
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
solve2(input)

# %%
