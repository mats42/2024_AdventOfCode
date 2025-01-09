# %%
# Load input data
with open('../resources/day9.txt', 'r') as file:
    input = file.read().strip()


# %%
def solve2(input):
    files = []   # List of file chunks (file-id, position, size)
    space = []   # List of free chunks (position, size)
    output = []         # List of blocks. File blocks contain file-id, free blocks contain nil
    file_id = 0
    pos = 0

    for i,v in enumerate(input):
        size = int(v)
        is_file = i % 2 == 0
        id = file_id if is_file else None
            
        if is_file:
            files.append((file_id, pos, size))
            file_id += 1
        else:
            space.append((pos, size))

        for j in range(size):
            output.append(id)
            pos += 1

    # Try relocate files
    for file_id, file_pos, file_size in reversed(files):
        for spc_i, (spc_pos, spc_size) in enumerate(space):
            if file_pos < spc_pos:
                break
            
            if file_size <= spc_size:
                for i in range(file_size):
                    # print("Relocating file", file_id, "from", file_pos, "to", spc_pos + i)
                    output[i + file_pos] = None
                    output[i + spc_pos] = file_id
                space[spc_i] = (spc_pos + file_size, spc_size - file_size)
                break

    checksum = 0
    for i, file_id in enumerate(output):
        if file_id != None:
            checksum += i * file_id
                
    print("Solution day 9 part 2: ", checksum)


# %%
# Try with example data
solve2("2333133121414131402")

# %%
solve2(input)


