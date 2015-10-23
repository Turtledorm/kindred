__author__ = 'Pedro'

import pygame

"""
The constants file correspond to a library
that contains all the critical parameters
for the functionality of the game. Such as
size of the screen, FPS, and so on
"""

# Width and Height of the screen
WIDTH = 1280
HEIGHT = 720

# FPS of the game
FPS = 30

# Time interval between frames
DT = 1/FPS

# Create the main clock of the game
MAIN_CLOCK = pygame.time.Clock()