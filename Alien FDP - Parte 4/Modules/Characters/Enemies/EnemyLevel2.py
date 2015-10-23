__author__ = 'Pedro'

import random
from Enemy import Enemy
from Constants import DT


class EnemyLevel2(Enemy):
    def __init__(self, image, groups):
        # To choose the position of an level 2 or 3 enemy we set
        # its x position to the same value but his y position
        # to be a random number
        y = random.randint(20, 651)

        # The level 1 enemy only has x velocity
        velocity = (240, 240)

        # We call the constructor of the character
        super().__init__("Level 2 Ship", y, velocity, image, 2, groups)

    def update(self, game):
        """
        Update method for the enemy level 2
        :param game:
        :return:
        """
        self.chase_player(game.player, DT)
        self.move(DT)