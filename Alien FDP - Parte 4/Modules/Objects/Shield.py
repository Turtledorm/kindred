__author__ = 'Pedro'

import pygame
import os
from Sprite import Sprite


class Shield(Sprite):
    def __init__(self, screen, groups):
        # Load shield image
        image = pygame.image.load(os.path.join("Images", "shield.png")).convert_alpha()

        # Calculate x coordinate of shield
        x = screen.get_width() - image.get_width()
        y = 0

        super().__init__((x, y), image, groups)