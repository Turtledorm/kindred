__author__ = 'Pedro'

import pygame
import os
from Character import Character
from Enemy import Enemy


class MotherShip(Enemy):
    def __init__(self, screen, groups):
        # Alive variable
        self.alive = True

        # Load the mother ship image
        image = pygame.image.load(os.path.join("Images", "mother_ship.png")).convert_alpha()

        # Set the position of the mother ship
        x = screen.get_width() - 1.05*image.get_width()
        y = (screen.get_height() - image.get_height())//2

        # Call the constructor of Character
        Character.__init__(self, "Mother Ship", (x, y), (0, 0), image, groups)

        # Create the life of the mother ship
        self.life = self.max_life = 200

        # Set the rect's of life for the mother ship
        self.out_rect = pygame.rect.Rect((x, y - 10), (image.get_width(), 5))
        self.life_rect = pygame.rect.Rect((x + 1, y - 9), (image.get_width() - 2, 3))