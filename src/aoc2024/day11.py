# %%
from collections import deque

# Load input data
with open('../resources/day11.txt', 'r') as file:
    input = file.read().strip()


# %%
def has_even_digits(n):
    return len(str(n)) % 2 == 0

def split_number(n):
    n = str(n)
    high = n[:len(n)//2]
    low = n[len(n)//2:]
    return int(high), int(low)

# Naive solution is too slow for part 2
def solve1(input):
    input = input.split(' ')
    stones = deque(map(int, input))

    def blink(stones):
        new_stones = []
        for stone in stones:
            if stone == 0:
                new_stones.append(1)
            elif has_even_digits(stone):
                high, low = split_number(stone)
                new_stones.append(high)
                new_stones.append(low)
            else:
                new_stones.append(2024*stone)

        return new_stones

    for i in range(25):
        stones = blink(stones)

    print("Day 11 part 1: ", len(stones))


def solve2(input):
    input = input.split(' ')
    stones = deque(map(int, input))
    cache = {} # Cache already calculated stone-blinks pairs

    def calc(stone, blinks):
        if (stone, blinks) in cache:
            return cache[(stone, blinks)]
        if blinks == 0:
            count = 1
        elif stone == 0:
            count = calc(1, blinks -1)
        elif has_even_digits(stone):
            high, low = split_number(stone)
            count = calc(high, blinks -1) + calc(low, blinks -1)
        else:
            count = calc(2024 * stone, blinks -1)

        cache[(stone, blinks)] = count
        return count
    
    count = 0
    for stone in stones:
        count += calc(stone, 75)

    print("Day 11 part 2: ", count)


# %%
solve1(input)
solve2(input)


