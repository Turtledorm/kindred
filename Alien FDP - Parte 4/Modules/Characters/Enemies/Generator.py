__author__ = 'Pedro'

import pygame
from Constants import DT
from Character import Character
from Enemy import Enemy
from Counter import Counter


class Generator(Enemy):
    def __init__(self, images, shield, screen, groups, pos=0):
        # We set the x position of the generator
        x = shield.rect.x

        # Set the generator position and his life's rectangle based on whether
        # its the top generator or bottom one
        if pos == 0:
            y = 593
        else:
            y = -64

        # Call the constructor of Character
        Character.__init__(self, "Generator", (x, y), (0, 0), images[0], groups)

        # Life of the enemy
        self.max_life = self.life = 50
        self.damage = 0

        # Counter of the images
        self.image_counter = Counter(2*DT)
        self.image_list = images
        self.image_number = 0
        self.image_max = 4

    def update(self, game):
        """
        Update method for the generator
        :param game: Game instance
        :return:
        """
        # We change the image
        if self.image_counter.update(DT):
            self.image_number = self.image_number + 1 if self.image_number + 1 < self.image_max else 0
            self.image = self.image_list[self.image_number]
