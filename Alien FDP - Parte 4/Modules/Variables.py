__author__ = 'Pedro'
import pygame
from pygame.locals import *
from Constants import WIDTH
from Constants import HEIGHT


class Variables(object):
    # Screen of the game
    screen = pygame.display.set_mode((WIDTH, HEIGHT), HWSURFACE | DOUBLEBUF)

    # Caption on the screen
    pygame.display.set_caption('Alien FDP')

    # Key used to pause
    PAUSE = K_RETURN

    # Key used to shoot
    SHOOT = K_SPACE

    # Keys used to move the ship
    UP = K_UP
    DOWN = K_DOWN
    LEFT = K_LEFT
    RIGHT = K_RIGHT