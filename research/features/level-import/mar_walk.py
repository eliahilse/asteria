"""Walk ApoMario ".mar" level files exactly as ApoMarioEditorIO.readLevel(DataInputStream) consumes them.

Read-only format check used while preparing the Level-import task. It prints the declared
dimensions, the tile/enemy type histogram and the number of unconsumed trailing bytes. A
well-formed shipped level ends with leftover=0. It builds no game objects and allocates
nothing proportional to the declared dimensions, so it also serves as a reference for the
structural walk the task text requires.

    python3 research/features/level-import/mar_walk.py apogames/Java/ApoMario/levels
"""
import glob
import os
import struct
import sys

# Tile-type codes of apoMario.ApoMarioConstants (EMPTY .. NO_GROUND_WALL).
EMPTY, WALL, QUESTIONMARKBOX, DESTRUCTIBLEBOX, CANNON, COIN, ONLY_ABOVE_WALL, NO_COLLISION_WALL, FINISH, NO_GROUND_WALL = range(10)
# Enemy-type codes of apoMario.game.panels.ApoMarioEditorIO (writeLevel emits EMPTY for other enemies).
ENEMY_GUMBA, ENEMY_KOOPA = 1, 2


def walk(data: bytes) -> dict:
    position = 0

    def read_int() -> int:
        nonlocal position
        if position + 4 > len(data): raise EOFError(f'int at offset {position}')
        value = struct.unpack('>i', data[position:position + 4])[0]; position += 4; return value

    def read_bool() -> bool:
        nonlocal position
        if position + 1 > len(data): raise EOFError(f'boolean at offset {position}')
        value = data[position] != 0; position += 1; return value

    width, height = read_int(), read_int()
    tiles = {}
    for y in range(height - 1, -1, -1):
        for x in range(width):
            kind = read_int(); tiles[kind] = tiles.get(kind, 0) + 1
            if kind == WALL:
                if read_bool(): read_int(); read_int(); read_bool()      # tube: timeToShow, startTime, bEnemy
            elif kind == QUESTIONMARKBOX: read_int()                     # goodie
            elif kind == NO_GROUND_WALL: read_bool()                     # bTube
            elif kind == DESTRUCTIBLEBOX: read_bool(); read_int()        # bNull, goodie
            elif kind == CANNON: read_int(); read_int()                  # shootTime, startTime
            # EMPTY, COIN, NO_COLLISION_WALL and unknown codes carry no payload in readLevel.
    enemies = read_int(); kinds = {}
    for _ in range(enemies):
        kind = read_int(); kinds[kind] = kinds.get(kind, 0) + 1
        if kind == ENEMY_GUMBA: read_int(); read_int(); read_bool(); read_bool(); read_bool(); read_bool()
        elif kind == ENEMY_KOOPA: read_int(); read_int(); read_bool(); read_bool(); read_bool()
    return {'width': width, 'height': height, 'tiles': tiles, 'enemies': enemies, 'enemyTypes': kinds,
            'leftover': len(data) - position, 'size': len(data)}


if __name__ == '__main__':
    directory = sys.argv[1] if len(sys.argv) > 1 else 'apogames/Java/ApoMario/levels'
    for path in sorted(glob.glob(os.path.join(directory, '*.mar'))):
        try:
            print(os.path.basename(path), walk(open(path, 'rb').read()))
        except EOFError as error:
            print(os.path.basename(path), 'truncated:', error)
