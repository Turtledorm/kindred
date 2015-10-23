__author__ = 'Pedro'

from Enemy import Enemy


class EnemyLevel1(Enemy):
    def __init__(self, image, y, groups):
        # The level 1 enemy only has x velocity
        velocity = (500, 0)

        # We call the constructor of the character
        super().__init__("Level 1 Ship", y, velocity, image, 1, groups)