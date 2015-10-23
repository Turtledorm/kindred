__author__ = 'Pedro'

import sys
import pygame
import os

# Add all the directories inside the game folder
# to the path
for (name, sub, files) in os.walk("."):
    sys.path.append(name[2:])


if __name__ == '__main__':
    pygame.init()

    from Variables import Variables

    from GameController import GameController
    g = GameController()
    g.main()